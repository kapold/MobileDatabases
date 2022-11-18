package by.adamovich.lw11_db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            "        (1, 1, 'ПОИТ-1', null),\n" +
            "        (1, 2, 'ПОИБМС-7', null),\n" +
            "        (2, 3, 'ТПК-2', null),\n" +
            "        (2, 4, 'ВЕЛ-5', null),\n" +
            "        (3, 1, 'ДВП-2', null),\n" +
            "        (3, 2, 'ДСП-9', null),\n" +
            "        (4, 3, 'ОКСЫВ-1', null),\n" +
            "        (4, 4, 'АВП-3', null);";
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
            "        (1, 1, '12.03.2020', 10, 'Пацей Н.В.'),\n" +
            "        (1, 2, '10.12.2023', 9, 'Блинова Е.А.'),\n" +
            "        (2, 3, '01.10.2019', 4, 'Шиман Д.В.'),\n" +
            "        (2, 4, '09.09.2009', 5, 'Преподаватель1'),\n" +
            "        (3, 5, '23.04.2014', 2, 'Преподаватель2'),\n" +
            "        (3, 6, '29.10.2021', 6, 'Преподаватель3'),\n" +
            "        (4, 1, '30.12.2022', 7, 'Преподаватель4'),\n" +
            "        (4, 3, '02.02.2012', 4, 'Преподаватель5'),\n" +
            "        (5, 5, '12.04.2022', 9, 'Преподаватель6'),\n" +
            "        (6, 2, '24.12.2005', 3, 'Преподаватель7');";

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

        // CONST VIEWS
        String getFaculties_VIEW = "CREATE VIEW getFacultiesVIEW AS SELECT * FROM " + TABLE_NAME_FACULTY;
        String getStudents_VIEW = "CREATE VIEW getStudentsVIEW AS SELECT * FROM " + TABLE_NAME_STUDENTS;
        String getSubjects_VIEW = "CREATE VIEW getSubjectsVIEW AS SELECT * FROM " + TABLE_NAME_SUBJECTS;
        String getProgresses_VIEW = "CREATE VIEW getProgressesVIEW AS SELECT * FROM " + TABLE_NAME_PROGRESS;
        String getGroups_VIEW= "CREATE VIEW getGroupsVIEW AS SELECT * FROM " + TABLE_NAME_GROUPS;

        db.execSQL(getFaculties_VIEW);
        db.execSQL(getStudents_VIEW);
        db.execSQL(getSubjects_VIEW);
        db.execSQL(getProgresses_VIEW);
        db.execSQL(getGroups_VIEW);

        // INDEXES
        String idx_Groups_Name = "CREATE INDEX IF NOT EXISTS idx_Groups_Name ON Groups (name)";
        String idx_Progresses_ExamDate = "CREATE INDEX IF NOT EXISTS idx_Progresses_ExamDate ON Progresses (examDate)";
        String idx_Subjects_Subject = "CREATE INDEX IF NOT EXISTS idx_Subjects_Subject ON Subjects (subject)";

        db.execSQL(idx_Groups_Name);
        db.execSQL(idx_Progresses_ExamDate);
        db.execSQL(idx_Subjects_Subject);

        // TRIGGERS
