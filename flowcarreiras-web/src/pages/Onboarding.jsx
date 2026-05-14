import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import SeletorTags from '../components/SeletorTags'
import {
  obterPerfilOnboarding,
  salvarEtapaArea,
  salvarEtapaCidade,
  salvarEtapaBio,
  salvarEtapaTags,
  pularEtapa,
  concluirOnboarding,
} from '../api/perfil'

const ETAPAS_OPCIONALES = [
  { id: 'area',   label: 'Área artística' },
  { id: 'cidade', label: 'Localização' },
  { id: 'bio',    label: 'Sobre você' },
  { id: 'tags',   label: 'O que quer aprender' },
]

const FEEDBACK = {
  area:   'Ótimo! Agora as pessoas certas vão te encontrar.',
  cidade: 'Recife tem talento demais. Você está no mapa!',
  bio:    'Sua história é única. As pessoas vão adorar te conhecer!',
  tags:   'Incrível! Já conseguimos te conectar com os mentores certos.',
}

// 0 = boas-vindas, 1-4 = etapas opcionais
const IDX_BOAS_VINDAS = 0

function calcularEtapaInicial(status) {
  if (!status) return IDX_BOAS_VINDAS
  const todasPendentes = ['area', 'cidade', 'bio', 'tags'].every(
    k => status[`statusEtapa${k.charAt(0).toUpperCase() + k.slice(1)}`] === 'PENDENTE'
  )
  if (todasPendentes) return IDX_BOAS_VINDAS
  const primeiraIdx = ETAPAS_OPCIONALES.findIndex(
    e => status[`statusEtapa${e.id.charAt(0).toUpperCase() + e.id.slice(1)}`] === 'PENDENTE'
  )
  return primeiraIdx === -1 ? ETAPAS_OPCIONALES.length + 1 : primeiraIdx + 1
}

function calcularProgresso(status) {
  if (!status) return 0
  const concluidas = ETAPAS_OPCIONALES.filter(e => {
    const val = status[`statusEtapa${e.id.charAt(0).toUpperCase() + e.id.slice(1)}`]
    return val === 'CONCLUIDA' || val === 'PULADA'
  }).length
  return Math.round((concluidas / ETAPAS_OPCIONALES.length) * 100)
}

