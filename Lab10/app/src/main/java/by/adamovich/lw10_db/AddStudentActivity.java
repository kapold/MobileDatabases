package by.adamovich.lw10_db;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        dbHandler = new DbHandler(this);

        name_et = findViewById(R.id.addStudentNameET);
        groupsSpinner = findViewById(R.id.groupsSpinner);

        List<Group> groups = dbHandler.getGroups();
        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(adapter);
    }
    EditText name_et;
    Spinner groupsSpinner;
    DbHandler dbHandler;

    public void AddStudentToGroup_Btn(View view)
    {
        try {
            if (name_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Заполните имя", Toast.LENGTH_SHORT).show();
                return;
            }
            Student student = new Student();
            student.name = name_et.getText().toString();

            Toast.makeText(this, "Студент добавлен", Toast.LENGTH_SHORT).show();
            name_et.setText("");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
