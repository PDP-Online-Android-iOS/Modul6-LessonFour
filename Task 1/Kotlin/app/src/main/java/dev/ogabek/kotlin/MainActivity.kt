package dev.ogabek.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {

        val createCache: Button = findViewById(R.id.createCache)
        val createFile: Button = findViewById(R.id.createFile)
        val deleteCache: Button = findViewById(R.id.deleteCache)
        val deleteFile: Button = findViewById(R.id.deleteFile)

        createCache.setOnClickListener {
            createFileInInternalCache()
        }

        createFile.setOnClickListener {
            createFileInInternalFile()
        }

        deleteCache.setOnClickListener {
            deleteFileInInternalCache()
        }

        deleteFile.setOnClickListener {
            deleteFileInInternalFile()
        }

    }

    // Create File in Internal File
    private fun createFileInInternalFile() {
        val fileName = "pdp_internal.txt"

        val file = File(filesDir, fileName)

        if (!file.exists()) {
            try {
                file.createNewFile()
                Toast.makeText(this, String.format("File %s has been created", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this, String.format("File %s creation failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s already exist", fileName), Toast.LENGTH_SHORT).show()
        }

    }

    // Create File in Internal Cache
    private fun createFileInInternalCache() {
        val fileName = "pdp_internal.txt"

        val file = File(cacheDir, fileName)

        if (!file.exists()) {
            try {
                file.createNewFile()
                Toast.makeText(this, String.format("File %s has been created", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s creation failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s already exist", fileName), Toast.LENGTH_SHORT).show()
        }

    }

    // Delete File in Internal File
    private fun deleteFileInInternalFile() {
        val fileName = "pdp_internal.txt"

        val file = File(filesDir, fileName)

        if (file.exists()) {
            try {
                file.delete()
                Toast.makeText(this, String.format("File %s has been deleted", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s deletion failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s doesn't exist", fileName), Toast.LENGTH_SHORT).show()
        }
    }

    // Delete FIle in Internal Cache
    private fun deleteFileInInternalCache() {
        val fileName = "pdp_internal.txt"

        val file = File(cacheDir, fileName)

        if (file.exists()) {
            try {
                file.delete()
                Toast.makeText(this, String.format("File %s has been deleted", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s deletion failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s doesn't exist", fileName), Toast.LENGTH_SHORT).show()
        }

    }

}