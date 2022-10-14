package by.adamovich.lw11_db;

public class Faculty {
    public int facultyID;
    public String faculty;
    public String dean;
    public String officeTimetable;

    public Faculty() {}

    public Faculty(int facultyID, String faculty, String dean, String officeTimetable)
    {
        this.facultyID = facultyID;
        this.faculty = faculty;
        this.dean = dean;
        this.officeTimetable = officeTimetable;
    }

    @Override
    public String toString()
    {
        return String.format("FacultyID: %s\nFacultyName: %s\nDean: %s\nOfficeTimetable: %s\n\n", facultyID, faculty, dean, officeTimetable);
    }
}
