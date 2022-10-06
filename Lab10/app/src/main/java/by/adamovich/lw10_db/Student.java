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
        return String.format("Студент [%s]\n" +
                "\tGroupID: %s\n" +
                "\tStudentID: %s\n");
    }
}
