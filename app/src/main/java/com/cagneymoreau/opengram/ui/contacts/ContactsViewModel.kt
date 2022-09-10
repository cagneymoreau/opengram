package com.cagneymoreau.opengram.ui.contacts

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel()  {

    val list = repositoryInterface.getContacts()


}