package ru.mirea.laptevavs.mireaproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import ru.mirea.laptevavs.mireaproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration// конфигурация панели приложения
    private lateinit var binding: ActivityMainBinding // переменная для связи макета и кода активности
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //связывание разметки и кода
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //макет

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.webViewFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mAuth = FirebaseAuth.getInstance() // Инициализация FirebaseAuth

        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            // Если пользователь не авторизован, перенаправьте его на FirebaseActivity
            startActivity(Intent(this, FirebaseActivity::class.java))
            finish()
        } else {
            // Если пользователь авторизован, выведите информацию о нем в логи
            Log.d("CurrentUser", "UID: ${currentUser.uid}")
            Log.d("CurrentUser", "Email: ${currentUser.email}")
            // Выведите другую информацию о пользователе, если она вам нужна
        }

        val workRequest = OneTimeWorkRequest.Builder(Worker::class.java).build()
        WorkManager.getInstance(this).enqueue(workRequest)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_button -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, FirebaseActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}