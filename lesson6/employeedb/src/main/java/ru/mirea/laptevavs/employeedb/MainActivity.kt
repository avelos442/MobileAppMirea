package ru.mirea.laptevavs.employeedb

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.laptevavs.employeedb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = App.getInstance().getDatabase()
        val employeeDao = db.employeeDao()


        var employee = Employee()
        employee.id = 3
        employee.name = "Петров Иван"
        employee.salary = 333000

        employeeDao?.insert(employee)

        val employees = employeeDao?.getAll()
        employees?.forEach { Log.d("Employees", "${it.name}, ${it.salary}") }

        employee = employeeDao?.getById(1)!!
        employee.salary = 20000
        employeeDao.update(employee)
        Log.d("Employee", "${employee.name} ${employee.salary}")
    }
}