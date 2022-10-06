package by.adamovich.lw10_db;

public class Group {
    public int idGroup;
    public String faculty;
    public int course;
    public String name;
    public String head;

    public Group() {}

    public Group(int idGroup, String faculty, int course, String name, String head){
        this.idGroup = idGroup;
        this.faculty = faculty;
        this.course = course;
        this.name = name;
        this.head = head;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public String getInfo() {
        return String.format("Группа %s\n" +
                "\tФакультет: %s\n" +
                "\tКурс: %s\n" +
                "\tНаименование: %s\n" +
                "\tСтароста: %s\n", idGroup, faculty, course, name, head);
    }
}
