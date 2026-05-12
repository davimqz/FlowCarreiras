import { useState, useEffect, useCallback, useRef } from 'react'
import { buscarTags } from '../api/tags'

export function useTagAutocomplete() {
  const [query, setQuery] = useState('')
  const [sugestoes, setSugestoes] = useState([])
  const [carregando, setCarregando] = useState(false)
  const debounceRef = useRef(null)

  const buscar = useCallback((q) => {
    clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(async () => {
      setCarregando(true)
      try {
        const resultado = await buscarTags(q)
        setSugestoes(resultado)
      } catch {
        setSugestoes([])
      } finally {
        setCarregando(false)
      }
    }, 250)
  }, [])

  useEffect(() => {
    buscar(query)
    return () => clearTimeout(debounceRef.current)
  }, [query, buscar])

  return { query, setQuery, sugestoes, carregando }
}
