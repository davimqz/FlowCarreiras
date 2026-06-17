import api from './client'

// Status de curtidas de uma obra (endpoint público).
// Retorna { total, curtidoPeloUsuario }.
export async function obterStatusCurtida(obraId) {
  const { data } = await api.get(`/obras/${obraId}/curtidas`)
  return data
}

// Curtir uma obra (requer autenticação). Idempotente.
export async function curtir(obraId) {
  const { data } = await api.post(`/obras/${obraId}/curtidas`)
  return data
}

// Remover a curtida (requer autenticação). Idempotente.
export async function descurtir(obraId) {
  const { data } = await api.delete(`/obras/${obraId}/curtidas`)
  return data
}
