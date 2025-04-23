package org.example.project.presentation.sectionDetails


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.city
import kotlinproject.composeapp.generated.resources.sport
import kotlinx.coroutines.flow.collectLatest
import org.example.project.presentation.ShowDialog
import org.example.project.presentation.root.ClubNavigation
import org.example.project.presentation.root.SectionsListNavigation
import org.example.project.presentation.sectionDetails.mvi.DetailsSportsSectionsViewModel
import org.example.project.presentation.sectionDetails.mvi.DetailsSportsSectionsViewModel.SectionDetailsEvent
import org.example.project.presentation.sectionDetails.mvi.DetailsSportsSectionsViewModel.SectionDetailsUserIntent

@Composable
fun SectionDetailsScreen(
    navController: NavController,
    viewModel: DetailsSportsSectionsViewModel
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SectionDetailsEvent.NavigateToSectionList -> navController.navigate(
                    SectionsListNavigation(
                        isAdmin = event.isAdmin
                    )
                )

                is SectionDetailsEvent.EmptyData -> {
                    showDialog = true
                    dialogText = "Немає даних. Спробуйте ще раз."
                }

                is SectionDetailsEvent.NavigateToClub -> navController.navigate(
                    ClubNavigation(
                        sectionId = event.sectionId,
                        isAdmin = state.isAdmin,
                        isAddingItem = event.isAddingItem,
                        detailsId = event.detailsId,
                    )
                )
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

        if (!state.isAddingItem){
        Image(
            org.jetbrains.compose.resources.painterResource(Res.drawable.city),
            contentDescription = "city",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                state.sportSection.sectionName,
                onValueChange = { viewModel.process(SectionDetailsUserIntent.ChangeSectionName(it)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black,
                    disabledIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    disabledTextColor = Color.Black
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                enabled = state.isAdmin,
                label = { Text("назва") }
            )
            if (state.isAdmin) {
                TextField(
                    state.sportSection.sectionInfo,
                    onValueChange = {
                        viewModel.process(
                            SectionDetailsUserIntent.ChangeSectionInfo(
                                it
                            )
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        disabledIndicatorColor = Color.Black,
                        cursorColor = Color.Black,
                        disabledTextColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                    ),
                    enabled = state.isAdmin,
                    label = { Text("інформація") }
                )
            }
        }

        if (!state.isAddingItem) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier.align(Alignment.Bottom),
                    text = "Клуби: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                if (state.isAdmin) {
                    Button(
                        onClick = {
                            viewModel.process(
                                SectionDetailsUserIntent.NavigateToClub(
                                    sectionId = state.sectionId ?: 0,
                                    isAddingItem = true,
                                    detailsId = null,
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF4080f0)
                        )
                    ) {
                        Text("Додати")
                    }
                }
            }
        }


        Column(
            modifier = Modifier
        ) {
            state.sportSection.sectionDetails.forEach { item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = item.address,
                        modifier = Modifier
                            .clickable {
                                viewModel.process(
                                    SectionDetailsUserIntent.NavigateToClub(
                                        sectionId = item.sectionId ?: 0,
                                        isAddingItem = false,
                                        detailsId = item.detailsId,
                                    )
                                )
                            })

                    if (state.isAdmin) {
                        Icon(
                            Icons.Default.Delete, "",
                            modifier = Modifier
                                .clickable {
                                    viewModel.process(
                                        SectionDetailsUserIntent.DeleteSectionDetails(
                                            item.detailsId ?: 0
                                        )
                                    )
                                })
                    }
                }
            }
        }

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
                        onClick = { viewModel.process(SectionDetailsUserIntent.NavigateToSectionList) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF4080f0),
                        )
                    ) {
                        Text("Назад")
                    }
                }
                Button(
                    onClick = {
                        if (state.isAdmin) {
                            if (state.isAddingItem) {
                                viewModel.process(SectionDetailsUserIntent.AddSportSection(state.sportSection))
                            } else {
                                viewModel.process(SectionDetailsUserIntent.UpdateSection(state.sportSection))
                            }
                        } else {
                            viewModel.process(SectionDetailsUserIntent.NavigateToSectionList)
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
