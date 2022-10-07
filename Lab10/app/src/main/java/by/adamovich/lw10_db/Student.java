package by.adamovich.lw10_db;

public class Student {
    public int idGroup;
    public int idStudent;
    public String name;

    public Student() {}

    public Student(int idGroup, int idStudent, String name) {
        this.idGroup = idGroup;
        this.idStudent = idStudent;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format("%s\n", name);
    }

    public String getInfo()
    {
        return String.format("\t%s\n" +
                "\t\t\t* GroupID: %s\n" +
                "\t\t\t* StudentID: %s\n", name, idGroup, idStudent);
    }
}
