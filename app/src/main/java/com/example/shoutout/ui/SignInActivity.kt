package com.example.shoutout.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoutout.R
import com.example.shoutout.models.User
import com.example.shoutout.viewmodels.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var signInButton: SignInButton
    private lateinit var progressBar: ProgressBar

    // ✅ use UserViewModel
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        signInButton = findViewById(R.id.signInButton)
        progressBar = findViewById(R.id.progressBar)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setColorScheme(SignInButton.COLOR_DARK)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
        auth = Firebase.auth

        signInButton.setOnClickListener { signIn() }
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        signInButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
            } else {
                Log.e(TAG, "Firebase sign-in failed: ${task.exception?.message}")
                signInButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            val user = User(
                firebaseUser.uid,
                firebaseUser.displayName ?: "",
                firebaseUser.photoUrl.toString()
            )
            userViewModel.addUser(user) // ✅ call through ViewModel
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            signInButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}
