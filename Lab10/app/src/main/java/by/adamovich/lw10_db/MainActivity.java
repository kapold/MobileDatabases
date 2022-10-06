package by.adamovich.lw10_db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseSpinner = findViewById(R.id.courseSpinner);
        dataListView = findViewById(R.id.dataLV);
    }
    Spinner courseSpinner;
    ListView dataListView;

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