package org.example.project.presentation.sectionList.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.example.project.domain.entity.SportSection
import org.example.project.domain.usecase.DeleteSectionWithDetails
import org.example.project.domain.usecase.GetSectionList
import org.example.project.domain.usecase.OrderAmount
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.DeleteSportSectionWithDetails
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.InputSearchText
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.NavigateToAuth
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.NavigateToSectionDetails
import org.example.project.presentation.sectionList.mvi.SectionListViewModel.SectionListUserIntent.Sorting
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SectionListViewModel(
    isAdmin: Boolean,
    private val getSectionList: GetSectionList,
    private val deleteSectionWithDetails: DeleteSectionWithDetails,
    private val orderAmount: OrderAmount,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private var _state = MutableStateFlow(SectionListState.default.copy(isAdmin = isAdmin))
    val state: StateFlow<SectionListState>
        get() = _state.asStateFlow()

    private val _userIntent = MutableSharedFlow<SectionListUserIntent>()

    private val _event = MutableSharedFlow<SectionListEvent>()
    val event: SharedFlow<SectionListEvent>
        get() = _event.asSharedFlow()

    init {
        viewModelScope.launch(dispatcher) {
            _userIntent.collectLatest { intent ->
                when (intent) {
                    is InputSearchText -> inputSearchText(searchText = intent.searchText)
                    is Sorting -> sort()
                    is NavigateToSectionDetails -> navigateToSectionDetails(
                        intent.sectionId,
                        intent.isAddingItem
                    )

                    is NavigateToAuth -> navigateToAuth()
                    is DeleteSportSectionWithDetails -> deleteSectionWithDetails(intent.sportSection)
                }
            }
        }
        viewModelScope.launch(dispatcher) { loadSportSections() }
    }

    fun process(userIntent: SectionListUserIntent) {
        viewModelScope.launch(dispatcher) {
            _userIntent.emit(userIntent)
        }
    }

    private suspend fun loadSportSections() {
        val sections = getSectionList.invoke()
        _state.emit(state.value.copy(sportSections = sections))
        _state.emit(state.value.copy(sum = orderAmount.invoke()))
    }

    private suspend fun inputSearchText(searchText: String) {
        _state.emit(state.value.copy(searchText = searchText))
    }

    private suspend fun sort() {
        _state.emit(state.value.copy(isSortedAsc = !state.value.isSortedAsc))
    }

    private suspend fun deleteSectionWithDetails(sportSection: SportSection) {
        deleteSectionWithDetails.invoke(sportSection)
        _state.emit(state.value.copy(sportSections = getSectionList.invoke()))
    }

    private suspend fun navigateToSectionDetails(sectionId: Long, isAddingItem: Boolean) {
        _event.emit(
            SectionListEvent.NavigateToSD(
                sportSectionId = sectionId,
                isAdmin = state.value.isAdmin,
                isAddingItem = isAddingItem,
            )
        )

    }

    private suspend fun navigateToAuth() {
        _event.emit(
            SectionListEvent.NavigateToA
        )

    }


    data class SectionListState(
        val isAdmin: Boolean,
        val searchText: String,
        val sportSections: List<SportSection>,
        val isSortedAsc: Boolean,
        val sum: Int,

        ) {
        val uiSportSection: List<SportSection>
            get() = sportSections
                .filter {
                    if (searchText.isEmpty()) {
                        true
                    } else {
                        it.sectionName.contains(searchText)
                    }
                }
                .let { list ->
                    if (isSortedAsc) {
                        list.sortedBy { it.sectionName }
                    } else {
                        list.sortedByDescending { it.sectionName }
                    }
                }
        val sortButtonText: String
            get() = if (isSortedAsc) {
                "Сортування за"
            } else {
                "Сортування проти"
            }

        companion object {
            val default = SectionListState(
                isAdmin = false,
                searchText = "",
                sportSections = emptyList(),
                isSortedAsc = true,
                sum = 0,
            )
        }
    }

    sealed interface SectionListUserIntent {
        data class InputSearchText(val searchText: String) : SectionListUserIntent
        data object Sorting : SectionListUserIntent
        data class NavigateToSectionDetails(val sectionId: Long = 0, val isAddingItem: Boolean) :
            SectionListUserIntent

        data object NavigateToAuth : SectionListUserIntent
        data class DeleteSportSectionWithDetails(val sportSection: SportSection) :
            SectionListUserIntent

    }

    sealed interface SectionListEvent {
        data class NavigateToSD(
            val sportSectionId: Long,
            val isAdmin: Boolean,
            val isAddingItem: Boolean
        ) : SectionListEvent

        data object NavigateToA : SectionListEvent
    }
}