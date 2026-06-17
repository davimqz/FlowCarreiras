import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { HeartIcon } from './StateIcons'
import ComentariosModal from './ComentariosModal'
import { useAuth } from '../context/AuthContext'
import { obterStatusCurtida, curtir, descurtir } from '../api/curtidas'

const ICONES_TIPO = {
  IMAGEM: '🖼️',
  AUDIO: '🎵',
  VIDEO: '🎬',
  EMBED: '▶️',
}

// Converte uma URL de página do YouTube/Vimeo na URL de player incorporável.
function urlEmbed(url) {
  if (!url) return null
  const yt = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([\w-]+)/)
  if (yt) return `https://www.youtube.com/embed/${yt[1]}`
  const vimeo = url.match(/vimeo\.com\/(\d+)/)
  if (vimeo) return `https://player.vimeo.com/video/${vimeo[1]}`
  return url
}

function Thumbnail({ obra }) {
  const [carregou, setCarregou] = useState(false)
  const [erro, setErro] = useState(false)

  if (obra.tipoMidia === 'IMAGEM' && obra.urlMidia) {
    return (
      <div className="relative w-full aspect-square bg-card">
        {!carregou && <div className="skeleton absolute inset-0" />}
        <img
          src={obra.urlMidia}
          alt={obra.titulo}
          loading="lazy"
          onLoad={() => setCarregou(true)}
          onError={() => { setErro(true); setCarregou(true) }}
          className={`w-full h-full object-cover transition-opacity duration-300 ${carregou ? 'opacity-100' : 'opacity-0'}`}
        />
        {erro && (
          <div className="absolute inset-0 flex items-center justify-center text-gray-500 text-4xl">🖼️</div>
        )}
      </div>
    )
  }

  if (obra.tipoMidia === 'VIDEO' && obra.urlMidia) {
    return (
      <div className="w-full aspect-square bg-black">
        <video
          src={obra.urlMidia}
          controls
          preload="metadata"
          className="w-full h-full object-contain"
        />
      </div>
    )
  }

  if (obra.tipoMidia === 'AUDIO' && obra.urlMidia) {
    return (
      <div className="w-full aspect-square bg-card flex flex-col items-center justify-center gap-4 p-4">
        <span className="text-5xl">🎵</span>
        <audio src={obra.urlMidia} controls preload="metadata" className="w-full" />
      </div>
    )
  }

  if (obra.tipoMidia === 'EMBED' && obra.urlMidia) {
    return (
      <div className="w-full aspect-square bg-black">
        <iframe
          src={urlEmbed(obra.urlMidia)}
          title={obra.titulo}
          loading="lazy"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
          className="w-full h-full border-0"
        />
      </div>
    )
  }

  return (
    <div className="w-full aspect-square bg-card flex items-center justify-center text-5xl">
      {ICONES_TIPO[obra.tipoMidia] ?? '📁'}
    </div>
  )
}

export default function CardObra({ obra, modoEdicao, onRemover }) {
  const { token } = useAuth()
  const navigate = useNavigate()
  const [comentariosAbertos, setComentariosAbertos] = useState(false)
  const [curtido, setCurtido] = useState(false)
  const [totalCurtidas, setTotalCurtidas] = useState(0)
  const [enviandoCurtida, setEnviandoCurtida] = useState(false)

  // Carrega o estado real de curtidas (contagem + se o usuário logado curtiu)
  useEffect(() => {
    let ativo = true
    obterStatusCurtida(obra.id)
      .then(status => {
        if (!ativo) return
        setTotalCurtidas(status.total)
        setCurtido(status.curtidoPeloUsuario)
      })
      .catch(() => { /* falha silenciosa: card continua utilizável sem o contador */ })
    return () => { ativo = false }
  }, [obra.id])

  async function toggleCurtir(e) {
    e.preventDefault()
    e.stopPropagation()

    // Sem login não há como persistir a curtida — leva ao login
    if (!token) {
      navigate('/login')
      return
    }
    if (enviandoCurtida) return

    // Atualização otimista, revertida em caso de erro
    const anterior = { curtido, total: totalCurtidas }
    const proximo = !curtido
    setCurtido(proximo)
    setTotalCurtidas(t => t + (proximo ? 1 : -1))
    setEnviandoCurtida(true)
    try {
      const status = proximo ? await curtir(obra.id) : await descurtir(obra.id)
      setTotalCurtidas(status.total)
      setCurtido(status.curtidoPeloUsuario)
    } catch {
      setCurtido(anterior.curtido)
      setTotalCurtidas(anterior.total)
    } finally {
      setEnviandoCurtida(false)
    }
  }

  return (
    <div className="card group relative hover:ring-2 hover:ring-brand transition-all">
      <Thumbnail obra={obra} />

      {!modoEdicao && (
        <button
          onClick={toggleCurtir}
          disabled={enviandoCurtida}
          aria-label={curtido ? 'Descurtir' : 'Curtir'}
          aria-pressed={curtido}
          className="absolute right-2 top-2 z-10 flex h-9 items-center gap-1 rounded-full bg-black/40 px-2.5 text-white backdrop-blur-sm transition-colors hover:bg-black/60 disabled:opacity-70"
        >
          <HeartIcon filled={curtido} size={20} />
          {totalCurtidas > 0 && (
            <span className="text-xs font-semibold tabular-nums">{totalCurtidas}</span>
          )}
        </button>
      )}

      {obra.status === 'RASCUNHO' && (
        <span className="absolute top-2 left-2 bg-yellow-600/90 text-xs font-semibold px-2 py-0.5 rounded-full">
          Rascunho
        </span>
      )}

      <div className="p-3 space-y-2">
        <h3 className="font-semibold text-sm leading-tight line-clamp-1">{obra.titulo}</h3>

        {obra.tags?.length > 0 && (
          <div className="flex flex-wrap gap-1">
            {obra.tags.slice(0, 3).map(tag => (
              <span key={tag.id} className="tag-chip">{tag.nome}</span>
            ))}
            {obra.tags.length > 3 && (
              <span className="tag-chip">+{obra.tags.length - 3}</span>
            )}
          </div>
        )}

        <button
          onClick={(e) => { e.preventDefault(); e.stopPropagation(); setComentariosAbertos(true) }}
          className="flex items-center gap-1.5 text-xs text-gray-400 transition-colors hover:text-brand-light"
        >
          <span>💬</span> Comentários
        </button>

        {modoEdicao && (
          <div className="flex gap-2 pt-1">
            <Link
              to={`/portfolio/editar/${obra.id}`}
              className="flex-1 text-center text-xs btn-secondary py-1 px-2"
            >
              Editar
            </Link>
            <button
              onClick={() => onRemover(obra)}
              className="flex-1 text-xs btn-danger py-1 px-2"
            >
              Remover
            </button>
          </div>
        )}
      </div>

      {comentariosAbertos && (
        <ComentariosModal obra={obra} onClose={() => setComentariosAbertos(false)} />
      )}
    </div>
  )
}
