package by.adamovich.lw10_db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddGroupActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        dbHandler = new DbHandler(this);

        groupName_et = findViewById(R.id.addGroupNameET);
        faculty_et = findViewById(R.id.addGroupFacultyET);
        course_et = findViewById(R.id.addGroupCourseET);
        headSpinner = findViewById(R.id.headSpinner);
        group = new Group();
    }
    EditText groupName_et, faculty_et, course_et;
    Spinner headSpinner;
    Group group;
    DbHandler dbHandler;
    ArrayAdapter<String> adapter;

    public void AddHeadToGroup_Btn(View view)
    {
        try {
            if (groupName_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Введите наименование группы", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void RefreshSpinner_Btn(View view)
    {
        try {
            if (groupName_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Введите наименование группы", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> heads = dbHandler.getHeads(groupName_et.getText().toString());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heads);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            headSpinner.setAdapter(adapter);

            Toast.makeText(this, "Спиннер обновлен", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void AddGroup_Btn(View view)
    {
        try {
            group.name = groupName_et.getText().toString();
            group.faculty = faculty_et.getText().toString();
            group.course = Integer.parseInt(course_et.getText().toString());

            dbHandler.addGroup(group);
            Toast.makeText(this, "Группа добавлена!" +
                    "\nДобавьте туда студентов и назначьте старосту.", Toast.LENGTH_SHORT).show();
            groupName_et.setText("");
            faculty_et.setText("");
            course_et.setText("");
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
