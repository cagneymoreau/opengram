package com.cagneymoreau.workstation.ui.chatlist

data class ChatDescriptionEntity(var avatarLoc: String,
                                 var title: String,
                                 var lastMess: String,
                                 var time:String,
                                 var count:String,
                                 val id: Long,
                                 var order: Long) : Comparable<ChatDescriptionEntity>
{


    override fun compareTo(other: ChatDescriptionEntity): Int {

        if (other.order < order) return 1
        if (other.order < order) return  -1
        else return 0


    }
}

