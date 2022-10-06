package com.example.lw4_db;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExistBase(filePath);
    }
    public String filePath = "Base_Lab.txt";

    public boolean ExistBase(String fname)
    {
        boolean rc = false;
        File file = new File(super.getFilesDir(), fname);
        if(rc = file.exists())
        {
            Log.d("Log_02","Файл " + fname + " существует");
            Toast.makeText(this, "Файл уже существует.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("Log_02","Файл " + fname + " не найден");
            Toast.makeText(this, "Файла нет, создан новый файл.", Toast.LENGTH_SHORT).show();

            ShowMessage(filePath);
            CreateFile(filePath);
        }

        return rc;
    }

    private void CreateFile(String fname)
    {
        File file = new File(super.getFilesDir(), fname);
        try {
            file.createNewFile();
            Log.d("Log_02","Файл " + fname + "создан");
        }
        catch (Exception e) {
            Log.d("Log_02","Файл " + fname + "не создан");
        }
    }

    private void ShowMessage(String fname)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Создается файл " + fname).setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Log_02","Создание файла " + fname);
                    }
                });
        AlertDialog ad = b.create();
        ad.show();
    }

    public void ReadFile_Btn(View view)
    {
        File file = new File(super.getFilesDir(), filePath);
        if(!file.exists())
        {
            Log.d("Log_02","Файл " + filePath + " не существует");
            Toast.makeText(this, "Файл не существует.", Toast.LENGTH_SHORT).show();
            CreateFile(filePath);
            ShowMessage(filePath);
        }

        TextView info_tv = findViewById(R.id.info_TV);
        FileInputStream fis = null;
        try {
            fis = openFileInput(filePath);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String text = new String (bytes);
            info_tv.setText(text);
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            try{
                if(fis!=null)
                    fis.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AddHuman_Btn(View view)
    {
        FileOutputStream fos = null;
        try {
            Log.d("Log_02","Файл " + filePath + " открыт");

            EditText surname_et = findViewById(R.id.surname_ED);
            EditText name_et = findViewById(R.id.name_ET);
            String surname = surname_et.getText().toString();
            String name = name_et.getText().toString();
            String resultStr = surname + ";" + name + ";" + "\r\n";

            fos = openFileOutput(filePath, MODE_APPEND);
            fos.write(resultStr.getBytes());
            Toast.makeText(this, "Человек добавлен", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ex) {
            Log.d("Log_02","Файл " + filePath + " не открыт");
        }
        finally {
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void DeleteFile_Btn(View view)
    {
        File f = new File(super.getFilesDir(), filePath);
        f.delete();
        TextView textView = findViewById(R.id.info_TV);
        textView.setText("");

        Toast.makeText(this, "Файл удален!", Toast.LENGTH_SHORT).show();
    }
}