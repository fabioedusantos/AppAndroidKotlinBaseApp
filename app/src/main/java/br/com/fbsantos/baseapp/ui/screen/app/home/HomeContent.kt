package br.com.fbsantos.baseapp.ui.screen.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity
import br.com.fbsantos.baseapp.ui.components.ActivityItem
import br.com.fbsantos.baseapp.ui.components.InfoCard
import br.com.fbsantos.baseapp.ui.components.QuickActionCard
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.helpers.DateTimeHelper
import br.com.fbsantos.baseapp.util.helpers.IconManager
import br.com.fbsantos.baseapp.util.helpers.ImageHelper
import br.com.fbsantos.baseapp.util.helpers.Nav
import br.com.fbsantos.ui.app.AppUiState
import coil.compose.AsyncImage

@Composable
fun HomeContent(
    navController: NavController,
    appState: AppUiState,
    onSair: () -> Unit
) {
    val context = LocalContext.current
    //para não ficar recarregando a imagem em toda abertura
    val imageBitmap = remember(appState.fotoBlob) {
        ImageHelper.blobBase64ToImage(context, appState.fotoBlob)
    }

    MainContainer(
        navController = navController,
        title = "Início",
        appState = appState,
        onSair = onSair
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageBitmap,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.placeholder_user),
                    error = painterResource(R.drawable.placeholder_user),
                    contentScale = ContentScale.Crop, //pegar a tela inteira
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Bem-vindo de volta,", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = appState.nomeCompleto,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Ações Rápidas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickActionCard(
                    "Perfil",
                    IconManager.fromName("Person"),
                    {
                        Nav.abrir(navController, Routes.Perfil.route)
                    }
                )
                QuickActionCard(
                    "Configurações",
                    IconManager.fromName("Settings"),
                    { Nav.abrir(navController, Routes.Configuracoes.route) }
                )
                QuickActionCard(
                    "Sair",
                    IconManager.fromName("Exit"),
                    { onSair() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("O que você quer fazer hoje?", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                InfoCard("📬 Mensagens", "12 novas")
                InfoCard("📅 Eventos do Dia", "2")
                InfoCard("⏳ Último acesso", DateTimeHelper.dateTimeToString(appState.ultimoAcesso))
            }

            if (appState.historicoAtividade.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Últimas atividades", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    appState.historicoAtividade.forEach { atividade ->
                        ActivityItem(
                            atividade.descricao,
                            IconManager.fromName(atividade.icone),
                            atividade.timestamp,
                            { atividade.rota?.let { rota -> Nav.abrir(navController, rota) } }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeContentPreview() {
    val navController = rememberNavController()

    val previewAppState = AppUiState(
        nome = "Fulano da Silva",
        email = "fulanosilva@outlook.com",
        fotoBlob = "",
        historicoAtividade = listOf(
            HistoricoAtividadeEntity(0, null, "Home", "Home", System.currentTimeMillis()),
            HistoricoAtividadeEntity(
                0,
                null,
                "Configurações",
                "Settings",
                System.currentTimeMillis()
            )
        )
    )

    BaseAppTheme(darkTheme = false) {
        HomeContent(
            navController = navController,
            appState = previewAppState,
            onSair = {}
        )
    }
}