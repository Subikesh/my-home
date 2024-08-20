package com.spacey.myhome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.spacey.myhome.auth.AuthViewModel
import com.spacey.myhome.data.network.RetrofitService
import com.spacey.myhome.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val authViewModel: AuthViewModel by viewModels { AuthViewModel.Factory(RetrofitService().authApiService) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginButton.setOnClickListener {
            authViewModel.login(binding.loginUserText.text.toString(), binding.loginUserPassword.text.toString())
        }

        authViewModel.isAuthenticated.observe(this) {
            if (it == AuthViewModel.AuthState.LOADING) {
                binding.loginButton.isEnabled = false
            } else {
                binding.loginButton.isEnabled = true
            }
            when (it) {
                AuthViewModel.AuthState.SUCCESS -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                AuthViewModel.AuthState.FAILURE -> Toast.makeText(this, "Login credentials incorrect!", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
}