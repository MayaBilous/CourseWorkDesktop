package org.example.project.presentation.sectionDetails.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.example.project.domain.entity.SectionDetails
import org.example.project.domain.entity.SportSection
import org.example.project.domain.usecase.AddSection
import org.example.project.domain.usecase.ChangeSection
import org.example.project.domain.usecase.CheckSectionName
import org.example.project.domain.usecase.DeleteDetails
import org.example.project.domain.usecase.GetSectionDetails
import org.example.project.presentation.sectionDetails.mvi.DetailsSportsSectionsViewModel.SectionDetailsUserIntent.NavigateToSectionList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsSportsSectionsViewModel(
    val sectionId: Long? = null,
    val isAdmin: Boolean,
    private val getSectionDetails: GetSectionDetails,
    val isAddingItem: Boolean,
    val changeSection: ChangeSection,
    val addSection: AddSection,
    val checkSectionName: CheckSectionName,
    private val deleteDetails: DeleteDetails,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private var _state = MutableStateFlow(
        SectionsDetailsListState(
            sectionId = sectionId,
            isAdmin = isAdmin,
            isAddingItem = isAddingItem,
            sportSection = SportSection.default,
            sectionDetails = SectionDetails.default,
            detailsId = null,
        )
    )
    val state: StateFlow<SectionsDetailsListState>
        get() = _state.asStateFlow()

    private val _userIntent = MutableSharedFlow<SectionDetailsUserIntent>()

    private val _event = MutableSharedFlow<SectionDetailsEvent>()
    val event: SharedFlow<SectionDetailsEvent>
        get() = _event.asSharedFlow()

    init {
        viewModelScope.launch(dispatcher) {
            _userIntent.collectLatest { intent ->
                when (intent) {
                    is NavigateToSectionList -> navigateToSectionList()
                    is SectionDetailsUserIntent.ChangeSectionName -> changeSectionName(sectionName = intent.sectionName)
                    is SectionDetailsUserIntent.AddSportSection -> addSportSectionWithDetails(intent.sportSection)
                    is SectionDetailsUserIntent.DeleteSectionDetails -> deleteDetails(intent.detailsId)
                    is SectionDetailsUserIntent.NavigateToClub -> navigateToClub(
                        sectionId = intent.sectionId,
                        isAddingItem = intent.isAddingItem,
                        detailsId = intent.detailsId,
                    )

                    is SectionDetailsUserIntent.UpdateSection -> updateSportSection(intent.sportSection)
                    is SectionDetailsUserIntent.ChangeSectionInfo -> changeSectionInfo(sectionInfo = intent.sectionInfo)
                }
            }
        }
        if (!isAddingItem) {
            viewModelScope.launch(dispatcher) { loadSportSectionDetails() }
        }
    }

    private suspend fun updateSportSection(sportSection: SportSection) {
        val result = checkSectionName(sportSection)
        if (result) {
            changeSection(sportSection)
            _event.emit(SectionDetailsEvent.NavigateToSectionList(isAdmin = isAdmin))
        } else {
            _event.emit(SectionDetailsEvent.EmptyData)
        }
    }

    private suspend fun addSportSectionWithDetails(sportSection: SportSection) {
        val result = checkSectionName(sportSection)
        if (result) {
            addSection(sportSection)
            _event.emit(SectionDetailsEvent.NavigateToSectionList(isAdmin = isAdmin))
        } else {
            _event.emit(SectionDetailsEvent.EmptyData)
        }
    }

    private suspend fun loadSportSectionDetails() {
        val sectionDetails = getSectionDetails.invoke(sectionId ?: 0)
        _state.emit(state.value.copy(sportSection = sectionDetails))
        _state.emit(state.value.copy(sectionDetails = sectionDetails.sectionDetails.find { it.sectionId == sectionId }))
    }

    private suspend fun changeSectionName(sectionName: String) {
        _state.emit(
            state.value.copy(
                sportSection = _state.value.sportSection.copy(sectionName = sectionName)
            )
        )
    }

    private suspend fun changeSectionInfo(sectionInfo: String) {
        _state.emit(
            state.value.copy(
                sportSection = _state.value.sportSection.copy(sectionInfo = sectionInfo)
            )
        )
    }

    private suspend fun navigateToClub(sectionId: Long, isAddingItem: Boolean, detailsId: Long?) {
        _event.emit(
            SectionDetailsEvent.NavigateToClub(
                isAdmin = state.value.isAdmin,
                isAddingItem = isAddingItem,
                sectionId = sectionId,
                detailsId = detailsId
            )
        )
    }


    private suspend fun deleteDetails(detailsId: Long) {
        deleteDetails.invoke(detailsId)
        loadSportSectionDetails()
    }

    fun process(userIntent: SectionDetailsUserIntent) {
        viewModelScope.launch(dispatcher) {
            _userIntent.emit(userIntent)
        }
    }

    private suspend fun navigateToSectionList() {
        _event.emit(SectionDetailsEvent.NavigateToSectionList(isAdmin = state.value.isAdmin))
    }

    data class SectionsDetailsListState(
        val sectionId: Long?,
        val detailsId: Long?,
        val isAdmin: Boolean,
        val isAddingItem: Boolean,
        val sportSection: SportSection,
        val sectionDetails: SectionDetails?,
    ) {
    }

    sealed interface SectionDetailsUserIntent {
        data class ChangeSectionName(val sectionName: String) : SectionDetailsUserIntent
        data class ChangeSectionInfo(val sectionInfo: String) : SectionDetailsUserIntent
        data class AddSportSection(val sportSection: SportSection) : SectionDetailsUserIntent
        data object NavigateToSectionList : SectionDetailsUserIntent
        data class NavigateToClub(
            val sectionId: Long,
            val isAddingItem: Boolean,
            val detailsId: Long?
        ) : SectionDetailsUserIntent

        data class DeleteSectionDetails(val detailsId: Long) : SectionDetailsUserIntent
        data class UpdateSection(val sportSection: SportSection) : SectionDetailsUserIntent
    }

    sealed interface SectionDetailsEvent {
        data class NavigateToSectionList(val isAdmin: Boolean) : SectionDetailsEvent
        data class NavigateToClub(
            val sectionId: Long,
            val isAdmin: Boolean,
            val isAddingItem: Boolean,
            val detailsId: Long?
        ) : SectionDetailsEvent

        data object EmptyData : SectionDetailsEvent
    }

}