package com.achirinus.actilog.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.achirinus.actilog.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var emailErrorTextView: TextView
    lateinit var passErrorTextView: TextView
    lateinit var emailEditText : EditText
    lateinit var passEditText : EditText
    lateinit var pass2EditText : EditText
    lateinit var loginButton: Button
    lateinit var signupTextView: TextView
    lateinit var errorTextView: TextView

    var isSignUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null)
        {
            Log.d(TAG, "User already logged in")
            startMainActivity()
        }
        emailErrorTextView = findViewById(R.id.emailErrorTextView)
        passErrorTextView = findViewById(R.id.passErrorTextView)
        emailEditText = findViewById(R.id.emailEditText)
        passEditText = findViewById(R.id.passEditText)
        pass2EditText = findViewById(R.id.pass2EditText)
        loginButton = findViewById(R.id.loginButton)
        signupTextView = findViewById(R.id.signupTextView)
        errorTextView = findViewById(R.id.errorTextView)

        signupTextView.setOnClickListener {
            setIsSignUp(!isSignUp)
        }

        emailEditText.setOnFocusChangeListener { view, isFocus ->
            //If the focus was lost, we validate the email format
            if(isFocus)
            {
                emailErrorTextView.visibility = View.GONE
            }
            else
            {
                checkEmailInput()
            }
        }
        emailEditText.doAfterTextChanged { emailErrorTextView.visibility = View.GONE }
        passEditText.doAfterTextChanged { passErrorTextView.visibility = View.GONE }

        passEditText.setOnFocusChangeListener { view, isFocus ->
            //If the focus was lost, we validate the email format
            if(isFocus)
            {
                passErrorTextView.visibility = View.GONE
            }
        }

        loginButton.setOnClickListener {
            checkEmailInput()
            checkPassInput()
            if((passErrorTextView.visibility == View.VISIBLE) ||
                (emailErrorTextView.visibility == View.VISIBLE))
            {
                return@setOnClickListener
            }

            val emailInput: String = emailEditText.text.toString()
            val passInput: String = passEditText.text.toString()
            if(isSignUp)
            {
                createUserEmailPass(emailInput, passInput)
            }
            else
            {
                signInUserEmailPass(emailInput, passInput)
            }
        }
    }

    fun setIsSignUp(newVal: Boolean) {
        isSignUp = newVal
        if(newVal)
        {
            signupTextView.text = getString(R.string.prompt_login)
            loginButton.text = getString(R.string.prompt_signup)
            pass2EditText.visibility = View.VISIBLE
        }
        else
        {
            signupTextView.text = getString(R.string.prompt_signup)
            loginButton.text = getString(R.string.prompt_login)
            pass2EditText.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null)
        {
            Log.d(TAG, "User already logged in")
            startMainActivity()
        }
    }

    fun createUserEmailPass(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            task ->
            if(task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail: success")
                val user = auth.currentUser
                startMainActivity()
            //UpdateUI
            } else {
                val erroMsg = task.exception?.message
                if(erroMsg != null)
                {
                    errorTextView.text = erroMsg
                }
                Log.w(TAG, "createUserWithEmail: failure: $erroMsg")
            }
        }
    }

    fun signInUserEmailPass(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass). addOnCompleteListener(this) { task ->
            if(task.isSuccessful) {
                Log.d(TAG, "signInUserWithEmail: success")
                val user = auth.currentUser
                startMainActivity()
                //UpdateUI
            } else {
                val erroMsg = task.exception?.message
                if(erroMsg != null)
                {
                    errorTextView.text = erroMsg
                }
                Log.w(TAG, "signInUserWithEmail: failure: $erroMsg")
            }
        }
    }

    fun setEmailError(resId: Int) {
        emailErrorTextView.visibility = View.VISIBLE
        emailErrorTextView.text = getString(resId)
    }
    fun setPassError(resId: Int) {
        passErrorTextView.visibility = View.VISIBLE
        passErrorTextView.text = getString(resId)
    }

    fun checkEmailInput() {
        val matches = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()
        if(!matches)
        {
            setEmailError(R.string.invalid_email)
        }
    }
    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun checkPassInput() {
        if(passEditText.text.count() < 5) {
            setPassError(R.string.small_pass)
            return
        }
        if(isSignUp)
        {
            val passInput: String = passEditText.text.toString()
            val pass2Input: String = pass2EditText.text.toString()
            if(!passInput.equals(pass2Input))
            {
                setPassError(R.string.pass_not_match)
            }
        }
    }
}