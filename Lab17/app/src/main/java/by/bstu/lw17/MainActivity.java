package by.bstu.lw17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBase(this);
        tableName_et = findViewById(R.id.tableNameET);
        info_tv = findViewById(R.id.infoTV);
        query_et = findViewById(R.id.queryET);
        batch_et = findViewById(R.id.batchET);
        studentID_et = findViewById(R.id.studentIDET);
        groupID_et = findViewById(R.id.groupIDET);
        groupList_et = findViewById(R.id.groupListET);
    }
    DataBase db;
    EditText tableName_et, query_et, batch_et, studentID_et, groupID_et, groupList_et;
    TextView info_tv;

    public void selectBtn(View view){
        if (tableName_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите название таблицы!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!db.getTableNames().contains(tableName_et.getText().toString())){
            Toast.makeText(this, "Такой таблицы не существует!", Toast.LENGTH_SHORT).show();
            return;
        }
        info_tv.setText(db.selectFromTable(tableName_et.getText().toString()));
    }

    public void preparedSelectBtn(View view){
        if (query_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите запрос!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!query_et.getText().toString().split(" ")[0].toUpperCase().equals("SELECT")) {
            Toast.makeText(this, "Не похоже на запрос... \nГде 'select'?", Toast.LENGTH_SHORT).show();
            return;
        }
        info_tv.setText(db.selectFromTableUsingPreparedQuery(query_et.getText().toString()));
    }

    public void insertBatchBtn(View view){
        if (tableName_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите ID группы в TableName!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (batch_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите список!", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] names = batch_et.getText().toString().split(" ");
        List<Student> students = new ArrayList<>();
        for (String s: names)
            students.add(new Student(s));
        db.insertStudentBatch(Integer.parseInt(tableName_et.getText().toString()), students);
        Toast.makeText(this, "Batch вставлен!", Toast.LENGTH_SHORT).show();
        tableName_et.setText("");
        batch_et.setText("");
    }

    public void deleteStudentBtn(View view){
        if (studentID_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите ID студента!", Toast.LENGTH_SHORT).show();
            return;
        }
        db.deleteStudent(Integer.parseInt(studentID_et.getText().toString()));
        Toast.makeText(this, "Успешно удален!", Toast.LENGTH_SHORT).show();
    }

    public void updateGroupBtn(View view){
        if (groupID_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите ID группы!", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] params = groupID_et.getText().toString().split(" ");
        db.updateGroup(Integer.parseInt(params[0]), params[1]);
        Toast.makeText(this, "Успешно изменена!", Toast.LENGTH_SHORT).show();
    }

    public void callProcedure(View view){
        if (groupList_et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Введите лист имен группы!", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] groupList = groupList_et.getText().toString().split(" ");
        db.callInsertProcedure(groupList[0], groupList[1]);
        Toast.makeText(this, "Успешно добавлен лист групп!", Toast.LENGTH_SHORT).show();
    }

    public void callFunction(View view){
        info_tv.setText(String.valueOf(db.callIncrementFunction()));
    }
}