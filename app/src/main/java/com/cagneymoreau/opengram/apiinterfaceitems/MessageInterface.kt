package com.cagneymoreau.opengram.apiinterfaceitems

interface MessageInterface : ContentInterface {

    val sender: UserInterface?
    val isOutgoing: Boolean
    val id: Long

}

//deafult is true for any uncertainty
fun compareSenders(m1:MessageInterface, m2:MessageInterface): Boolean
{
    if (m1.sender == null || m2.sender == null) return true
    if(m1.sender!!.id == m2.sender!!.id) return false
    return true
}