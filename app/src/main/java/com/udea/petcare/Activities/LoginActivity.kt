package com.udea.petcare.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.udea.petcare.Entities.User
import com.udea.petcare.R
import com.udea.petcare.Services.UserApiService
import com.udea.petcare.ViewModel.PublicationViewModel
import com.udea.petcare.ViewModel.UserViewModel
import es.dmoral.toasty.Toasty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    val GOOGLE_SIGN_IN = 100

    private lateinit var mUserViewModel: UserViewModel
    lateinit var publicationViewModel: PublicationViewModel
    var userNew: User? = null
    private var disposable: Disposable? = null
    var idUserGoogle:Int = 0


    private val userApiServe by lazy {
        UserApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        registroButton.setOnClickListener {
            val intent: Intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        publicationViewModel=ViewModelProvider(this).get(PublicationViewModel::class.java)
        publicationViewModel.deleteAllPublications()

        getUsers()

        loginButton.setOnClickListener {
            var currentUser:User
            val email = LogEmailEditText.editText?.text.toString()
            val password = LogPasswordEditText.editText?.text.toString()
            Handler().postDelayed({
                disposable = userApiServe.getUserByEmail(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result ->
                            currentUser = User(
                                result.id,
                                result.name,
                                result.lastName,
                                result.email,
                                result.password,
                                result.city,
                                result.description
                            )
                            if (result.password == password) {
                                val intent: Intent = Intent(this, NavDrawer::class.java).apply {
                                    putExtra("user", currentUser)
                                }
                                val preferencias = getSharedPreferences(
                                    "currentUser",
                                    Context.MODE_PRIVATE
                                )
                                val editor = preferencias.edit()
                                val gson = Gson() //Instancia Gson.
                                var json = gson.toJson(currentUser)
                                editor.putString("user", json)
                                editor.commit()
                                finish()
                                val currentPref = preferencias.getInt("userId", 0)
                                Log.d("sharePref", currentPref.toString())
                                startActivity(intent)
                                Toasty.success(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                                //R.id.correoEncabezado.setText(result.email)
                                //var nameCom=result.name.plus(" ").plus(result.lastName)
                                //nombreEncabezado.setText(nameCom)

                            } else {
                                Toasty.error(this, "Contraseña Incorrecta", Toast.LENGTH_SHORT)
                                    .show()

                            }

                        },

                        { error ->
                            Toasty.error(
                                this,
                                "Contraseña o Correo Incorrecto",
                                Toast.LENGTH_SHORT
                            ).show()
                            error.message?.let { Log.d("error", it) }
                        }
                    )
            }, 1000)

        }

        inicioGoogleButton.setOnClickListener {

            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                disposable = userApiServe.getUserByEmail(account.email.toString())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        { result ->
                                            userNew = User(
                                                result.id,
                                                result.name,
                                                result.lastName,
                                                result.email,
                                                result.password,
                                                result.city,
                                                result.description
                                            )

                                        },

                                        { error ->  //Toasty.error(this, "Contraseña o Correo Incorrecto", Toast.LENGTH_SHORT).show()
                                            error.message?.let { Log.d("error", it) }
                                        }
                                    )



                                Handler().postDelayed(Runnable {
                                    if (userNew != null) {
                                        var currentUserG = userNew!!
                                        //currentUserG= User(currentUserG.id,currentUserG.name,it.lastName,it.email,it.password,it.city,it.description)
                                        Log.d("Foto1", currentUserG.toString())
                                        val intent: Intent =
                                            Intent(this, NavDrawer::class.java).apply {
                                                putExtra("user", currentUserG)
                                            }
                                        val preferencias = getSharedPreferences(
                                            "currentUser",
                                            Context.MODE_PRIVATE
                                        )
                                        val editor = preferencias.edit()
                                        val gson = Gson() //Instancia Gson.
                                        var json = gson.toJson(currentUserG)
                                        editor.putString("user", json)
                                        editor.commit()
                                        finish()
                                        val currentPref = preferencias.getInt("userId", 0)
                                        Log.d("sharePref", currentPref.toString())
                                        startActivity(intent)
                                        Toasty.success(this, "Bienvenido", Toast.LENGTH_SHORT)
                                            .show()

                                    } else {
                                        var googleEmail: String = account.email!!
                                        var user = User(
                                            0,
                                            account.givenName!!,
                                            account.familyName!!,
                                            googleEmail,
                                            "usaGoogle",
                                            "",
                                            ""
                                        )
                                        Log.d("Foto2", user.toString())
                                        userApiServe.createUser(user).enqueue(object :
                                            Callback<User> {
                                            override fun onFailure(call: Call<User>, t: Throwable) {
                                                Log.d("Error", "fatal")
                                            }

                                            override fun onResponse(
                                                call: Call<User>,
                                                response: Response<User>
                                            ) {
                                                var userGoogle = response.body()!!
                                                idUserGoogle = userGoogle.id
                                                val preferencias = getSharedPreferences(
                                                    "currentUser",
                                                    Context.MODE_PRIVATE
                                                )
                                                val editor = preferencias.edit()
                                                val gson = Gson() //Instancia Gson.
                                                var json = gson.toJson(user)
                                                editor.putString("user", json)
                                                editor.commit()
                                                finish()
                                                Log.d("Response", response.body().toString())

                                            }

                                        })

                                        mUserViewModel.addUser(user)
                                        val intent: Intent =
                                            Intent(this, NavDrawer::class.java).apply {
                                                putExtra("user", user)
                                            }


                                        startActivity(intent)
                                        Toasty.success(this, "Bienvenido", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                }, 3000)



                            } else {
                                Toasty.error(this, "La autenticación falló", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            } catch (e: ApiException) {
                Log.d("Falló", "falló")
            }


        }
    }


    private fun getUsers(){
       mUserViewModel.deleteAllUsers()
        disposable = userApiServe.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    for (i: Int in result.indices) {
                        var user = User(
                            result[i].id,
                            result[i].name,
                            result[i].lastName,
                            result[i].email,
                            result[i].password,
                            result[i].city,
                            result[i].description
                        )

                        Log.d("UsuarioApi", result.toString())
                        mUserViewModel.addUser(user)
                    }

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )
    }






}
