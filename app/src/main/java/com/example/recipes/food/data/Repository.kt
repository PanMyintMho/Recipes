package com.example.recipes.food.data

import android.icu.util.LocaleData
import com.example.recipes.food.data.database.LocalDataSource
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote=remoteDataSource
    val local=localDataSource
}