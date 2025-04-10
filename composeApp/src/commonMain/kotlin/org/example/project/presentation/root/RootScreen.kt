package org.example.project.presentation.root

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.data.repository.AuthorizationRepositoryImpl
import org.example.project.data.repository.SportSectionListRepositoryImpl
import org.example.project.domain.usecase.AddDetailsUseCase
import org.example.project.domain.usecase.AddSectionUseCase
import org.example.project.domain.usecase.ChangeDetailsUseCase
import org.example.project.domain.usecase.ChangeSectionUseCase
import org.example.project.domain.usecase.CheckAuthorizationUseCase
import org.example.project.domain.usecase.CheckSectionDetailsUseCase
import org.example.project.domain.usecase.CheckSectionNameUseCase
import org.example.project.domain.usecase.DeleteDetailsUseCase
import org.example.project.domain.usecase.DeleteSectionWithDetailsUseCase
import org.example.project.domain.usecase.GetSectionDetailsUseCase
import org.example.project.domain.usecase.GetSectionListUseCase
import org.example.project.domain.usecase.OrderAmountUseCase
import org.example.project.presentation.auth.Authorization
import org.example.project.presentation.auth.mvi.AuthViewModel
import org.example.project.presentation.club.ClubScreen
import org.example.project.presentation.club.mvi.ClubViewModel
import org.example.project.presentation.sectionDetails.SectionDetailsScreen
import org.example.project.presentation.sectionDetails.mvi.DetailsSportsSectionsViewModel
import org.example.project.presentation.sectionList.SectionListScreen
import org.example.project.presentation.sectionList.mvi.SectionListViewModel
import kotlinx.serialization.*
import kotlin.reflect.KClass

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AuthNavigation) {
        composable<AuthNavigation> {
            Authorization(
                navController = navController,
                viewModel = viewModel(
                    factory = viewModelFactory {
                        AuthViewModel(
                            CheckAuthorizationUseCase(
                                authorizationRepository = AuthorizationRepositoryImpl()
                            )
                        )
                    }
                )
            )
        }
        composable<SectionsListNavigation> {
            val arg = it.toRoute<SectionsListNavigation>()
            SectionListScreen(
                navController = navController,
                viewModel = viewModel(factory = viewModelFactory {
                    SectionListViewModel(
                        arg.isAdmin,
                        GetSectionListUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        DeleteSectionWithDetailsUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        OrderAmountUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                    )
                })
            )
        }

        composable<SectionDetailsNavigation> {
            val arg = it.toRoute<SectionDetailsNavigation>()
            SectionDetailsScreen(
                navController = navController,
                viewModel = viewModel(factory = viewModelFactory {
                    DetailsSportsSectionsViewModel(
                        arg.sectionId,
                        arg.isAdmin,

                        GetSectionDetailsUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        arg.isAddingItem,
                        ChangeSectionUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        AddSectionUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        CheckSectionNameUseCase(),
                        DeleteDetailsUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                    )
                })
            )
        }

        composable<ClubNavigation> {
            val arg = it.toRoute<ClubNavigation>()
            ClubScreen(
                navController = navController,
                viewModel = viewModel(factory = viewModelFactory {
                    ClubViewModel(
                        arg.sectionId,
                        arg.detailsId,
                        arg.isAdmin,

                        GetSectionDetailsUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        arg.isAddingItem,
                        CheckSectionDetailsUseCase(),
                        ChangeDetailsUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                        AddDetailsUseCase(
                            sportSectionListRepository = SportSectionListRepositoryImpl()
                        ),
                    )
                })
            )
        }
    }
}


@Serializable
data object AuthNavigation

@Serializable
data class SectionsListNavigation(val isAdmin: Boolean)

@Serializable
data class SectionDetailsNavigation(
    val sectionId: Long,
    val isAdmin: Boolean,
    val isAddingItem: Boolean
)

@Serializable
data class ClubNavigation(
    val sectionId: Long,
    val detailsId: Long?,
    val isAdmin: Boolean,
    val isAddingItem: Boolean
)

inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T = f() as T
    }
