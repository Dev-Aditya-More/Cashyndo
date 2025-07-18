package com.example.cashyndoapp.di

import com.example.cashyndoapp.data.auth.AuthRepositoryImpl
import com.example.cashyndoapp.data.auth.GoogleAuthUIClient
import com.example.cashyndoapp.domain.auth.AuthRepository
import com.example.cashyndoapp.ui.auth.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(Firebase.auth) }
    viewModel { AuthViewModel(get()) }
    single { GoogleAuthUIClient(androidContext()) }
}
