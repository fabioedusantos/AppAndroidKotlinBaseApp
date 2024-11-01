package br.com.fbsantos.baseapp.domain.usecase.configuracoes

import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository

class BuscarDeviceAuthUseCase(
    private val repository: ConfiguracoesRepository
) {
    fun execute() = repository.getBoolean("isDeviceAuthEnabled")
}