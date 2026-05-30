import apiClient from './client'

export const listarNotificacoesOportunidades = (limit = 8) =>
  apiClient.get('/notificacoes/oportunidades', { params: { limit } }).then(r => r.data)

export const marcarNotificacaoOportunidadeLida = (id) =>
  apiClient.patch(`/notificacoes/oportunidades/${id}/lida`)
