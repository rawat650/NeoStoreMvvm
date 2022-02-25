package com.example.neostoremvvm.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.R
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.factoryclass.LoginFactory
import com.example.neostoremvvm.model.LoginViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel
    lateinit var accessToken: String
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgplus.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        btnLoginButton.setOnClickListener {
            validateForm()
        }
        txtforgotPassword.setOnClickListener(){
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
        accessToken = SharedPreferenceManager.getInstance(this).data.access_token
        registerObserve()
    }
    private fun registerObserve(){
        viewModel = ViewModelProvider(this, LoginFactory(MainRepository(retrofitService))).get(LoginViewModel::class.java)
        viewModel.loginLiveData.observe(this, Observer {
                Toast.makeText(this@MainActivity , "Login Succeessful", Toast.LENGTH_LONG).show()
                SharedPreferenceManager.getInstance(applicationContext).saveUser(it.data)
                updateCartValue()
                val intent = Intent(this@MainActivity,HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

        })
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this@MainActivity, "UnSuccessful", Toast.LENGTH_LONG).show()
        })
    }
    private fun updateCartValue(){
        viewModel.fetchDetailData.observe(this, Observer {
            if(it != null){
                val totalCarts = it.data.total_carts
                val sharedPreferences = this@MainActivity.getSharedPreferences("my_private_sharedpref",
                    Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("total_carts",totalCarts)
                editor.apply()
            }
            else{
                Toast.makeText(this@MainActivity,"$it",Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getUpdateData(accessToken)
    }
    override fun onStart() {
        super.onStart()
        if(SharedPreferenceManager.getInstance(this).loggedIn){
            val intent = Intent(applicationContext,HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
    private fun validateForm(){
        if(!userNameValidate() or !passWordValidate()){
            return
        }else{
            userLogin()
        }
    }
    private fun userLogin() {
        viewModel.getlogin(
            txtInputUserName.editText?.text.toString(),
            txtInputPassword.editText?.text.toString()
        )
    }
    private fun userNameValidate():Boolean{
        if(txtInputUserName.editText?.text.toString().isEmpty()){
            txtInputUserName.apply{

                error= "Filed Can't be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
        else{
            txtInputUserName.apply{
                error = null
                isExpandedHintEnabled = true
            }
            txtInputUserName.requestFocus()
            return true
        }

    }

    private fun passWordValidate():Boolean{
        if(txtInputPassword.editText?.text.toString().isEmpty()){
            txtInputPassword.apply {
                error = "field can't be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
        else{
            txtInputPassword.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputUserName.requestFocus()
            return true

        }
    }
}