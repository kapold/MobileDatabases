package by.adamovich.lw10_db;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.viewmodel.CreationExtras;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "University";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_STUDENTS = "Students";
    private static final String TABLE_NAME_GROUPS = "Groups";


    // Groups
    private static final String ID_GROUP_COL = "ID_Group";
    private static final String FACULTY_COL = "Faculty";
    private static final String COURSE_COL = "Course";
    private static final String NAME_COL = "Name";
    private static final String HEAD_COL = "Head";
    // Students
    //private static final String ID_GROUP_COL = "ID_Group";
    private static final String ID_STUDENT_COL = "ID_Student";
    //private static final String NAME_COL = "Name";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_groups = "CREATE TABLE " + TABLE_NAME_GROUPS + " ("
                + ID_GROUP_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FACULTY_COL + " TEXT,"
                + COURSE_COL + " INTEGER,"
                + NAME_COL + " TEXT UNIQUE,"
                + HEAD_COL + " TEXT,"
                + " CONSTRAINT groupConstr FOREIGN KEY(head) REFERENCES Students(name))";

        String query_students = "CREATE TABLE " + TABLE_NAME_STUDENTS + " ("
                + ID_GROUP_COL + " INTEGER , "
                + ID_STUDENT_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COL + " TEXT,"
                + " CONSTRAINT studentConstr FOREIGN KEY (ID_Group) REFERENCES Groups (ID_Group) ON DELETE CASCADE )";

        db.execSQL(query_groups);
        db.execSQL(query_students);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS);
        onCreate(db);
    }

    public void addGroup(Group group)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(FACULTY_COL, group.faculty);
            values.put(COURSE_COL, group.course);
            values.put(NAME_COL, group.name);

            db.insert(TABLE_NAME_GROUPS, null, values);
            db.close();
        }
        catch (Exception ex){
            Log.d("addGroup(): ", ex.getMessage());
        }
    }

    public void addStudent(Student student)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ID_GROUP_COL, student.idGroup);
            values.put(NAME_COL, student.name);

            db.insert(TABLE_NAME_STUDENTS, null, values);
            db.close();
        }
        catch (Exception ex){
            Log.d("addStudent(): ", ex.getMessage());
        }
    }

    public List<Group> getGroups()
    {
        ArrayList<Group> groupsList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorGroups = db.rawQuery("SELECT * FROM " + TABLE_NAME_GROUPS, null);

            if (cursorGroups.moveToFirst()) {
                do {
                    groupsList.add(new Group(
                            cursorGroups.getInt(0),
                            cursorGroups.getString(1),
                            cursorGroups.getInt(2),
                            cursorGroups.getString(3),
                            cursorGroups.getString(4)
                    ));

                } while (cursorGroups.moveToNext());
            }

            cursorGroups.close();
            return groupsList;
        }
        catch (Exception ex){
            Log.d("getGroups(): ", ex.getMessage());
        }
        return groupsList;
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
                            cursorStudents.getString(2)
                    ));

                } while (cursorStudents.moveToNext());
            }
            cursorStudents.close();
            return studentsList;
        }
        catch (Exception ex){
            Log.d("getStudents(): ", ex.getMessage());
        }
        return studentsList;
    }

    public List<String> getHeads(String groupName)
    {
        ArrayList<String> headsList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorHeads = db.rawQuery("SELECT Head " +
                    "FROM Students as s " +
                    "INNER JOIN Groups as g " +
                    "ON s.ID_Group=g.ID_Group " +
                    "WHERE g.Name=?", new String[] {groupName});

            if (cursorHeads.moveToFirst()) {
                do {
                    headsList.add(cursorHeads.getString(0));
                }
                while (cursorHeads.moveToNext());
            }
            cursorHeads.close();
            return headsList;
        }
        catch (Exception ex){
            Log.d("getHeads(): ", ex.getMessage());
        }
        return headsList;
    }

    public int getGroupID(String groupName)
    {
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorHeads = db.rawQuery("SELECT ID_Group FROM Groups WHERE Name=?", new String[] {groupName});
            int id = 0;

            if (cursorHeads.moveToFirst()) {
                id = cursorHeads.getInt(0);
            }
            cursorHeads.close();
            return id;
        }
        catch (Exception ex){
            Log.d("getGroupID(): ", ex.getMessage());
        }

        return -1;
    }

    public void pickGroupHead(int groupID, String headName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.d("groupID: ", String.valueOf(groupID));
        Log.d("headName: ", headName);

        values.put(HEAD_COL, headName);

        db.update(TABLE_NAME_GROUPS, values, "ID_Group=?", new String[]{ String.valueOf(groupID) });
        db.close();
    }

    public void deleteGroup(String groupName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_GROUPS, "name=?", new String[]{ groupName });
        db.close();
    }

//    public void changeTimetables(String savedID, String ttName, String ttDayOfWeek, String ttWeek, String ttAudience,
//                                 String ttBuilding, String ttTime, String ttTeacher, boolean ttIsOneTime) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(NAME_COL, ttName);
//        values.put(DAYOFWEEK_COL, ttDayOfWeek);
//        values.put(WEEK_COL, ttWeek);
//        values.put(AUDIENCE_COL, ttAudience);
//        values.put(BUILDING_COL, ttBuilding);
//        values.put(TIME_COL, ttTime);
//        values.put(TEACHER_COL, ttTeacher);
//        values.put(ISONETIME_COL, ttIsOneTime);
//
//        db.update(TABLE_NAME, values, "id=?", new String[]{savedID});
//        db.close();
//    }
//
//    public void deleteTimetable(String savedID) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, "id=?", new String[]{savedID});
//        db.close();
//    }
}