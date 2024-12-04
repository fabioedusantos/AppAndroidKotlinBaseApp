package br.com.fbsantos.baseapp.ui.screen.app.perfil.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import br.com.fbsantos.baseapp.ui.components.TextBlock
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.DateTimeHelper
import br.com.fbsantos.baseapp.util.Utils
import br.com.fbsantos.ui.app.AppUiState
import coil.compose.AsyncImage

@Composable
fun PerfilViewContent(
    navController: NavController,
    appState: AppUiState,
    onSair: () -> Unit,
    onToggleEditarPerfil: () -> Unit
) {
    val context = LocalContext.current

    MainContainer(
        navController = navController,
        title = "Perfil",
        appState = appState,
        onSair = onSair
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            //para não ficar recarregando a imagem em toda abertura
            val imageBitmap = remember(appState.fotoBlob) {
                Utils.fotoBase64ToImage(context, appState.fotoBlob)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = imageBitmap,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.placeholder_user),
                        error = painterResource(R.drawable.placeholder_user),
                        contentScale = ContentScale.Crop, //pegar a tela inteira
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = appState.nomeCompleto,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = { onToggleEditarPerfil() },
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar Perfil")
                    }
                }
            }

            TextBlock("Nome", appState.nome)
            TextBlock("Sobrenome", appState.sobrenome)
            TextBlock(
                if (appState.isContaGoogle) "Email (Conta Google)" else "Email",
                Utils.ofuscarEmail(appState.email)
            )
            if (!appState.isContaGoogle) {
                TextBlock("Senha", "**********")
            }
            TextBlock("Último Acesso", DateTimeHelper.dateTimeToString(appState.ultimoAcesso))
            TextBlock("Criação", DateTimeHelper.dateTimeToString(appState.criacao))
            TextBlock("Ultima Edição", DateTimeHelper.dateTimeToString(appState.alteracao))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PerfilViewContentPreview() {
    val navController = rememberNavController()

    val appState = AppUiState(
        nome = "Fábio",
        sobrenome = "Santos",
        email = "fabioedusantos@gmail.com",
        isContaGoogle = true
    )

    BaseAppTheme(darkTheme = false) {
        PerfilViewContent(
            navController = navController,
            appState = appState,
            onSair = {},
            onToggleEditarPerfil = {}
        )
    }
}