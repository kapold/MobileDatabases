package by.adamovich.lw11_db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "StudentsDb";
    private static final int DB_VERSION = 1;

    private static final String scriptFaculty = "insert into Faculties (faculty, dean, officeTimetable)\n" +
            "    values\n" +
            "        ('ФИТ', 'Шиман Д.В.', 'Среда с 16.00 до 17.30'),\n" +
            "        ('ЛХФ', 'Ярмолович В.А.', 'Среда с 16.00 до 17.30 '),\n" +
            "        ('ЛИД', 'Лой В.Н.', 'Среда с 16.00 до 17.30 '),\n" +
            "        ('ПИМ', 'Долгова Т.А.', 'Среда с 16.00 до 17.30 ');";
    private static final String scriptGroups = "insert into Groups (faculty, course, name, head)\n" +
            "    values\n" +
            "        (1, 1, 'ПОИТ-1', ''),\n" +
            "        (1, 2, 'ПОИБМС-7', ''),\n" +
            "        (2, 3, 'ТПК-2', ''),\n" +
            "        (2, 4, 'ВЕЛ-5', ''),\n" +
            "        (3, 1, 'ДВП-2', ''),\n" +
            "        (3, 2, 'ДСП-9', ''),\n" +
            "        (4, 3, 'ОКСЫВ-1', ''),\n" +
            "        (4, 4, 'АВП-3', '');";
    private static final String scriptStudents = "insert into Students (groupID, name, birthDate, address)\n" +
            "    values\n" +
            "        (1, 'Поитовец1', '2000-01-01', 'Архангельская область, город Озёры, проезд Бухарестская, 66'),\n" +
            "        (2, 'Адамович А.М.', '2000-02-02', 'Московская область, город Клин, пл. Косиора, 42'),\n" +
            "        (3, 'Тпковец1', '2000-03-03', 'Новгородская область, город Талдом, пл. Космонавтов, 37'),\n" +
            "        (4, 'Веловец1', '2000-04-04', 'Иркутская область, город Шаховская, ул. Славы, 35'),\n" +
            "        (5, 'Двповец1', '2000-05-05', 'Пензенская область, город Коломна, наб. Домодедовская, 03'),\n" +
            "        (6, 'Дсповец1', '2000-06-06', 'Курская область, город Шатура, шоссе Косиора, 76'),\n" +
            "        (7, 'Оксывец1', '2000-07-07', 'Свердловская область, город Домодедово, шоссе Сталина, 89'),\n" +
            "        (8, 'Авповец1', '2000-08-08', 'Белгородская область, город Кашира, проезд Гоголя, 23');";
    private static final String scriptSubjects = "insert into Subjects (subject)\n" +
            "    values\n" +
            "        ('ООП'),\n" +
            "        ('БД'),\n" +
            "        ('КСИС'),\n" +
            "        ('ИГИГ'),\n" +
            "        ('КЯР'),\n" +
            "        ('Математика');";
    private static final String scriptProgresses = "insert into Progresses (studentID, subjectID, examDate, mark, teacher)\n" +
            "    values\n" +
            "        (1, 1, '2020-03-12', 10, 'Пацей Н.В.'),\n" +
            "        (1, 2, '2023-12-10', 9, 'Блинова Е.А.'),\n" +
            "        (2, 3, '2019-10-01', 4, 'Шиман Д.В.'),\n" +
            "        (2, 4, '2009-09-09', 5, 'Преподаватель1'),\n" +
            "        (3, 5, '2014-04-23', 2, 'Преподаватель2'),\n" +
            "        (3, 6, '2021-10-29', 6, 'Преподаватель3'),\n" +
            "        (4, 1, '2022-12-30', 7, 'Преподаватель4'),\n" +
            "        (4, 3, '2012-02-02', 4, 'Преподаватель5'),\n" +
            "        (5, 5, '2022-04-12', 9, 'Преподаватель6'),\n" +
            "        (6, 2, '2005-12-24', 3, 'Преподаватель7');";

    // Table names
    private static final String TABLE_NAME_FACULTY = "Faculties";
    private static final String TABLE_NAME_GROUPS = "Groups";
    private static final String TABLE_NAME_STUDENTS = "Students";
    private static final String TABLE_NAME_SUBJECTS = "Subjects";
    private static final String TABLE_NAME_PROGRESS = "Progresses";

    // Faculty
    private static final String ID_FACULTY_COL = "facultyID";
    private static final String FACULTY_COL = "faculty";
    private static final String DEAN_COL = "dean";
    private static final String OFFICE_TIMETABLE_COL = "officeTimetable";
    // Groups
    private static final String ID_GROUP_COL = "groupID";
//  private static final String FACULTY_COL = "faculty";
    private static final String COURSE_COL = "course";
    private static final String NAME_COL = "name";
    private static final String HEAD_COL = "head";
    // Students
    private static final String ID_STUDENT_COL = "studentID";
