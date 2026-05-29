export default function CardOportunidade({ oportunidade }) {
  return (
    <div className="card p-4 hover:ring-2 hover:ring-brand transition-all">
      <div className="flex items-center justify-between gap-3 mb-2">
        <span className="text-xs px-2 py-1 rounded-full bg-brand/20 text-brand">
          {oportunidade.tipo}
        </span>
        {oportunidade.data && (
          <span className="text-xs text-gray-500">{oportunidade.data}</span>
        )}
      </div>
      <h3 className="font-semibold text-white mb-1">
        {oportunidade.titulo || 'Oportunidade'}
      </h3>
      {oportunidade.descricao && (
        <p className="text-sm text-gray-400 line-clamp-3 mb-3">
          {oportunidade.descricao}
        </p>
      )}
      <div className="flex flex-wrap gap-2 text-xs text-gray-500 mb-3">
        {oportunidade.localidade && (
          <span className="px-2 py-1 rounded-full bg-gray-800">{oportunidade.localidade}</span>
        )}
        {oportunidade.areaArtistica && (
          <span className="px-2 py-1 rounded-full bg-gray-800">{oportunidade.areaArtistica}</span>
        )}
        {oportunidade.fonte && (
          <span className="px-2 py-1 rounded-full bg-gray-800">{oportunidade.fonte}</span>
        )}
      </div>
      {oportunidade.tags?.length > 0 && (
        <div className="flex flex-wrap gap-1 mb-4">
          {oportunidade.tags.slice(0, 5).map((tag, idx) => (
            <span key={`${tag}-${idx}`} className="tag-chip">{tag}</span>
          ))}
          {oportunidade.tags.length > 5 && (
            <span className="tag-chip">+{oportunidade.tags.length - 5}</span>
          )}
        </div>
      )}
      {oportunidade.link && (
        <a
          href={oportunidade.link}
          target="_blank"
          rel="noopener noreferrer"
          className="btn-secondary text-sm px-3 py-1.5 inline-flex"
        >
          Ver detalhes
        </a>
      )}
    </div>
  )
}
