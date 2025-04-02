package com.example.llab7

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.util.Date

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_WRITE_PERM = 401
    }

    private lateinit var tvData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        tvData = findViewById(R.id.tvData)

        findViewById<Button>(R.id.btnWriteFile).setOnClickListener {
            writeFile("Hello: " + Date(System.currentTimeMillis()).toString())
        }

        findViewById<Button>(R.id.btnReadFile).setOnClickListener {
            val data = readFile()
            tvData.text = data
        }

        requestNeededPermission()
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_PERM
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_WRITE_PERM) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun writeFile(data: String) {
        val file = File(getExternalFilesDir(null), "test.txt")
        Toast.makeText(this, file.absolutePath, Toast.LENGTH_LONG).show()
        try {
            file.writeText(data)
            Toast.makeText(this, "Файл успешно записан", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка записи файла", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFile(): String {
        val file = File(getExternalFilesDir(null), "test.txt")
        return try {
            file.readText()
        } catch (e: IOException) {
            e.printStackTrace()
            "Ошибка чтения файла"
        }
    }
}
