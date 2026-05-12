import api from './client'

export async function listarTags() {
  const { data } = await api.get('/tags')
  return data
}

export async function buscarTags(query) {
  const { data } = await api.get('/tags/search', { params: { q: query } })
  return data
}
