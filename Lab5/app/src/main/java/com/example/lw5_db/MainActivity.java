package com.example.lw5_db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key_et1 = findViewById(R.id.keySet_et);
        key_et2 = findViewById(R.id.keyGet_et);
        value_et1 = findViewById(R.id.valueSet_et);
        value_et2 = findViewById(R.id.valueGet_et);

        file = new File(super.getFilesDir(), filePath);
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    String filePath = "LW5.txt";
    File file;
    EditText key_et1, key_et2, value_et1, value_et2;

    public void AddValue_Btn(View view)
    {
        try{
            if (value_et1.getText().toString().isEmpty() || key_et1.getText().toString().isEmpty()){
                Toast.makeText(this, "Введите ключ и значение", Toast.LENGTH_SHORT).show();
                return;
            }

            String value = String.valueOf(value_et1.getText());
            String key = String.valueOf(key_et1.getText());

            HashTable.SaveCouple(key, value, file);
            Toast.makeText(this, "Пара добавлена", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void GetValue_Btn(View view)
    {
        try {
            if (key_et2.getText().toString().isEmpty()){
                Toast.makeText(this, "Введите ключ, чтобы получить значение", Toast.LENGTH_SHORT).show();
                return;
            }

            String key = String.valueOf(key_et2.getText());
            String value = HashTable.GetValue(key, file);

            if (value == "")
                Toast.makeText(this, "Значение не найдено", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Значение найдено", Toast.LENGTH_SHORT).show();
            value_et2.setText(value);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearFile_Btn(View view)
    {
        try {
            file.delete();
            file.createNewFile();
            Toast.makeText(this, "Файл очищен", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка очистки файла", Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearFields_Btn(View view)
    {
        key_et1.setText("");
        key_et2.setText("");
        value_et1.setText("");
        value_et2.setText("");
    }
}