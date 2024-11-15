package br.com.fbsantos.baseapp.ui.screen.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.DateTimeHelper
import br.com.fbsantos.baseapp.util.IconHelper
import br.com.fbsantos.baseapp.util.NavHelper
import br.com.fbsantos.baseapp.util.Utils
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
        Utils.fotoBase64ToImage(context, appState.fotoBlob)
    }

    MainContainer(
        navController = navController,
        title = "Início",
        appState = appState,
        onSair = onSair
    ) { snackbarHostState ->
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
                    Icons.Default.Person,
                    {
                        //todo abrir perfil
                    }
                )
                QuickActionCard(
                    "Configurações",
                    Icons.Default.Settings,
                    { NavHelper.abrir(navController, Routes.Configuracoes.route) }
                )
                QuickActionCard(
                    "Sair",
                    Icons.AutoMirrored.Filled.ExitToApp,
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
                            IconHelper.fromName(atividade.icone),
                            atividade.timestamp,
                            { atividade.rota?.let { rota -> NavHelper.abrir(navController, rota) } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.QuickActionCard(title: String, icon: ImageVector, action: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = { action?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ActivityItem(
    text: String,
    icon: ImageVector,
    timestamp: Long?,
    action: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp),
        onClick = { action?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Top)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text)

                if (timestamp != null) {
                    Text(
                        text = DateTimeHelper.toUserFriendly(timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Bottom)
                    )
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