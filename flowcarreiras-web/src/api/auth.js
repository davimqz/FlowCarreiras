import api from './client'

export async function login(email, senha) {
  const { data } = await api.post('/auth/login', { email, senha })
  return data
}

export async function registrar(nome, email, senha, desejaSerMentor = false) {
  const { data } = await api.post('/auth/registro', { nome, email, senha, desejaSerMentor })
  return data
}
