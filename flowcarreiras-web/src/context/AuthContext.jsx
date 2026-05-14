import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

function lerUsuarioStorage() {
  const raw = localStorage.getItem('fc_usuario')
  return raw ? JSON.parse(raw) : null
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('fc_token'))
  const [usuario, setUsuario] = useState(lerUsuarioStorage)
  const [onboardingConcluido, setOnboardingConcluido] = useState(
    () => lerUsuarioStorage()?.onboardingConcluido ?? false
  )

  function login(data) {
    localStorage.setItem('fc_token', data.token)
    localStorage.setItem('fc_usuario', JSON.stringify(data))
    setToken(data.token)
    setUsuario(data)
    setOnboardingConcluido(data.onboardingConcluido ?? false)
  }

  function logout() {
    localStorage.removeItem('fc_token')
    localStorage.removeItem('fc_usuario')
    setToken(null)
    setUsuario(null)
    setOnboardingConcluido(false)
  }

  function marcarOnboardingConcluido() {
    setOnboardingConcluido(true)
    const raw = localStorage.getItem('fc_usuario')
    if (raw) {
      const userData = JSON.parse(raw)
      userData.onboardingConcluido = true
      localStorage.setItem('fc_usuario', JSON.stringify(userData))
    }
  }

  return (
    <AuthContext.Provider value={{ token, usuario, onboardingConcluido, login, logout, marcarOnboardingConcluido }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
