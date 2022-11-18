package by.adamovich.lw11_db;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DbHandler(this);
        dataListView = findViewById(R.id.mainListView);
        dataSpinner = findViewById(R.id.spinner);
        fromPeriod_et = findViewById(R.id.fromPeriodET);
        toPeriod_et = findViewById(R.id.toPeriodET);
        group_et = findViewById(R.id.groupET);
        subject_et = findViewById(R.id.subjectET);

        if (dbHandler.getFaculties().size() == 0 &&
            dbHandler.getProgresses().size() == 0 &&
            dbHandler.getGroups().size() == 0 &&
            dbHandler.getStudents().size() == 0 &&
            dbHandler.getSubjects().size() == 0){
            Toast.makeText(this, "База данных пустая!\nЗаполняю данными из скрипта...", Toast.LENGTH_SHORT).show();
            dbHandler.FillDb();
        }

        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               taskStr = dataSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        dataSpinner.setSelection(0); // чтобы проинициализировать переменные
    }
    DbHandler dbHandler;
    ListView dataListView;
    Spinner dataSpinner;
    EditText fromPeriod_et, toPeriod_et, group_et, subject_et;
    String fromStr, toStr, taskStr, groupStr, subjectStr;

    public void OpenProvider_Btn(View view)
    {
        Intent providerIntent = new Intent(this, ProviderActivity.class);
        startActivity(providerIntent);
    }

    public boolean CheckCorrectDate()
    {
        fromStr = fromPeriod_et.getText().toString();
        toStr = toPeriod_et.getText().toString();
        if (fromStr.isEmpty() || toStr.isEmpty() || fromStr.length() != 10 || toStr.length() != 10){
            Toast.makeText(this, "Введите корректную дату", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean CheckCorrectGroup()
    {
        boolean result = false;
        groupStr = group_et.getText().toString();
        List<Group> groupList = dbHandler.getGroups();
        for (Group g: groupList)
            if(groupStr.equals(g.name))
                result = true;
        if (!result)
            Toast.makeText(this, "Такой группы нет", Toast.LENGTH_SHORT).show();
        return result;
    }

    public boolean CheckCorrectSubject()
    {
        boolean result = false;
        subjectStr = subject_et.getText().toString();
        List<Subject> subjectList = dbHandler.getSubjects();
        for (Subject s: subjectList)
            if(subjectStr.equals(s.subject))
                result = true;
        if (!result)
            Toast.makeText(this, "Такого предмета нет", Toast.LENGTH_SHORT).show();
        return result;
    }

    public void GetTask(View view)
    {
        ArrayAdapter<String> adapter;
        switch (taskStr)
        {
            case ("Список группы со средней оценкой для каждого студента по предмету"):
                if (!CheckCorrectGroup())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAvgStudSubj(groupStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Список группы со средней оценкой для студента в целом"):
                if (!CheckCorrectGroup())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAvgStud(groupStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Список группы со средней оценкой для группы в целом с выбором периода"):
                if (!CheckCorrectGroup())
                    break;
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getGroupByPeriod(fromStr, toStr, groupStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Список наилучших студентов для всех групп факультета за период"):
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getBestByPeriod(fromStr, toStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Список студентов, получивших оценки ниже 4, для всех групп факультета за период"):
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getLessThanFourByPeriod(fromStr, toStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Сравнительный анализ по группам за период: по предмету"):
                if (!CheckCorrectDate())
                    break;
                if (!CheckCorrectSubject())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAnalyticsBySubj(fromStr, toStr, subjectStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Сравнительный анализ по группам за период: в целом"):
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAllAnalytics_EXTENDED());
                dataListView.setAdapter(adapter);
                break;
            case ("Отсортированный список факультетов за определенный период от минимальной средней оценки к максимальной"):
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getSortedFacultiesByMark(fromStr, toStr));
                dataListView.setAdapter(adapter);
                break;
            default:
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}