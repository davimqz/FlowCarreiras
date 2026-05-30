import api from './client'

// Obras publicadas de qualquer artista (endpoint público)
export async function listarObrasDoArtista(artistaId) {
  const { data } = await api.get(`/obras/artista/${artistaId}`)
  return data
}

// Todas as obras do usuário logado, incluindo rascunhos (endpoint autenticado)
export async function listarMinhasObras() {
  const { data } = await api.get('/obras/minhas')
  return data
}

export async function buscarObra(id) {
  const { data } = await api.get(`/obras/${id}`)
  return data
}

export async function explorarObras(filtros = {}) {
  const params = {}
  if (filtros.tags?.length) params.tags = filtros.tags.join(',')
  if (filtros.area) params.area = filtros.area
  if (filtros.formatos?.length) params.formatos = filtros.formatos.join(',')
  if (filtros.recencia) params.recencia = filtros.recencia
  const { data } = await api.get('/obras/explorar', { params })
  return data
}

export async function criarObra(dados, file, onProgress) {
  const formData = new FormData()
  formData.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }))
  if (file) formData.append('file', file)

  const { data } = await api.post('/obras', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    onUploadProgress: (e) => {
      if (onProgress && e.total) onProgress(Math.round((e.loaded * 100) / e.total))
    },
  })
  return data
}

export async function editarObra(id, dados, file, onProgress) {
  const formData = new FormData()
  formData.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }))
  if (file) formData.append('file', file)

  const { data } = await api.put(`/obras/${id}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    onUploadProgress: (e) => {
      if (onProgress && e.total) onProgress(Math.round((e.loaded * 100) / e.total))
    },
  })
  return data
}

export async function removerObra(id) {
  await api.delete(`/obras/${id}`)
}
