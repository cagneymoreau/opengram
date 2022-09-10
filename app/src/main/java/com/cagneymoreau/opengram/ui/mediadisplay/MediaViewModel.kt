package com.cagneymoreau.opengram.ui.mediadisplay

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.ContentInterface
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
) : ViewModel() {


    fun getContent(key: Int) :ContentInterface
    {
        return repositoryInterface.getMediaToView(key)
    }



}