package br.com.fbsantos.domain.usecase.configuracoes

import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository

class SalvarTemaUseCase(
    private val repository: ConfiguracoesRepository
) {
    suspend fun execute(isDark: Boolean) {
        repository.salvar("isDarkTheme", isDark)
    }
}