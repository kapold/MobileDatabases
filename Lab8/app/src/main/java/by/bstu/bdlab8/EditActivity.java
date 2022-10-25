package by.bstu.bdlab8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private String Title;
    private String Category;
    private String Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        Date = extras.getString("Date");
        Title = extras.getString("Title");
        Category = extras.getString("Category");

        TextView editDateView = findViewById(R.id.editDateView);
        editDateView.setText(Date);

        EditText editInput = findViewById(R.id.editInput);
        editInput.setText(Title);
        Spinner spinner = findViewById(R.id.editSpinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Task.Categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        int pos = Task.Categories.indexOf(Category);
        spinner.setSelection(pos, false);
    }

    public void editTask(View view) {
        EditText editInput = findViewById(R.id.editInput);
        Spinner spinner = findViewById(R.id.editSpinner);
        String newTitle = String.valueOf(editInput.getText());
        String newCategory = String.valueOf(spinner.getSelectedItem());
        if(newTitle.length() <= 0){
            Toast.makeText(this, "Введите новое название!", Toast.LENGTH_SHORT).show();
        }
        else{
            for(Task t: Task.Tasks){
                if(t.Title.equals(Title) && t.Date.equals(Date) && t.Category.equals(Category)){
                    t.Title = newTitle;
                    t.Category = newCategory;
                    break;
                }
            }
            Toast.makeText(this, "Задача изменена!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TaskActivity.class);
            intent.putExtra("SelectedDate", Date);
            startActivity(intent);
        }
    }
}