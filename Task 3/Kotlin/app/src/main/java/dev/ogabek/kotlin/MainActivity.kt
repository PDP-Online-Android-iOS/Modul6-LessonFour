package dev.ogabek.kotlin

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

    }

    private fun cameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun locationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions() {

        val permissionToRequest = mutableListOf<String>()
        if (!cameraPermission()) {
            permissionToRequest.add(Manifest.permission.CAMERA)
        }

        if (!locationPermission()) {
            permissionToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionToRequest.toTypedArray())
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->


        if (!permission[Manifest.permission.CAMERA]!!) openPermission()
        if (!permission[Manifest.permission.ACCESS_COARSE_LOCATION]!!) openPermission()

    }

    // Show Dialog Message if User Denied Permission
    private fun openPermission() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("GIVE PERMISSION")
        alertDialogBuilder
            .setMessage("We need CAMERA and LOCATION Permission to run the app")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", applicationInfo.packageName, null)
                startActivity(intent)
                dialog.dismiss()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onRestart() {
        super.onRestart()
        if (!cameraPermission() || !locationPermission()) openPermission()
    }

}