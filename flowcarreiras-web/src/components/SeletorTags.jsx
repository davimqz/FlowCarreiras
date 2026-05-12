import { useState, useRef, useEffect } from 'react'
import { useTagAutocomplete } from '../hooks/useTagAutocomplete'

export default function SeletorTags({ tagsSelecionadas, onChange, maxTags = 10 }) {
  const [aberto, setAberto] = useState(false)
  const { query, setQuery, sugestoes, carregando } = useTagAutocomplete()
  const inputRef = useRef(null)
  const containerRef = useRef(null)

  // Fecha dropdown ao clicar fora
  useEffect(() => {
    const handler = (e) => {
      if (!containerRef.current?.contains(e.target)) setAberto(false)
    }
    document.addEventListener('mousedown', handler)
    return () => document.removeEventListener('mousedown', handler)
  }, [])

  function adicionarTag(tag) {
    if (tagsSelecionadas.find(t => t.id === tag.id)) return
    if (tagsSelecionadas.length >= maxTags) return
    onChange([...tagsSelecionadas, tag])
    setQuery('')
    inputRef.current?.focus()
  }

  function removerTag(tagId) {
    onChange(tagsSelecionadas.filter(t => t.id !== tagId))
  }

  const disponiveis = sugestoes.filter(s => !tagsSelecionadas.find(t => t.id === s.id))
  const limitAtingido = tagsSelecionadas.length >= maxTags

  return (
    <div ref={containerRef} className="relative">
      {/* Tags selecionadas */}
      <div className="flex flex-wrap gap-2 mb-2">
        {tagsSelecionadas.map(tag => (
          <span key={tag.id} className="tag-chip">
            {tag.nome}
            <button
              type="button"
              onClick={() => removerTag(tag.id)}
              className="hover:text-white ml-1 leading-none"
              aria-label={`Remover ${tag.nome}`}
            >
              ×
            </button>
          </span>
        ))}
      </div>

      {/* Input de busca */}
      {!limitAtingido && (
        <input
          ref={inputRef}
          type="text"
          value={query}
          onChange={(e) => { setQuery(e.target.value); setAberto(true) }}
          onFocus={() => setAberto(true)}
          placeholder="Buscar tag... (ex: ilustração, minimalista)"
          className="input"
        />
      )}

      {limitAtingido && (
        <p className="text-xs text-yellow-400 mt-1">Limite de {maxTags} tags atingido</p>
      )}

      {/* Dropdown */}
      {aberto && !limitAtingido && (
        <div className="absolute z-10 w-full mt-1 bg-card border border-gray-700 rounded-lg shadow-xl max-h-52 overflow-y-auto">
          {carregando && (
            <p className="px-3 py-2 text-gray-500 text-sm">Buscando...</p>
          )}
          {!carregando && disponiveis.length === 0 && (
            <p className="px-3 py-2 text-gray-500 text-sm">Nenhuma tag encontrada</p>
          )}
          {!carregando && disponiveis.map(tag => (
            <button
              key={tag.id}
              type="button"
              onClick={() => adicionarTag(tag)}
              className="w-full text-left px-3 py-2 hover:bg-brand/20 text-sm transition-colors"
            >
              <span className="font-medium">{tag.nome}</span>
              <span className="text-gray-500 text-xs ml-2">
                {tag.categoria?.replace('_', ' ').toLowerCase()}
              </span>
            </button>
          ))}
        </div>
      )}
    </div>
  )
}
