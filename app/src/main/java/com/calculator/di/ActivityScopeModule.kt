package com.calculator.di

import com.calculator.input.api.CalculatorInputComponent
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ActivityScopeModule {
    fun provideCalculatorInputComponent(): CalculatorInputComponent {
        TODO()
    }
}