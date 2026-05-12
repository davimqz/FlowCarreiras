import { useState, useEffect } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import api from '../api/client'
import GridPortfolio from '../components/GridPortfolio'
import { useAuth } from '../context/AuthContext'

export default function PortfolioPublico() {
  const { urlPublica } = useParams()
  const navigate = useNavigate()
  const { usuario } = useAuth()
  const [obras, setObras] = useState([])
  const [artista, setArtista] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState(null)
  const [copiado, setCopiado] = useState(false)

  const temHistorico = window.history.length > 1
  const isProprioPortfolio = usuario?.urlPublica === urlPublica

  useEffect(() => {
    // O backend expõe obras por artistaId; para URL pública precisamos
    // de um endpoint de lookup — aqui usamos uma chamada aproximada.
    // Em produção, adicione GET /api/perfis/publico/{urlPublica}
    async function carregar() {
      try {
        const { data } = await api.get(`/obras/publico/${urlPublica}`)
        setArtista(data.artista)
        setObras(data.obras)
      } catch {
        setErro('Portfólio não encontrado')
      } finally {
        setCarregando(false)
      }
    }
    carregar()
  }, [urlPublica])

  function copiarUrl() {
    navigator.clipboard.writeText(window.location.href)
    setCopiado(true)
    setTimeout(() => setCopiado(false), 2000)
  }

  if (erro) {
    return (
      <div className="min-h-screen flex items-center justify-center text-center px-4">
        <div>
          <p className="text-6xl mb-4">🔍</p>
          <h1 className="text-2xl font-bold mb-2">Portfólio não encontrado</h1>
          <p className="text-gray-400">A URL pode estar incorreta ou o artista não existe.</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen">
      <header className="bg-card border-b border-gray-800">
        <div className="max-w-5xl mx-auto px-4 py-4 flex items-center justify-between gap-3">
          <div className="flex items-center gap-3 min-w-0">
            {temHistorico && (
              <button
                onClick={() => navigate(-1)}
                className="text-gray-400 hover:text-white text-xl shrink-0"
                aria-label="Voltar"
              >
                ←
              </button>
            )}
            <div className="min-w-0">
              <h1 className="text-xl font-bold truncate">{artista?.nome ?? 'Portfólio'}</h1>
              {artista?.cidade && (
                <p className="text-gray-400 text-sm">📍 {artista.cidade}</p>
              )}
            </div>
          </div>

          <div className="flex items-center gap-2 shrink-0">
            {isProprioPortfolio && (
              <Link to="/portfolio/minhas-obras" className="btn-secondary text-sm py-1.5 px-3">
                ✏️ Editar
              </Link>
            )}
            <button onClick={copiarUrl} className="btn-secondary text-sm py-1.5 px-3">
              {copiado ? '✅ Copiado!' : '🔗 Copiar link'}
            </button>
          </div>
        </div>
        {artista?.bio && (
          <div className="max-w-5xl mx-auto px-4 pb-4">
            <p className="text-gray-300 text-sm">{artista.bio}</p>
          </div>
        )}
      </header>

      <main className="max-w-5xl mx-auto px-4 py-6">
        <GridPortfolio
          obras={obras}
          carregando={carregando}
          modoEdicao={false}
        />
      </main>
    </div>
  )
}
