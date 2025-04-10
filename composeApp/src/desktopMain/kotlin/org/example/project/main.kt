package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.data.db.InitData
import org.example.project.data.db.SectionDataBaseProvider
import org.example.project.presentation.root.RootScreen

fun main() = application {
    SectionDataBaseProvider.init()
    InitData(SectionDataBaseProvider.db)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Спортивні секції міста",
    ) {

        RootScreen()
    }
}
