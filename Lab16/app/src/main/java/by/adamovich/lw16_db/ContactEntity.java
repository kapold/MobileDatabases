package by.adamovich.lw16_db;

import java.io.Serializable;

public class ContactEntity implements Serializable {
    public String ID;
    public String Name;
    public String Phone;

    public ContactEntity() {}

    @Override
    public String toString() { return String.format("%s: %s \n", Name, Phone); }
}
