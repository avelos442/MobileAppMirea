package ru.mirea.laptevavs.mireaproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.mirea.laptevavs.mireaproject.databinding.ActivityFirebaseBinding

class FirebaseActivity : AppCompatActivity() {

    // Тег для журналирования
    companion object {
        private val TAG = FirebaseActivity::class.java.simpleName
    }

    // Привязка представлений и экземпляр аутентификации Firebase
    private lateinit var binding: ActivityFirebaseBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализация привязки представлений
        binding = ActivityFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация аутентификации Firebase
        mAuth = FirebaseAuth.getInstance()

        // Установка отступов для обработки системных полос (например, строки состояния)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установка слушателей кликов для различных кнопок
        binding.signInButton.setOnClickListener {
            signIn(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
        binding.createAccountButton.setOnClickListener {
            createAccount(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
        binding.signOutButton.setOnClickListener {
            signOut()
        }
        binding.verifyEmailButton.setOnClickListener {
            sendEmailVerification()
        }
    }

    override fun onStart() {
        super.onStart()

        // Проверка, вошёл ли пользователь в систему ранее
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    // Обновление пользовательского интерфейса в зависимости от текущего пользователя
    private fun updateUI(user: FirebaseUser?) {
        // Если пользователь вошёл в систему
        if (user != null) {
            // Обновить элементы пользовательского интерфейса
            binding.statusTextView.text = getString(R.string.emailpassword_status_fmt, user.email, user.isEmailVerified)
            binding.detailTextView.text = getString(R.string.firebase_status_fmt, user.uid)
            binding.signInSection.visibility = View.GONE
            binding.signedInSection.visibility = View.VISIBLE
            binding.verifyEmailButton.isEnabled = !user.isEmailVerified
            toMainActivity(user) // Перейти к главной активности
        } else {
            // Если пользователь вышел из системы
            binding.statusTextView.text = getString(R.string.signed_out)
            binding.detailTextView.text = null
            binding.signInSection.visibility = View.VISIBLE
            binding.signedInSection.visibility = View.GONE
        }
    }

    // Создание новой учётной записи с использованием электронной почты и пароля
    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount: $email")

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail: success")
                    val user = mAuth.currentUser
                    toMainActivity(user) // Перейти к главной активности
                } else {
                    Log.w(TAG, "createUserWithEmail: failure", it.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    // Вход в систему с использованием электронной почты и пароля
    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn: $email")
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "signInWithEmail: success")
                    val user = mAuth.currentUser
                    toMainActivity(user) // Перейти к главной активности
                } else {
                    Log.w(TAG, "signInWithEmail: failure", it.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

                if (!it.isSuccessful) {
                    binding.statusTextView.text = getString(R.string.auth_failed)
                }
            }
    }

    // Переход к главной активности
    private fun toMainActivity(user: FirebaseUser?){
        if(user != null){
            val intent = Intent(this@FirebaseActivity, MainActivity::class.java)
            intent.putExtra("email", user.email)
            startActivity(intent)
        }
    }

    // Выход из системы текущего пользователя
    private fun signOut() {
        mAuth.signOut()
        updateUI(null)
    }

    // Отправка электронного подтверждения
    private fun sendEmailVerification() {
        binding.verifyEmailButton.isEnabled = false

        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) {
                binding.verifyEmailButton.isEnabled = true

                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent to ${user.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.w(TAG, "sendEmailVerification", it.exception)
                    Toast.makeText(
                        this,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
