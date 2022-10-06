package com.example.lw8_db;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskInfoActivity extends AppCompatActivity {
    private Taska task;
    private List<String> categories;
    private String category;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Bundle arguments = getIntent().getExtras();
        task = (Taska) arguments.get("task");
        EditText infoTaskName = findViewById(R.id.infoTaskName);
        infoTaskName.setText(task.value);

        categories = JsonSerializator.importFromJSON(this);
        if(categories==null)
            categories = new ArrayList<>();

        int i = 0;
        String[] catsArray = new String[categories.size()];
        for(String cat : categories){
            catsArray[i] = cat;
            i++;
        }
        category = catsArray[0];

        Spinner cats = findViewById(R.id.catSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cats.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                category = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        cats.setOnItemSelectedListener(itemSelectedListener);
    }

    public void delTaskInfoBtn(View view) {
        List<Taska> TaskList;
        XmlSerializator xml = new XmlSerializator();
        TaskList = xml.DeserializeFromXml(this);

        if(TaskList==null)
            TaskList = new ArrayList<>();
        for(Taska var : TaskList){
            if(var.equals(task))
                TaskList.remove(task);
        }

        xml.SerializeToXml(this, TaskList);
        this.finish();
    }

    public void saveTaskInfoBtn(View view) {
        List<Taska> TaskList;
        XmlSerializator xml = new XmlSerializator();
        TaskList = xml.DeserializeFromXml(this);

        if(TaskList==null)
            TaskList = new ArrayList<>();
        for(Taska var : TaskList){
            if(var.equals(task))
                TaskList.remove(var);
        }

        EditText infoTaskName = findViewById(R.id.infoTaskName);
        String newInfoTaskName = infoTaskName.getText().toString();
        TaskList.add(new Taska(newInfoTaskName, category, task.date));

        xml.SerializeToXml(this, TaskList);
        this.finish();
    }
}
