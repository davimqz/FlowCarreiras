import { useState } from 'react'
import { resolverUrlBackend } from '../config/runtime'

const ICONES_TIPO = {
  IMAGEM: '🖼️',
  AUDIO: '🎵',
  VIDEO: '🎬',
  EMBED: '▶️',
}

// Converte uma URL de página do YouTube/Vimeo na URL de player incorporável.
export function urlEmbed(url) {
  if (!url) return null
  const yt = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([\w-]+)/)
  if (yt) return `https://www.youtube.com/embed/${yt[1]}`
  const vimeo = url.match(/vimeo\.com\/(\d+)/)
  if (vimeo) return `https://player.vimeo.com/video/${vimeo[1]}`
  return url
}

/**
 * Renderiza a mídia de uma obra. `grande` alterna entre a miniatura (grade) e a
 * versão ampliada (modal de detalhe).
 */
export default function MidiaObra({ obra, grande = false }) {
  const [carregou, setCarregou] = useState(false)
  const [erro, setErro] = useState(false)
  const urlMidia = resolverUrlBackend(obra.urlMidia)

  if (obra.tipoMidia === 'IMAGEM' && urlMidia) {
    if (grande) {
      return (
        <div className="flex w-full items-center justify-center bg-black/40">
          {erro ? (
            <div className="flex h-72 items-center justify-center text-6xl text-gray-500">🖼️</div>
          ) : (
            <img
              src={urlMidia}
              alt={obra.titulo}
              onError={() => setErro(true)}
              className="max-h-[72vh] w-auto object-contain"
            />
          )}
        </div>
      )
    }
    return (
      <div className="relative w-full aspect-square bg-card">
        {!carregou && <div className="skeleton absolute inset-0" />}
        <img
          src={urlMidia}
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

  if (obra.tipoMidia === 'VIDEO' && urlMidia) {
    return (
      <div className={`w-full bg-black ${grande ? 'aspect-video' : 'aspect-square'}`}>
        <video src={urlMidia} controls preload="metadata" className="w-full h-full object-contain" />
      </div>
    )
  }

  if (obra.tipoMidia === 'AUDIO' && urlMidia) {
    return (
      <div className={`w-full bg-card flex flex-col items-center justify-center gap-4 ${grande ? 'py-12 px-6' : 'aspect-square p-4'}`}>
        <span className={grande ? 'text-7xl' : 'text-5xl'}>🎵</span>
        <audio src={urlMidia} controls preload="metadata" className="w-full max-w-md" />
      </div>
    )
  }

  if (obra.tipoMidia === 'EMBED' && obra.urlMidia) {
    return (
      <div className={`w-full bg-black ${grande ? 'aspect-video' : 'aspect-square'}`}>
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
    <div className={`w-full bg-card flex items-center justify-center ${grande ? 'py-20 text-7xl' : 'aspect-square text-5xl'}`}>
      {ICONES_TIPO[obra.tipoMidia] ?? '📁'}
    </div>
  )
}
