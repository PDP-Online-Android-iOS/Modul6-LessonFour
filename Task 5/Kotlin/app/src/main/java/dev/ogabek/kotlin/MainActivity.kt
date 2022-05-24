package dev.ogabek.kotlin

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var rvPhotos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        initViews()

    }

    private fun initViews() {

        rvPhotos = findViewById(R.id.rv_photos)

        rvPhotos.layoutManager = LinearLayoutManager(this)

        if (permissionGranted()) {
            refreshAdapter()
        }

    }

    private fun refreshAdapter() {
        rvPhotos.adapter = Adapter(this, loadPhotosFromExternalStorage() as ArrayList<Uri>)
    }

    private fun permissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
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

    override fun onRestart() {
        super.onRestart()
        if (!permissionGranted()) openPermission() else saveImagesToStorage()
    }

    private fun saveImagesToStorage() {
        savePhotoToExternalStorage("img", R.drawable.img)
        savePhotoToExternalStorage("img_1", R.drawable.img_1)
        savePhotoToExternalStorage("img_2", R.drawable.img_2)
        savePhotoToExternalStorage("img_3", R.drawable.img_3)
        savePhotoToExternalStorage("img_4", R.drawable.img_4)
        savePhotoToExternalStorage("img_5", R.drawable.img_5)
        savePhotoToExternalStorage("img_6", R.drawable.img_6)
        savePhotoToExternalStorage("img_7", R.drawable.img_7)
        refreshAdapter()
    }

    private fun loadPhotosFromExternalStorage(): List<Uri> {

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT,)

        val photos = mutableListOf<Uri>()
        return contentResolver.query(collection, projection, null, null, "${MediaStore.Images.Media.DISPLAY_NAME} ASC")?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val width = cursor.getInt(widthColumn)
                val height = cursor.getInt(heightColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                photos.add(contentUri)
            }
            photos.toList()
        } ?: listOf()
    }

    private fun savePhotoToExternalStorage(filename: String, image: Int): Boolean {

        val bmp = BitmapFactory.decodeResource(resources, image)

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }
        return try {
            contentResolver.insert(collection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}