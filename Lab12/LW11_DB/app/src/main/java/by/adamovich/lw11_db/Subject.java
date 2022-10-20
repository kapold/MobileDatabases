package by.adamovich.lw11_db;

public class Subject {
    public int subjectID;
    public String subject;

    public Subject() {}

    public Subject(int subjectID, String subject)
    {
        this.subjectID = subjectID;
        this.subject = subject;
    }

    @Override
    public String toString()
    {
        return String.format("SubjectID: %s\nSubject: %s\n\n", subjectID, subject);
    }
}
