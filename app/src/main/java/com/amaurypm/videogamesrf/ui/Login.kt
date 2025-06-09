package com.amaurypm.videogamesrf.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.databinding.ActivityLoginBinding
import com.amaurypm.videogamesrf.utils.message
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var contrasena = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{
            false
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
             if (!validateFields()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

             authenticateUser(email, contrasena)
        }

        binding.btnRegistrarse.setOnClickListener {
            if (!validateFields()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            createUser(email, contrasena)
        }

        binding.tvRestablecerPassword.setOnClickListener {
               resetPassword()
        }


    }

    private fun validateFields(): Boolean{
        email = binding.tietEmail.text.toString().trim()  //Elimina los espacios en blanco
        contrasena = binding.tietContrasena.text.toString().trim()

        //Verifica que el campo de correo no esté vacío
        if(email.isEmpty()){
            binding.tietEmail.error = getString(R.string.mail_required)
            binding.tietEmail.requestFocus()
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tietEmail.error = getString(R.string.email_format_invalid)
            binding.tietEmail.requestFocus()
            return false
        }

        //Verifica que el campo de la contraseña no esté vacía y tenga al menos 6 caracteres
        if(contrasena.isEmpty()){
            binding.tietContrasena.error = getString(R.string.pswrequired)
            binding.tietContrasena.requestFocus()
            return false
        }else if(contrasena.length < 6){
            binding.tietContrasena.error = getString(R.string.psw6)
            binding.tietContrasena.requestFocus()
            return false
        }
        return true
    }


    private fun handleErrors(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                message(getString(R.string.formatemailerror))
                binding.tietEmail.error = getString(R.string.formatemailincorrect)
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                message(getString(R.string.invalidpsw))
                binding.tietContrasena.error = getString(R.string.invalidpsw)
                binding.tietContrasena.requestFocus()
                binding.tietContrasena.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                message("Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso")
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                message("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmail.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                message("Error: La sesión ha expirado. Favor de ingresar nuevamente.")
            }
            "ERROR_USER_NOT_FOUND" -> {
                message("Error: No existe el usuario con la información proporcionada.")
            }
            "ERROR_WEAK_PASSWORD" -> {
                message("La contraseña porporcionada es inválida")
                binding.tietContrasena.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.tietContrasena.requestFocus()
            }
            "NO_NETWORK" -> {
                message("Red no disponible o se interrumpió la conexión")
            }
            else -> {
                message("Error. No se pudo autenticar exitosamente.")
            }
        }

    }

    private fun actionLoginSuccessful(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun authenticateUser(usr: String, psw: String){
        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful){
                message(getString(R.string.succes_auth))
                actionLoginSuccessful()
            } else {
                binding.progressBar.visibility = View.GONE
                handleErrors(authResult)
            }
        }
    }

    private fun createUser(user: String, psw: String){
        firebaseAuth.createUserWithEmailAndPassword(user, psw).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {

              /*  firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    message("El correo de verifiación ha sido enviado.")
                }?.addOnFailureListener {
                    message("No se pudo enviar el correo de verificación.")
                }*/
                message(getString(R.string.created_user))
                actionLoginSuccessful()
            } else {
                binding.progressBar.visibility = View.GONE
                handleErrors(authResult)
            }
        }
    }


    private fun resetPassword(){
        val resetMail = EditText(this)
        resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.res_psw))
            .setMessage(getString(R.string.capture_mail))
            .setView(resetMail)
            .setPositiveButton(getString(R.string.send)) { _, _ ->
                val mail = resetMail.text.toString()
                if (mail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                  firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener {
                      message(getString(R.string.link_send))
                  }.addOnFailureListener {
                      message(getString(R.string.fail_send_message))
                  }
                } else {
                    message(getString(R.string.plis_mail))
                }
            }
            .setNegativeButton(getString(R.string.calcel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

}