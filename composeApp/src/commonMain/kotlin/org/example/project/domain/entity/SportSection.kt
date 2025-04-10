package org.example.project.domain.entity

data class SportSection(
    val id: Long?,
    val sectionName: String,
    val sectionDetails: List<SectionDetails>
    ) {

    companion object {
        val default = SportSection(
            id = null,
            sectionName = "",
            sectionDetails = emptyList(),
        )
    }
}
