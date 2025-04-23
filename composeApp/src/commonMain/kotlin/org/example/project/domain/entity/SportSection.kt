package org.example.project.domain.entity

data class SportSection(
    val id: Long?,
    val sectionName: String,
    val sectionInfo:String,
    val sectionDetails: List<SectionDetails>
    ) {

    companion object {
        val default = SportSection(
            id = null,
            sectionName = "",
            sectionInfo = "",
            sectionDetails = emptyList(),
        )
    }
}
