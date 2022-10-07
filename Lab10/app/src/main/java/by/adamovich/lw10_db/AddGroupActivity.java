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
        group = new Group();
    }
    EditText groupName_et, faculty_et, course_et;
    Group group;
    DbHandler dbHandler;

    public void AddHeadToGroup_Btn(View view)
    {
        try {
            if (groupName_et.getText().toString().isEmpty()){
                Toast.makeText(this, "Введите наименование группы", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent headIntent = new Intent(this, AddHeadActivity.class);
            headIntent.putExtra("headName", groupName_et.getText().toString());
            headIntent.putExtra("groupID", String.valueOf(dbHandler.getGroupID(groupName_et.getText().toString())));
            startActivity(headIntent);
            this.finish();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteGroup_Btn(View view)
    {
        try {
            dbHandler.deleteGroup(groupName_et.getText().toString());
            Toast.makeText(this, "Группа удалена!", Toast.LENGTH_SHORT).show();
            groupName_et.setText("");
        }
        catch (Exception ex) {
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
