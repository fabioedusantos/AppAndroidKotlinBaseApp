package br.com.fbsantos.baseapp.ui.components.container

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.util.helpers.ImageHelper
import br.com.fbsantos.baseapp.util.helpers.Nav
import br.com.fbsantos.baseapp.util.helpers.SnackbarManager
import br.com.fbsantos.ui.app.AppUiState
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    navController: NavController,
    title: String,
    appState: AppUiState,
    onSair: () -> Unit,
    topBarTitle: @Composable () -> Unit = { Text(title) },
    fixedBottomContent: (@Composable () -> Unit)? = null,
    isShowFixedBottomContent: Boolean = (fixedBottomContent != null),
    content: @Composable (SnackbarHostState) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    //para não ficar recarregando a imagem em toda abertura
    val imageBitmap = remember(appState.fotoBlob) {
        ImageHelper.blobBase64ToImage(context, appState.fotoBlob)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
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
                        Text(
                            text = appState.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Conteúdo com rolagem
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(appState.menuItems) { item ->
                        NavigationDrawerItem(
                            label = { Text(item.title) },
                            selected = title == item.title,
                            onClick = {
                                if (item.routeOpen.isNotEmpty()) {
                                    Nav.abrir(navController, item.routeOpen)
                                    scope.launch { drawerState.close() }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            shape = RoundedCornerShape(0.dp) // sem bordas arredondadas
                        )
                    }
                }

                HorizontalDivider()

                // Botão sair fixo no fim
                NavigationDrawerItem(
                    label = { Text("Sair") },
                    selected = false,
                    onClick = { onSair() },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair"
                        )
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = topBarTitle,
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Box {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(
                            bottom = if (isShowFixedBottomContent) 80.dp else padding.calculateBottomPadding()
                        )
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content(snackbarHostState)
                }
                if (isShowFixedBottomContent) {
                    fixedBottomContent?.let {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(24.dp)
                        ) {
                            it()
                        }
                    }
                }
            }
        }
    }

    //botão voltar fecha o drawer
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    SnackbarManager.listener(snackbarHostState)
}