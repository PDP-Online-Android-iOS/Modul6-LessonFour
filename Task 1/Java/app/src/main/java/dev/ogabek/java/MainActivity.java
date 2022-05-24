package dev.ogabek.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        Button createCache = findViewById(R.id.createCache);
        Button createFile = findViewById(R.id.createFile);
        Button deleteCache = findViewById(R.id.deleteCache);
        Button deleteFile = findViewById(R.id.deleteFile);

        createCache.setOnClickListener(view -> createFileInInternalCache());

        createFile.setOnClickListener(view -> createFileInInternalFile());

        deleteCache.setOnClickListener(view -> deleteFileInInternalCache());

        deleteFile.setOnClickListener(view -> deleteFileInInternalFile());

    }

    // Create File In Internal File
    private void createFileInInternalFile() {
        String fileName = "pdp_internal.txt";

        File file = new File(getFilesDir(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(this, String.format("File %s has been created", fileName), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, String.format("File %s creation failed", fileName), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, String.format("File %s is already exist", fileName), Toast.LENGTH_SHORT).show();
        }

    }

    // Create File In Internal Cache
    private void createFileInInternalCache() {
        String fileName = "pdp_internal.txt";

        File file = new File(getCacheDir(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(this, String.format("File %s has been created", fileName), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, String.format("File %s creation failed", fileName), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, String.format("File %s is already exist", fileName), Toast.LENGTH_SHORT).show();
        }
    }

    // Delete File In Internal File
    private void deleteFileInInternalFile() {
        String fileName = "pdp_internal.txt";

        File file = new File(getFilesDir(), fileName);

        if (file.exists()) {
            try {
                file.delete();
                Toast.makeText(this, String.format("File %s has been deleted", fileName), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, String.format("File %s deletion failed", fileName), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, String.format("File %s doesn't exist", fileName), Toast.LENGTH_SHORT).show();
        }
    }

    // Delete File In Internal Cache
    private void deleteFileInInternalCache() {
        String fileName = "pdp_internal.txt";

        File file = new File(getCacheDir(), fileName);

        if (file.exists()) {
            try {
                file.delete();
                Toast.makeText(this, String.format("File %s has been deleted", fileName), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, String.format("File %s deletion failed", fileName), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, String.format("File %s doesn't exist", fileName), Toast.LENGTH_SHORT).show();
        }
    }

}