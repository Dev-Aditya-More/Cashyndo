package com.aditya1875.payu.di

import androidx.room.Room
import com.aditya1875.payu.data.database.TransactionDatabase
import com.aditya1875.payu.data.repository.auth.AuthRepositoryImpl
import com.aditya1875.payu.data.repository.auth.GoogleAuthUIClient
import com.aditya1875.payu.data.repository.transaction.TransactionRepositoryImpl
import com.aditya1875.payu.domain.repository.auth.AuthRepository
import com.aditya1875.payu.domain.repository.transaction.TransactionRepository
import com.aditya1875.payu.domain.usecases.AddTransactionUseCase
import com.aditya1875.payu.domain.usecases.DeleteTransactionUseCase
import com.aditya1875.payu.domain.usecases.GetAnalyticsDataUseCase
import com.aditya1875.payu.domain.usecases.GetHomeDataUseCase
import com.aditya1875.payu.domain.usecases.GetTransactionsUseCase
import com.aditya1875.payu.domain.usecases.UpdateTransactionUseCase
import com.aditya1875.payu.ui.auth.viewmodel.AuthViewModel
import com.aditya1875.payu.ui.presentation.home.viewmodel.HomeViewModel
import com.aditya1875.payu.ui.presentation.profile.viewmodel.ProfileViewModel
import com.aditya1875.payu.ui.presentation.balances.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // Firebase
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            TransactionDatabase::class.java,
            TransactionDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    // DAO
    single { get<TransactionDatabase>().transactionDao() }

    // Repository
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }

    // UseCases
    singleOf(::GetTransactionsUseCase)
    singleOf(::AddTransactionUseCase)
    singleOf(::DeleteTransactionUseCase)
    singleOf(::UpdateTransactionUseCase)
    singleOf(::GetHomeDataUseCase)
    singleOf(::GetAnalyticsDataUseCase)

    // ViewModels
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::TransactionViewModel)
    viewModelOf(::ProfileViewModel)

    // Google Auth
    singleOf(::GoogleAuthUIClient)
}
