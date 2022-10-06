package com.example.lw6_contacts_db;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        name_et = findViewById(R.id.personName_ET);
        surname_et = findViewById(R.id.personSurname_ET);
        phone_et = findViewById(R.id.personPhone_ET);
        birth_et = findViewById(R.id.personBirthday_ET);
        public_rb = findViewById(R.id.publicWrite_RB);
        private_rb = findViewById(R.id.privateWrite_RB);

        File privateFile = new File(super.getFilesDir(), fileName);
        File publicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
        if (!privateFile.exists())
        {
            try {
                privateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!publicFile.exists())
        {
            try {
                publicFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        personList = new ArrayList<Person>();
    }
    EditText name_et;
    EditText surname_et;
    EditText phone_et;
    EditText birth_et;
    RadioButton public_rb;
    RadioButton private_rb;
    String fileName = "ContactsInfo.json";
    List<Person> personList;

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

    public void AddContact(View view)
    {
        if (name_et.getText().length() == 0 ||
            surname_et.getText().length() == 0 ||
            phone_et.getText().length() == 0 ||
            birth_et.getText().length() == 0)
        {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Person person = new Person(name_et.getText().toString(), surname_et.getText().toString(), phone_et.getText().toString(), birth_et.getText().toString());

        if (public_rb.isChecked())
        {
            personList = PersonSerialize.importFromJSONExternal(this);
            assert personList != null;
            personList.add(person);
            boolean resultExternal = PersonSerialize.exportToJSONExternal(this, personList);
            if (resultExternal)
                Toast.makeText(this, "Добавлено в External память", Toast.LENGTH_SHORT).show();
        }
        else if (private_rb.isChecked())
        {
            personList = PersonSerialize.importFromJSONInternal(this);
            personList.add(person);
            boolean resultInternal = PersonSerialize.exportToJSONInternal(this, personList);
            if (resultInternal)
                Toast.makeText(this, "Добавлено в Internal память", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Что-то пошло не так :/", Toast.LENGTH_SHORT).show();

        name_et.setText("");
        surname_et.setText("");
        phone_et.setText("");
        birth_et.setText("");
    }

    public void OpenSearchIntent(View view)
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}