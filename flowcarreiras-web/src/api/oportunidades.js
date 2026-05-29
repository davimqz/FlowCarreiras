import apiClient from './client'

export const listarOportunidades = (filtro = {}) => {
  const params = {}
  if (filtro.tipos?.length) params.tipos = filtro.tipos.join(',')
  if (filtro.tags?.length) params.tags = filtro.tags.join(',')
  if (filtro.area) params.area = filtro.area
  if (filtro.localidade) params.localidade = filtro.localidade
  if (filtro.query) params.q = filtro.query
  if (filtro.limit) params.limit = filtro.limit
  if (filtro.offset) params.offset = filtro.offset
  return apiClient.get('/oportunidades', { params }).then(r => r.data)
}
