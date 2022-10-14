package by.adamovich.lw11_db;

public class Progress {
    public int studentID;
    public int subjectID;
    public String examDate;
    public int mark;
    public String teacher;

    public Progress() {}

    public Progress(int studentID, int subjectID, String examDate, int mark, String teacher)
    {
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.examDate = examDate;
        this.mark = mark;
        this.teacher = teacher;
    }

    @Override
    public String toString()
    {
        return String.format("StudentID: %s\nSubjectID: %s\nExamDate: %s\nMark: %s\nTeacher: %s\n\n", studentID, subjectID, examDate, mark, teacher);
    }
}
