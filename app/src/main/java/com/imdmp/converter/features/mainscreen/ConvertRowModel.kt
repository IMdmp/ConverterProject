package com.imdmp.converter.features.mainscreen

import androidx.lifecycle.MutableLiveData

data class ConvertRowModel(
    val currency: String,
    val data: MutableLiveData<String>
)