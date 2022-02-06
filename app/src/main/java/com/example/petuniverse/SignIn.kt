package com.example.petuniverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignIn : AppCompatActivity() {
    private lateinit var name : TextInputEditText
    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var confirmPassword : TextInputEditText
    private lateinit var LogInButton : Button
    private lateinit var signIn : TextView
    private lateinit var signInGoogle: TextView
    private lateinit var auth: FirebaseAuth
    val Req_Code = 16042003

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
        name  = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirm_password)
        LogInButton = findViewById(R.id.log_in_button)
        signIn = findViewById(R.id.Signin)
        signInGoogle = findViewById(R.id.SigninGoogle)
        auth = Firebase.auth


        signIn.setOnClickListener {
            signInUser()
        }
        signInGoogle.setOnClickListener {
            signInUserWithGoogle()
        }
        LogInButton.setOnClickListener {
            startActivity(Intent(this,LogIn::class.java))
        }

    }

    private fun signInUserWithGoogle() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)

        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateFields(): Boolean {
        return when {
            name.text.isNullOrBlank() -> {
                ToastMessage("Name is required")
                return false
            }
            email.text.isNullOrBlank() -> {
                ToastMessage("Email is required")
                return false
            }
            password.text.isNullOrBlank() -> {
                ToastMessage("Password is required")
                return false
            }
            confirmPassword.text.isNullOrBlank() -> {
                ToastMessage("Enter the password again")
                return false
            }
            password.text.toString() != confirmPassword.text.toString() -> {
                ToastMessage("Password does not matches")
                return false
            }
            email.text.toString().matches(emailPattern.toRegex()) -> {
                true
            }
            else -> {
                ToastMessage("Enter Valid Email")
                false
            }
        }
    }

    private fun ToastMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun signInUser() {
        if(validateFields()){
            auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnSuccessListener {
                    val addName = UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(name.text.toString()).build()
                    it.user!!.updateProfile(addName)
                        .addOnSuccessListener {
                            startActivity(Intent(this,LogIn::class.java))
                        }
                        .addOnFailureListener {
                            ToastMessage(it.message.toString())
                        }

                }
                .addOnFailureListener {
                    ToastMessage(it.message.toString())
                }
        }
    }
}