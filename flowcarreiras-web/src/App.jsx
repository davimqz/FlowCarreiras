import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import MinhasObras from './pages/MinhasObras'
import NovaObra from './pages/NovaObra'
import EditarObra from './pages/EditarObra'
import PortfolioPublico from './pages/PortfolioPublico'
import Login from './pages/Login'
import NetworkStatus from './components/NetworkStatus'

function RotaProtegida({ children }) {
  const { token } = useAuth()
  return token ? children : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <NetworkStatus />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/portfolio/:urlPublica" element={<PortfolioPublico />} />
          <Route path="/portfolio/minhas-obras" element={
            <RotaProtegida><MinhasObras /></RotaProtegida>
          } />
          <Route path="/portfolio/nova-obra" element={
            <RotaProtegida><NovaObra /></RotaProtegida>
          } />
          <Route path="/portfolio/editar/:id" element={
            <RotaProtegida><EditarObra /></RotaProtegida>
          } />
          <Route path="/" element={<Navigate to="/portfolio/minhas-obras" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
