package com.example.lw3_db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void FillWithInfo(File file)
    {
        TextView absolute = findViewById(R.id.absolute_TV);
        TextView name = findViewById(R.id.name_TV);
        TextView path = findViewById(R.id.path_TV);
        TextView readOrWrite = findViewById((R.id.readWrite_TV));
        TextView external = findViewById(R.id.external_TV);

        absolute.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        name.setText(String.format("Name: %s", file.getName()));
        path.setText(String.format("Path: %s", file.getPath()));
        readOrWrite.setText(String.format("Read/Write: %s/%s", file.canRead(), file.canWrite()));
        external.setText(String.format("External: %s", Environment.getExternalStorageState()));
    }

    public void FillWithStorageInfo(File file)
    {
        TextView absolute = findViewById(R.id.absolute_TV);
        TextView name = findViewById(R.id.name_TV);
        TextView path = findViewById(R.id.path_TV);
        TextView readOrWrite = findViewById((R.id.readWrite_TV));
        TextView external = findViewById(R.id.external_TV);

        absolute.setText(String.format("Absolute: %s", file.getAbsolutePath()));
        name.setText(String.format("Name: %s", file.getName()));
        path.setText(String.format("Path: %s", file.getPath()));
        readOrWrite.setText(String.format("Read/Write: %s/%s", file.canRead(), file.canWrite()));

        String m = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(m))
            external.setText(String.format("External: %s", "mounted"));
        else
            external.setText(String.format("External: %s", m));
    }

    public void getFilesDir_Btn(View view)
    {
        File file = getFilesDir();
        FillWithInfo(file);
    }

    public void getCacheDir_Btn(View view)
    {
        File file = getCacheDir();
        FillWithInfo(file);
    }

    public void getExternalFilesDir_Btn(View view)
    {
        File file = super.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        FillWithInfo(file);
    }

    public void getExternalCacheDir_Btn(View view)
    {
        File file = getExternalCacheDir();
        FillWithInfo(file);
    }

    public void getExternalStorageDir_Btn(View view)
    {
        File file = Environment.getExternalStorageDirectory();
        FillWithStorageInfo(file);
    }

    public void getExternalStoragePublicDir_Btn(View view)
    {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        FillWithStorageInfo(file);
    }
}