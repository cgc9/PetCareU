package com.udea.petcare.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.udea.petcare.Entities.Publication
import com.udea.petcare.Entities.PublicationApi
import com.udea.petcare.Entities.User
import com.udea.petcare.Fragments.*
import com.udea.petcare.R
import com.udea.petcare.Services.PublicationApiService
import com.udea.petcare.ViewModel.PublicationViewModel
import com.udea.petcare.ViewModel.UserViewModel
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_addpublication.*
import kotlinx.android.synthetic.main.dialog_addpublication.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NavDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var publicationViewModel: PublicationViewModel
    lateinit var userViewModel: UserViewModel
    private var disposable: Disposable? = null
    lateinit var type:String


    private val publicationApiServe by lazy {
        PublicationApiService.create()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val currentUser=intent.getParcelableExtra<User>("user")
        var logUser= currentUser!!
        publicationViewModel= ViewModelProvider(this).get(PublicationViewModel::class.java)
        userViewModel= ViewModelProvider(this).get(UserViewModel::class.java)


        val prefs=getSharedPreferences("iniciosesion", Context.MODE_PRIVATE).edit()
       // prefs.putString("email",email)
        //prefs.putString("provider",provider)
        //prefs.apply()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val header:View=navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.nombreEncabezado).setText(logUser.name.plus(" ").plus(logUser.lastName))
        header.findViewById<TextView>(R.id.correoEncabezado).setText(logUser.email)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_adoption,
                R.id.nav_losing,
                R.id.nav_veterinary,
                R.id.nav_guarderias
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)




        addButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nueva publicación")
            val view = layoutInflater.inflate(R.layout.dialog_addpublication, null)
            builder.setView(view);

            val radioG:RadioGroup=view.findViewById(R.id.radio_type)
            radioG.setOnCheckedChangeListener { group, checkedId ->

                if(checkedId==R.id.radio_adoption){
                    type="Adopción"
                }
                if(checkedId==R.id.radio_losing){
                    type="Perdida"
                }
            }


            builder.setPositiveButton("Guardar",
                DialogInterface.OnClickListener { dialog, id ->

                    var publicationTitle: String = view.publicationTitle.editText?.text.toString()
                    var publicationDescription = view.publicationDescription.editText?.text.toString()
                    var publicationCity = view.publicationCity.editText?.text.toString()
                    val currentDateTime: LocalDateTime = LocalDateTime.now()
                    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                    val formattedDateTime = currentDateTime.format(formatter)


                    var nPublication =
                        Publication(
                            0,
                            publicationTitle,
                            publicationDescription,
                            publicationCity,
                            type,
                            formattedDateTime,
                            "no",
                            logUser.id
                        )


                    var apiPublication=PublicationApi(
                        0,
                        publicationTitle,
                        publicationDescription,
                        publicationCity,
                        type,
                        formattedDateTime,
                        "no",
                        logUser
                    )

                    publicationApiServe.createPublication(apiPublication).enqueue(object:
                        Callback<PublicationApi> {
                        override fun onFailure(call: Call<PublicationApi>, t: Throwable) {
                            Log.d("Error","fatal publicacion")
                        }

                        override fun onResponse(call: Call<PublicationApi>, response: Response<PublicationApi>) {
                            Log.d("Respuesta",response.body().toString())

                        }
                    })

                    publicationViewModel.addPublication(nPublication)


                    dialog?.dismiss()


                })
                .setNegativeButton("Cancelar") { dialog: DialogInterface?, which: Int ->

                    dialog?.dismiss()
                }

            builder.show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_salir -> {
                val intent = Intent(this, LoginActivity::class.java)
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                Intent.FLAG_ACTIVITY_CLEAR_TASK
                Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}