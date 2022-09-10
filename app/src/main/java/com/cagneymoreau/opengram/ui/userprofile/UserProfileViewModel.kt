package com.cagneymoreau.opengram.ui.userprofile

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel() {
}