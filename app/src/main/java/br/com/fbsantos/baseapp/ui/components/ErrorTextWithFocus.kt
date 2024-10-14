package br.com.fbsantos.baseapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ErrorTextWithFocus(
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    if (errorMessage == null) return

    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        fontSize = 12.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .bringIntoViewRequester(bringIntoViewRequester),
        textAlign = TextAlign.Center
    )

    LaunchedEffect(errorMessage) {
        bringIntoViewRequester.bringIntoView()
    }
}