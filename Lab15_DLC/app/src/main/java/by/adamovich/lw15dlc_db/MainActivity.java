package by.adamovich.lw15dlc_db;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info_tv = findViewById(R.id.infoTV);
        input_et = findViewById(R.id.inputET);
    }
    TextView info_tv;
    EditText input_et;

    public void ShowAllGroups_Btn(View view)
    {
        try{
            Uri uri = Uri.parse("content://by.adamovich.provider/groupslist");
            Cursor cursor = getContentResolver().query(uri, null ,null ,null ,null);
            String info = "Список групп: \n\n";
            if (cursor.moveToFirst()) {
                do {
                    info += "\t" + cursor.getString(0) + "\t" + cursor.getString(3) + "\t" + cursor.getString(2) + "\n";
                }
                while (cursor.moveToNext());
            }
            info_tv.setText(info);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteAllGroups_Btn(View view)
    {
        try {
            Uri uri = Uri.parse("content://by.adamovich.provider/groupslist");
            int cnt = getContentResolver().delete(uri,null,null);
            Log.d("RowsCount_DELETED(): ", String.valueOf(cnt));

            Toast.makeText(this, "Группы очищены!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void ShowCurrentGroup_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите номер группы", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
            Uri uri = ContentUris.withAppendedId(test, Integer.valueOf(input_et.getText().toString()));
            Cursor cursor = getContentResolver().query(uri, null ,null ,null ,null);

            String info = String.format("Группа %s: \n\n", input_et.getText());
            if (cursor.moveToFirst()) {
                do {
                    info += "\t" + cursor.getString(0) + "\t" + cursor.getString(3) + "\n";
                }
                while (cursor.moveToNext());
            }
            info_tv.setText(info);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void AddNewGroup_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите название группы", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ContentValues cv = new ContentValues();
            cv.put("faculty", 1);
            cv.put("course", 1);
            cv.put("name", input_et.getText().toString());
            cv.put("head", "");

            Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
            Uri uri = getContentResolver().insert(test,cv);
            Log.d("Insert Group():" , "Uri: " + uri.toString());

            Toast.makeText(this, "Группа добавлена!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteSelectedGroup_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите id группы для удаления", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
            Uri uri = ContentUris.withAppendedId(test, Integer.valueOf(input_et.getText().toString()));
            int cnt = getContentResolver().delete(uri,null,null);

            Toast.makeText(this, "Группа удалена!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void UpdateGroupHead_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите id группы и имя старосты", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String[] params = input_et.getText().toString().split(" ");

            ContentValues cv = new ContentValues();
            cv.put("faculty", 1);
            cv.put("course", 1);
            cv.put("name", "TEST");
            cv.put("head", params[1]);

            Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
            Uri uri = ContentUris.withAppendedId(test,Integer.valueOf(params[0].toString()));
            int cnt = getContentResolver().update(uri, cv, null);

            Toast.makeText(this, "Староста назначен!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void ShowAllStudents_Btn(View view)
    {
        try {
            Uri uri = Uri.parse("content://by.adamovich.provider/studentlist");
            Cursor cursor = getContentResolver().query(uri, null ,null ,null ,null);
            String info = "Список студентов: \n\n";
            if (cursor.moveToFirst()) {
                do {
                    info += "\t" + cursor.getString(2) + "\n";
                }
                while (cursor.moveToNext());
            }
            info_tv.setText(info);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void ShowSelectedStudent_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите id студента", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri test = Uri.parse("content://by.adamovich.provider/studentlist");
            Uri uri = ContentUris.withAppendedId(test, Integer.valueOf(input_et.getText().toString()));
            Cursor cursor = getContentResolver().query(uri, null ,null ,null ,null);

            String info = String.format("Студент %s: \n\n", input_et.getText());
            if (cursor.moveToFirst()) {
                do {
                    info += "\t" + cursor.getString(0) + "\t" + cursor.getString(2) + "\t" + cursor.getString(3) + "\n";
                }
                while (cursor.moveToNext());
            }
            info_tv.setText(info);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void AddNewStudent_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите id группы и имя студента", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String[] params = input_et.getText().toString().split(" ");

            ContentValues cv = new ContentValues();
            cv.put("groupID", Integer.valueOf(params[0]));
            cv.put("name", params[1]);
            cv.put("birthdate", "01-01-2000");
            cv.put("address", "TEST_ADDRESS");

            Uri test = Uri.parse("content://by.adamovich.provider/studentlist");
            Uri uri = getContentResolver().insert(test,cv);
            Log.d("Insert Group():" , "Uri: " + uri.toString());
            Toast.makeText(this, "Студент добавлен!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteSelectedStudent_Btn(View view)
    {
        if (input_et.getText().toString().equals("")){
            Toast.makeText(this, "Введите id студента", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri test = Uri.parse("content://by.adamovich.provider/studentlist");
            Uri uri = ContentUris.withAppendedId(test, Integer.valueOf(input_et.getText().toString()));
            int cnt = getContentResolver().delete(uri,null,null);

            Toast.makeText(this, "Студент удален!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}