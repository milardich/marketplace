package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    val database = Firebase.database.reference
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        setContent {
            MarketplaceTheme {
                Column {
                    Text("Signed in as: " + auth.currentUser?.email.toString())

                    Button(onClick = {
                        signOut()
                    }) {
                        Text(text = "Sign out")
                    }
                }

                
                NavigationButtons(context = this)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        var openItemIntent: Intent
        openItemIntent = Intent(this, LoginActivity::class.java)
        startActivity(openItemIntent)
    }
}