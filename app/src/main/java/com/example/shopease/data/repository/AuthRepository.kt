package com.example.shopease.data.repository

import android.R.attr.password
import com.example.shopease.data.model.User
import com.example.shopease.data.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun checkUserExists(email : String) : Boolean = try {
        val result = db.collection("users").whereEqualTo("email",email).get().await()
        !result.isEmpty
    }catch (e : Exception){
        false
    }

    suspend fun registerUser(user : User, password : String) : Resource<User> = try {

        val email = user.email.trim()
        val pass = password.trim()

        auth.createUserWithEmailAndPassword(email, pass).await()

        val uid = auth.currentUser?.uid

        if (uid.isNullOrBlank()) {
            throw IllegalStateException("User UID is null or blank after registration.")
        }

        val newUser = user.copy(uid = uid)
        db.collection("users").document(uid).set(newUser).await()
        Resource.Success(newUser)

    }catch (e: Exception){
        Resource.Error(e.message ?: "Signin Failed")
    }

    suspend fun login(email : String, password: String) : Resource<FirebaseUser> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Resource.Success(auth.currentUser!!)
    }catch (e : Exception){
        Resource.Error(e.message ?: "Login Failed")
    }

     suspend fun getCurrentUser(): Resource<User> = try {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId.isNullOrBlank()) throw IllegalStateException("User not logged in")

        val userDocument = db.collection("users")
            .document(currentUserId)
            .get()
            .await()

        val userData = userDocument.toObject(User::class.java)

        if (userData != null) {
            Resource.Success(userData)
        } else {
            Resource.Error("User data not found")
        }

    } catch (error: Exception) {
        Resource.Error(error.message ?: "Failed to get user data")
    }

}