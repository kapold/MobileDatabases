package com.example.lw9_db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DbHandler(MainActivity.this);

        id_et = findViewById(R.id.id_et);
        float_et = findViewById(R.id.float_et);
        text_et = findViewById(R.id.text_et);
    }
    DbHandler dbHandler;
    EditText id_et, float_et, text_et;

    public void InsertTable_Btn(View view)
    {
        try {
            if (id_et.getText().toString().isEmpty() ||
                float_et.getText().toString().isEmpty() ||
                text_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHandler.addTable(id_et.getText().toString(),
                               float_et.getText().toString(),
                               text_et.getText().toString());
            Toast.makeText(this, "Значение успешно добавлено", Toast.LENGTH_SHORT).show();
            id_et.setText("");
            float_et.setText("");
            text_et.setText("");
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void SelectTable_Btn(View view)
    {
        try {
            if (id_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Заполните id", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleTable st = dbHandler.getTable(id_et.getText().toString());
            if (String.valueOf(st.f) != "0.0" && st.t == null) {
                Toast.makeText(this, "Нет такого элемента", Toast.LENGTH_SHORT).show();
                float_et.setText("");
                text_et.setText("");
                return;
            }
            float_et.setText(String.valueOf(st.f));
            text_et.setText(st.t);
            Toast.makeText(this, "Элемент получен", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void SelectRawTable_Btn(View view)
    {
        try {
            if (id_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Заполните id", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleTable st = dbHandler.getTableRaw(id_et.getText().toString());
            if (String.valueOf(st.f) != "0.0" && st.t == null) {
                Toast.makeText(this, "Нет такого элемента", Toast.LENGTH_SHORT).show();
                float_et.setText("");
                text_et.setText("");
                return;
            }
            float_et.setText(String.valueOf(st.f));
            text_et.setText(st.t);
            Toast.makeText(this, "Элемент получен", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateTable_Btn(View view)
    {
        try {
            if (id_et.getText().toString().isEmpty() ||
                float_et.getText().toString().isEmpty() ||
                text_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHandler.updateTable(id_et.getText().toString(),
                    float_et.getText().toString(),
                    text_et.getText().toString());
            Toast.makeText(this, "Значение успешно изменено", Toast.LENGTH_SHORT).show();
            id_et.setText("");
            float_et.setText("");
            text_et.setText("");
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteTable_Btn(View view)
    {
        try {
            if (id_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Заполните id", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHandler.deleteTable(id_et.getText().toString());
            Toast.makeText(this, "Значение успешно удалено", Toast.LENGTH_SHORT).show();
            id_et.setText("");
            float_et.setText("");
            text_et.setText("");
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}