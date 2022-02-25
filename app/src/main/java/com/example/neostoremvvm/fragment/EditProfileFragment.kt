package com.example.neostoremvvm.fragment

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.activities.MyAccountActivity
import com.example.neostoremvvm.api.RetrofitService.Companion.retrofitService
import com.example.neostoremvvm.databinding.FragmentEditProfileBinding
import com.example.neostoremvvm.factoryclass.EditProfileFactory
import com.example.neostoremvvm.model.EditProfileViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.registratio_actionbar_custom_layout.*
import java.io.ByteArrayOutputStream
import java.util.*

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val IMAGE = 100
    lateinit var mbitmap: Bitmap
    lateinit var accessToken: String
    lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mbitmap = createBitmap(1000, 1000)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this, EditProfileFactory(MainRepository(retrofitService))).get(
            EditProfileViewModel::class.java)

        val activity = activity as MyAccountActivity
        val firstName = SharedPreferenceManager.getInstance(activity).data.first_name
        val lastName = SharedPreferenceManager.getInstance(activity).data.last_name
        val email = SharedPreferenceManager.getInstance(activity).data.email
        val phoneNumber = SharedPreferenceManager.getInstance(activity).data.phone_no
        accessToken = SharedPreferenceManager.getInstance(activity).data.access_token
        binding.txtEditProfileFirstName.setText(firstName)
        binding.txtEditProfileLastName.setText(lastName)
        binding.txtEditProfileEmail.setText(email)
        binding.txtEditProfilePhoneNumber.setText(phoneNumber.toString())

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        profileObserve()
    }
    private fun setupClickListeners() {
        binding.editAccProfileImage.setOnClickListener(){
            selectImage()
        }

        binding.editAccountBackPress.setOnClickListener(){
            activity?.supportFragmentManager?.popBackStack("Edit_Profile",FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.btnEditProfileSubmit.setOnClickListener(){
            uploadEditProfileData(accessToken)
        }

        binding.txtEditProfileDob.setOnClickListener(){
            val activity = activity as MyAccountActivity
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                txtEditProfileDob.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
            }, year, month, day)
            dpd.show()
        }
    }
    private fun selectImage() {
        val imageIntent = Intent()
        imageIntent.setType("image/*")
        imageIntent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(imageIntent,IMAGE)
    }
    private fun uploadEditProfileData(accessToken: String) {
        viewModel.getProfile(
            accessToken,
            txtEditProfileFirstName.text.toString(),
            txtEditProfileLastName.text.toString(),
            txtEditProfileEmail.text.toString(),
            txtEditProfileDob.text.toString(),
            txtEditProfilePhoneNumber.text.toString(),
            "data:image/jpg;base64,"+convertToString()!!
        )
    }
    private fun profileObserve() {
        viewModel.profile.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null){
                Toast.makeText((activity as MyAccountActivity).applicationContext, "${it.user_msg}", Toast.LENGTH_LONG).show()
                fetchUserData(accessToken)
            }else{
                Log.d(ContentValues.TAG, "onResponse: Something went wrong")
            }
        })
    }
    private fun convertToString(): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgByte: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgByte, Base64.DEFAULT)
    }
    private fun fetchUserData(accessToken: String) {
        viewModel.fetchDetailData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                SharedPreferenceManager.getInstance((activity as MyAccountActivity).applicationContext)
                    .saveUser(it.data?.user_data!!)
                Log.d(
                    ContentValues.TAG,
                    "onResponse: picprofile : ${it.data?.user_data?.profile_pic}"
                )
                (activity as MyAccountActivity).backPress
            } else {
                Log.d(ContentValues.TAG, "onResponse: User Detail not fetch")
            }
        })
        viewModel.getData(accessToken)
    }
}
