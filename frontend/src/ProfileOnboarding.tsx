import {
  useState,
  type FormEventHandler,
} from 'react'

import type {
  CreateMigrantProfileRequest,
  ProfileOptions,
} from './api'

type ProfileOnboardingProps = {
  options: ProfileOptions
  loading: boolean
  error: string
  onSubmit: (
    profile: CreateMigrantProfileRequest,
  ) => Promise<void>
  onLogout: () => void
}

function ProfileOnboarding({
  options,
  loading,
  error,
  onSubmit,
  onLogout,
}: ProfileOnboardingProps) {
  const [nationality, setNationality] = useState('')
  const [currentCity, setCurrentCity] = useState('')
  const [arrivalDate, setArrivalDate] = useState('')
  const [migrationSituation, setMigrationSituation] =
    useState(options.migrationSituations[0] ?? '')

  const [primaryGoal, setPrimaryGoal] =
    useState(options.primaryGoals[0] ?? '')

  const [consent, setConsent] = useState(false)

  const handleSubmit:
    FormEventHandler<HTMLFormElement> = async (event) => {
      event.preventDefault()

      await onSubmit({
        nationality: nationality.trim(),
        currentCity: currentCity.trim(),
        arrivalDate: arrivalDate || null,
        migrationSituation,
        primaryGoal,
        consent,
      })
    }

  return (
    <main className="onboarding-page">
      <header className="onboarding-header">
        <span className="brand">CHEGA</span>

        <button
          type="button"
          onClick={onLogout}
        >
          Sair
        </button>
      </header>

      <section className="onboarding-content">
        <div className="onboarding-introduction">
          <p className="eyebrow">
            ÚLTIMA ETAPA
          </p>

          <h1>
            Conte um pouco sobre sua jornada.
          </h1>

          <p>
            Essas informações permitem organizar documentos
            e orientações relevantes para o seu momento.
          </p>

          <div className="onboarding-privacy">
            <strong>Você mantém o controle.</strong>

            <span>
              Seus dados não serão compartilhados e poderão
              ser atualizados posteriormente.
            </span>
          </div>
        </div>

        <form
          className="onboarding-form"
          onSubmit={handleSubmit}
        >
          <div className="onboarding-form-heading">
            <span>Perfil migratório</span>
            <strong>Passo 1 de 1</strong>
          </div>

          <label htmlFor="nationality">
            Nacionalidade
          </label>

          <input
            id="nationality"
            type="text"
            placeholder="Ex.: Haitiana"
            value={nationality}
            onChange={(event) =>
              setNationality(event.target.value)
            }
            minLength={2}
            maxLength={100}
            required
          />

          <label htmlFor="currentCity">
            Cidade atual
          </label>

          <input
            id="currentCity"
            type="text"
            placeholder="Ex.: Chapecó"
            value={currentCity}
            onChange={(event) =>
              setCurrentCity(event.target.value)
            }
            minLength={2}
            maxLength={120}
            required
          />

          <label htmlFor="arrivalDate">
            Data de chegada ao Brasil
            <span> opcional</span>
          </label>

          <input
            id="arrivalDate"
            type="date"
            max={new Date().toISOString().split('T')[0]}
            value={arrivalDate}
            onChange={(event) =>
              setArrivalDate(event.target.value)
            }
          />

          <label htmlFor="migrationSituation">
            Situação migratória
          </label>

          <select
            id="migrationSituation"
            value={migrationSituation}
            onChange={(event) =>
              setMigrationSituation(event.target.value)
            }
            required
          >
            {options.migrationSituations.map((option) => (
              <option key={option} value={option}>
                {formatValue(option)}
              </option>
            ))}
          </select>

          <label htmlFor="primaryGoal">
            Objetivo principal
          </label>

          <select
            id="primaryGoal"
            value={primaryGoal}
            onChange={(event) =>
              setPrimaryGoal(event.target.value)
            }
            required
          >
            {options.primaryGoals.map((option) => (
              <option key={option} value={option}>
                {formatValue(option)}
              </option>
            ))}
          </select>

          <label className="consent-field">
            <input
              type="checkbox"
              checked={consent}
              onChange={(event) =>
                setConsent(event.target.checked)
              }
              required
            />

            <span>
              Concordo com o uso desses dados para
              personalizar minha jornada no CHEGA.
            </span>
          </label>

          {error && (
            <p className="error-message" role="alert">
              {error}
            </p>
          )}

          <button
            className="onboarding-submit"
            type="submit"
            disabled={loading}
          >
            {loading
              ? 'Salvando perfil...'
              : 'Concluir e acessar minha jornada'}
          </button>
        </form>
      </section>
    </main>
  )
}

function formatValue(value: string) {
  return value
    .toLowerCase()
    .split('_')
    .map(
      (word) =>
        word.charAt(0).toUpperCase() +
        word.slice(1),
    )
    .join(' ')
}

export default ProfileOnboarding