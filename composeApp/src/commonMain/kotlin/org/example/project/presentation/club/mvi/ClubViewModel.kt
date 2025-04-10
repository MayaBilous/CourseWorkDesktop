package org.example.project.presentation.club.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.example.project.domain.entity.SectionDetails
import org.example.project.domain.entity.SportSection
import org.example.project.domain.usecase.AddDetails
import org.example.project.domain.usecase.ChangeDetails
import org.example.project.domain.usecase.CheckSectionDetails
import org.example.project.domain.usecase.GetSectionDetails
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ClubViewModel(
    val sectionId: Long = 0,
    val detailsId: Long? = 0,
    val isAdmin: Boolean,
    private val getSectionDetails: GetSectionDetails,
    val isAddingItem: Boolean,
    val checkSectionDetails: CheckSectionDetails,
    val changeDetails: ChangeDetails,
    val addDetails: AddDetails,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private var _state = MutableStateFlow(
        SectionsDetailsListState(
            sectionId = sectionId,
            detailsId = detailsId,
            isAdmin = isAdmin,
            isAddingItem = isAddingItem,
            sportSection = SportSection.default,
            sectionDetails = SectionDetails.default,
        )
    )
    val state: StateFlow<SectionsDetailsListState>
        get() = _state.asStateFlow()

    private val _userIntent = MutableSharedFlow<ClubUserIntent>()

    private val _event = MutableSharedFlow<ClubEvent>()
    val event: SharedFlow<ClubEvent>
        get() = _event.asSharedFlow()

    init {
        viewModelScope.launch(dispatcher){
            _userIntent.collectLatest { intent ->
                when (intent) {
                    is ClubUserIntent.ChangeAddress -> changeAddress(address = intent.address)
                    is ClubUserIntent.ChangePhoneNumber -> changePhoneNumber(phoneNumber = intent.phoneNumber)
                    is ClubUserIntent.ChangeWorkingDays -> changeWorkingDays(workingDays = intent.workingDays)
                    is ClubUserIntent.ChangePrice -> changePrice(price = intent.price)
                    is ClubUserIntent.UpdateClub -> updateSectionDetails(intent.sectionDetails)
                    is ClubUserIntent.AddClub -> addSectionDetails(intent.sectionDetails)
                    is ClubUserIntent.NavigateToDetails -> navigateToDetails(intent.sectionId)
                    is ClubUserIntent.ChangeSelected -> changeSelected()
                }
            }
        }
        viewModelScope.launch(dispatcher) { loadSportSectionDetails() }
    }

    private suspend fun updateSectionDetails(sectionDetails: SectionDetails) {
        val result = checkSectionDetails(sectionDetails)
        if (result) {
            changeDetails(sectionDetails)
            _event.emit(
                ClubEvent.NavigateToDetails(
                    sectionId = sectionId,
                    isAdmin = isAdmin,
                    isAddingItem = false
                )
            )
        } else {
            _event.emit(ClubEvent.EmptyData)
        }
    }

    private suspend fun addSectionDetails(sectionDetails: SectionDetails) {
        val result = checkSectionDetails(sectionDetails)
        if (result) {
            addDetails(sectionId, sectionDetails)
            _event.emit(
                ClubEvent.NavigateToDetails(
                    isAdmin = state.value.isAdmin,
                    isAddingItem = false,
                    sectionId = sectionId
                )
            )
        } else {
            _event.emit(ClubEvent.EmptyData)
        }
    }


    private suspend fun loadSportSectionDetails() {
        val sectionDetails = getSectionDetails.invoke(sectionId)
        _state.emit(state.value.copy(sportSection = sectionDetails))
        if (!isAddingItem) {
            _state.emit(state.value.copy(sectionDetails = sectionDetails.sectionDetails.find { it.detailsId == detailsId }!!))
        }
    }

    private suspend fun changePrice(price: Int) {
        _state.emit(
            state.value.copy(
                sectionDetails = _state.value.sectionDetails.copy(price = price)
            )
        )
    }

    private suspend fun changeAddress(address: String) {
        _state.emit(
            state.value.copy(
                sectionDetails = _state.value.sectionDetails.copy(address = address)
            )
        )
    }


    private suspend fun changePhoneNumber(phoneNumber: String) {
        _state.emit(
            state.value.copy(
                sectionDetails = _state.value.sectionDetails.copy(phoneNumber = phoneNumber)
            )
        )
    }

    private suspend fun changeWorkingDays(workingDays: String) {
        _state.emit(
            state.value.copy(
                sectionDetails = _state.value.sectionDetails.copy(workingDays = workingDays)
            )
        )
    }

    private suspend fun changeSelected() {
        _state.emit(
            state.value.copy(
                sectionDetails = _state.value.sectionDetails.copy(isSelected = !_state.value.sectionDetails.isSelected)
            )
        )
        changeDetails(state.value.sectionDetails)
    }

    fun process(userIntent: ClubUserIntent) {
        viewModelScope.launch(dispatcher) {
            _userIntent.emit(userIntent)
        }
    }

    private suspend fun navigateToDetails(sectionId: Long) {
        _event.emit(
            ClubEvent.NavigateToDetails(
                isAdmin = state.value.isAdmin,
                isAddingItem = false,
                sectionId = sectionId
            )
        )
    }

    data class SectionsDetailsListState(
        val sectionId: Long,
        val detailsId: Long?,
        val isAdmin: Boolean,
        val isAddingItem: Boolean,
        val sportSection: SportSection,
        val sectionDetails: SectionDetails,
    ) {
    }

    sealed interface ClubUserIntent {
        data class ChangeAddress(val address: String) : ClubUserIntent
        data class ChangeWorkingDays(val workingDays: String) : ClubUserIntent
        data class ChangePhoneNumber(val phoneNumber: String) : ClubUserIntent
        data class ChangePrice(val price: Int) : ClubUserIntent
        data class UpdateClub(val sectionDetails: SectionDetails) : ClubUserIntent
        data class AddClub(val sectionDetails: SectionDetails) : ClubUserIntent
        data class NavigateToDetails(val sectionId: Long, val isAddingItem: Boolean) :
            ClubUserIntent

        data object ChangeSelected : ClubUserIntent
    }

    sealed interface ClubEvent {
        data class NavigateToDetails(
            val sectionId: Long,
            val isAdmin: Boolean,
            val isAddingItem: Boolean
        ) : ClubEvent

        data object EmptyData : ClubEvent
    }

}