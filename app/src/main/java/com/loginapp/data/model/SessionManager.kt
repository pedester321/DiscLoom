package com.loginapp.data.model

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

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
}