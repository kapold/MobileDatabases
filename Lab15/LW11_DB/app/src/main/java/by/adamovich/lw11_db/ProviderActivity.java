package by.adamovich.lw11_db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProviderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        dbHandler = new DbHandler(this);
        info_tv = findViewById(R.id.providerInfo);
    }
    private DbHandler dbHandler;
    private TextView info_tv;

    public void Select_Btn(View view)
    {
        Uri uri = Uri.parse("content://by.adamovich.provider/groupslist");
        Cursor cursor = getContentResolver().query(uri, null ,null ,null ,null);
        String info = "Список групп: \n\n";
        if (cursor.moveToFirst()) {
            do {
                info += "\t" + cursor.getString(3) + "\n";
            }
            while (cursor.moveToNext());
        }
        info_tv.setText(info);

        Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
    }

    public void InsertGroup_Btn(View view)
    {
        ContentValues cv = new ContentValues();
        cv.put("faculty", 1);
        cv.put("course", 4);
        cv.put("name", "TEST_GROUP");
        cv.put("head", "TEST_HEAD");

        Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
        Uri uri = getContentResolver().insert(test,cv);
        Log.d("Insert Group():" , "Uri: " + uri.toString());

        Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
    }

    public void InsertStudent_Btn(View view)
    {
        ContentValues cv = new ContentValues();
        cv.put("studentID", 100);
        cv.put("groupID", 1);
        cv.put("name", "TEST_STUDENT");
        cv.put("birthdate", "01-01-2000");
        cv.put("address", "TEST_ADDRESS");

        Uri test = Uri.parse("content://by.adamovich.provider/studentlist");
        Uri uri = getContentResolver().insert(test,cv);
        Log.d("Insert Student(): ", "Uri : " + uri.toString());

        Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
    }

    public void DeleteGroup_Btn(View view){
        Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
        Uri uri = ContentUris.withAppendedId(test,1);
        int cnt = getContentResolver().delete(uri,null,null);
        Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
    }

    public void DeleteStudent_Btn(View view){
        Uri test = Uri.parse("content://by.adamovich.provider/studentlist");
        Uri uri = ContentUris.withAppendedId(test,1);
        int cnt = getContentResolver().delete(uri,null,null);
        Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
    }

    public void UpdateGroup_Btn(View view){
        try{
            ContentValues cv = new ContentValues();
            cv.put("faculty", 1);
            cv.put("course", 4);
            cv.put("name", "TEST_GROUP_UPDATED");
            cv.put("head", "TEST_HEAD_UPDATED");

            Uri test = Uri.parse("content://by.adamovich.provider/groupslist");
            Uri uri = ContentUris.withAppendedId(test,2);
            int cnt = getContentResolver().update(uri, cv, null);
            Toast.makeText(this, "Успешно!", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateStudent_Btn(View view){
        try{
            ContentValues cv = new ContentValues();
            cv.put("studentID", 2);
            cv.put("groupID", 1);
            cv.put("name", "TEST STUDENT");
            cv.put("birthDate", "08-04-2000");
            cv.put("address", "TEST_ADDRESS");

            Uri test = Uri.parse("content://by.adamovich.provider/studentlist");
            Uri uri = ContentUris.withAppendedId(test,2);
            int cnt = getContentResolver().update(uri, cv, null);
            Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
