package com.example.lw8_db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CurrentTasksActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_tasks);

        textViewDate = findViewById(R.id.currentDateTasks_tv);
        listView = findViewById(R.id.taskList);
        Intent thisIntent = getIntent();
        date = thisIntent.getStringExtra("date");
        textViewDate.setText(date);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String)parent.getItemAtPosition(position);
                loadData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

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

        Spinner cats = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cats.setAdapter(adapter);
        cats.setOnItemSelectedListener(itemSelectedListener);
        category = catsArray[0];

        tasks = findViewById(R.id.taskList);
        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                task = CurrentTasksList.get(position); // по позиции получаем выбранный элемент
                taskInfo();
            }
        });
        loadData();
    }
    TextView textViewDate;
    private List<String> categories;
    private String date;
    private String category;
    private Taska task;
    private List<Taska> CurrentTasksList;
    private ListView tasks;
    ListView listView;

    private void taskInfo(){
        try{
            Intent intent1 = new Intent(this, TaskInfoActivity.class);
            intent1.putExtra("task",  task);
            startActivity(intent1);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(){
        List<Taska> TaskList = new ArrayList<>();
        List<Taska> tl = new ArrayList<>();
        try {
            XmlSerializator xml = new XmlSerializator();
            TaskList = xml.DeserializeFromXml(this);

            tl = xml.DeserializeFromXml_XPath(this, category);
            Toast.makeText(this, "Попытка ХПаза", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        CurrentTasksList = new ArrayList<>();
        if(TaskList==null)
            TaskList = new ArrayList<>();
        for(Taska task : TaskList)
            if(task.date.equals(date) && task.category.equals(category))
                CurrentTasksList.add(task);

        ArrayAdapter<Taska> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tl);
        listView.setAdapter(adapter);
    }

    public void Previous_Btn(View view) {
        this.finish();
    }
}
