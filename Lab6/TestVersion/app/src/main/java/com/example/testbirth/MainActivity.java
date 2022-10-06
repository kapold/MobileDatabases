package com.example.testbirth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);
        personList = new ArrayList<>();

        searchBar_ed = findViewById(R.id.birth_TV);
        searchInfo_tv = findViewById(R.id.allInfo_TV);

        File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        if (!publicFile.exists())
        {
            Toast.makeText(this, "Создается файл External", Toast.LENGTH_SHORT).show();
            try {
                publicFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    EditText searchBar_ed;
    TextView searchInfo_tv;
    String fullInfo = "";
    String currentSearchText;
    List<Person> personList;
    String fileName = "ContactsInfo.json";

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void SearchInfo_Btn(View view)
    {
        personList = PersonSerialize.importFromJSONExternal(this);

        try {
            currentSearchText = searchBar_ed.getText().toString();
            fullInfo = "";
            for (Person p: personList) {
                if (p.birth.equals(currentSearchText))
                    fullInfo += String.format("Name: %s %s\nPhone: %s\n",p.name, p.surname, p.phone);
            }
            searchInfo_tv.setText(fullInfo);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}