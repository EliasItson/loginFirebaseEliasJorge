package elias.jorge.practicaautenticacioneliasjorge

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var textViewError: TextView
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        textViewError = findViewById(R.id.textViewError)
        buttonRegister = findViewById(R.id.buttonRegister)

        textViewError.visibility = TextView.INVISIBLE

        buttonRegister.setOnClickListener {
            signUp()
        }

    }

    private fun signUp() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val passwordConfirm = editTextConfirmPassword.text.toString()

        if (!areValidFields(email, password, passwordConfirm)) return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user", user?.email)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    textViewError.text = "Error: ${task.exception?.message}"
                    textViewError.visibility = TextView.VISIBLE
                }
            }
    }

    private fun areValidFields(email: String, password: String, passwordConfirm: String): Boolean{
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            textViewError.text = "all the fields must be filled"
            textViewError.visibility = TextView.VISIBLE
            return false
        }

        if (password != passwordConfirm) {
            textViewError.text = "passwords do not match"
            textViewError.visibility = TextView.VISIBLE
            return false
        }
        return true
    }

}