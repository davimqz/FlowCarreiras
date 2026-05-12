import { useState, useCallback } from 'react'

const TIPOS_PERMITIDOS = {
  IMAGEM: ['image/jpeg', 'image/png'],
  AUDIO: ['audio/mpeg', 'audio/wav'],
  VIDEO: ['video/mp4'],
}

const MAX_TAMANHOS = {
  IMAGEM: 10 * 1024 * 1024,   // 10 MB
  AUDIO: 30 * 1024 * 1024,    // 30 MB
  VIDEO: 30 * 1024 * 1024,    // 30 MB
}

export function useFileUpload(tipoMidia) {
  const [arquivo, setArquivo] = useState(null)
  const [preview, setPreview] = useState(null)
  const [progresso, setProgresso] = useState(0)
  const [erro, setErro] = useState(null)
  const [uploading, setUploading] = useState(false)

  const validar = useCallback((file) => {
    if (!tipoMidia || tipoMidia === 'EMBED') return null

    const permitidos = TIPOS_PERMITIDOS[tipoMidia]
    if (!permitidos?.includes(file.type)) {
      return `Formato não suportado. Permitidos: ${permitidos?.join(', ')}`
    }

    const maxBytes = MAX_TAMANHOS[tipoMidia]
    if (file.size > maxBytes) {
      return `Arquivo muito grande. Máximo: ${maxBytes / (1024 * 1024)} MB`
    }

    return null
  }, [tipoMidia])

  const selecionarArquivo = useCallback((file) => {
    setErro(null)
    const mensagemErro = validar(file)
    if (mensagemErro) {
      setErro(mensagemErro)
      return false
    }

    setArquivo(file)

    // Gera preview para imagens
    if (file.type.startsWith('image/')) {
      const reader = new FileReader()
      reader.onload = (e) => setPreview(e.target.result)
      reader.readAsDataURL(file)
    } else {
      setPreview(null)
    }

    return true
  }, [validar])

  const limpar = useCallback(() => {
    setArquivo(null)
    setPreview(null)
    setProgresso(0)
    setErro(null)
    setUploading(false)
  }, [])

  return {
    arquivo,
    preview,
    progresso,
    erro,
    uploading,
    setProgresso,
    setUploading,
    setErro,
    selecionarArquivo,
    limpar,
  }
}
