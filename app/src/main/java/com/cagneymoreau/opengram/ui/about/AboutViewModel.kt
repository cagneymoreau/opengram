package com.cagneymoreau.opengram.ui.about

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel() {


    fun getAbout(): String
    {
        return repositoryInterface.getAbout()
    }

    fun click()
    {
        repositoryInterface.aboutClickCounter()
    }


}