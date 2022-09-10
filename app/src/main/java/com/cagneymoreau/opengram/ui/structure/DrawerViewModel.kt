package com.cagneymoreau.opengram.ui.structure

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel() {



    fun getMyself(): UserInterface?
    {
      return  repositoryInterface.mySelf
    }




}