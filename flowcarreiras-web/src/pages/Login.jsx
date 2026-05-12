import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { login as apiLogin, registrar as apiRegistrar } from '../api/auth'

export default function Login() {
  const [modo, setModo] = useState('login')
  const [form, setForm] = useState({ nome: '', email: '', senha: '' })
  const [erro, setErro] = useState(null)
  const [carregando, setCarregando] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  function set(campo, valor) {
    setForm(f => ({ ...f, [campo]: valor }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErro(null)
    setCarregando(true)
    try {
      let data
      if (modo === 'login') {
        data = await apiLogin(form.email, form.senha)
      } else {
        data = await apiRegistrar(form.nome, form.email, form.senha)
      }
      login(data)
      navigate('/portfolio/minhas-obras')
    } catch (err) {
      setErro(err.response?.data?.mensagem ?? 'Erro ao autenticar. Verifique seus dados.')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-4">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-brand">Flow Carreiras</h1>
          <p className="text-gray-400 text-sm mt-1">Plataforma para artistas de Recife</p>
        </div>

        <div className="card p-6">
          <div className="flex gap-1 mb-6 bg-surface rounded-lg p-1">
            {['login', 'registro'].map(m => (
              <button
                key={m}
                type="button"
                onClick={() => { setModo(m); setErro(null) }}
                className={`flex-1 py-1.5 text-sm font-medium rounded-md transition-colors ${
                  modo === m ? 'bg-brand text-white' : 'text-gray-400 hover:text-white'
                }`}
              >
                {m === 'login' ? 'Entrar' : 'Cadastrar'}
              </button>
            ))}
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {modo === 'registro' && (
              <div>
                <label className="block text-sm font-medium mb-1">Nome</label>
                <input
                  className="input"
                  value={form.nome}
                  onChange={e => set('nome', e.target.value)}
                  placeholder="Seu nome artístico"
                  required
                />
              </div>
            )}

            <div>
              <label className="block text-sm font-medium mb-1">Email</label>
              <input
                type="email"
                className="input"
                value={form.email}
                onChange={e => set('email', e.target.value)}
                placeholder="seu@email.com"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Senha</label>
              <input
                type="password"
                className="input"
                value={form.senha}
                onChange={e => set('senha', e.target.value)}
                placeholder="••••••"
                required
                minLength={6}
              />
            </div>

            {erro && (
              <div className="bg-red-900/30 border border-red-700 rounded-lg px-3 py-2 text-red-400 text-sm">
                {erro}
              </div>
            )}

            <button type="submit" disabled={carregando} className="btn-primary w-full py-2.5">
              {carregando ? 'Aguarde...' : modo === 'login' ? 'Entrar' : 'Criar conta'}
            </button>
          </form>

          <p className="text-center text-xs text-gray-500 mt-4">
            Teste: marina@test.com / senha123
          </p>
        </div>
      </div>
    </div>
  )
}
