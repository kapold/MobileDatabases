package by.adamovich.lw11_db;

public class Student {
    public int studentID;
    public int groupID;
    public String name;
    public String birthDate;
    public String address;

    public Student() {}

    public Student(int studentID, int groupID, String name, String birthDate, String address)
    {
        this.studentID = studentID;
        this.groupID = groupID;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
    }

    @Override
    public String toString()
    {
        return String.format("StudentID: %s\nGroupID: %s\nName: %s\nBirthDate: %s\nAddress: %s\n\n", studentID, groupID, name, birthDate, address);
    }
}
