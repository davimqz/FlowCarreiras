import { useState } from 'react'
import { Link } from 'react-router-dom'

const ICONES_TIPO = {
  IMAGEM: '🖼️',
  AUDIO: '🎵',
  VIDEO: '🎬',
  EMBED: '▶️',
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
          onError={() => setErro(true)}
          className={`w-full h-full object-cover transition-opacity duration-300 ${carregou ? 'opacity-100' : 'opacity-0'}`}
        />
        {erro && (
          <div className="absolute inset-0 flex items-center justify-center text-gray-500 text-4xl">🖼️</div>
        )}
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
  return (
    <div className="card group relative hover:ring-2 hover:ring-brand transition-all">
      <Thumbnail obra={obra} />

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
    </div>
  )
}
