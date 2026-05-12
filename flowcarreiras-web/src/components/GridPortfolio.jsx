import CardObra from './CardObra'
import { SkeletonGrid } from './SkeletonLoader'

export default function GridPortfolio({ obras, carregando, modoEdicao, onRemover }) {
  if (carregando) return <SkeletonGrid />

  if (!obras?.length) {
    return (
      <div className="flex flex-col items-center justify-center py-20 text-center text-gray-500">
        <span className="text-6xl mb-4">🎨</span>
        <p className="text-lg font-medium">Nenhuma obra ainda</p>
        <p className="text-sm mt-1">Faça upload da sua primeira obra!</p>
      </div>
    )
  }

  return (
    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      {obras.map(obra => (
        <CardObra
          key={obra.id}
          obra={obra}
          modoEdicao={modoEdicao}
          onRemover={onRemover}
        />
      ))}
    </div>
  )
}
