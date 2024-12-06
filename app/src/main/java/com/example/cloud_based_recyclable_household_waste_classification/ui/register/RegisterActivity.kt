package com.example.cloud_based_recyclable_household_waste_classification.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityLoginBinding
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityRegisterBinding
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBarRegister.visibility = View.GONE

        playAnimation()

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (binding.edRegisterName.text.toString().isEmpty() ||
                binding.edRegisterEmail.text.toString().isEmpty() ||
                binding.edRegisterPassword.text.toString().isEmpty()
            ) {
                showToast("All field must be filled")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Invalid Email Format")
            } else if (binding.edRegisterPassword.text.toString().length < 8) {
                showToast("Password less than 8 Char")
            } else {
                viewModel.registerRequest(name, email, password)
                binding.blackBg.apply {
                    visibility = View.VISIBLE
                    alpha = 0f
                    animate().alpha(1f).setDuration(300).start()
                }
            }

            viewModel.isSuccess.observe(this) { isSuccess ->

                if (isSuccess == true) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

            }
        }

        binding.btnGoLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        viewModel.isLoading.observe(this) { loadingState ->
            if (loadingState == true) {
                binding.progressBarRegister.visibility = View.VISIBLE
            } else {
                binding.progressBarRegister.visibility = View.GONE
                binding.progressBarRegister.visibility = View.GONE
            }
        }

        viewModel.message.observe(this) { message ->
            binding.blackBg.animate().alpha(0f).setDuration(300).withEndAction {
                binding.blackBg.visibility = View.GONE
            }.start()
            message?.let {
                showToast(it)
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    private fun playAnimation() {
   

    val nameText = ObjectAnimator.ofFloat(binding.textInputLayoutRegisterName, View.ALPHA, 1f)
        .setDuration(700)

    val titleText =
            ObjectAnimator.ofFloat(binding.textViewTitleRegister, View.ALPHA, 1f).setDuration(400)

    val titleText2 =
            ObjectAnimator.ofFloat(binding.secondTextViewTitleRegister, View.ALPHA, 1f).setDuration(400)
        
    val emailEditText =
        ObjectAnimator.ofFloat(binding.textInputEmailRegister, View.ALPHA, 1f).setDuration(400)

    val passwordEditText =
        ObjectAnimator.ofFloat(binding.textInputPasswordRegister, View.ALPHA, 1f)
            .setDuration(700)

    val buttonRegister =
        ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(400)

    val textOr =
        ObjectAnimator.ofFloat(binding.tvOrLogin, View.ALPHA, 1f).setDuration(400)

    val buttonGoLogin =
        ObjectAnimator.ofFloat(binding.btnGoLogin, View.ALPHA, 1f).setDuration(400)


    AnimatorSet().apply {
        playSequentially(
            titleText,
            titleText2,
            nameText,
            emailEditText,
            passwordEditText,
            buttonRegister,
            textOr,
            buttonGoLogin
        )
        start()
    }
}

}