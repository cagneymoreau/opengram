package com.cagneymoreau.mockapiintegration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.cagneymoreau.opengram.ui.structure.Searchable

class MockSearchAbleEntity : Searchable {

    override var searchTarget: MutableState<String> = mutableStateOf("")


    override fun query(query: String) {

    }
}