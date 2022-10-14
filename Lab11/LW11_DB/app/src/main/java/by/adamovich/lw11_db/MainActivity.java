package by.adamovich.lw11_db;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;

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
    EditText fromPeriod_et, toPeriod_et;
    String fromStr, toStr, taskStr;

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

    public void GetTask(View view)
    {
        ArrayAdapter<String> adapter;
        switch (taskStr)
        {
            case ("Список группы со средней оценкой для каждого студента по предмету"):
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAvgStudSubj());
                dataListView.setAdapter(adapter);
                break;
            case ("Список группы со средней оценкой для студента в целом"):
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAvgStud());
                dataListView.setAdapter(adapter);
                break;
            case ("Список группы со средней оценкой для группы в целом с выбором периода"):
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getGroupByPeriod(fromStr, toStr));
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
            case ("Сравнительный анализ по группам за период: по предмету и в целом"):
                if (!CheckCorrectDate())
                    break;
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAllAnalytics(fromStr, toStr));
                dataListView.setAdapter(adapter);
                break;
            case ("Сравнение по факультетам: отсортированный список факультетов за определенный период от минимальной средней оценки к максимальной"):
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