package com.example.lw6_births_db;

import java.io.Serializable;

public class Person implements Serializable {
    public String name;
    public String surname;
    public String phone;
    public String birth;

    public Person()
    {
        this.name = "";
        this.surname = "";
        this.phone = "";
        this.birth = "";
    }

    public Person(String name, String surname, String phone, String birth)
    {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.birth = birth;
    }

    @Override
    public String toString()
    {
        return String.format("Name: %s\nSurname: %s\nPhone: %s\nBirth: %s\n\n", name, surname, phone, birth);
    }
}
