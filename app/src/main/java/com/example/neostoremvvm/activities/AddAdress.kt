package com.example.neostoremvvm.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.neostoremvvm.R
import com.example.neostoremvvm.dataclass.AddressInfo
import com.example.neostoremvvm.model.AddressViewModel
import com.example.neostoremvvm.storage.AddressDb
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddAdress:AppCompatActivity(){

    lateinit var addressDb: AddressDb
    lateinit var viewmodel: AddressViewModel
    lateinit var addressListRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        /* btnAddToCartToolbarBack.setOnClickListener() {
             onBackPressed()
         }*/
        val btnSave = findViewById<Button>(R.id.btnSaveAddress)
        val edittxtAddress = findViewById<EditText>(R.id.edittxtAddress)
        val edittxtCityName= findViewById<EditText>(R.id.edittxtCityName)
        val edittxtstate= findViewById<EditText>(R.id.edittxtstate)
        val edittxtZipCode=findViewById<EditText>(R.id.edittxtZipCode)
        val edittxtCountry= findViewById<EditText>(R.id.edittxtCountry)
        viewmodel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(AddressViewModel::class.java)

       /* addressDb = Room.databaseBuilder(
            applicationContext, AddressDb::class.java, "address"
        ).build()*/

        btnSave.setOnClickListener {
            if (!addressEmpty() or !landMarkEmpty() or !cityNameEmpty() or !stateNameEmpty() or !countryNameEmpty() or !zipcodeEmpty()) {
                return@setOnClickListener
            } else {

                GlobalScope.launch {
                    val addressInfo = AddressInfo(
                        0,
                        edittxtAddress.text.toString(),
                        edittxtstate.text.toString(),
                        edittxtCityName.text.toString(),
                        edittxtZipCode.text.toString(),
                        edittxtCountry.text.toString(),
                    )
                    viewmodel.insertAddress(addressInfo)
                }
                 val finalAdd = edittxtAddress.text.toString().trim()
                val intent = Intent()
                intent.putExtra("finalAddress", finalAdd)
                setResult(101, intent)
                finish()
            }
            val intent = Intent(this, AddressListScreen::class.java)
            startActivity(intent)
            }

        }

    private fun addressEmpty(): Boolean {
        if(edittxtAddress.text.isEmpty()){
            edittxtAddress.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtAddress.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun zipcodeEmpty(): Boolean {
        if(edittxtZipCode.text.isEmpty()){
            edittxtZipCode.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else if(edittxtZipCode.text.length == 6){
            edittxtZipCode.setBackgroundResource(R.drawable.border_white)
            return true
        }else
            Toast.makeText(this,"Please enter valid zipcode", Toast.LENGTH_LONG).show()
        return false

    }

    private fun countryNameEmpty(): Boolean {
        if(edittxtCountry.text.isEmpty()){
            edittxtCountry.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtCountry.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun stateNameEmpty(): Boolean {
        if(edittxtstate.text.isEmpty()){
            edittxtstate.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtstate.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun cityNameEmpty(): Boolean {
        if(edittxtCityName.text.isEmpty()){
            edittxtCityName.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtCityName.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun landMarkEmpty(): Boolean {
        if(edittxtLandMark.text.isEmpty()){
            edittxtLandMark.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else{
            edittxtLandMark.setBackgroundResource(R.drawable.border_white)
            return true
        }
    }
}
