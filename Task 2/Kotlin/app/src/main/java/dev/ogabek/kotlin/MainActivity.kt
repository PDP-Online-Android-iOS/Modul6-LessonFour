package dev.ogabek.kotlin

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        initViews()

    }

    private fun permissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permission", "Granted")
        } else {
            Log.d("Permission", "Not Granted")
            openPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "checkSelfPermission")
        } else {
            Log.d("Permission", "requestPermissionLauncher.launch")
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun initViews() {
        val createCache = findViewById<Button>(R.id.createCache)
        val createFile = findViewById<Button>(R.id.createFile)
        val deleteCache = findViewById<Button>(R.id.deleteCache)
        val deleteFile = findViewById<Button>(R.id.deleteFile)

        createCache.setOnClickListener {
            if (permissionGranted()) {
                createFileInExternalCache()
            } else {
                openPermission()
            }
        }

        createFile.setOnClickListener {
            if (permissionGranted()) {
                createFileInExternalFile()
            } else {
                openPermission()
            }
        }

        deleteCache.setOnClickListener {
            if (permissionGranted()) {
                deleteFileInExternalCache()
            } else {
                openPermission()
            }
        }

        deleteFile.setOnClickListener {
            if (permissionGranted()) {
                deleteFileInExternalFile()
            } else {
                openPermission()
            }
        }
    }

    // Show Dialog Message if User Denied Permission
    private fun openPermission() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Give Permission")
        alertDialogBuilder
            .setMessage("We need Permission to Write and Delete Files")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog: DialogInterface, id: Int ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", applicationInfo.packageName, null)
                startActivity(intent)
                dialog.dismiss()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Create File In External File
    private fun createFileInExternalFile() {
        val fileName = "pdp_external.txt"
        val file = File(getExternalFilesDir(null), fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
                Toast.makeText(this, String.format("File %s has been created", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s creation failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s is already exist", fileName), Toast.LENGTH_SHORT).show()
        }
    }

    // Create File In External Cache
    private fun createFileInExternalCache() {
        val fileName = "pdp_external.txt"
        val file = File(externalCacheDir, fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
                Toast.makeText(this, String.format("File %s has been created", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s creation failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s is already exist", fileName), Toast.LENGTH_SHORT).show()
        }
    }

    // Delete File In External File
    private fun deleteFileInExternalFile() {
        val fileName = "pdp_external.txt"
        val file = File(getExternalFilesDir(null), fileName)
        if (file.exists()) {
            try {
                file.delete()
                Toast.makeText(this, String.format("File %s has been deleted", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s deletion failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s doesn't exist", fileName), Toast.LENGTH_SHORT).show()
        }
    }

    // Delete File In External Cache
    private fun deleteFileInExternalCache() {
        val fileName = "pdp_external.txt"
        val file = File(externalCacheDir, fileName)
        if (file.exists()) {
            try {
                file.delete()
                Toast.makeText(this, String.format("File %s has been deleted", fileName), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, String.format("File %s deletion failed", fileName), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, String.format("File %s doesn't exist", fileName), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (!permissionGranted()) openPermission()
    }

}