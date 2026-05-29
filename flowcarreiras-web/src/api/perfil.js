import apiClient from './client'

export const obterPerfilOnboarding = () =>
    apiClient.get('/perfil/me').then(r => r.data)

export const obterMeuPerfil = () =>
    apiClient.get('/perfil/me').then(r => r.data)

export const atualizarPerfil = (dados) =>
    apiClient.patch('/perfil', dados).then(r => r.data)

export const atualizarFotoPerfil = (file) => {
    const form = new FormData()
    form.append('foto', file)
    return apiClient.patch('/perfil/foto', form, {
        headers: { 'Content-Type': 'multipart/form-data' },
    }).then(r => r.data)
}

export const salvarEtapaArea = (areaArtisticaPrincipal) =>
    apiClient.patch('/perfil/onboarding/area-artistica', { areaArtisticaPrincipal }).then(r => r.data)

export const salvarEtapaCidade = (cidade) =>
    apiClient.patch('/perfil/onboarding/cidade', { cidade }).then(r => r.data)

export const salvarEtapaBio = (bio) =>
    apiClient.patch('/perfil/onboarding/bio', { bio }).then(r => r.data)

export const salvarEtapaTags = (tagNecessidadeIds) =>
    apiClient.patch('/perfil/onboarding/tags', { tagNecessidadeIds }).then(r => r.data)

export const salvarEtapaFoto = (file) => {
    const form = new FormData()
    form.append('foto', file)
    return apiClient.patch('/perfil/onboarding/foto', form, {
        headers: { 'Content-Type': 'multipart/form-data' },
    }).then(r => r.data)
}

export const salvarEtapaLinks = (linksExternos) =>
    apiClient.patch('/perfil/onboarding/links', { linksExternos }).then(r => r.data)

export const pularEtapa = (etapa) =>
    apiClient.patch(`/perfil/onboarding/pular/${etapa}`).then(r => r.data)

export const concluirOnboarding = () =>
    apiClient.post('/perfil/onboarding/concluir').then(r => r.data)
