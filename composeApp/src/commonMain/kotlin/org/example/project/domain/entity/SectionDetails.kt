package org.example.project.domain.entity

data class SectionDetails(
    val detailsId: Long?,
    val sectionId: Long?,
    val address: String,
    val workingDays: String,
    val phoneNumber: String,
    val price: Int,
    val isSelected: Boolean,
) {

    companion object {
        val default = SectionDetails(
            detailsId = null,
            address = "",
            workingDays = "",
            phoneNumber = "",
            price = 0,
            sectionId = null,
            isSelected = false,
        )
    }
}
