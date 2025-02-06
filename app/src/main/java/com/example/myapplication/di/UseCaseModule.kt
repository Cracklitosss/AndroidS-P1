package com.example.myapplication.di

import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.usecase.auth.LoginUseCase
import com.example.myapplication.domain.usecase.auth.RegisterUseCase
import com.example.myapplication.domain.usecase.product.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productRepository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideCreateProductUseCase(productRepository: ProductRepository): CreateProductUseCase {
        return CreateProductUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProductUseCase(productRepository: ProductRepository): UpdateProductUseCase {
        return UpdateProductUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteProductUseCase(productRepository: ProductRepository): DeleteProductUseCase {
        return DeleteProductUseCase(productRepository)
    }
} 