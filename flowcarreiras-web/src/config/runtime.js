const semBarrasFinais = (valor = '') => valor.replace(/\/+$/, '')

const apiConfigurada = semBarrasFinais(import.meta.env.VITE_API_URL || '')

export const backendOrigin = apiConfigurada.endsWith('/api')
  ? apiConfigurada.slice(0, -4)
  : apiConfigurada

export const apiBaseUrl = apiConfigurada
  ? (apiConfigurada.endsWith('/api') ? apiConfigurada : `${apiConfigurada}/api`)
  : '/api'

export const dashboardBaseUrl = semBarrasFinais(
  import.meta.env.VITE_DASHBOARD_URL || '/dashboard'
)

export function resolverUrlBackend(url) {
  if (!url || !backendOrigin || !url.startsWith('/')) return url
  return `${backendOrigin}${url}`
}
