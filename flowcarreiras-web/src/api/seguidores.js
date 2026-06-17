import api from './client'

// Status de seguidores de um usuário (endpoint público).
// Retorna { totalSeguidores, totalSeguindo, seguindoPeloUsuario }.
export async function obterStatusSeguidores(usuarioId) {
  const { data } = await api.get(`/usuarios/${usuarioId}/seguidores`)
  return data
}

// Passar a seguir um usuário (requer autenticação). Idempotente.
export async function seguir(usuarioId) {
  const { data } = await api.post(`/usuarios/${usuarioId}/seguidores`)
  return data
}

// Deixar de seguir um usuário (requer autenticação). Idempotente.
export async function deixarDeSeguir(usuarioId) {
  const { data } = await api.delete(`/usuarios/${usuarioId}/seguidores`)
  return data
}
