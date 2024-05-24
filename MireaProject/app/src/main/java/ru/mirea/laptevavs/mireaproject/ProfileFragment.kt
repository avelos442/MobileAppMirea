package ru.mirea.laptevavs.mireaproject

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels



class ProfileFragment  : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment ()
    }

    private val viewModel: ProfileViewModel by viewModels()


    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val RegLogin: EditText = view.findViewById(R.id.editTextRegLogin)
        val RegPassword: EditText = view.findViewById(R.id.editTextRegPassword)
        val LoginLogin: EditText = view.findViewById(R.id.editTextLoginLogin)
        val LoginPassword: EditText = view.findViewById(R.id.editTextLoginPass)
        val buttonReg: Button = view.findViewById(R.id.buttonRegistration)
        val buttonLogin: Button = view.findViewById(R.id.buttonLogin)

        sharedPref =
            requireActivity().getSharedPreferences("mirea_settings", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPref.edit()

        val savedLogin = sharedPref.getString("login", "unknown")
        val savedPassword = sharedPref.getString("password", "unknown")
        if (!savedLogin.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            LoginLogin.setText(savedLogin)
            LoginPassword.setText(savedPassword)
        }

        buttonReg.setOnClickListener {
            val login = RegLogin.text.toString()
            val password = RegPassword.text.toString()
            try {

                editor.putString("password", password)
                editor.putString("login", login)
                editor.apply()
                LoginLogin.setText(login)
                LoginPassword.setText(password)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        buttonLogin.setOnClickListener {
            val login = LoginLogin.text.toString()
            val password = LoginPassword.text.toString()
            var isCorrect = true
            try {
                if (sharedPref.getString("password", "unknown") != password) {
                    isCorrect = false
                }
                if (sharedPref.getString("login", "unknown") != login) {
                    isCorrect = false
                }
                val text = if (isCorrect) "Вы вошли!" else "Вы НЕ вошли :("
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }
}