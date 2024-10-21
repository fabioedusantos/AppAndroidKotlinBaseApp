package br.com.fbsantos.baseapp.ui.components.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkdownContainer(
    navController: NavController,
    title: String,
    assetsOpenPath: String
) {

    val context = LocalContext.current
    var markdownText by remember { mutableStateOf("") }

    val typography = markdownTypography(
        h1 = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        h2 = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
        h3 = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp),
        h4 = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
        h5 = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
        h6 = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
        text = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
        code = MaterialTheme.typography.bodySmall.copy(
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
        ),
        inlineCode = MaterialTheme.typography.bodySmall.copy(
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
        ),
        quote = MaterialTheme.typography.bodySmall.plus(
            SpanStyle(fontStyle = FontStyle.Italic)
        ),
        paragraph = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
        ordered = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
        bullet = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
        list = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
        table = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
    )

    LaunchedEffect(Unit) {
        val inputStream = context.assets.open(assetsOpenPath)
        val reader = BufferedReader(InputStreamReader(inputStream))
        markdownText = reader.readText()
        reader.close()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (markdownText.isNotEmpty()) {
                Markdown(
                    content = markdownText,
                    typography = typography
                )
            } else {
                Text("Carregando...")
            }
        }
    }
}