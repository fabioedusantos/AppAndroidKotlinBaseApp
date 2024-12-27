package br.com.fbsantos.baseapp.ui.screen.app.perfil.content

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.SelectPhotoModal
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.ImageHelper
import br.com.fbsantos.baseapp.util.SnackbarManager
import br.com.fbsantos.baseapp.util.ToastManager
import br.com.fbsantos.baseapp.util.Utils
import br.com.fbsantos.ui.app.AppUiState
import br.com.fbsantos.ui.main.perfil.PerfilUiState
import coil.compose.AsyncImage

@Composable
fun PerfilEditContent(
    navController: NavController,
    appState: AppUiState,
    onSair: () -> Unit,
    state: PerfilUiState,
    onToggleEditarPerfil: () -> Unit,
    onFotoChange: (String) -> Unit,
    onRemovePhoto: () -> Unit,
    onNomeChange: (String) -> Unit,
    onSobreNomeChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onSenhaVisivelToggle: () -> Unit,
    onConfirmeSenhaChange: (String) -> Unit,
    onConfirmeSenhaVisivelToggle: () -> Unit,
    onEditarPerfil: (Boolean, () -> Unit) -> Unit,
    onAtualizarPerfilVisual: () -> Unit
) {

    val context = LocalContext.current

    MainContainer(
        navController = navController,
        title = "Perfil",
        appState = appState,
        onSair = onSair,
        fixedBottomContent = {
            Button(
                onClick = {
                    onEditarPerfil(appState.isContaGoogle, {
                        onAtualizarPerfilVisual()
                        SnackbarManager.show("Perfil atualizado com sucesso.")
                        onToggleEditarPerfil()
                    })
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormEnabled
            ) {
                Text("Salvar")
            }
        },
        isShowFixedBottomContent = state.isEditarPerfil
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            //para não ficar recarregando a imagem em toda abertura
            val imageBitmap = remember(state.isRemoverFoto, state.foto, appState.fotoBlob) {
                if (state.isRemoverFoto) {
                    Utils.fotoBase64ToImage(context, null)
                } else if (state.foto.isNotEmpty()) {
                    Utils.fotoBase64ToImage(context, state.foto)
                } else {
                    Utils.fotoBase64ToImage(context, appState.fotoBlob)
                }
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        //se for conta google não pode alterar foto
                        if (!appState.isContaGoogle) {
                            var isShowBottomSheet by remember { mutableStateOf(false) }
                            Button(
                                onClick = { isShowBottomSheet = true },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera, // ícone de foto
                                    contentDescription = "Alterar Foto",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Alterar Foto")
                            }

                            if (isShowBottomSheet) {
                                SelectPhotoModal(
                                    context = context,
                                    modalTitle = "Alterar foto",
                                    onDismiss = { isShowBottomSheet = false },
                                    onImageSelected = { uri ->
                                        uri?.let {
                                            val base64 = ImageHelper.uriToBase64(context, it)
                                            Log.i("teste", base64.toString())
                                            if (base64 != null) {
                                                onFotoChange(base64)
                                            } else {
                                                ToastManager.show("Não foi possível obter a foto.")
                                            }
                                        }
                                    },
                                    onRemovePhoto = onRemovePhoto
                                )
                            }
                        }

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
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Visualizar Perfil")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                //inicializa uma vez
                LaunchedEffect(Unit) {
                    onNomeChange(appState.nome)
                    onSobreNomeChange(appState.sobrenome)
                }

                OutlinedTextField(
                    value = state.nome,
                    onValueChange = onNomeChange,
                    label = { Text("Nome") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    isError = state.isNomeError,
                    supportingText = {
                        if (state.isNomeError) Text(state.nomeErrorText)
                    },
                    enabled = state.isFormEnabled
                )

                OutlinedTextField(
                    value = state.sobrenome,
                    onValueChange = onSobreNomeChange,
                    label = { Text("Sobrenome") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    isError = state.isSobrenomeError,
                    supportingText = {
                        if (state.isSobrenomeError) Text(state.sobrenomeErrorText)
                    },
                    enabled = state.isFormEnabled
                )
            }

            OutlinedTextField(
                value = appState.email,
                onValueChange = {},
                label = { Text(if (appState.isContaGoogle) "Email (Conta Google)" else "Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false
            )

            //se for conta google não se deve alterar senha
            if (!appState.isContaGoogle) {
                OutlinedTextField(
                    value = state.senha,
                    onValueChange = onSenhaChange,
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (state.isSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (state.isSenhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        IconButton(onClick = onSenhaVisivelToggle) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    isError = state.isSenhaError,
                    supportingText = {
                        if (state.isSenhaError) Text(state.senhaErrorText)
                    },
                    enabled = state.isFormEnabled
                )

                OutlinedTextField(
                    value = state.confirmeSenha,
                    onValueChange = onConfirmeSenhaChange,
                    label = { Text("Confirme sua senha") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (state.isConfirmeSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (state.isConfirmeSenhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        IconButton(onClick = onConfirmeSenhaVisivelToggle) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    isError = state.isConfirmeSenhaError,
                    enabled = state.isFormEnabled
                )
            }

            ErrorTextWithFocus(state.error)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PerfilEditContentPreview() {
    val navController = rememberNavController()

    val appState = AppUiState(
        nome = "Fábio",
        sobrenome = "Santos",
        email = "fabioedusantos@gmail.com",
        isContaGoogle = false
    )

    val uiState = PerfilUiState(
        isEditarPerfil = true,
        isFormEnabled = true
    )

    BaseAppTheme (darkTheme = false) {
        PerfilEditContent(
            navController = navController,
            appState = appState,
            onSair = {},
            state = uiState,
            onToggleEditarPerfil = {},
            onFotoChange = {},
            onRemovePhoto = {},
            onNomeChange = {},
            onSobreNomeChange = {},
            onSenhaChange = {},
            onSenhaVisivelToggle = {},
            onConfirmeSenhaChange = {},
            onConfirmeSenhaVisivelToggle = {},
            onEditarPerfil = { _, _: () -> Unit -> },
            onAtualizarPerfilVisual = {}
        )
    }
}