//  private static final String ID_GROUP_COL = "groupID";
//  private static final String NAME_COL = "name";
    private static final String BIRTH_DATE_COL = "birthDate";
    private static final String ADDRESS_COL = "address";
    // Subject
    private static final String ID_SUBJECT_COL = "subjectID";
    private static final String SUBJECT_COL = "subject";
    // Progress
//  private static final String ID_STUDENT_COL = "studentID";
//  private static final String ID_SUBJECT_COL = "subjectID";
    private static final String EXAM_DATE_COL = "examDate";
    private static final String MARK_COL = "mark";
    private static final String TEACHER_COL = "teacher";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_faculty = "CREATE TABLE " + TABLE_NAME_FACULTY + " ("
                + ID_FACULTY_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FACULTY_COL + " TEXT UNIQUE,"
                + DEAN_COL + " TEXT,"
                + OFFICE_TIMETABLE_COL + " TEXT)";

        String query_group = "CREATE TABLE " + TABLE_NAME_GROUPS + " ("
                + ID_GROUP_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FACULTY_COL + " INTEGER,"
                + COURSE_COL + " INTEGER,"
                + NAME_COL + " TEXT UNIQUE,"
                + HEAD_COL + " TEXT,"
                + " CONSTRAINT groupContsr FOREIGN KEY (faculty) REFERENCES Faculties(facultyID) ON DELETE CASCADE )";

        String query_student = "CREATE TABLE " + TABLE_NAME_STUDENTS + " ("
                + ID_STUDENT_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_GROUP_COL + " INTEGER,"
                + NAME_COL + " TEXT,"
                + BIRTH_DATE_COL + " TEXT UNIQUE,"
                + ADDRESS_COL + " TEXT,"
                + " CONSTRAINT studentConstr FOREIGN KEY (groupID) REFERENCES Groups(groupID) ON DELETE CASCADE )";

        String query_subject = "CREATE TABLE " + TABLE_NAME_SUBJECTS + " ("
                + ID_SUBJECT_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_COL + " TEXT UNIQUE)";

        String query_progress = "CREATE TABLE " + TABLE_NAME_PROGRESS + " ("
                + ID_STUDENT_COL + " INTEGER, "
                + ID_SUBJECT_COL + " INTEGER,"
                + EXAM_DATE_COL + " TEXT,"
                + MARK_COL + " INTEGER,"
                + TEACHER_COL + " TEXT,"
                + " CONSTRAINT studIdConstr FOREIGN KEY (studentID) REFERENCES Students(studentID) ON DELETE CASCADE ,"
                + " CONSTRAINT subjIdConstr FOREIGN KEY (subjectID) REFERENCES Subjects(subjectID) ON DELETE CASCADE )";

        db.execSQL(query_faculty);
        db.execSQL(query_group);
        db.execSQL(query_student);
        db.execSQL(query_subject);
        db.execSQL(query_progress);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FACULTY);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void FillDb()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(scriptFaculty);
        db.execSQL(scriptGroups);
        db.execSQL(scriptStudents);
        db.execSQL(scriptSubjects);
        db.execSQL(scriptProgresses);
        db.close();
    }

    public List<Faculty> getFaculties()
    {
        ArrayList<Faculty> facultiesList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorFaculties = db.rawQuery("SELECT * FROM " + TABLE_NAME_FACULTY, null);

            if (cursorFaculties.moveToFirst()) {
                do {
                    facultiesList.add(new Faculty(
                            cursorFaculties.getInt(0),
                            cursorFaculties.getString(1),
                            cursorFaculties.getString(2),
                            cursorFaculties.getString(3)
                    ));
                }
                while (cursorFaculties.moveToNext());
            }

            cursorFaculties.close();
            return facultiesList;
        }
        catch (Exception ex){
            Log.d("getFaculties(): ", ex.getMessage());
        }
        return facultiesList;
    }

    public List<Student> getStudents()
    {
        ArrayList<Student> studentsList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorStudents = db.rawQuery("SELECT * FROM " + TABLE_NAME_STUDENTS, null);

            if (cursorStudents.moveToFirst()) {
                do {
                    studentsList.add(new Student(
                            cursorStudents.getInt(0),
                            cursorStudents.getInt(1),
                            cursorStudents.getString(2),
                            cursorStudents.getString(3),
                            cursorStudents.getString(4)
                    ));
                }
                while (cursorStudents.moveToNext());
            }

            cursorStudents.close();
            return studentsList;
        }
        catch (Exception ex){
            Log.d("getStudents(): ", ex.getMessage());
        }
        return studentsList;
    }

    public List<Subject> getSubjects()
    {
        ArrayList<Subject> subjectsList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorSubjects = db.rawQuery("SELECT * FROM " + TABLE_NAME_SUBJECTS, null);

            if (cursorSubjects.moveToFirst()) {
                do {
                    subjectsList.add(new Subject(
                            cursorSubjects.getInt(0),
                            cursorSubjects.getString(1)
                    ));
                }
                while (cursorSubjects.moveToNext());
            }

            cursorSubjects.close();
            return subjectsList;
        }
        catch (Exception ex){
            Log.d("getSubjects(): ", ex.getMessage());
        }
        return subjectsList;
    }

    public List<Progress> getProgresses()
    {
        ArrayList<Progress> progressesList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorProgresses = db.rawQuery("SELECT * FROM " + TABLE_NAME_PROGRESS, null);

            if (cursorProgresses.moveToFirst()) {
                do {
                    progressesList.add(new Progress(
                            cursorProgresses.getInt(0),
                            cursorProgresses.getInt(1),
                            cursorProgresses.getString(2),
                            cursorProgresses.getInt(3),
                            cursorProgresses.getString(4)
                    ));
                }
                while (cursorProgresses.moveToNext());
            }

            cursorProgresses.close();
            return progressesList;
        }
        catch (Exception ex){
            Log.d("getProgresses(): ", ex.getMessage());
        }
        return progressesList;
    }

    public List<Group> getGroups()
    {
        ArrayList<Group> groupsList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorGroups = db.rawQuery("SELECT * FROM " + TABLE_NAME_PROGRESS, null);

            if (cursorGroups.moveToFirst()) {
                do {
                    groupsList.add(new Group(
                            cursorGroups.getInt(0),
                            cursorGroups.getString(1),
                            cursorGroups.getInt(2),
                            cursorGroups.getString(3),
                            cursorGroups.getString(4)
                    ));
                }
                while (cursorGroups.moveToNext());
            }

            cursorGroups.close();
            return groupsList;
        }
        catch (Exception ex){
            Log.d("getGroups(): ", ex.getMessage());
        }
        return groupsList;
    }

    public List<String> getAvgStudSubj()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select G.name [Название группы],\n" +
                "       Students.name [Студент],\n" +
                "       subject [Предмет],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "inner join Subjects S on P.subjectID = S.subjectID\n" +
                "group by subject", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                    String.format("Название группы: %s\nСтудент: %s\nПредмет: %s\nСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    public List<String> getAvgStud()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select G.name [Название группы],\n" +
                "       Students.name [Студент],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "group by Students.name", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Название группы: %s\nСтудент: %s\nСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    public List<String> getGroupByPeriod(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select G.name [Название группы],\n" +
                "       Students.name [Студент],\n" +
                "       mark [Оценка],\n" +
                "       examDate [Дата экзамена]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "where examDate > '%s' and examDate < '%s'", fromDate, toDate), null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Название группы: %s\nСтудент: %s\nОценка: %s\nДата экзамена: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                );
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    public List<String> getBestByPeriod(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select Students.name [Студент],\n" +
                "      avg(mark) [Средняя оценка],\n" +
                "      examDate [Дата экзамена]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "where examDate > '%s' and examDate < '%s'\n" +
                "group by Students.name\n" +
                "order by avg(mark) desc", fromDate, toDate), null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Студент: %s\nСредняя оценка: %s\nДата экзамена: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        if (resultList.size() > 3)
            do resultList.remove(resultList.size() - 1);
            while (resultList.size() > 3);

        cursor.close();
        return resultList;
    }

    public List<String> getLessThanFourByPeriod(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select Students.name [Студент],\n" +
                "      avg(mark) [Средняя оценка],\n" +
                "      examDate [Дата экзамена]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "group by Students.name\n" +
                "having examDate > '%s' and examDate < '%s' and avg(mark) < 4\n" +
                "order by avg(mark) desc\n", fromDate, toDate), null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Студент: %s\nСредняя оценка: %s\nДата экзамена: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    public List<String> getAllAnalytics(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select G.name [Название группы],\n" +
                "       subject [Предмет],\n" +
                "       S.name [Студент],\n" +
                "       mark [Оценка]\n" +
                "from Groups as G\n" +
                "inner join Students S on G.groupID = S.groupID\n" +
                "inner join Progresses P on S.studentID = P.studentID\n" +
                "inner join Subjects S2 on P.subjectID = S2.subjectID\n" +
                "where examDate > '%s' and examDate < '%s'", fromDate, toDate), null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Название группы: %s\nПредмет: %s\nСтудент: %s\nОценка: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    public List<String> getSortedFacultiesByMark(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select Faculties.faculty [Факультет],\n" +
                "       avg(mark)\n" +
                "from Faculties\n" +
                "inner join Groups G on Faculties.facultyID = G.faculty\n" +
                "inner join Students S on G.groupID = S.groupID\n" +
                "inner join Progresses P on S.studentID = P.studentID\n" +
                "group by Faculties.faculty\n" +
                "having examDate > '%s' and examDate < '%s'\n" +
                "order by Faculties.faculty desc", fromDate, toDate), null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Факультет: %s\nСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }
}