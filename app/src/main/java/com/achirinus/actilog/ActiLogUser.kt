package com.achirinus.actilog

import com.google.firebase.auth.FirebaseUser

class ActiLogUser {
    var email = ""

    constructor() {

    }
    constructor(firebaseUser: FirebaseUser) {
        if(firebaseUser.email != null) email = firebaseUser.email!!

    }

    fun getID() :String {
        if(BuildConfig.DEBUG)
        {
            return "$email-DEBUG"
        }
        return email
    }
}