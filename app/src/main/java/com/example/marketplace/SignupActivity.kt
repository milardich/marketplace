package com.example.marketplace

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        setContent {
            displaySignupForm()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun displaySignupForm() {
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Signup for EzMarketplace!", Modifier.padding(30.dp), fontSize = 20.sp)

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )

            Spacer(modifier = Modifier.padding(30.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    signUp(email, password)
                }
            ) {
                Text("Sign up")
            }

            Spacer(modifier = Modifier.padding(3.dp))

            ElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    goToLoginActivity()
                }
            ) {
                Text(text = "Already signed up? Log in.")
            }
        }

    }

    fun goToLoginActivity() {
        startActivity(
            Intent(this, LoginActivity::class.java)
        )
    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    Toast.makeText(
                        baseContext,
                        "Signup successful.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val user = auth.currentUser
                    // TODO: Go to login page
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Signup failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    // TODO: error message Signup failed, try again
                }
            }
    }
}