import React from 'react'

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props)
    this.state = { erro: null }
  }

  static getDerivedStateFromError(erro) {
    return { erro }
  }

  componentDidCatch(erro, info) {
    console.error('Erro ao renderizar a aplicacao', erro, info)
  }

  limparSessao() {
    localStorage.removeItem('fc_token')
    localStorage.removeItem('fc_usuario')
    window.location.href = '/login'
  }

  render() {
    if (!this.state.erro) return this.props.children

    return (
      <div className="min-h-screen flex items-center justify-center px-4">
        <div className="bg-card rounded-xl p-6 max-w-md w-full border border-red-900/60">
          <p className="text-sm text-red-300 font-medium">Nao foi possivel carregar esta tela.</p>
          <h1 className="text-xl font-semibold text-white mt-2">A aplicacao encontrou um erro no navegador.</h1>
          <p className="text-sm text-gray-400 mt-2">
            Limpe a sessao local e entre novamente para carregar a versao atual do front.
          </p>
          <button onClick={() => this.limparSessao()} className="btn-primary w-full mt-5">
            Limpar sessao e voltar ao login
          </button>
        </div>
      </div>
    )
  }
}
