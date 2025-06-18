package com.hfad.easyspeak.presentation.mainscreen

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

// Сервис для работы с пользователями Firebase
class UserService() {
    private val auth = Firebase.auth
    private val database = Firebase.database.reference

    //текущий пользователь
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    //получение данных пользователя
    suspend fun getUser(user: FirebaseUser): UserModel {
        val userId = user.uid
        //получение данных по id
        val snapshot = database.child("users").child(userId).get().await()
        if (!snapshot.exists()) {
            throw Exception(
                "Ошибка при получении данных пользователя!"
            )
        }
        return snapshot.getValue(UserModel::class.java) ?: UserModel()

    }
}