export default function Onboarding() {
  const { onboardingConcluido, marcarOnboardingConcluido } = useAuth()
  const navigate = useNavigate()

  const [etapaAtual, setEtapaAtual] = useState(IDX_BOAS_VINDAS)
  const [status, setStatus] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)
  const [feedback, setFeedback] = useState(null) // { msg, tipo }

  // Valores dos campos por etapa
  const [area, setArea] = useState('')
  const [cidade, setCidade] = useState('')
  const [bio, setBio] = useState('')
  const [tagsSelecionadas, setTagsSelecionadas] = useState([])

  useEffect(() => {
    if (onboardingConcluido) {
      navigate('/portfolio/minhas-obras', { replace: true })
      return
    }
    obterPerfilOnboarding()
      .then(s => {
        setStatus(s)
        setArea(s.areaArtisticaPrincipal || '')
        setCidade(s.cidade || '')
        setBio(s.bio || '')
        const inicial = calcularEtapaInicial(s)
        if (inicial > ETAPAS_OPCIONALES.length) {
          // Todos os passos já foram tratados mas onboarding não foi concluído — finaliza
          return concluirOnboarding().then(() => {
            marcarOnboardingConcluido()
            navigate('/portfolio/minhas-obras', { replace: true })
          })
        }
        setEtapaAtual(inicial)
      })
      .finally(() => setCarregando(false))
  }, []) // eslint-disable-line react-hooks/exhaustive-deps

  function mostrarFeedback(etapaId, novoStatus) {
    setStatus(novoStatus)
    setFeedback(FEEDBACK[etapaId])
    setTimeout(() => {
      setFeedback(null)
      avancar()
    }, 1500)
  }

  function avancar() {
    const proxima = etapaAtual + 1
    if (proxima > ETAPAS_OPCIONALES.length) {
      finalizarOnboarding()
    } else {
      setEtapaAtual(proxima)
    }
  }

  async function finalizarOnboarding() {
    setSalvando(true)
    try {
      await concluirOnboarding()
      marcarOnboardingConcluido()
      navigate('/portfolio/minhas-obras', { replace: true })
    } finally {
      setSalvando(false)
    }
  }

  async function handleSalvarArea() {
    if (!area.trim()) return
    setSalvando(true)
    try {
      const novo = await salvarEtapaArea(area.trim())
      mostrarFeedback('area', novo)
    } finally {
      setSalvando(false)
    }
  }

  async function handleSalvarCidade() {
    if (!cidade.trim()) return
    setSalvando(true)
    try {
      const novo = await salvarEtapaCidade(cidade.trim())
      mostrarFeedback('cidade', novo)
    } finally {
      setSalvando(false)
    }
  }

  async function handleSalvarBio() {
    if (!bio.trim()) return
    setSalvando(true)
    try {
      const novo = await salvarEtapaBio(bio.trim())
      mostrarFeedback('bio', novo)
    } finally {
      setSalvando(false)
    }
  }

  async function handleSalvarTags() {
    if (tagsSelecionadas.length === 0) return
    setSalvando(true)
    try {
      const novo = await salvarEtapaTags(tagsSelecionadas.map(t => t.id))
      mostrarFeedback('tags', novo)
    } finally {
      setSalvando(false)
    }
  }

  async function handlePular(etapaId) {
    setSalvando(true)
    try {
      const novo = await pularEtapa(etapaId)
      setStatus(novo)
      avancar()
    } finally {
      setSalvando(false)
    }
  }

  if (carregando) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-gray-400 text-sm">Carregando...</div>
      </div>
    )
  }

  const progresso = calcularProgresso(status)
  const etapaOpcional = etapaAtual >= 1 ? ETAPAS_OPCIONALES[etapaAtual - 1] : null

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-4 py-8">
      <div className="w-full max-w-md">

        {/* Barra de progresso (visível apenas nas etapas opcionais) */}
        {etapaAtual >= 1 && (
          <div className="mb-6">
            <div className="flex justify-between text-xs text-gray-400 mb-1.5">
              <span>Etapa {etapaAtual} de {ETAPAS_OPCIONALES.length}</span>
              <span>{progresso}% concluído</span>
            </div>
            <div className="h-1.5 bg-gray-800 rounded-full overflow-hidden">
              <div
                className="h-full bg-brand rounded-full transition-all duration-500"
                style={{ width: `${progresso}%` }}
              />
            </div>
          </div>
        )}

        {/* Feedback animado */}
        {feedback && (
          <div className="mb-4 px-4 py-3 bg-green-900/30 border border-green-700 rounded-xl text-green-400 text-sm text-center animate-pulse">
            {feedback}
          </div>
        )}

        <div className="card p-6 md:p-8">

          {/* Etapa 0 — Boas-vindas */}
          {etapaAtual === IDX_BOAS_VINDAS && (
            <div className="text-center">
              <div className="text-4xl mb-4">🎨</div>
              <h1 className="text-2xl font-bold text-brand mb-2">Bem-vinda ao Flow Carreiras</h1>
              <p className="text-gray-300 text-sm leading-relaxed mb-2">
                Uma plataforma feita <strong>por</strong> e <strong>para</strong> artistas emergentes de Recife.
              </p>
              <ul className="text-gray-400 text-sm space-y-2 text-left my-5 px-2">
                <li className="flex items-start gap-2">
                  <span className="text-brand mt-0.5">•</span>
                  <span><strong className="text-white">Portfólio profissional</strong> sem depender de outras plataformas</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="text-brand mt-0.5">•</span>
                  <span><strong className="text-white">Mentoria entre artistas</strong> baseada em compatibilidade real</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="text-brand mt-0.5">•</span>
                  <span><strong className="text-white">Exposição justa</strong> sem likes, sem algoritmo de popularidade</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="text-brand mt-0.5">•</span>
                  <span><strong className="text-white">Oportunidades centralizadas</strong> — editais, vagas, workshops</span>
                </li>
              </ul>
              <p className="text-gray-500 text-xs mb-6">
                Vamos configurar seu perfil em 4 etapas rápidas. Você pode pular qualquer uma.
              </p>
              <button
                onClick={() => setEtapaAtual(1)}
                className="btn-primary w-full py-3 text-base"
              >
                Começar
              </button>
            </div>
          )}

          {/* Etapa 1 — Área artística */}
          {etapaAtual === 1 && (
            <EtapaWrapper
              titulo="Qual é sua área artística principal?"
              descricao="Isso ajuda outros artistas e mentores a te encontrarem."
              salvando={salvando}
              podeSalvar={area.trim().length > 0}
              onSalvar={handleSalvarArea}
              onPular={() => handlePular('area')}
              onPularTudo={finalizarOnboarding}
            >
              <input
                className="input"
                value={area}
                onChange={e => setArea(e.target.value)}
                placeholder="Ex: Música, Artes Visuais, Teatro, Dança..."
                maxLength={100}
                autoFocus
              />
            </EtapaWrapper>
          )}

          {/* Etapa 2 — Cidade */}
          {etapaAtual === 2 && (
            <EtapaWrapper
              titulo="Em qual cidade você está sediada?"
              descricao="Conectamos artistas da mesma região para colaborações presenciais."
              salvando={salvando}
              podeSalvar={cidade.trim().length > 0}
              onSalvar={handleSalvarCidade}
              onPular={() => handlePular('cidade')}
              onPularTudo={finalizarOnboarding}
            >
              <input
                className="input"
                value={cidade}
                onChange={e => setCidade(e.target.value)}
                placeholder="Ex: Recife, Olinda, Caruaru..."
                maxLength={100}
                autoFocus
              />
            </EtapaWrapper>
          )}

          {/* Etapa 3 — Bio */}
          {etapaAtual === 3 && (
            <EtapaWrapper
              titulo="Conte um pouco sobre você"
              descricao="Uma apresentação breve ajuda outras artistas a te conhecerem antes de fazer contato."
              salvando={salvando}
              podeSalvar={bio.trim().length > 0}
              onSalvar={handleSalvarBio}
              onPular={() => handlePular('bio')}
              onPularTudo={finalizarOnboarding}
            >
              <textarea
                className="input resize-none"
                rows={4}
                value={bio}
                onChange={e => setBio(e.target.value)}
                placeholder="Ex: Sou ilustradora independente com foco em identidade visual para artistas..."
                maxLength={1000}
                autoFocus
              />
              <p className="text-xs text-gray-500 mt-1 text-right">{bio.length}/1000</p>
            </EtapaWrapper>
          )}

          {/* Etapa 4 — Tags de necessidade */}
          {etapaAtual === 4 && (
            <EtapaWrapper
              titulo="O que você quer desenvolver?"
              descricao="Adicione tags das áreas em que busca orientação. Usamos isso para encontrar mentores compatíveis."
              salvando={salvando}
              podeSalvar={tagsSelecionadas.length > 0}
              onSalvar={handleSalvarTags}
              onPular={() => handlePular('tags')}
              onPularTudo={finalizarOnboarding}
              labelSalvar="Finalizar perfil"
            >
              <SeletorTags
                tagsSelecionadas={tagsSelecionadas}
                onChange={setTagsSelecionadas}
                maxTags={10}
              />
              {tagsSelecionadas.length === 0 && (
                <p className="text-xs text-gray-500 mt-2">
                  Selecione ao menos uma tag para encontrar mentores compatíveis.
                </p>
              )}
            </EtapaWrapper>
          )}

        </div>

        {/* Indicador de etapas */}
        {etapaAtual >= 1 && (
          <div className="flex justify-center gap-2 mt-5">
            {ETAPAS_OPCIONALES.map((e, i) => {
              const statusKey = `statusEtapa${e.id.charAt(0).toUpperCase() + e.id.slice(1)}`
              const s = status?.[statusKey]
              const ativa = i + 1 === etapaAtual
              const concluida = s === 'CONCLUIDA'
              const pulada = s === 'PULADA'
              return (
                <div
                  key={e.id}
                  title={e.label}
                  className={`h-2 rounded-full transition-all duration-300 ${
                    ativa ? 'w-6 bg-brand' :
                    concluida ? 'w-2 bg-brand/60' :
                    pulada ? 'w-2 bg-gray-600' :
                    'w-2 bg-gray-700'
                  }`}
                />
              )
            })}
          </div>
        )}
      </div>
    </div>
  )
}

function EtapaWrapper({ titulo, descricao, children, salvando, podeSalvar, onSalvar, onPular, onPularTudo, labelSalvar }) {
  return (
    <div>
      <h2 className="text-xl font-semibold mb-1">{titulo}</h2>
      {descricao && <p className="text-gray-400 text-sm mb-5">{descricao}</p>}

      <div className="mb-5">{children}</div>

      <div className="space-y-2">
        <button
          type="button"
          onClick={onSalvar}
          disabled={salvando || !podeSalvar}
          className="btn-primary w-full py-2.5 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {salvando ? 'Salvando...' : (labelSalvar || 'Próximo')}
        </button>
        <button
          type="button"
          onClick={onPular}
          disabled={salvando}
          className="w-full py-2 text-sm text-gray-400 hover:text-white transition-colors"
        >
          Pular por agora
        </button>
      </div>

      <div className="mt-4 pt-4 border-t border-gray-800 text-center">
        <button
          type="button"
          onClick={onPularTudo}
          disabled={salvando}
          className="text-xs text-gray-600 hover:text-gray-400 transition-colors"
        >
          Explorar a plataforma agora
        </button>
      </div>
    </div>
  )
}
