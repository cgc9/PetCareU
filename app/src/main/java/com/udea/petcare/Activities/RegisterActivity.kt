package com.udea.petcare.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.udea.petcare.Entities.User
import com.udea.petcare.R
import com.udea.petcare.Services.UserApiService
import com.udea.petcare.ViewModel.UserViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var mUserViewModel: UserViewModel
    var userNew: User? = null


    private val userApiServe by lazy {
        UserApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        this.setTitle(R.string.registarse)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        registrarseButton.setOnClickListener {

            val name = nameEditText.editText?.text.toString()
            val lastName = lastNameEditText.editText?.text.toString()
            val email = rEmailEditText.editText?.text.toString()
            val password = rPasswordEditText.editText?.text.toString()
            val city = ciudadEditText.editText?.text.toString()
            val descrip = desEditText.editText?.text.toString()

            if (inputCheck(name, lastName, email, password, city, descrip)) {
                mUserViewModel.findUserByEmail(email).observe(this, Observer {
                    userNew = it

                })

                Handler().postDelayed({

                    if (userNew != null) {
                        Toasty.error(
                            this,
                            "El usuario ya se encuentra registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        var user = User(0, name, lastName, email, password, city, descrip)
                        userApiServe.createUser(user).enqueue(object :
                            Callback<User> {
                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.d("Error", "fatal")
                            }

                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                succesRegister(response.body().toString())


                            }

                        })

                        mUserViewModel.addUser(user)
                    }
                }, 1000)


            } else {
                Toasty.error(this, "Por favor ingresar todos los campos", Toast.LENGTH_LONG).show()

            }

        }

    }

    private fun succesRegister(response: String) {
        Log.d("RegistroRta", response)
        Toasty.success(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            rEmailEditText.editText?.text.toString(),
            rPasswordEditText.editText?.text.toString()
        ).addOnCompleteListener(this) {
            Log.d("email", rEmailEditText.editText?.text.toString())
            Log.d("password", rPasswordEditText.editText?.text.toString())
        }
        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)


    }


    private fun inputCheck(
        name: String,
        lastName: String,
        email: String,
        password: String,
        city: String,
        descrip: String
    ): Boolean {

        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(
            password
        ) || TextUtils.isEmpty(city) || TextUtils.isEmpty(descrip))

    }

}
