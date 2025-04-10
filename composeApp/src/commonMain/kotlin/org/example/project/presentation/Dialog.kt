package org.example.project.presentation

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ShowDialog(text: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK".uppercase())
            }
        }
    )
}