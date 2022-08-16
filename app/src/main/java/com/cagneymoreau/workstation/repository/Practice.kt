package com.cagneymoreau.workstation.repository

/*
class Practice {


    fun splitListToParts(head: ListNode?, k: Int): Array<ListNode?> {

        val list = Array(k) { _ -> ListNode(null) }

        val search = true
        var count = 1
        var curr = head!! // TODO: first could be null
        while (search){

            if(curr.next != null){
                curr = curr.next!!
                count++

            }

        }

        var per = 0
        per = if (k > count) k
        else count/k
        var leftover = count % k

        curr = head
        for (i in 0 until k)
        {

            list[i] = curr

            for (i in 0 until per){
                curr = head.next!!
            }

            if (leftover > 0){
                curr = head.next!!
                leftover--
            }

            var next = curr.next!!
            curr.next = null
            curr = next

        }



    }


}


 class ListNode(var `val`: Int) {
    var next: ListNode? = null
}


 */