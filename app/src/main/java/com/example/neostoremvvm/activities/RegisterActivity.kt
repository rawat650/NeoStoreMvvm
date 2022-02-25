package com.example.neostoremvvm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.R
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.factoryclass.RegisterFactory
import com.example.neostoremvvm.model.RegisterViewModel
import com.example.neostoremvvm.repository.MainRepository
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var viewModel: RegisterViewModel
    private val retrofitService = RetrofitService.getInstance()
    var selectGender : String? = null
    lateinit var radioBtnId: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.registratio_actionbar_custom_layout);

        btnRegister.setOnClickListener {
            validateForm()
        }
        registerObserve()
    }
    private fun validateForm() {
        if(!firstNameValidate() or !lastNameValidate() or !emailAddressValidate() or !passWordValidate()
            or !confirmedPasswordValidate() or !phoneNumberValidate() or !checkGender() or !checkCheckBox()){
            return
        }
        else {
            createUser()
        }
    }
    private fun registerObserve(){

        viewModel = ViewModelProvider(this, RegisterFactory(MainRepository(retrofitService))).get(
            RegisterViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this@RegisterActivity, "Unsuccessful", Toast.LENGTH_SHORT).show()
        })

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.registerLiveData.observe(this, Observer {
                Toast.makeText(this@RegisterActivity, "Registered Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity , MainActivity::class.java)
                startActivity(intent)
                finish()
        })
    }

    private fun createUser() {
        radioBtnId = findViewById(radioGroupButton.checkedRadioButtonId)
        when (radioBtnId.id) {
            R.id.radioBtnMale -> selectGender = "M"
            R.id.radioBtnFemale -> selectGender = "F"
        }
        selectGender?.let {
            Log.d("Test", selectGender!!)
            viewModel.getRegister(
                txtInputFirstName.editText?.text.toString(),
                txtInputLastName.editText?.text.toString(),
                txtInputEmail.editText?.text.toString(),
                txtInputRegPassword.editText?.text.toString(),
                txtInputConfirmedPassword.editText?.text.toString(),
                it,
                txtInputPhoneNumber.editText?.text.toString().toLong()
            )
        }
        //   "Nikeeta","Patil","nikeetaPatil123@gmail.com","Nikita123","Nikita123","F",9890111521)
    }
    private fun firstNameValidate(): Boolean {
        if(txtInputFirstName.editText?.text.toString().isEmpty()){
            txtInputFirstName.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else{
            txtInputFirstName.apply{
                error = null
                isExpandedHintEnabled = true
            }
            txtInputFirstName.requestFocus()
            return true
        }
    }

    private fun lastNameValidate(): Boolean {
        if(txtInputLastName.editText?.text.toString().isEmpty()){
            txtInputLastName.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else{
            txtInputLastName.apply{
                error = null
                isExpandedHintEnabled = true
            }
            txtInputLastName.requestFocus()
            return true
        }
    }

    private fun emailAddressValidate(): Boolean {
        val email = txtEmail.text.toString()
        if(email.isEmpty()){
            txtInputEmail.apply {
                error = "Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtInputEmail.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputEmail.requestFocus()
            return true
        }
        else{
            txtInputEmail.apply {
                error="Please Insert Valid Email Address"
                isExpandedHintEnabled = false
            }
            txtInputEmail.requestFocus()
            return false
        }
    }

    private fun passWordValidate(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val mPassWord = txtInputRegPassword.editText?.text.toString()
        if(txtInputRegPassword.editText?.text.toString().isEmpty()){
            txtInputRegPassword.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else if(Pattern.compile(passwordPattern).matcher(mPassWord).matches() && mPassWord.length >= 8){
            txtInputRegPassword.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputRegPassword.requestFocus()
            return true
        }
        else{

            txtInputRegPassword.apply{
                error = "Password should be alphanumeric and 8 character long"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
    }

    private fun confirmedPasswordValidate(): Boolean {
        if(txtInputConfirmedPassword.editText?.text.toString().isEmpty()){
            txtInputConfirmedPassword.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else if(txtInputConfirmedPassword.editText?.text.toString() == txtInputRegPassword.editText?.text.toString()){
            txtInputConfirmedPassword.apply {
                error = null
                isExpandedHintEnabled = true

            }
            txtInputConfirmedPassword.requestFocus()
            return true
        }
        else{
            txtInputConfirmedPassword.apply{
                error = "Confirmed password does not match"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
    }

    private fun phoneNumberValidate(): Boolean {
        val phoneNumber = txtInputPhoneNumber.editText?.text.toString()
        if(phoneNumber.isEmpty()){
            txtInputPhoneNumber.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else if (phoneNumber.length == 10){
            txtInputPhoneNumber.apply {
                error = null
                isExpandedHintEnabled=true

            }
            txtInputPhoneNumber.requestFocus()
            return true
        }
        else{
            txtInputPhoneNumber.apply{
                error = "Please Input Valid Phone Number"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
    }

    private fun checkGender(): Boolean {
        if(radioGroupButton.checkedRadioButtonId == -1){
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkCheckBox(): Boolean {
        if(checkBox.isChecked){
            return true
        }
        else{
            Toast.makeText(this, "Please Select Terms & Conditions", Toast.LENGTH_SHORT).show()
        }
        return false
    }
}