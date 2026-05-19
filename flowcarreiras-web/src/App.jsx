import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import MinhasObras from './pages/MinhasObras'
import NovaObra from './pages/NovaObra'
import EditarObra from './pages/EditarObra'
import PortfolioPublico from './pages/PortfolioPublico'
import Login from './pages/Login'
import Onboarding from './pages/Onboarding'
import MeuPerfil from './pages/MeuPerfil'
import ConfigurarMentoria from './pages/ConfigurarMentoria'
import ArtistasParaMentoria from './pages/ArtistasParaMentoria'
import Mentores from './pages/Mentores'
import MinhasMentorias from './pages/MinhasMentorias'
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
          <Route path="/onboarding" element={
            <RotaProtegida><Onboarding /></RotaProtegida>
          } />
          <Route path="/portfolio/minhas-obras" element={
            <RotaProtegida><MinhasObras /></RotaProtegida>
          } />
          <Route path="/portfolio/nova-obra" element={
            <RotaProtegida><NovaObra /></RotaProtegida>
          } />
          <Route path="/portfolio/editar/:id" element={
            <RotaProtegida><EditarObra /></RotaProtegida>
          } />
          <Route path="/meu-perfil" element={
            <RotaProtegida><MeuPerfil /></RotaProtegida>
          } />
          <Route path="/mentoria/configurar" element={
            <RotaProtegida><ConfigurarMentoria /></RotaProtegida>
          } />
          <Route path="/mentoria/artistas" element={
            <RotaProtegida><ArtistasParaMentoria /></RotaProtegida>
          } />
          <Route path="/mentores" element={
            <RotaProtegida><Mentores /></RotaProtegida>
          } />
          <Route path="/mentoria/minhas" element={
            <RotaProtegida><MinhasMentorias /></RotaProtegida>
          } />
          <Route path="/" element={<Navigate to="/portfolio/minhas-obras" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
