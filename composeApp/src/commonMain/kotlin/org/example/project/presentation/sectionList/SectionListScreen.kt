package org.example.project.presentation.sectionList

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.sport
import kotlinx.coroutines.flow.collectLatest
import org.example.project.presentation.root.AuthNavigation
import org.example.project.presentation.root.SectionDetailsNavigation
import org.example.project.presentation.sectionList.mvi.SectionListViewModel
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListEvent
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.DeleteSportSectionWithDetails
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.InputSearchText
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.NavigateToAuth
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.NavigateToSectionDetails
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.Sorting

@Composable
fun SectionListScreen(
    navController: NavController,
    viewModel: SectionListViewModel
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SectionListEvent.NavigateToSD -> navController.navigate(
                    SectionDetailsNavigation(
                        sectionId = event.sportSectionId,
                        isAdmin = state.isAdmin,
                        isAddingItem = event.isAddingItem,
                    )
                )

                is SectionListEvent.NavigateToA -> navController.navigate(
                    AuthNavigation
                )
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Image(
            org.jetbrains.compose.resources.painterResource(Res.drawable.sport),
            contentDescription = "sport",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            TextField(
                modifier = Modifier
                    .width(90.dp)
                    .height(50.dp),
                value = state.searchText,
                onValueChange = { viewModel.process(InputSearchText(it)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black
                ),
                label = { Text("Пошук", fontSize = 12.sp) })

            Button(
                onClick = { viewModel.process(Sorting) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF4080f0)
                )
            ) {
                Text(state.sortButtonText)
            }

        }

        Column(
            modifier = Modifier
        ) {
            state.uiSportSection.forEach { item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = item.sectionName,
                        modifier = Modifier
                            .clickable {
                                viewModel.process(NavigateToSectionDetails(item.id ?: 0, false))
                            })

                    if (state.isAdmin) {
                        Icon(
                            Icons.Default.Delete, "",
                            modifier = Modifier
                                .clickable {
                                    viewModel.process(
                                        DeleteSportSectionWithDetails(item)
                                    )
                                })
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.process(
                        NavigateToAuth
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF4080f0)
                )
            ) {
                Text("Назад")
            }

            if (state.isAdmin) {
                Icon(
                    Icons.Default.AddCircle, "",
                    modifier = Modifier
                        .clickable {
                            viewModel.process(
                                NavigateToSectionDetails(isAddingItem = true)
                            )
                        })
            } else {
                Text(
                    modifier = Modifier.align(Alignment.Top),
                    text = "Сума: ${state.sum}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

