import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { listarOportunidades } from '../api/oportunidades'
import SeletorTags from '../components/SeletorTags'
import CardOportunidade from '../components/CardOportunidade'

const TIPOS = [
  { id: 'EDITAL', label: 'Editais' },
  { id: 'VAGA', label: 'Vagas' },
  { id: 'EVENTO', label: 'Eventos' },
  { id: 'OPORTUNIDADE', label: 'Oportunidades' },
]

export default function PortalOportunidades() {
  const { usuario, logout } = useAuth()
  const [carregando, setCarregando] = useState(true)
  const [oportunidades, setOportunidades] = useState([])

  const [query, setQuery] = useState('')
  const [area, setArea] = useState('')
  const [localidade, setLocalidade] = useState('')
  const [tagsSelecionadas, setTagsSelecionadas] = useState([])
  const [tiposSelecionados, setTiposSelecionados] = useState(['EDITAL', 'VAGA', 'EVENTO', 'OPORTUNIDADE'])

  async function carregar() {
    setCarregando(true)
    try {
      const dados = await listarOportunidades({
        query,
        area,
        localidade,
        tags: tagsSelecionadas.map(t => t.nome ?? t),
        tipos: tiposSelecionados,
      })
      setOportunidades(dados)
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregar()
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  function toggleTipo(tipo) {
    setTiposSelecionados(prev =>
      prev.includes(tipo) ? prev.filter(t => t !== tipo) : [...prev, tipo]
    )
  }

  return (
    <div className="min-h-screen">
      <header className="bg-card border-b border-gray-800 sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
          <h1 className="font-bold text-brand">Flow Carreiras</h1>
          <nav className="flex items-center gap-4">
            <Link to="/oportunidades" className="text-sm text-gray-300 hover:text-white font-medium border-b-2 border-brand pb-0.5">
              Oportunidades
            </Link>
            <Link to="/portfolio/minhas-obras" className="text-sm text-gray-400 hover:text-white">
              Portfolio
            </Link>
            <Link to="/meu-perfil" className="text-sm text-gray-400 hover:text-white">
              Meu Perfil
            </Link>
            <Link to="/mentores" className="text-sm text-gray-400 hover:text-white">
              Mentores
            </Link>
            <span className="text-gray-600">|</span>
            <span className="text-sm text-gray-400 hidden sm:block">{usuario?.nome}</span>
            <button onClick={logout} className="text-xs text-gray-500 hover:text-white">Sair</button>
          </nav>
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-4 py-6">
        {!usuario?.onboardingConcluido && (
          <div className="bg-card border border-gray-800 rounded-xl p-4 mb-6">
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
              <div>
                <p className="text-sm text-white font-semibold">Complete seu cadastro para desbloquear mais oportunidades</p>
                <p className="text-xs text-gray-500">{usuario?.percentualCompletude ?? 0}% concluido</p>
              </div>
              <Link to="/onboarding" className="btn-primary text-sm px-4 py-2">Continuar cadastro</Link>
            </div>
            <div className="w-full h-1.5 bg-gray-800 rounded-full overflow-hidden mt-3">
              <div className="h-full bg-brand" style={{ width: `${usuario?.percentualCompletude ?? 0}%` }} />
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          <aside className="lg:col-span-1 space-y-4">
            <div className="bg-card rounded-xl p-4">
              <h2 className="text-sm font-semibold mb-3">Filtros</h2>
              <div className="space-y-3">
                <input
                  className="input"
                  placeholder="Buscar por palavra-chave"
                  value={query}
                  onChange={e => setQuery(e.target.value)}
                />
                <input
                  className="input"
                  placeholder="Area artistica"
                  value={area}
                  onChange={e => setArea(e.target.value)}
                />
                <input
                  className="input"
                  placeholder="Localidade"
                  value={localidade}
                  onChange={e => setLocalidade(e.target.value)}
                />
              </div>
            </div>

            <div className="bg-card rounded-xl p-4">
              <h3 className="text-sm font-semibold mb-3">Tipos</h3>
              <div className="flex flex-wrap gap-2">
                {TIPOS.map(tipo => (
                  <button
                    key={tipo.id}
                    onClick={() => toggleTipo(tipo.id)}
                    className={`text-xs px-3 py-1.5 rounded-full border transition-colors ${
                      tiposSelecionados.includes(tipo.id)
                        ? 'border-brand text-brand'
                        : 'border-gray-700 text-gray-500 hover:text-white'
                    }`}
                  >
                    {tipo.label}
                  </button>
                ))}
              </div>
            </div>

            <div className="bg-card rounded-xl p-4">
              <h3 className="text-sm font-semibold mb-3">Tags</h3>
              <SeletorTags tagsSelecionadas={tagsSelecionadas} onChange={setTagsSelecionadas} maxTags={10} />
            </div>

            <button onClick={carregar} className="btn-primary w-full">Aplicar filtros</button>
          </aside>

          <section className="lg:col-span-3">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h2 className="text-xl font-semibold">Portal de Oportunidades</h2>
                <p className="text-sm text-gray-400">{oportunidades.length} resultado(s)</p>
              </div>
              <button onClick={carregar} className="btn-secondary text-sm px-3 py-1.5">Atualizar</button>
            </div>

            {carregando ? (
              <div className="text-gray-400 text-sm">Carregando oportunidades...</div>
            ) : oportunidades.length === 0 ? (
              <div className="bg-card rounded-xl p-6 text-center text-gray-400">
                Nenhuma oportunidade encontrada. Tente ajustar os filtros.
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {oportunidades.map(o => (
                  <CardOportunidade key={o.id} oportunidade={o} />
                ))}
              </div>
            )}
          </section>
        </div>
      </main>
    </div>
  )
}
