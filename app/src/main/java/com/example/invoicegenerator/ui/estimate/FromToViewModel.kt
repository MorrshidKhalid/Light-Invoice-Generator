package com.example.invoicegenerator.ui.estimate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FromToViewModel : ViewModel() {

    private var _business = MutableLiveData("Unknowing business ")

    val business: LiveData<String> = _business

    fun setBusiness(newBusiness: String) {
        _business.value = newBusiness
    }

}