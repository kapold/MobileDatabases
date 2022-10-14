package by.adamovich.lw10_db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddHeadActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_head);

        dbHandler = new DbHandler(this);
        allStudents = new ArrayList<>();
        headSpinner = findViewById(R.id.headSpinner);
        Intent intent = getIntent();
        savedName = intent.getStringExtra("headName");
        savedID = Integer.parseInt(intent.getStringExtra("groupID"));

        allStudents = dbHandler.getStudents();
        List<Student> neededStudents = new ArrayList<>();
        for (Student student: allStudents)
            if (student.idGroup == savedID)
                neededStudents.add(student);

        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, neededStudents);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        headSpinner.setAdapter(adapter);
    }
    DbHandler dbHandler;
    Spinner headSpinner;
    String savedName;
    int savedID;
    List<Student> allStudents;

    public void AddHead_Btn(View view)
    {
        try {
            String headName = String.valueOf(headSpinner.getSelectedItem());
            dbHandler.pickGroupHead(savedID, headName);
            Toast.makeText(this, "Староста выбран!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
