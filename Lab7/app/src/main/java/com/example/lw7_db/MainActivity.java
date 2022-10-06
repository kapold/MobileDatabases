package com.example.lw7_db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainCalendar = findViewById(R.id.mainCalendar_CV);
        noteText_et = findViewById(R.id.noteText);
        add_btn = findViewById(R.id.addNoteBtn);
        change_btn = findViewById(R.id.changeNoteBtn);
        delete_btn = findViewById(R.id.deleteNoteBtn);
        mainNote = new Note();
        notionsList = new ArrayList<>();

        File file = new File(super.getFilesDir(), fileName);
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            notionsList = JsonSerializtor.importFromJSON(this);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Записок нет", Toast.LENGTH_SHORT).show();
        }

        mainCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    selectedDate = String.valueOf(dayOfMonth)+"."+String.valueOf(month)+"."+String.valueOf(year);
                    boolean ifBusy = CheckForBinding(year, month, dayOfMonth);
                    for (Note note: notionsList) {
                        if (note.date.equals(selectedDate))
                        {
                            noteText_et.setText(note.text);
                            break;
                        }
                        else
                            noteText_et.setText("");
                    }
                    ChangeVisibility(ifBusy);
                }
                catch (Exception ex)
                {
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    String fileName = "CalendarNotions.json";
    List<Note> notionsList;
    Note mainNote;
    EditText noteText_et;
    Button add_btn, change_btn, delete_btn;
    CalendarView mainCalendar;
    public String selectedDate = "";

    public boolean CheckForBinding(int year, int month, int dayOfMonth)
    {
        for (Note n: notionsList) {
            if (n.date.equals(selectedDate))
                return true;
        }
        return false;
    }

    public void ChangeVisibility(boolean ifBusy)
    {
        if (ifBusy){
            add_btn.setVisibility(View.INVISIBLE);
            change_btn.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
        }
        else{
            add_btn.setVisibility(View.VISIBLE);
            change_btn.setVisibility(View.INVISIBLE);
            delete_btn.setVisibility(View.INVISIBLE);
        }
    }

    public void AddNote_Btn(View view)
    {
        if(notionsList.size() >= 10){
            Toast.makeText(this, "Кол-во записей превышено, удалите что-нибудь", Toast.LENGTH_LONG).show();
            return;
        }
        else if (noteText_et.getText().toString().isEmpty()) {
            Toast.makeText(this, "Пустая заметка", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            notionsList = JsonSerializtor.importFromJSON(this);
        }
        catch (Exception ex) {}

        try {

            mainNote.text = noteText_et.getText().toString();
            mainNote.date = selectedDate;
            notionsList.add(mainNote);
            JsonSerializtor.exportToJSON(this, notionsList);
            ChangeVisibility(true);
            Toast.makeText(this, "Записка добавлена", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ChangeNote_Btn(View view)
    {
        try {
            if (noteText_et.getText().toString().isEmpty()) {
                Toast.makeText(this, "Пустая заметка", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                notionsList = JsonSerializtor.importFromJSON(this);
            }
            catch (Exception ex) {}

            for(Note note: notionsList){
                if(note.date.equals(selectedDate)){
                    notionsList.remove(note);
                    notionsList.add(new Note(noteText_et.getText().toString(), selectedDate));
                    JsonSerializtor.exportToJSON(this, notionsList);
                    Toast.makeText(this, "Заметка изменена", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteNote_Btn(View view)
    {
        try {
            notionsList = JsonSerializtor.importFromJSON(this);
        }
        catch (Exception ex) {}

        for(Note note: notionsList){
            if(note.date.equals(selectedDate)){
                notionsList.remove(note);
                JsonSerializtor.exportToJSON(this, notionsList);
                ChangeVisibility(false);
                Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                noteText_et.setText("");
                break;
            }
        }
    }
}