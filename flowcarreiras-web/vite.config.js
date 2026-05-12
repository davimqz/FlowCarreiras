import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.ico', 'robots.txt'],
      manifest: {
        name: 'Flow Carreiras',
        short_name: 'FlowCarreiras',
        description: 'Plataforma para artistas emergentes de Recife',
        theme_color: '#1a1a2e',
        background_color: '#ffffff',
        display: 'standalone',
        start_url: '/',
        icons: [
          { src: '/icons/icon-192.png', sizes: '192x192', type: 'image/png' },
          { src: '/icons/icon-512.png', sizes: '512x512', type: 'image/png' }
        ]
      },
      workbox: {
        // Network-first para obras (dados frescos quando online)
        runtimeCaching: [
          {
            urlPattern: /^https?:\/\/localhost:8080\/api\/obras/,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'obras-cache',
              expiration: { maxEntries: 100, maxAgeSeconds: 86400 }
            }
          },
          {
            urlPattern: /^https?:\/\/localhost:8080\/uploads\//,
            handler: 'CacheFirst',
            options: {
              cacheName: 'media-cache',
              expiration: { maxEntries: 200, maxAgeSeconds: 604800 }
            }
          },
          {
            urlPattern: /^https?:\/\/localhost:8080\/api\/tags/,
            handler: 'StaleWhileRevalidate',
            options: { cacheName: 'tags-cache' }
          }
        ]
      }
    })
  ],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
      '/uploads': 'http://localhost:8080'
    }
  }
})
