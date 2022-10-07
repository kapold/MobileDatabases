package by.adamovich.lw10_db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DbHandler(this);

        courseSpinner = findViewById(R.id.courseSpinner);
        dataListView = findViewById(R.id.dataLV);
        group = new Group();

        groups = dbHandler.getGroups();
        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group = groups.get(position);
                LoadData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        courseSpinner.setOnItemSelectedListener(itemSelectedListener);
    }
    Spinner courseSpinner;
    ListView dataListView;
    DbHandler dbHandler;
    Group group;
    List<Group> groups;

    public void LoadData()
    {
        try {
            List<Student> startList = new ArrayList<>();
            List<Student> resultList = new ArrayList<>();
            startList = dbHandler.getStudents();

            for(Student student : startList)
                if (student.idGroup == group.idGroup)
                    resultList.add(student);

            ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, resultList);
            dataListView.setAdapter(adapter);
        }
        catch (Exception ex){
            Log.d("LoadData(): ", ex.getMessage());
        }
    }

    public void OpenAddGroup_Btn(View view)
    {
        Intent groupIntent = new Intent(this, AddGroupActivity.class);
        startActivity(groupIntent);
    }

    public void OpenAddStudent_Btn(View view)
    {
        Intent studentIntent = new Intent(this, AddStudentActivity.class);
        startActivity(studentIntent);
    }
}