package dev.ogabek.kotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savePhotoToInternalStorage("img", R.drawable.img)
        savePhotoToInternalStorage("img_1", R.drawable.img_1)
        savePhotoToInternalStorage("img_2", R.drawable.img_2)
        savePhotoToInternalStorage("img_3", R.drawable.img_3)
        savePhotoToInternalStorage("img_4", R.drawable.img_4)
        savePhotoToInternalStorage("img_5", R.drawable.img_5)
        savePhotoToInternalStorage("img_6", R.drawable.img_6)
        savePhotoToInternalStorage("img_7", R.drawable.img_7)

        initViews()

    }

    private fun initViews() {
        val rvPhotos = findViewById<RecyclerView>(R.id.rv_photos)

        rvPhotos.layoutManager = LinearLayoutManager(this)

        rvPhotos.adapter = Adapter(this, loadPhotosFromInternalStorage() as ArrayList<Bitmap>)

    }

    private fun savePhotoToInternalStorage(filename: String, image: Int): Boolean {

        val bmp = BitmapFactory.decodeResource(resources,image);

        return try {
            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun loadPhotosFromInternalStorage(): List<Bitmap> {
        val files = filesDir.listFiles()
        return files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
            val bytes = it.readBytes()
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bmp
        } ?: listOf()
    }


}