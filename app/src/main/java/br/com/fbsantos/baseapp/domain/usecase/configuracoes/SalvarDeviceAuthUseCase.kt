package br.com.fbsantos.baseapp.domain.usecase.configuracoes

import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository

class SalvarDeviceAuthUseCase(
    private val repository: ConfiguracoesRepository
) {
    suspend fun execute(isDeviceAuthEnabled: Boolean) {
        repository.salvar("isDeviceAuthEnabled", isDeviceAuthEnabled)
    }
}