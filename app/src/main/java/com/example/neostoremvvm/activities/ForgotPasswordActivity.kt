package com.example.neostoremvvm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.R
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.factoryclass.ForgotPasswordFactory
import com.example.neostoremvvm.factoryclass.RegisterFactory
import com.example.neostoremvvm.model.ForgotPasswordViewModel
import com.example.neostoremvvm.model.RegisterViewModel
import com.example.neostoremvvm.repository.MainRepository
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var viewModel: ForgotPasswordViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnForgotEmialSubmit.setOnClickListener() {
            validateForm()
        }

    }
    private fun validateForm() {
        if(!forgotEmailValidate()){
            return
        }else{
            passwordObserve()
        }
    }
    private fun passwordObserve(){
        viewModel = ViewModelProvider(this, ForgotPasswordFactory(MainRepository(retrofitService))).get(
            ForgotPasswordViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this@ForgotPasswordActivity, "Wrong EmailId", Toast.LENGTH_LONG).show()
        })

        viewModel.forgotPassordLiveData.observe(this, Observer {
            Toast.makeText(this@ForgotPasswordActivity,"${it.user_msg}", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ForgotPasswordActivity,MainActivity::class.java))
        })

        viewModel.getForgotPassword(
            txtInputForgotEmail.editText?.text.toString()
        )
    }
    private fun forgotEmailValidate(): Boolean {
        val email = txtForgotEmail.text.toString()
        if(email.isEmpty()){
            txtInputForgotEmail.apply {
                error = "Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtInputForgotEmail.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputForgotEmail.requestFocus()
            return true
        }
        else{
            txtInputForgotEmail.apply {
                error="please insert valid Email address"
                isExpandedHintEnabled = false
            }
            txtInputForgotEmail.requestFocus()
            return false
        }
    }
}