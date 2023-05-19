package com.example.loginandregistration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Loan(
    var lendee: String = "someone",
    var reason: String = "some reason",
    var debt: Int = 0,
    var dateLoaned: Date = Date(1678726574923),
    var dateRepaid: Date ?= null,
    var amountRepaid: Int = 0,
    var isRepaid: Boolean = false,
    var ownerId: String? = null,
    var objectId: String? = null,

) : Parcelable {
    fun balanceRemaining(): Int {
        return debt - amountRepaid
    }
}
