package com.meanwhile.quickrecipes.di

import com.meanwhile.quickrecipes.data.UserRepository
import com.meanwhile.quickrecipes.data.UserRepositoryImp
import com.meanwhile.quickrecipes.domain.GetLoggedUserUseCase
import com.meanwhile.quickrecipes.domain.GetLoginStateUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface QuickRecipesModule {

    @Binds
    fun bindGetLoggedInUseCase(useCase: GetLoggedUserUseCase): GetLoginStateUseCase

    @Binds
    @Singleton
     fun bindUserRepository(userRepositoryImpl: UserRepositoryImp): UserRepository
}