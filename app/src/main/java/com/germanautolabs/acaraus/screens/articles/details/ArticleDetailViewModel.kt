package com.germanautolabs.acaraus.screens.articles.details

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class ArticleDetailViewModel(
    @InjectedParam val articleId: String,
) : ViewModel()
