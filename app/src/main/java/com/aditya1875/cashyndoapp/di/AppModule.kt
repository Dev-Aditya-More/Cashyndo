package com.aditya1875.cashyndoapp.di

import androidx.room.Room
import com.aditya1875.cashyndoapp.data.database.TransactionDatabase
import com.aditya1875.cashyndoapp.data.repository.auth.AuthRepositoryImpl
import com.aditya1875.cashyndoapp.data.repository.auth.GoogleAuthUIClient
import com.aditya1875.cashyndoapp.data.repository.transaction.TransactionRepositoryImpl
import com.aditya1875.cashyndoapp.domain.repository.auth.AuthRepository
import com.aditya1875.cashyndoapp.domain.repository.transaction.TransactionRepository
import com.aditya1875.cashyndoapp.domain.usecases.AddTransactionUseCase
import com.aditya1875.cashyndoapp.domain.usecases.DeleteTransactionUseCase
import com.aditya1875.cashyndoapp.domain.usecases.GetAnalyticsDataUseCase
import com.aditya1875.cashyndoapp.domain.usecases.GetHomeDataUseCase
import com.aditya1875.cashyndoapp.domain.usecases.GetTransactionsUseCase
import com.aditya1875.cashyndoapp.domain.usecases.UpdateTransactionUseCase
import com.aditya1875.cashyndoapp.ui.auth.viewmodel.AuthViewModel
import com.aditya1875.cashyndoapp.ui.presentation.analytics.viewmodel.AnalyticsViewModel
import com.aditya1875.cashyndoapp.ui.presentation.home.viewmodel.HomeViewModel
import com.aditya1875.cashyndoapp.ui.presentation.profile.viewmodel.ProfileViewModel
import com.aditya1875.cashyndoapp.ui.presentation.transaction.viewmodel.TransactionViewModel
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
    viewModelOf(::AnalyticsViewModel)

    // Google Auth
    singleOf(::GoogleAuthUIClient)
}
