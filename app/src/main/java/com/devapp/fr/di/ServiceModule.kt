package com.devapp.fr.di

import android.content.Context
import com.devapp.fr.data.repositories.ServiceRepository
import com.devapp.fr.network.RealTimeService
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@InstallIn(ServiceComponent::class)
@Module
object ServiceModule {
    @Provides
    @ServiceScoped
    fun provideServiceRepository(realTimeViewModel: RealTimeService): ServiceRepository = ServiceRepository(realTimeViewModel)
}