package com.example.marketplace

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        // auth.signOut()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        var intent: Intent

        if (currentUser != null) {
            // intent = Intent(this, AddItemActivity::class.java)
            intent = Intent(this, BrowseItemsActivity::class.java)
        }
        else {
            intent = Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
    }
}
