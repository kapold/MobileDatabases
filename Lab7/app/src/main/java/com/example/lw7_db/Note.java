package com.example.lw7_db;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Note implements Serializable {
    public String text;
    public String date;

    public Note() {}

    public Note(String text, String date)
    {
        this.text = text;
        this.date = date;
    }

    @Override
    public String toString()
    {
        return String.format("Text: %s\nDate: %s\n\n", text, date);
    }
}
