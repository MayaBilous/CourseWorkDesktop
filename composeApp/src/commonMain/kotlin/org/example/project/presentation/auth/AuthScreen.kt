package org.example.project.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import org.example.project.presentation.ShowDialog
import org.example.project.presentation.auth.mvi.AuthEvent
import org.example.project.presentation.auth.mvi.AuthUserIntent
import org.example.project.presentation.auth.mvi.AuthViewModel
import org.example.project.presentation.root.SectionsListNavigation

@Composable
fun Authorization(
    navController: NavController,
    viewModel: AuthViewModel,
) {

    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AuthEvent.Navigate -> navController.navigate(
                    SectionsListNavigation(
                        isAdmin = event.isAdmin
                    )
                )

                is AuthEvent.EmptyData -> {
                    showDialog = true
                    dialogText = "Немає даних. Спробуйте ще раз."
                }

                is AuthEvent.IncorrectData -> {
                    showDialog = true
                    dialogText = "Дані невірні. Спробуйте ще раз."
                }
            }
        }
    }

    if (showDialog) {
        ShowDialog(text = dialogText, onDismiss = { showDialog = false })
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    ) {
        TextField(
            value = state.login.userName,
            onValueChange = { viewModel.process(AuthUserIntent.ChangeLogin(it)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFc4d9fe),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black
            ),
            label = { Text("Введіть логін") }
        )

        TextField(
            value = state.login.password,
            onValueChange = { viewModel.process(AuthUserIntent.ChangePassword(it)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFc4d9fe),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black
            ),
            label = { Text("Введіть пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = { viewModel.process(AuthUserIntent.Authorize) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4080f0)
            )
        ) {
            Text("Авторизуватися")
        }
    }
}

