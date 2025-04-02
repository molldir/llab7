package com.android.filereadwrite

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
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

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        requestNeededPermission()
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "I need it for File", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_PERM)
        } else {
            Toast.makeText(this, "Already have permission", Toast.LENGTH_SHORT).show()
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
        val file = Environment.getExternalStorageDirectory().absolutePath + "/test.txt"
        Toast.makeText(this, file, Toast.LENGTH_LONG).show()
        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(file)
            os.write(data.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            os?.close()
        }
    }

    private fun readFile(): String {
        val file = Environment.getExternalStorageDirectory().absolutePath + "/test.txt"
        var result = ""
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file)
            val bos = ByteArrayOutputStream()
            var ch: Int
            while (inputStream.read().also { ch = it } != -1) {
                bos.write(ch)
            }
            result = bos.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return result
    }
}
