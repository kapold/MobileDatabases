package by.bstu.bdlab8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task.DeserializeCategories(getFilesDir());
        Task.DeserializeTasks(getFilesDir());

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = "";
                if(dayOfMonth / 10 == 0){
                    selectedDate = new StringBuilder().append(mYear)
                            .append("-").append(mMonth+1).append("-").append("0").append(mDay).toString();
                }
                else{
                    selectedDate = new StringBuilder().append(mYear)
                            .append("-").append(mMonth+1).append("-").append(mDay).toString();
                }
                choseDate = selectedDate;
            }
        });
    }
    private String choseDate;

    public void categoriesButtonClick(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    public void openTask(View view){
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("SelectedDate", choseDate);
        startActivity(intent);
    }
}