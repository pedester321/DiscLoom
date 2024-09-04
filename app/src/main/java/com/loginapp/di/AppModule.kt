package com.loginapp.di

import android.content.Context
import com.loginapp.data.model.SessionManager
import com.loginapp.data.model.UserSingleton
import com.loginapp.data.remote.LoginApi
import com.loginapp.data.repository.LoginRepository
import com.loginapp.domain.use_cases.ValidateBirthDate
import com.loginapp.domain.use_cases.ValidateConfirmPassword
import com.loginapp.domain.use_cases.ValidateEmail
import com.loginapp.domain.use_cases.ValidateName
import com.loginapp.domain.use_cases.ValidatePassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        return Retrofit.Builder()
            .baseUrl(LoginApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideUserSingleton(): UserSingleton = UserSingleton()

    @Provides
    fun provideLoginRepository(loginApi: LoginApi, userSingleton: UserSingleton): LoginRepository{
        return LoginRepository(loginApi, userSingleton)
    }


    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
    // Use cases
    @Provides
    fun provideValidateEmail(): ValidateEmail = ValidateEmail()

    @Provides
    fun provideValidatePassword(): ValidatePassword = ValidatePassword()

    @Provides
    fun provideValidateConfirmPassword(): ValidateConfirmPassword = ValidateConfirmPassword()

    @Provides
    fun provideValidateName(): ValidateName = ValidateName()

    @Provides
    fun provideValidateBirthDate(): ValidateBirthDate = ValidateBirthDate()

}