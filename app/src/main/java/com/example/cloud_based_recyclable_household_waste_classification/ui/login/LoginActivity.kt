package com.example.cloud_based_recyclable_household_waste_classification.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserModel
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityLoginBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.main.MainActivity
import com.example.cloud_based_recyclable_household_waste_classification.ui.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.progressBarLogin.visibility = View.GONE

        binding.buttonGoRegister.setOnClickListener{
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            email = binding.edLoginEmail.text.toString()
            password = binding.edLoginPassword.text.toString()

            if (binding.edLoginEmail.text.toString().isEmpty() ||
                binding.edLoginPassword.text.toString().isEmpty()
            ) {
                showToast("All field must be filled")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Invalid Email Format")
            } else if (binding.edLoginPassword.text.toString().length < 8) {
                showToast("Password less than 8 Char")
            } else {
                viewModel.loginRequest(email, password)
            }

            viewModel.loginValue.observe(this) { loginResponse ->
                lifecycleScope.launch {
                    viewModel.saveSession(
                        UserModel(
                            username = loginResponse.user,
                            email = email,
                            exp = loginResponse.exp,
                            token = loginResponse.token,
                            isLogin = true
                        )
                    )

                    delay(1000)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }



        }

        viewModel.message.observe(this) { message ->
            message?.let {
                showToast(it)
            }

        }

        viewModel.isLoading.observe(this) { loadingState ->
            if (loadingState == true) {
                binding.progressBarLogin.visibility = View.VISIBLE
            } else {
                binding.progressBarLogin.visibility = View.GONE
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun playAnimation() {


        val titleText =
            ObjectAnimator.ofFloat(binding.textViewTitleLogin, View.ALPHA, 1f).setDuration(400)
        val titleText2 =
            ObjectAnimator.ofFloat(binding.secondTextViewTitleLogin, View.ALPHA, 1f).setDuration(400)

        val emailEditText =
            ObjectAnimator.ofFloat(binding.textInputEmail, View.ALPHA, 1f).setDuration(400)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.textInputPassword, View.ALPHA, 1f).setDuration(400)

        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)

        val textOr =
            ObjectAnimator.ofFloat(binding.textViewOrRegister, View.ALPHA, 1f).setDuration(400)

        val buttonGoRegister =
            ObjectAnimator.ofFloat(binding.buttonGoRegister, View.ALPHA, 1f).setDuration(400)


        AnimatorSet().apply {
            playSequentially(
                titleText,
                titleText2,
                emailEditText,
                passwordEditText,
                buttonLogin,
                textOr,
                buttonGoRegister
            )
            start()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}