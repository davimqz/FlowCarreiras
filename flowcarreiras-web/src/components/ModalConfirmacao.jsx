import { useEffect } from 'react'

export default function ModalConfirmacao({ titulo, mensagem, onConfirmar, onCancelar, carregando }) {
  // Fecha com Escape
  useEffect(() => {
    const handler = (e) => { if (e.key === 'Escape') onCancelar() }
    window.addEventListener('keydown', handler)
    return () => window.removeEventListener('keydown', handler)
  }, [onCancelar])

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm px-4"
      onClick={onCancelar}
    >
      <div
        className="bg-card rounded-2xl p-6 max-w-sm w-full shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="text-lg font-semibold mb-2">{titulo}</h2>
        <p className="text-gray-400 text-sm mb-6">{mensagem}</p>
        <div className="flex gap-3">
          <button
            onClick={onCancelar}
            className="flex-1 btn-secondary"
            disabled={carregando}
          >
            Cancelar
          </button>
          <button
            onClick={onConfirmar}
            className="flex-1 btn-danger"
            disabled={carregando}
          >
            {carregando ? 'Removendo...' : 'Remover'}
          </button>
        </div>
      </div>
    </div>
  )
}
