package com.api.reader.ui.mvvm

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.reader.ui.apiclass.Reciters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelReciter(private val repository: Repository = Repository()) : ViewModel() {
    val myReciter: MutableLiveData<Reciters> = MutableLiveData(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // use loop to make new request every 3.5 min\
            // use catch because if there is not Internet
            try {
                myReciter.postValue(repository.getReciters())

            } catch (e: Exception) {
                Log.d("RERERW", "FSDFSDFSDFS")

            }
        }
    }
}