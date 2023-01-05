package com.example.sqlitekotlinexample

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.example.sqlitekotlinexample.databinding.ActivityAddEditBinding
import com.example.sqlitekotlinexample.db.DatabaseHandler
import com.example.sqlitekotlinexample.models.Tasks
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddOrEditActivity : AppCompatActivity() {

    private var dbHandler: DatabaseHandler? = null
    private var isEditMode = false

    private lateinit var binding: ActivityAddEditBinding

    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initDB()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        binding.btnDelete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "Edit") {
            isEditMode = true
            val tasks: Tasks = dbHandler!!.getTask(intent.getIntExtra("Id",0))
            binding.inputName.setText(tasks.name)
            binding.inputDesc.setText(tasks.desc)
            binding.inputDeveloper.setText(tasks.developer)
            binding.swtCompleted.isChecked = tasks.completed == "Y"
            binding.btnDelete.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initOperations() {
        binding.btnSave.setOnClickListener {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val success: Boolean
            if (!isEditMode) {
                val tasks = Tasks()
                tasks.name = binding.inputName.text.toString()
                tasks.desc = binding.inputDesc.text.toString()
                tasks.developer = binding.inputDeveloper.text.toString()
                tasks.time =LocalDateTime.now().format(formatter)
                if (binding.swtCompleted.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.addTask(tasks) as Boolean
            } else {
                val tasks = Tasks()
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.name = binding.inputName.text.toString()
                tasks.desc = binding.inputDesc.text.toString()
                tasks.developer = binding.inputDeveloper.text.toString()
                tasks.time = LocalDateTime.now().format(formatter)
                if (binding.swtCompleted.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.updateTask(tasks) as Boolean
            }

            if (success)
                finish()
        }

        binding.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Info")
                .setMessage("Click 'YES' Delete the Task.")
                .setPositiveButton("YES") { dialog, i ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
