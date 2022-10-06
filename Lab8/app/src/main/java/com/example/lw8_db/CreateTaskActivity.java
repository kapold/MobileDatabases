package com.example.lw8_db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        editTaskText = findViewById(R.id.taskText_et);

        try {
            categories = JsonSerializator.importFromJSON(this);
        }
        catch (Exception ex) {}

        if(categories==null)
            categories = new ArrayList<>();

        int i = 0;
        String[] catsArray = new String[categories.size()];
        for(String cat : categories){
            catsArray[i] = cat;
            i++;
        }

        Spinner cats = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cats.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        cats.setOnItemSelectedListener(itemSelectedListener);

        Intent thisIntent = getIntent();
        date = thisIntent.getStringExtra("date");

        TextView currentDate = findViewById(R.id.currentDate_tv);
        currentDate.setText(date);
    }
    private String date, category;
    private List<String> categories;
    private List<Taska> Tasks;
    EditText editTaskText;

    public void saveNewTaskBtn(View view) {
        String taskText = editTaskText.getText().toString();
        Taska task = new Taska(taskText, category, date);
        if (taskText == ""){
            Toast.makeText(this, "Введите значение", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Taska> TaskList = new ArrayList<>();
        XmlSerializator xml = new XmlSerializator();

        try {
            TaskList = xml.DeserializeFromXml(this);
        }
        catch (Exception ex){
            Toast.makeText(this, "Пустой документ", Toast.LENGTH_SHORT).show();
        }

        if(TaskList.size() > 19)
            Toast.makeText(this, "Нельзя создать больше 20 задач", Toast.LENGTH_LONG).show();

        int i = 0;
        for(Taska var: TaskList){
            if(var.date.equals(date))
                i++;

            if(i==5){
                Toast.makeText(this, "Нельзя создать больше 5 задач на одну дату", Toast.LENGTH_LONG).show();
                return;
            }
        }

        TaskList.add(task);
        xml.SerializeToXml(this, TaskList);
        Toast.makeText(this, "Задача добавлена успешно", Toast.LENGTH_SHORT).show();
    }

    public void backFromNewBtn(View view) { this.finish();}
}
