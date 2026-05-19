import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { login as apiLogin, registrar as apiRegistrar } from '../api/auth'

export default function Login() {
  const [modo, setModo] = useState('login')
  const [form, setForm] = useState({ nome: '', email: '', senha: '', desejaSerMentor: false })
  const [erros, setErros] = useState({})
  const [erroGeral, setErroGeral] = useState(null)
  const [carregando, setCarregando] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  function set(campo, valor) {
    setForm(f => ({ ...f, [campo]: valor }))
    if (erros[campo]) setErros(e => ({ ...e, [campo]: null }))
  }

  function validar() {
    const novosErros = {}
    if (modo === 'registro' && !form.nome.trim()) {
      novosErros.nome = 'Nome é obrigatório'
    }
    if (!form.email.trim()) {
      novosErros.email = 'Email é obrigatório'
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      novosErros.email = 'Informe um e-mail válido (ex: usuario@dominio.com)'
    }
    if (!form.senha) {
      novosErros.senha = 'Senha é obrigatória'
    } else if (modo === 'registro' && form.senha.length < 8) {
      novosErros.senha = 'A senha deve ter no mínimo 8 caracteres'
    }
    setErros(novosErros)
    return Object.keys(novosErros).length === 0
  }

  const podeContinuar = modo === 'registro'
    ? form.nome.trim() && form.email.trim() && form.senha
    : form.email.trim() && form.senha

  async function handleSubmit(e) {
    e.preventDefault()
    if (!validar()) return
    setErroGeral(null)
    setCarregando(true)
    try {
      let data
      if (modo === 'login') {
        data = await apiLogin(form.email, form.senha)
      } else {
        data = await apiRegistrar(form.nome, form.email, form.senha, form.desejaSerMentor)
      }
      login(data)
      if (data.desejaConfigurarMentoria) {
        navigate('/mentoria/configurar', { state: { primeiraConfiguracao: true } })
      } else if (!data.onboardingConcluido) {
        navigate('/onboarding')
      } else {
        navigate('/portfolio/minhas-obras')
      }
    } catch (err) {
      const msg = err.response?.data?.mensagem ?? 'Erro ao autenticar. Verifique seus dados.'
      if (msg.toLowerCase().includes('e-mail') || msg.toLowerCase().includes('email')) {
        setErros(e => ({ ...e, email: 'Este e-mail já está em uso. Faça login ou recupere sua senha.' }))
      } else {
        setErroGeral(msg)
      }
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
                onClick={() => { setModo(m); setErros({}); setErroGeral(null) }}
                className={`flex-1 py-1.5 text-sm font-medium rounded-md transition-colors ${
                  modo === m ? 'bg-brand text-white' : 'text-gray-400 hover:text-white'
                }`}
              >
                {m === 'login' ? 'Entrar' : 'Cadastrar'}
              </button>
            ))}
          </div>

          <form onSubmit={handleSubmit} className="space-y-4" noValidate>
            {modo === 'registro' && (
              <div>
                <label className="block text-sm font-medium mb-1">Nome</label>
                <input
                  className={`input ${erros.nome ? 'border-red-500' : ''}`}
                  value={form.nome}
                  onChange={e => set('nome', e.target.value)}
                  placeholder="Seu nome artístico"
                />
                {erros.nome && <p className="text-red-400 text-xs mt-1">{erros.nome}</p>}
              </div>
            )}

            <div>
              <label className="block text-sm font-medium mb-1">Email</label>
              <input
                type="email"
                className={`input ${erros.email ? 'border-red-500' : ''}`}
                value={form.email}
                onChange={e => set('email', e.target.value)}
                placeholder="seu@email.com"
              />
              {erros.email && <p className="text-red-400 text-xs mt-1">{erros.email}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Senha</label>
              <input
                type="password"
                className={`input ${erros.senha ? 'border-red-500' : ''}`}
                value={form.senha}
                onChange={e => set('senha', e.target.value)}
                placeholder={modo === 'registro' ? 'Mínimo 8 caracteres' : '••••••••'}
              />
              {erros.senha && <p className="text-red-400 text-xs mt-1">{erros.senha}</p>}
            </div>

            {modo === 'registro' && (
              <label className="flex items-start gap-3 cursor-pointer select-none rounded-lg border border-gray-800 p-3 hover:border-gray-700 transition-colors">
                <input
                  type="checkbox"
                  checked={form.desejaSerMentor}
                  onChange={e => set('desejaSerMentor', e.target.checked)}
                  className="mt-1"
                />
                <span>
                  <span className="block text-sm text-white font-medium">Quero atuar como mentor</span>
                  <span className="block text-xs text-gray-500 mt-0.5">
                    Depois do cadastro voce configura preco, modalidade e areas de expertise.
                  </span>
                </span>
              </label>
            )}

            {erroGeral && (
              <div className="bg-red-900/30 border border-red-700 rounded-lg px-3 py-2 text-red-400 text-sm">
                {erroGeral}
              </div>
            )}

            <button
              type="submit"
              disabled={carregando || !podeContinuar}
              className="btn-primary w-full py-2.5 disabled:opacity-50 disabled:cursor-not-allowed"
            >
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
