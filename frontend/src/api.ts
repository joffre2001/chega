export type LoginRequest = {
  email: string
  password: string
}
export type ProfileOptions = {
  migrationSituations: string[]
  primaryGoals: string[]
}

export type CreateMigrantProfileRequest = {
  nationality: string
  currentCity: string
  arrivalDate: string | null
  migrationSituation: string
  primaryGoal: string
  consent: boolean
}
export type LoginResponse = {
  accessToken: string
}

export type RegisterRequest = {
  fullName: string
  email: string
  password: string
  preferredLanguage: string
}

export type CurrentUser = {
  id: number
  fullName: string
  email: string
  role: string
  preferredLanguage: string
  status: string
  createdAt: string
  updatedAt: string
}

export type MigrantProfile = {
  id: number
  userId: number
  nationality: string
  currentCity: string
  arrivalDate: string | null
  migrationSituation: string
  primaryGoal: string
  consentGivenAt: string
  createdAt: string
  updatedAt: string
}

export type DocumentProgressStatus =
  | 'PENDING'
  | 'IN_PROGRESS'
  | 'COMPLETED'
  | 'NOT_APPLICABLE'

export type DocumentChecklistItem = {
  requirementId: number
  code: string
  title: string
  description: string | null
  officialSourceUrl: string | null
  required: boolean
  status: DocumentProgressStatus
  notes: string | null
  completedAt: string | null
}

type ApiErrorResponse = {
  message?: string
  fieldErrors?: Record<string, string>
}

export class ApiRequestError extends Error {
  readonly status: number

  constructor(
    status: number,
    message: string,
  ) {
    super(message)

    this.name = 'ApiRequestError'
    this.status = status
  }
}

async function request<T>(
  path: string,
  options: RequestInit = {},
  accessToken?: string,
): Promise<T> {
  const headers = new Headers(options.headers)

  headers.set('Accept', 'application/json')

  if (options.body) {
    headers.set('Content-Type', 'application/json')
  }

  if (accessToken) {
    headers.set(
      'Authorization',
      `Bearer ${accessToken}`,
    )
  }

  const response = await fetch(path, {
    ...options,
    headers,
  })

  if (!response.ok) {
    let message =
      'Não foi possível concluir a solicitação.'

    try {
      const error: ApiErrorResponse =
        await response.json()

      if (error.message) {
        message = error.message
      }

      if (
        error.fieldErrors &&
        Object.keys(error.fieldErrors).length > 0
      ) {
        message = Object.values(
          error.fieldErrors,
        ).join(' ')
      }
    } catch {
      // Mantém a mensagem padrão quando a API não envia JSON.
    }

    if (
      response.status === 401 ||
      response.status === 403
    ) {
      throw new ApiRequestError(
        response.status,
        'Sua sessão expirou. Entre novamente.',
      )
    }

    throw new ApiRequestError(
      response.status,
      message,
    )
  }

  return response.json() as Promise<T>
}

export function login(
  credentials: LoginRequest,
): Promise<LoginResponse> {
  return request<LoginResponse>(
    '/api/v1/auth/login',
    {
      method: 'POST',
      body: JSON.stringify(credentials),
    },
  )
}

export function registerUser(
  user: RegisterRequest,
): Promise<CurrentUser> {
  return request<CurrentUser>(
    '/api/v1/users',
    {
      method: 'POST',
      body: JSON.stringify(user),
    },
  )
}

export function getCurrentUser(
  accessToken: string,
): Promise<CurrentUser> {
  return request<CurrentUser>(
    '/api/v1/users/me',
    {},
    accessToken,
  )
}

export function getCurrentProfile(
  accessToken: string,
): Promise<MigrantProfile> {
  return request<MigrantProfile>(
    '/api/v1/profile',
    {},
    accessToken,
  )
}

export function getDocumentChecklist(
  accessToken: string,
): Promise<DocumentChecklistItem[]> {
  return request<DocumentChecklistItem[]>(
    '/api/v1/documents/checklist',
    {},
    accessToken,
  )
}
export function getProfileOptions(
  accessToken: string,
): Promise<ProfileOptions> {
  return request<ProfileOptions>(
    '/api/v1/profile/options',
    {},
    accessToken,
  )
}

export function createMigrantProfile(
  profile: CreateMigrantProfileRequest,
  accessToken: string,
): Promise<MigrantProfile> {
  return request<MigrantProfile>(
    '/api/v1/profile',
    {
      method: 'POST',
      body: JSON.stringify(profile),
    },
    accessToken,
  )
}