package br.com.fbsantos.baseapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import br.com.fbsantos.baseapp.ui.AppViewModel
import org.koin.compose.koinInject

@Composable
fun LogAtividade(rota: String, descricao: String, icone: String) {
    val appViewModel: AppViewModel = koinInject()
    LaunchedEffect(Unit) {
        appViewModel.addAtividade(rota, descricao, icone)
    }
}