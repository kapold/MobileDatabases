package com.example.lw8_db;

import java.io.Serializable;
import java.util.Objects;

public class Taska implements Serializable {
    public String value;
    public String category;
    public String date;

    public Taska(String value, String category, String date) {
        this.value = value;
        this.category = category;
        this.date = date;
    }

    public Taska(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taska task = (Taska) o;
        return Objects.equals(date, task.date) && Objects.equals(value, task.value) && Objects.equals(category, task.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, value, category);
    }

    @Override
    public String toString() {
        return value;
    }
}
