package com.example.neostoremvvm.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.activities.MyAccountActivity
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.databinding.FragmentResetPasswordBinding
import com.example.neostoremvvm.factoryclass.ResetPasswordFactory
import com.example.neostoremvvm.model.ResetPasswordViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_reset_password.*
import java.util.regex.Pattern

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    lateinit var accessToken: String
    lateinit var viewModel: ResetPasswordViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this, ResetPasswordFactory(MainRepository(RetrofitService.retrofitService))).get(
            ResetPasswordViewModel::class.java)
        accessToken = SharedPreferenceManager.getInstance(activity as MyAccountActivity).data.access_token
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        resetPasswordObserve()
    }
    private fun setupClickListeners(){
        binding.btnResetPasswordSubmit.setOnClickListener(){
            validatePassWordField()
        }
        binding.resetPasswprdTollbarBack.setOnClickListener(){
            (activity as MyAccountActivity).supportFragmentManager.popBackStack("Reset_Password",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
    private fun validatePassWordField() {
        if (!currentPassword() or !NewPassword() or !confirmedPassword())
            return
        else
            resetPassWord()
    }
    private fun resetPasswordObserve() {
       viewModel.resetPass.observe(viewLifecycleOwner, Observer {
           if(it != null){
               Toast.makeText((activity as MyAccountActivity).applicationContext,"${it.user_msg}",
                   Toast.LENGTH_LONG).show()

           }else{
               Toast.makeText((activity as MyAccountActivity).applicationContext,"${it}",
                   Toast.LENGTH_LONG).show()
           }
       })
    }
    private fun resetPassWord() {
        viewModel.getPassword(
            accessToken,
            txtInputCurrentPassword.editText?.text.toString(),
            txtInputNewPassword.editText?.text.toString(),
            txtInputConPassword.editText?.text.toString()
        )
    }
    private fun confirmedPassword(): Boolean {
        if(txtInputConPassword.editText?.text.toString().isEmpty()){
            txtInputConPassword.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else if(txtInputConPassword.editText?.text.toString() == txtInputNewPassword.editText?.text.toString()){
            txtInputConPassword.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputConPassword.requestFocus()
            return true
        }
        else{
            txtInputConPassword.apply{
                error = "Confirmed password doesn't match"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
    }
    private fun currentPassword(): Boolean {
        val password = txtInputCurrentPassword.editText?.text.toString()
        if (password.isEmpty()){
            txtInputCurrentPassword.apply {
                error = "Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else{
            txtInputCurrentPassword.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputNewPassword.requestFocus()
            return true
        }
    }
    private fun NewPassword():Boolean  {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val mPassWord = txtInputNewPassword.editText?.text.toString()
        if(mPassWord.isEmpty()){
            txtInputNewPassword.apply {
                error="Field cannot be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }else if(Pattern.compile(passwordPattern).matcher(mPassWord).matches() && mPassWord.length >= 8){
            txtInputNewPassword.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputNewPassword.requestFocus()
            return true
        }
        else{
            txtInputNewPassword.apply{
                error = "Password should be alphanumeric and 8 character long"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
    }
}