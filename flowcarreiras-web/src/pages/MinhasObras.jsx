import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { listarMinhasObras, removerObra } from '../api/obras'
import GridPortfolio from '../components/GridPortfolio'
import ModalConfirmacao from '../components/ModalConfirmacao'

export default function MinhasObras() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()

  const [obras, setObras] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [obraParaRemover, setObraParaRemover] = useState(null)
  const [removendo, setRemovendo] = useState(false)

  useEffect(() => {
    async function carregar() {
      try {
        const dados = await listarMinhasObras()
        setObras(dados)
      } finally {
        setCarregando(false)
      }
    }
    carregar()
  }, [])

  async function confirmarRemocao() {
    setRemovendo(true)
    try {
      await removerObra(obraParaRemover.id)
      setObras(prev => prev.filter(o => o.id !== obraParaRemover.id))
      setObraParaRemover(null)
    } catch {
      alert('Erro ao remover obra. Tente novamente.')
    } finally {
      setRemovendo(false)
    }
  }

  const urlPublica = `${window.location.origin}/portfolio/${usuario?.urlPublica}`

  return (
    <div className="min-h-screen">
      {/* Header */}
      <header className="bg-card border-b border-gray-800 sticky top-0 z-10">
        <div className="max-w-5xl mx-auto px-4 py-3 flex items-center justify-between">
          <h1 className="font-bold text-brand">Flow Carreiras</h1>
          <nav className="flex items-center gap-4">
            <Link
              to="/portfolio/minhas-obras"
              className="text-sm text-gray-300 hover:text-white font-medium border-b-2 border-brand pb-0.5"
            >
              Portfólio
            </Link>
            <Link
              to="/meu-perfil"
              className="text-sm text-gray-400 hover:text-white"
            >
              Meu Perfil
            </Link>
            <Link
              to="/mentores"
              className="text-sm text-gray-400 hover:text-white"
            >
              Mentores
            </Link>
            <Link
              to="/mentoria/artistas"
              className="text-sm text-gray-400 hover:text-white"
            >
              Mentoria
            </Link>
            <Link
              to="/mentoria/minhas"
              className="text-sm text-gray-400 hover:text-white"
            >
              Minhas
            </Link>
            <span className="text-gray-600">|</span>
            <span className="text-sm text-gray-400 hidden sm:block">{usuario?.nome}</span>
            <button onClick={logout} className="text-xs text-gray-500 hover:text-white">Sair</button>
          </nav>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-4 py-6">
        {/* Título + ações */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
          <div>
            <h2 className="text-xl font-semibold">Meu Portfólio</h2>
            <p className="text-gray-400 text-sm mt-0.5">{obras.length} obra{obras.length !== 1 ? 's' : ''}</p>
          </div>
          <div className="flex gap-2 flex-wrap">
            <button
              onClick={() => {
                navigator.clipboard.writeText(urlPublica)
                alert('URL copiada!')
              }}
              className="btn-secondary text-sm py-1.5 px-3"
            >
              📋 Copiar URL pública
            </button>
            <a
              href={`/portfolio/${usuario?.urlPublica}`}
              target="_blank"
              rel="noopener noreferrer"
              className="btn-secondary text-sm py-1.5 px-3"
            >
              👁️ Ver como público
            </a>
            <Link to="/portfolio/nova-obra" className="btn-primary text-sm py-1.5 px-3">
              + Nova obra
            </Link>
          </div>
        </div>

        <GridPortfolio
          obras={obras}
          carregando={carregando}
          modoEdicao={true}
          onRemover={setObraParaRemover}
        />
      </main>

      {obraParaRemover && (
        <ModalConfirmacao
          titulo="Remover obra"
          mensagem={`Tem certeza que deseja remover "${obraParaRemover.titulo}"? Esta ação não pode ser desfeita.`}
          onConfirmar={confirmarRemocao}
          onCancelar={() => setObraParaRemover(null)}
          carregando={removendo}
        />
      )}
    </div>
  )
}
