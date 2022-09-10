package com.cagneymoreau.opengram.ui.settings

import androidx.lifecycle.ViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * sounds
 * darkmode
 * data use
 * stickers and images
 * devices
 * languages
 */


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel() {

}