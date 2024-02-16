package com.example.tanu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.fragment.app.Fragment

class MainViewModel : ViewModel() {
    private val _currentFragment = MutableLiveData<Fragment>()
    val currentFragment: LiveData<Fragment> = _currentFragment

    fun changeFragment(fragment: Fragment) {
        _currentFragment.value = fragment
    }
}
