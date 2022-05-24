package dev.ogabek.java;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        initViews();

    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Permission", "Granted");
                } else {
                    Log.d("Permission", "Not Granted");
                    openPermission();
                }
            });

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "checkSelfPermission");
        } else {
            Log.d("Permission", "requestPermissionLauncher.launch");
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void initViews() {
        Button createCache = findViewById(R.id.createCache);
        Button createFile = findViewById(R.id.createFile);
        Button deleteCache = findViewById(R.id.deleteCache);
        Button deleteFile = findViewById(R.id.deleteFile);

        createCache.setOnClickListener(view -> {
            if (permissionGranted()) {
                createFileInExternalCache();
            } else {
                openPermission();
            }
        });

        createFile.setOnClickListener(view -> {
            if (permissionGranted()) {
                createFileInExternalFile();
            } else {
                openPermission();
            }
        });

        deleteCache.setOnClickListener(view -> {
            if (permissionGranted()) {
                deleteFileInExternalCache();
            } else {
                openPermission();
            }
        });

        deleteFile.setOnClickListener(view -> {
            if (permissionGranted()) {
                deleteFileInExternalFile();
            } else {
                openPermission();
            }
        });

    }

    // Show Dialog Message if User Denied Permission
    private void openPermission() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Second Chance");
        alertDialogBuilder
                .setMessage("We need Permission to Write and Delete Files")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getApplicationInfo().packageName, null));
                    startActivity(intent);
                    dialog.dismiss();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Create File In External File
    private void createFileInExternalFile() {
        String fileName = "pdp_external.txt";

        File file = new File(getExternalFilesDir(null), fileName);

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

    // Create File In External Cache
    private void createFileInExternalCache() {
        String fileName = "pdp_external.txt";

        File file = new File(getExternalCacheDir(), fileName);

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

    // Delete File In External File
    private void deleteFileInExternalFile() {
        String fileName = "pdp_external.txt";

        File file = new File(getExternalFilesDir(null), fileName);

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

    // Delete File In External Cache
    private void deleteFileInExternalCache() {
        String fileName = "pdp_external.txt";

        File file = new File(getExternalCacheDir(), fileName);

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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!permissionGranted()) openPermission();
    }
}