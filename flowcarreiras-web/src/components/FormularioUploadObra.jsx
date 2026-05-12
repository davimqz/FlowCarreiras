import { useState, useRef } from 'react'
import { useFileUpload } from '../hooks/useFileUpload'
import SeletorTags from './SeletorTags'

const TIPOS_MIDIA = [
  { value: 'IMAGEM', label: '🖼️ Imagem (JPG/PNG, até 10 MB)' },
  { value: 'AUDIO', label: '🎵 Áudio (MP3/WAV, até 30 MB)' },
  { value: 'VIDEO', label: '🎬 Vídeo (MP4, até 30 MB)' },
  { value: 'EMBED', label: '▶️ Link Embed (YouTube/Vimeo)' },
]

export default function FormularioUploadObra({ valorInicial, onSubmit, carregando }) {
  const [form, setForm] = useState({
    titulo: valorInicial?.titulo ?? '',
    descricao: valorInicial?.descricao ?? '',
    tipoMidia: valorInicial?.tipoMidia ?? 'IMAGEM',
    urlMidia: valorInicial?.urlMidia ?? '',
    status: valorInicial?.status ?? 'RASCUNHO',
    tags: valorInicial?.tags ?? [],
  })
  const [dragging, setDragging] = useState(false)
  const [erroForm, setErroForm] = useState(null)
  const fileInputRef = useRef(null)

  const upload = useFileUpload(form.tipoMidia)

  function set(campo, valor) {
    setForm(f => ({ ...f, [campo]: valor }))
  }

  function handleDrop(e) {
    e.preventDefault()
    setDragging(false)
    const file = e.dataTransfer.files[0]
    if (file) upload.selecionarArquivo(file)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErroForm(null)

    if (form.tags.length === 0) {
      setErroForm('Adicione ao menos uma tag antes de publicar')
      return
    }

    if (form.tipoMidia !== 'EMBED' && !upload.arquivo && !valorInicial?.urlMidia) {
      setErroForm('Selecione um arquivo de mídia')
      return
    }

    const dados = {
      titulo: form.titulo,
      descricao: form.descricao,
      tipoMidia: form.tipoMidia,
      urlMidia: form.tipoMidia === 'EMBED' ? form.urlMidia : undefined,
      status: form.status,
      tagIds: form.tags.map(t => t.id),
    }

    upload.setUploading(true)
    try {
      await onSubmit(dados, upload.arquivo, upload.setProgresso)
    } catch (err) {
      const msg = err.response?.data?.mensagem ?? 'Erro ao salvar. Tente novamente.'
      setErroForm(msg)
      upload.setUploading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-5">

      {/* Título */}
      <div>
        <label className="block text-sm font-medium mb-1">Título *</label>
        <input
          className="input"
          value={form.titulo}
          onChange={e => set('titulo', e.target.value)}
          placeholder="Nome da obra"
          required
          maxLength={200}
        />
      </div>

      {/* Descrição */}
      <div>
        <label className="block text-sm font-medium mb-1">Descrição</label>
        <textarea
          className="input resize-none"
          rows={3}
          value={form.descricao}
          onChange={e => set('descricao', e.target.value)}
          placeholder="Conte sobre esta obra..."
          maxLength={2000}
        />
      </div>

      {/* Tipo de mídia */}
      <div>
        <label className="block text-sm font-medium mb-1">Tipo de mídia *</label>
        <select
          className="input"
          value={form.tipoMidia}
          onChange={e => { set('tipoMidia', e.target.value); upload.limpar() }}
        >
          {TIPOS_MIDIA.map(t => (
            <option key={t.value} value={t.value}>{t.label}</option>
          ))}
        </select>
      </div>

      {/* Upload / Embed */}
      {form.tipoMidia === 'EMBED' ? (
        <div>
          <label className="block text-sm font-medium mb-1">URL do vídeo *</label>
          <input
            className="input"
            value={form.urlMidia}
            onChange={e => set('urlMidia', e.target.value)}
            placeholder="https://www.youtube.com/watch?v=..."
            required
          />
          <p className="text-xs text-gray-500 mt-1">Apenas YouTube ou Vimeo</p>
        </div>
      ) : (
        <div>
          <label className="block text-sm font-medium mb-1">
            Arquivo {!valorInicial && '*'}
          </label>

          {/* Área de drag-and-drop */}
          <div
            onDragOver={e => { e.preventDefault(); setDragging(true) }}
            onDragLeave={() => setDragging(false)}
            onDrop={handleDrop}
            onClick={() => fileInputRef.current?.click()}
            className={`border-2 border-dashed rounded-xl p-6 text-center cursor-pointer transition-colors ${
              dragging ? 'border-brand bg-brand/10' : 'border-gray-700 hover:border-gray-500'
            }`}
          >
            {upload.preview ? (
              <img src={upload.preview} alt="Preview" className="max-h-40 mx-auto rounded-lg object-contain" />
            ) : upload.arquivo ? (
              <p className="text-sm text-gray-400">📎 {upload.arquivo.name}</p>
            ) : valorInicial?.urlMidia ? (
              <p className="text-sm text-gray-500">Arquivo atual mantido. Clique para substituir.</p>
            ) : (
              <>
                <p className="text-2xl mb-2">📂</p>
                <p className="text-sm text-gray-400">Arraste o arquivo ou clique para selecionar</p>
              </>
            )}
            <input
              ref={fileInputRef}
              type="file"
              className="hidden"
              onChange={e => upload.selecionarArquivo(e.target.files[0])}
            />
          </div>

          {upload.erro && <p className="text-red-400 text-xs mt-1">{upload.erro}</p>}

          {/* Barra de progresso */}
          {upload.uploading && upload.progresso > 0 && (
            <div className="mt-3">
              <div className="flex justify-between text-xs text-gray-400 mb-1">
                <span>Enviando...</span>
                <span>{upload.progresso}%</span>
              </div>
              <div className="w-full bg-gray-700 rounded-full h-2">
                <div
                  className="bg-brand h-2 rounded-full transition-all duration-200"
                  style={{ width: `${upload.progresso}%` }}
                />
              </div>
            </div>
          )}
        </div>
      )}

      {/* Tags */}
      <div>
        <label className="block text-sm font-medium mb-1">
          Tags * <span className="text-gray-500 font-normal">(mínimo 1, máximo 10)</span>
        </label>
        <SeletorTags
          tagsSelecionadas={form.tags}
          onChange={tags => set('tags', tags)}
        />
      </div>

      {/* Status */}
      <div>
        <label className="block text-sm font-medium mb-2">Status</label>
        <div className="flex gap-3">
          {['RASCUNHO', 'PUBLICADA'].map(s => (
            <label key={s} className="flex items-center gap-2 cursor-pointer">
              <input
                type="radio"
                name="status"
                value={s}
                checked={form.status === s}
                onChange={() => set('status', s)}
                className="accent-brand"
              />
              <span className="text-sm">{s === 'RASCUNHO' ? '📝 Rascunho' : '🌐 Publicada'}</span>
            </label>
          ))}
        </div>
      </div>

      {erroForm && (
        <div className="bg-red-900/30 border border-red-700 rounded-lg px-4 py-3 text-red-400 text-sm">
          {erroForm}
        </div>
      )}

      <button
        type="submit"
        disabled={carregando || upload.uploading}
        className="btn-primary w-full py-3"
      >
        {(carregando || upload.uploading) ? 'Salvando...' : 'Salvar obra'}
      </button>
    </form>
  )
}
