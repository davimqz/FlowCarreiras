import { useState, useEffect } from 'react'

export default function NetworkStatus() {
  const [offline, setOffline] = useState(!navigator.onLine)

  useEffect(() => {
    const online = () => setOffline(false)
    const offline = () => setOffline(true)
    window.addEventListener('online', online)
    window.addEventListener('offline', offline)
    return () => {
      window.removeEventListener('online', online)
      window.removeEventListener('offline', offline)
    }
  }, [])

  if (!offline) return null

  return (
    <div className="fixed bottom-4 left-1/2 -translate-x-1/2 z-50 bg-yellow-600 text-white text-sm font-medium px-4 py-2 rounded-full shadow-lg flex items-center gap-2">
      <span>⚡</span>
      Você está offline — exibindo conteúdo em cache
    </div>
  )
}
