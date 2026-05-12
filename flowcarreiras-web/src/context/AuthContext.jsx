import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('fc_token'))
  const [usuario, setUsuario] = useState(() => {
    const raw = localStorage.getItem('fc_usuario')
    return raw ? JSON.parse(raw) : null
  })

  function login(data) {
    localStorage.setItem('fc_token', data.token)
    localStorage.setItem('fc_usuario', JSON.stringify(data))
    setToken(data.token)
    setUsuario(data)
  }

  function logout() {
    localStorage.removeItem('fc_token')
    localStorage.removeItem('fc_usuario')
    setToken(null)
    setUsuario(null)
  }

  return (
    <AuthContext.Provider value={{ token, usuario, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
