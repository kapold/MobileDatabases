package by.adamovich.lw11_db;

public class Group {
    public int groupID;
    public String faculty;
    public int course;
    public String name;
    public String head;

    public Group() {}

    public Group(int groupID, String faculty, int course, String name, String head)
    {
        this.groupID = groupID;
        this.faculty = faculty;
        this.course = course;
        this.name = name;
        this.head = head;
    }

    @Override
    public String toString()
    {
        return String.format("GroupID: %s\nFaculty: %s\nCourse: %s\nName: %s\nHead: %s\n\n", groupID, faculty, course, name, head);
    }
}