//        String tr_checkStudentCount = "CREATE TRIGGER IF NOT EXISTS tr_checkStudentCount\n" +
//                "BEFORE INSERT ON Students\n" +
//                "    WHEN 6 < (SELECT count(*) from Students)\n" +
//                "BEGIN\n" +
//                "    select RAISE (FAIL , 'Количество пользователей более 6');\n" +
//                "END;";
//        db.execSQL(tr_checkStudentCount);
//        String tr_banStudentDelete = "CREATE TRIGGER IF NOT EXISTS tr_banStudentDelete\n" +
//                "BEFORE DELETE ON Students\n" +
//                "    WHEN 3 > (select count(Students.name)\n" +
//                "              from Students\n" +
//                "              inner join Groups G on G.groupID = Students.groupID\n" +
//                "              group by G.name)\n" +
//                "BEGIN\n" +
//                "    select RAISE (FAIL , 'Количество студентов в группе < 3');\n" +
//                "END;";
//        db.execSQL(tr_banStudentDelete);
//
//        String head_VIEW = "CREATE VIEW head_VIEW AS\n" +
//                "    SELECT groupID, head\n" +
//                "    FROM Groups\n" +
//                "    INNER JOIN Students S USING (groupID);";
//        db.execSQL(head_VIEW);
//        String tr_update_Groups = "CREATE TRIGGER IF NOT EXISTS tr_insert_Groups\n" +
//                "INSTEAD OF UPDATE ON head_VIEW\n" +
//                "BEGIN\n" +
//                "    UPDATE Groups SET head = new.head WHERE groupID = old.groupID;\n" +
//                "END;";
//        db.execSQL(tr_update_Groups);
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
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(scriptFaculty);
            db.execSQL(scriptGroups);
            db.execSQL(scriptStudents);
            db.execSQL(scriptSubjects);
            db.execSQL(scriptProgresses);
            db.close();
        }
        catch (Exception ex){
            Log.d("FillDB() - Trigger: ", ex.getMessage());
        }
    }

    public List<Faculty> getFaculties()
    {
        ArrayList<Faculty> facultiesList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorFaculties = db.rawQuery("SELECT * FROM getFacultiesVIEW", null);

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
            Cursor cursorStudents = db.rawQuery("SELECT * FROM getStudentsVIEW", null);

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
            Cursor cursorSubjects = db.rawQuery("SELECT * FROM getSubjectsVIEW", null);

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
            Cursor cursorProgresses = db.rawQuery("SELECT * FROM getProgressesVIEW", null);

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
            Cursor cursorGroups = db.rawQuery("SELECT * FROM getGroupsVIEW", null);

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

    public List<String> getAvgStudSubj(String groupName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getAvgStudSubj_TVIEW AS " + String.format("select Students.name [Студент],\n" +
                "       subject [Предмет],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Students\n" +
                "inner join Groups G on G.groupID = Students.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "inner join Subjects S on S.subjectID = P.subjectID\n" +
                "where G.name = '%s'\n" +
                "group by subject", groupName);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getAvgStudSubj_TVIEW", null);

        resultList.add(String.format("Группа %s\n", groupName));
        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                    String.format("\tСтудент: %s\n\tПредмет: %s\n\tСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return resultList;
    }

    public List<String> getAvgStud(String groupName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getAvgStud_TVIEW AS " + String.format("select Students.name [Студент],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "where G.name = '%s'\n" +
                "group by Students.name", groupName);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getAvgStud_TVIEW", null);

        resultList.add(String.format("Группа %s\n", groupName));
        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("\tСтудент: %s\n\tСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1))
                );
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return resultList;
    }

    public List<String> getGroupByPeriod(String fromDate, String toDate, String groupName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getGroupByPeriod_TVIEW AS " + String.format("select G.name [Имя группы],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Students\n" +
                "inner join Groups G on G.groupID = Students.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "group by G.name\n" +
                "having G.name = '%s'", groupName);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getGroupByPeriod_TVIEW", null);

        resultList.add(String.format("Группа %s\n", groupName));
        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("\tСредняя оценка: %s\n\n", cursor.getString(1))
                );
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return resultList;
    }

    public List<String> getBestByPeriod(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getBestByPeriod_TVIEW AS " + String.format("select Students.name [Студент],\n" +
                "      avg(mark) [Средняя оценка],\n" +
                "      examDate [Дата экзамена]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "where examDate > '%s' and examDate < '%s'\n" +
                "group by Students.name\n" +
                "order by avg(mark) desc", fromDate, toDate);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getBestByPeriod_TVIEW", null);

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

        db.close();
        cursor.close();
        return resultList;
    }

    public List<String> getLessThanFourByPeriod(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getLessThanFourByPeriod_TVIEW AS " + String.format("select Students.name [Студент],\n" +
                "      avg(mark) [Средняя оценка],\n" +
                "      examDate [Дата экзамена]\n" +
                "from Students\n" +
                "inner join Groups G on Students.groupID = G.groupID\n" +
                "inner join Progresses P on Students.studentID = P.studentID\n" +
                "group by Students.name\n" +
                "having examDate > '%s' and examDate < '%s' and avg(mark) < 4\n" +
                "order by avg(mark) desc\n", fromDate, toDate);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getLessThanFourByPeriod_TVIEW", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Студент: %s\nСредняя оценка: %s\nДата экзамена: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return resultList;
    }

    public List<String> getAnalyticsBySubj(String fromDate, String toDate, String subjectName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getAnalyticsBySubj_TVIEW AS " + String.format("select G.name [Название группы],\n" +
                "       subject [Предмет],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Groups G\n" +
                "inner join Students S on G.groupID = S.groupID\n" +
                "inner join Progresses P on S.studentID = P.studentID\n" +
                "inner join Subjects S2 on S2.subjectID = P.subjectID\n" +
                "group by G.name, subject\n" +
                "having subject = '%s'", subjectName);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getAnalyticsBySubj_TVIEW", null);

        resultList.add(String.format("Предмет %s\n", subjectName));
        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("\tНазвание группы: %s\n\tСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return resultList;
    }

    public List<String> getAllAnalytics(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("select G.name [Название группы],\n" +
                "       subject [Предмет],\n" +
                "       avg(mark) [Средняя оценка]\n" +
                "from Groups G\n" +
                "inner join Students S on G.groupID = S.groupID\n" +
                "inner join Progresses P on S.studentID = P.studentID\n" +
                "inner join Subjects S2 on S2.subjectID = P.subjectID\n" +
                "group by G.name, subject\n" +
                "order by subject;"), null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Название группы: %s\nПредмет: %s\nСредняя: %s\n\n", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    public List<String> getAllAnalytics_EXTENDED()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        List<Subject> subjectList = getSubjects();

        Cursor cursor;
        for (Subject s: subjectList) {
            cursor = db.rawQuery(String.format("select G.name [Имя группы],\n" +
                    "       subject [Предмет],\n" +
                    "       avg(mark) [Средняя оценка]\n" +
                    "from Groups G\n" +
                    "inner join Students S on G.groupID = S.groupID\n" +
                    "inner join Progresses P on S.studentID = P.studentID\n" +
                    "inner join Subjects S2 on S2.subjectID = P.subjectID\n" +
                    "group by G.name, subject\n" +
                    "having subject = '%s'\n" +
                    "order by subject;", s.subject), null);

            if (cursor.getCount() < 2)
                continue;
            resultList.add(String.format("Предмет %s\n", s.subject));
            if (cursor.moveToFirst()) {
                do {
                    resultList.add(
                            String.format("\tНазвание группы: %s\n\tОценка: %s\n\n", cursor.getString(0), cursor.getString(2))
                    );
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }

        return resultList;
    }

    public List<String> getSortedFacultiesByMark(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        // Temp View Creating
        String tempView = "CREATE TEMP VIEW getSortedFacultiesByMark_TVIEW AS " + String.format("select Faculties.faculty [Факультет],\n" +
                "       avg(mark)\n" +
                "from Faculties\n" +
                "inner join Groups G on Faculties.facultyID = G.faculty\n" +
                "inner join Students S on G.groupID = S.groupID\n" +
                "inner join Progresses P on S.studentID = P.studentID\n" +
                "group by Faculties.faculty\n" +
                "having examDate > '%s' and examDate < '%s'\n" +
                "order by avg(mark)", fromDate, toDate);
        db.execSQL(tempView);
        // Getting data
        Cursor cursor = db.rawQuery("SELECT * FROM getSortedFacultiesByMark_TVIEW", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Факультет: %s\nСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1))
                );
            }
            while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return resultList;
    }
}