package com.example.lw8_db;

import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_menu);

        loadList();
        textField = findViewById(R.id.cat_et);
        File file = new File(super.getFilesDir(), fileName);
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        categoriesList = findViewById(R.id.categoriesList);
        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Button editBtn = findViewById(R.id.changeBtn);
                Button delBtn = findViewById(R.id.deleteBtn);

                editBtn.setEnabled(true);
                delBtn.setEnabled(true);

                String pickedCat =categories.get(position);
                textField.setText(pickedCat);
                textField.setEnabled(false);
            }
        });
    }
    public String fileName = "categories.json";
    public ListView categoriesList;
    public EditText textField;
    public List<String> categories;

    private void loadList(){
        categories = null;
        try {
            categories = JsonSerializator.importFromJSON(this);
        }
        catch (Exception ex) {}
        if(categories == null)
            categories = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        ListView listView = findViewById(R.id.categoriesList);
        listView.setAdapter(adapter);
    }

    private boolean isRepeat(List<String> categories, String name){
        for(String category: categories)
            if(category.equals(name))
                return true;
        return false;
    }

    public void AddCategory_Btn(View view) {
        String name = textField.getText().toString();
        if(name.length() < 1){
            Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            categories = JsonSerializator.importFromJSON(this);
        }
        catch (Exception ex) {}

        if(categories == null)
            categories = new ArrayList<>();

        if(!isRepeat(categories,name) && categories.size()<5){
            categories.add(name);
            JsonSerializator.exportToJSON(this,categories);
            textField.setText("");
            loadList();
        }
        else{
            Toast.makeText(this, "Такая категория уже есть или превышен лимит категорий(5)", Toast.LENGTH_LONG).show();
        }
    }

    public void EditCategory_Btn(View view) {
        String name = textField.getText().toString();
        textField.setEnabled(true);

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setEnabled(true);

        if(name.length()<1){
            Toast.makeText(this, "Введите нормальное название", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            categories = JsonSerializator.importFromJSON(this);
        }
        catch (Exception ex) {}

        if(categories == null)
            categories = new ArrayList<>();

        for(String category: categories){
            if(category.equals(name)){
                categories.remove(category);
                break;
            }
        }
    }

    public void SaveCategory_Btn(View view) {
        String name = textField.getText().toString();

        if(name.length()<1){
            Toast.makeText(this, "Введите нормальное название", Toast.LENGTH_LONG).show();
            return;
        }

        if(!isRepeat(categories,name) && categories.size()<5) {
            categories.add(name);
            JsonSerializator.exportToJSON(this,categories);
            textField.setText("");
            setFalseEnabled();
            loadList();
        }
        else{
            Toast.makeText(this, "Такая категория уже есть или первышен лимит категорий(5)", Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteCategory_Btn(View view) {
        String name = textField.getText().toString();

        try {
            categories = JsonSerializator.importFromJSON(this);
        }
        catch (Exception ex) {}

        if(categories == null)
            categories = new ArrayList<>();

        for(String category: categories){
            if(category.equals(name)){
                categories.remove(category);
                break;
            }
        }
        textField.setEnabled(true);

        JsonSerializator.exportToJSON(this,categories);
        textField.setText("");
        setFalseEnabled();
        loadList();
    }

    private void setFalseEnabled(){
        Button editBtn = findViewById(R.id.changeBtn);
        Button delBtn = findViewById(R.id.deleteBtn);
        Button saveBtn = findViewById(R.id.saveBtn);

        editBtn.setEnabled(false);
        delBtn.setEnabled(false);
        saveBtn.setEnabled(false);
    }
}
