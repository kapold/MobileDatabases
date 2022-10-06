package com.example.lw8_db;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.taskCalendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectedDate = String.valueOf(day)+"."+String.valueOf(month)+"."+String.valueOf(year);
            }
        });

        file = new File(super.getFilesDir(), fileName);
        if (!file.exists())
        {
            try {
                file.createNewFile();
                Toast.makeText(this, "Файл создан!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String selectedDate;
    String fileName = "tasksInfo.xml";
    File file;
    List<Taska> tasksList;
    CalendarView calendarView;

    public void ShowAllTasks_Btn(View view)
    {
        Intent currentTasks = new Intent(this, CurrentTasksActivity.class);
        currentTasks.putExtra("date", selectedDate);
        startActivity(currentTasks);
    }

    public void ShowCategories_Btn(View view)
    {
        try {
            Intent categories = new Intent(this, CategoriesMenuActivity.class);
            startActivity(categories);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    public void AddTask_Btn(View view)
    {
        Intent addIntent = new Intent(this, CreateTaskActivity.class);
        addIntent.putExtra("date", selectedDate);
        startActivity(addIntent);
    }

    public void ClearXML_Btn(View view)
    {
        try {
            file.delete();
            file.createNewFile();
            Toast.makeText(this, "Файл очищен", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex) {
            Toast.makeText(this, "Ошибка очистки файла", Toast.LENGTH_SHORT).show();
        }
    }
}