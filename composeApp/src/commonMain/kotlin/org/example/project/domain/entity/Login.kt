package org.example.project.domain.entity

data class Login(
    val userName: String,
    val password: String,
    val isAdmin: Boolean,
) {

    companion object {
        val default = Login(
            userName = "user",
            password = "321",
            isAdmin = false,
        )
    }
}
