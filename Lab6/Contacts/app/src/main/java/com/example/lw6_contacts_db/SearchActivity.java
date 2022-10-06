package com.example.lw6_contacts_db;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar_ed = findViewById(R.id.searchBar_ET);
        searchInfo_tv = findViewById(R.id.allContactsInfo_TV);
        personList = new ArrayList<>();
        personList = PersonSerialize.importFromJSONExternal(this);
    }
    EditText searchBar_ed;
    TextView searchInfo_tv;
    List<Person> personList;
    String fullInfo = "";
    String currentSearchText;
    String fileName = "ContactsInfo.json";

    public void Search_Btn(View view)
    {
        currentSearchText = searchBar_ed.getText().toString();
        fullInfo = "";
        for (Person p: personList) {
            if (p.name.toUpperCase().equals(currentSearchText.toUpperCase()) || p.surname.toUpperCase().equals(currentSearchText.toUpperCase()))
                fullInfo += String.format("Name: %s %s\nPhone: %s\n",p.name, p.surname, p.phone);
        }
        searchInfo_tv.setText(fullInfo);
    }
}
