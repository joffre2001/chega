import {
  useCallback,
  useEffect,
  useState,
  type FormEvent,
} from 'react'

import {
  ApiRequestError,
  getCurrentProfile,
  getCurrentUser,
  getDocumentChecklist,
  getProfileOptions,
  login,
  registerUser,
  type CurrentUser,
  type DocumentChecklistItem,
  type MigrantProfile,
  type ProfileOptions,
  type RegisterRequest,
} from './api'

import AuthCarousel from './AuthCarousel'
import ProfileOnboarding from './ProfileOnboarding'
import './App.css'

type AuthMode = 'login' | 'register'

const ACCESS_TOKEN_KEY = 'chega_access_token'

function App() {
  const [authMode, setAuthMode] =
    useState<AuthMode>('login')

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [fullName, setFullName] = useState('')

  const [preferredLanguage, setPreferredLanguage] =
    useState<RegisterRequest['preferredLanguage']>(
      'PT_BR',
    )

  const [accessToken, setAccessToken] = useState<
    string | null
  >(() => localStorage.getItem(ACCESS_TOKEN_KEY))

  const [currentUser, setCurrentUser] =
    useState<CurrentUser | null>(null)

  const [currentProfile, setCurrentProfile] =
    useState<MigrantProfile | null>(null)

  const [profileOptions, setProfileOptions] =
    useState<ProfileOptions | null>(null)

  const [checklist, setChecklist] = useState<
    DocumentChecklistItem[]
  >([])

  const [needsProfile, setNeedsProfile] =
    useState(false)

  const [loading, setLoading] = useState(false)

  const [loadingSession, setLoadingSession] =
    useState(Boolean(accessToken))

  const [error, setError] = useState('')

  const clearSession = useCallback(() => {
    localStorage.removeItem(ACCESS_TOKEN_KEY)

    setAccessToken(null)
    setCurrentUser(null)
    setCurrentProfile(null)
    setProfileOptions(null)
    setChecklist([])
    setNeedsProfile(false)
    setLoadingSession(false)
    setPassword('')
    setError('')
  }, [])

  const loadAuthenticatedData = useCallback(
    async (token: string) => {
      setLoadingSession(true)
      setError('')

      try {
        const user = await getCurrentUser(token)

        setCurrentUser(user)

        try {
          const [profile, documentChecklist] =
            await Promise.all([
              getCurrentProfile(token),
              getDocumentChecklist(token),
            ])

          setCurrentProfile(profile)
          setChecklist(documentChecklist)
          setNeedsProfile(false)
          setProfileOptions(null)
        } catch (exception) {
          if (
            exception instanceof ApiRequestError &&
            exception.status === 404
          ) {
            const options =
              await getProfileOptions(token)

            setCurrentProfile(null)
            setChecklist([])
            setProfileOptions(options)
            setNeedsProfile(true)

            return
          }

          throw exception
        }
      } catch (exception) {
        clearSession()

        const message =
          exception instanceof Error
            ? exception.message
            : 'Não foi possível carregar sua conta.'

        setError(message)
      } finally {
        setLoadingSession(false)
      }
    },
    [clearSession],
  )

  useEffect(() => {
    if (!accessToken) {
      setLoadingSession(false)
      return
    }

    void loadAuthenticatedData(accessToken)
  }, [accessToken, loadAuthenticatedData])

  function changeMode(mode: AuthMode) {
    setAuthMode(mode)
    setError('')
    setPassword('')
  }

  function saveToken(token: string) {
    localStorage.setItem(ACCESS_TOKEN_KEY, token)
    setAccessToken(token)
  }

  async function handleLoginSubmit(
    event: FormEvent<HTMLFormElement>,
  ) {
    event.preventDefault()

    setError('')
    setLoading(true)

    try {
      const response = await login({
        email: email.trim(),
        password,
      })

      saveToken(response.accessToken)
    } catch (exception) {
      const message =
        exception instanceof Error
          ? exception.message
          : 'Não foi possível entrar.'

      setError(message)
    } finally {
      setLoading(false)
    }
  }

  async function handleRegisterSubmit(
    event: FormEvent<HTMLFormElement>,
  ) {
    event.preventDefault()

    setError('')
    setLoading(true)

    try {
      await registerUser({
        fullName: fullName.trim(),
        email: email.trim(),
        password,
        preferredLanguage,
      })

      const response = await login({
        email: email.trim(),
        password,
      })

      saveToken(response.accessToken)
    } catch (exception) {
      const message =
        exception instanceof Error
          ? exception.message
          : 'Não foi possível criar sua conta.'

      setError(message)
    } finally {
      setLoading(false)
    }
  }

  async function handleProfileCreated(
    profile: MigrantProfile,
  ) {
    setCurrentProfile(profile)
    setNeedsProfile(false)
    setProfileOptions(null)
    setError('')

    if (!accessToken) {
      return
    }

    try {
      const documentChecklist =
        await getDocumentChecklist(accessToken)

      setChecklist(documentChecklist)
    } catch (exception) {
      const message =
        exception instanceof Error
          ? exception.message
          : 'Não foi possível carregar o checklist.'

      setError(message)
    }
  }

  function handleLogout() {
    clearSession()
    setAuthMode('login')
  }

  if (loadingSession) {
    return (
      <main className="loading-page">
        <section className="loading-card">
          <span className="brand">CHEGA</span>

          <div className="loading-spinner" />

          <p>Preparando sua jornada...</p>
        </section>
      </main>
    )
  }

  if (
    accessToken &&
    currentUser &&
    needsProfile &&
    profileOptions
  ) {
    return (
      <ProfileOnboarding
        options={profileOptions}
        onCreated={handleProfileCreated}
        onLogout={handleLogout}
      />
    )
  }

  if (
    accessToken &&
    currentUser &&
    currentProfile
  ) {
    const completedDocuments = checklist.filter(
      (item) => item.status === 'COMPLETED',
    ).length

    const progressPercentage =
      checklist.length === 0
        ? 0
        : Math.round(
            (completedDocuments /
              checklist.length) *
              100,
          )

    return (
      <main className="dashboard-page">
        <header className="dashboard-header">
          <div>
            <span className="brand">CHEGA</span>
            <p>Sua jornada no Brasil</p>
          </div>

          <div className="dashboard-user">
            <div>
              <strong>
                {currentUser.fullName}
              </strong>

              <span>{currentUser.email}</span>
            </div>

            <button
              className="secondary-button"
              type="button"
              onClick={handleLogout}
            >
              Sair
            </button>
          </div>
        </header>

        <section className="dashboard-hero">
          <div>
            <p className="eyebrow">
              OLÁ, {currentUser.fullName}
            </p>

            <h1>
              Cada documento organizado é um
              passo adiante.
            </h1>

            <p>
              Acompanhe sua documentação e veja
              o que ainda precisa ser preparado.
            </p>
          </div>

          <article className="progress-card">
            <span>Progresso geral</span>

            <strong>
              {progressPercentage}%
            </strong>

            <div className="progress-track">
              <div
                className="progress-fill"
                style={{
                  width: `${progressPercentage}%`,
                }}
              />
            </div>

            <small>
              {completedDocuments} de{' '}
              {checklist.length} documentos
              concluídos
            </small>
          </article>
        </section>

        {error && (
          <p
            className="error-message"
            role="alert"
          >
            {error}
          </p>
        )}

        <section className="dashboard-content">
          <div className="checklist-section">
            <div className="section-heading">
              <div>
                <p className="eyebrow">
                  SEU CHECKLIST
                </p>

                <h2>
                  Documentos importantes
                </h2>
              </div>

              <span>
                {checklist.length} itens
              </span>
            </div>

            <div className="document-list">
              {checklist.length === 0 ? (
                <article className="empty-card">
                  <h3>
                    Nenhum documento encontrado
                  </h3>

                  <p>
                    Seu checklist ainda não
                    possui itens.
                  </p>
                </article>
              ) : (
                checklist.map(
                  (item, index) => (
                    <article
                      className="document-card"
                      key={item.requirementId}
                    >
                      <div
                        className={
                          item.status ===
                          'COMPLETED'
                            ? 'document-status completed'
                            : 'document-status'
                        }
                      >
                        {item.status ===
                        'COMPLETED'
                          ? '✓'
                          : index + 1}
                      </div>

                      <div className="document-information">
                        <div className="document-title-row">
                          <h3>{item.title}</h3>

                          {item.required && (
                            <span className="required-badge">
                              Obrigatório
                            </span>
                          )}
                        </div>

                        <p>
                          {item.description}
                        </p>

                        <span className="document-progress-label">
                          {item.status ===
                          'COMPLETED'
                            ? 'Concluído'
                            : item.status ===
                                'IN_PROGRESS'
                              ? 'Em andamento'
                              : 'Pendente'}
                        </span>

                        {item.officialSourceUrl && (
                          <a
                            href={
                              item.officialSourceUrl
                            }
                            target="_blank"
                            rel="noopener noreferrer"
                          >
                            Consultar fonte oficial ↗
                          </a>
                        )}
                      </div>
                    </article>
                  ),
                )
              )}
            </div>
          </div>

          <aside className="profile-summary">
            <p className="eyebrow">
              SEUS DADOS
            </p>

            <h2>Perfil migratório</h2>

            <dl>
              <div>
                <dt>Nome</dt>
                <dd>
                  {currentUser.fullName}
                </dd>
              </div>

              <div>
                <dt>Email</dt>
                <dd>{currentUser.email}</dd>
              </div>

              <div>
                <dt>Nacionalidade</dt>
                <dd>
                  {
                    currentProfile.nationality
                  }
                </dd>
              </div>

              <div>
                <dt>Cidade atual</dt>
                <dd>
                  {
                    currentProfile.currentCity
                  }
                </dd>
              </div>

              <div>
                <dt>
                  Situação migratória
                </dt>
                <dd>
                  {
                    currentProfile
                      .migrationSituation
                  }
                </dd>
              </div>

              <div>
                <dt>Objetivo principal</dt>
                <dd>
                  {
                    currentProfile
                      .primaryGoal
                  }
                </dd>
              </div>
            </dl>
          </aside>
        </section>
      </main>
    )
  }

  return (
    <main className="auth-showcase-page">
      <header className="auth-showcase-header">
        <span className="brand">CHEGA</span>

        <button
          className="header-mode-button"
          type="button"
          onClick={() =>
            changeMode(
              authMode === 'login'
                ? 'register'
                : 'login',
            )
          }
        >
          {authMode === 'login'
            ? 'Criar uma conta'
            : 'Já tenho uma conta'}
        </button>
      </header>

      <section className="auth-showcase-card">
        <AuthCarousel />

        <form
          className="auth-showcase-form"
          onSubmit={
            authMode === 'login'
              ? handleLoginSubmit
              : handleRegisterSubmit
          }
        >
          <div className="auth-mode-tabs">
            <button
              type="button"
              className={
                authMode === 'login'
                  ? 'active'
                  : ''
              }
              onClick={() =>
                changeMode('login')
              }
            >
              Entrar
            </button>

            <button
              type="button"
              className={
                authMode === 'register'
                  ? 'active'
                  : ''
              }
              onClick={() =>
                changeMode('register')
              }
            >
              Criar conta
            </button>
          </div>

          <div className="auth-form-heading">
            <p className="eyebrow">
              {authMode === 'login'
                ? 'BEM-VINDO DE VOLTA'
                : 'COMECE SUA JORNADA'}
            </p>

            <h2>
              {authMode === 'login'
                ? 'Entre na sua conta'
                : 'Crie sua conta'}
            </h2>

            <p>
              {authMode === 'login'
                ? 'Continue de onde você parou.'
                : 'Leva apenas alguns minutos.'}
            </p>
          </div>

          {authMode === 'register' && (
            <>
              <label htmlFor="fullName">
                Nome completo
              </label>

              <input
                id="fullName"
                name="fullName"
                type="text"
                autoComplete="name"
                placeholder="Como devemos chamar você?"
                value={fullName}
                onChange={(event) =>
                  setFullName(
                    event.target.value,
                  )
                }
                minLength={2}
                maxLength={150}
                required
              />
            </>
          )}

          <label htmlFor="email">
            Email
          </label>

          <input
            id="email"
            name="email"
            type="email"
            autoComplete="email"
            placeholder="voce@email.com"
            value={email}
            onChange={(event) =>
              setEmail(event.target.value)
            }
            required
          />

          <label htmlFor="password">
            Senha
          </label>

          <input
            id="password"
            name="password"
            type="password"
            autoComplete={
              authMode === 'login'
                ? 'current-password'
                : 'new-password'
            }
            placeholder={
              authMode === 'login'
                ? 'Digite sua senha'
                : 'Crie uma senha segura'
            }
            value={password}
            onChange={(event) =>
              setPassword(event.target.value)
            }
            minLength={8}
            required
          />

          {authMode === 'register' && (
            <>
              <label htmlFor="preferredLanguage">
                Idioma preferido
              </label>

              <select
                id="preferredLanguage"
                name="preferredLanguage"
                value={preferredLanguage}
                onChange={(event) =>
                  setPreferredLanguage(
                    event.target
                      .value as RegisterRequest['preferredLanguage'],
                  )
                }
              >
                <option value="PT_BR">
                  Português
                </option>

                <option value="HT">
                  Kreyòl ayisyen
                </option>

                <option value="FR_FR">
                  Français
                </option>

                <option value="ES_ES">
                  Español
                </option>

                <option value="EN_US">
                  English
                </option>
              </select>
            </>
          )}

          {error && (
            <p
              className="error-message"
              role="alert"
            >
              {error}
            </p>
          )}

          <button
            className="primary-button"
            type="submit"
            disabled={loading}
          >
            {loading
              ? authMode === 'login'
                ? 'Entrando...'
                : 'Criando conta...'
              : authMode === 'login'
                ? 'Entrar'
                : 'Criar minha conta'}
          </button>

          <p className="privacy-note">
            Seus dados são protegidos e
            utilizados somente para
            personalizar sua experiência.
          </p>
        </form>
      </section>
    </main>
  )
}

export default App