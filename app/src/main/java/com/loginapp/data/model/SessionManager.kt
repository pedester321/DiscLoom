package com.loginapp.data.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.nio.charset.StandardCharsets

class SessionManager(context: Context) {

    private val sharedPreferences = createEncryptedSharedPreferences(context)

    // Function to create EncryptedSharedPreferences using MasterKey.Builder
    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences {
        // Create a master key using the MasterKey.Builder
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM) // Set the encryption scheme
            .build()

        // Create the EncryptedSharedPreferences instance
        return EncryptedSharedPreferences.create(
            context,
            "secure_prefs", // Name of the SharedPreferences file
            masterKey, // MasterKey used to encrypt/decrypt the SharedPreferences
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveJwt(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getJwt(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun clearJwt() {
        sharedPreferences.edit().remove("jwt_token").apply()
    }

    fun decodeJwtToUser(): User? {
        val token = getJwt() ?: return null
        val parts = token.split(".")
        if (parts.size != 3) return null // A valid JWT consists of 3 parts

        // Extract payload
        val payload = parts[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val decodedString = String(decodedBytes, StandardCharsets.UTF_8)

        // Set up Moshi and the adapter for the User class
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val userAdapter = moshi.adapter(User::class.java)

        // Deserialize JSON payload to User object
        return userAdapter.fromJson(decodedString)
    }
}