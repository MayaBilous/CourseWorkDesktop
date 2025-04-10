package org.example.project.presentation.club

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.example.project.presentation.ShowDialog
import org.example.project.presentation.club.mvi.ClubViewModel
import org.example.project.presentation.club.mvi.ClubViewModel.ClubEvent
import org.example.project.presentation.club.mvi.ClubViewModel.ClubUserIntent
import org.example.project.presentation.root.SectionDetailsNavigation
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ClubScreen(
    navController: NavController,
    viewModel: ClubViewModel
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ClubEvent.NavigateToDetails -> navController.navigate(
                    SectionDetailsNavigation(
                        sectionId = event.sectionId,
                        isAdmin = state.isAdmin,
                        isAddingItem = event.isAddingItem,
                    )
                )

                is ClubEvent.EmptyData -> {
                    showDialog = true
                    dialogText = "Немає даних. Спробуйте ще раз."
                }
            }
        }
    }

    if (showDialog) {
        ShowDialog(text = dialogText, onDismiss = { showDialog = false })
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                state.sportSection.sectionName,
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    disabledTextColor = Color.Black
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                enabled = false,
                label = { Text("назва") }
            )
        }

        TextField(
            state.sectionDetails.address,
            onValueChange = { viewModel.process(ClubUserIntent.ChangeAddress(it)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            enabled = state.isAdmin,
            label = { Text("адреса") }
        )

        TextField(
            state.sectionDetails.workingDays,
            onValueChange = { viewModel.process(ClubUserIntent.ChangeWorkingDays(it)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            enabled = state.isAdmin,
            label = { Text("робочі дні") }
        )


        TextField(
            state.sectionDetails.price.toString(),
            onValueChange = { viewModel.process(ClubUserIntent.ChangePrice(it.toInt())) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            enabled = state.isAdmin,
            label = { Text("ціна грн/заняття") }
        )

        TextField(
            state.sectionDetails.phoneNumber,
            onValueChange = { viewModel.process(ClubUserIntent.ChangePhoneNumber(it)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            enabled = state.isAdmin,
            label = { Text("номер телефону") }
        )

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isAdmin) {
                    Button(
                        onClick = {
                            viewModel.process(
                                ClubUserIntent.NavigateToDetails(
                                    state.sectionId,
                                    false
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF4080f0)
                        )
                    ) {
                        Text("Назад")
                    }
                } else {
                    Column {
                        Text(
                            text = "Додати до кошика",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Checkbox(
                            checked = state.sectionDetails.isSelected,
                            onCheckedChange = { viewModel.process(ClubUserIntent.ChangeSelected) },
                        )
                    }
                }

                Button(
                    onClick = {
                        if (state.isAdmin) {
                            if (state.isAddingItem) {
                                viewModel.process(ClubUserIntent.AddClub(state.sectionDetails))
                            } else {
                                viewModel.process(ClubUserIntent.UpdateClub(state.sectionDetails))
                            }
                        } else {
                            viewModel.process(
                                ClubUserIntent.NavigateToDetails(
                                    state.sectionId,
                                    false
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF4080f0)
                    )
                ) {
                    Text("Ок")
                }
            }
        }
    }
}
