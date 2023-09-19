package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                Column (
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Text(auth.currentUser?.email.toString(), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    
                    Spacer(modifier = Modifier.padding(15.dp))
                    
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