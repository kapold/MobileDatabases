package by.adamovich.lw11_db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UniversityProvider extends ContentProvider {
    private DbHandler db;
    public int choose;

    static final String CONTENT_AUTHORITY = "by.adamovich.provider";
    static final String GROUPS_LIST_PATH = "groupslist";
    static final String STUDENT_LIST_PATH = "studentlist";
    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + GROUPS_LIST_PATH);

    static final int URI_GROUPS = 1;
    static final int URI_GROUP_ID = 2;
    static final int URI_STUDENTS = 3;
    static final int URI_STUDENT_ID = 4;

    @Override
    public boolean onCreate() {
        db = new DbHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, GROUPS_LIST_PATH, URI_GROUPS);
        uriMatcher.addURI(CONTENT_AUTHORITY,GROUPS_LIST_PATH + "/#", URI_GROUP_ID);
        uriMatcher.addURI(CONTENT_AUTHORITY, STUDENT_LIST_PATH, URI_STUDENTS);
        uriMatcher.addURI(CONTENT_AUTHORITY,STUDENT_LIST_PATH + "/#", URI_STUDENT_ID);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri))
        {
            case URI_GROUPS:
                if (sortOrder == null)
                    sortOrder = "name ASC";
                choose = 1;
                break;
            case URI_GROUP_ID:
                String gID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("groupID = %s", gID);
                else
                    selection = selection + String.format(" and groupID = %s", gID);
                choose = 1;
                break;
            case URI_STUDENTS:
                if(sortOrder == null)
                    sortOrder = "name ASC";
                choose = 2;
                break;
            case URI_STUDENT_ID:
                String uID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("studentID = %s", uID);
                else
                    selection = selection + String.format(" and studentID = %s", uID);
                choose = 2;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }

        Cursor cursor;
        if (choose == 1)
            cursor = db.getWritableDatabase().query("Groups", projection, selection, selectionArgs,
                    null, null, sortOrder);
        else if (choose == 2)
            cursor = db.getWritableDatabase().query("Students", projection, selection, selectionArgs,
                    null, null, sortOrder);
        else {
            cursor = null;
            Log.d("Cursor Creation: ", "Cursor = NULL");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_AUTHORITY_URI);
        Log.d("QUERY(): ", "SUCCESS");
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result;
        long rowID;
        if (uriMatcher.match(uri) == URI_GROUPS){
            rowID = db.getWritableDatabase().insert("Groups", null, values);
            result = ContentUris.withAppendedId(CONTENT_AUTHORITY_URI, rowID);
        }
        else if (uriMatcher.match(uri) == URI_STUDENTS){
            rowID = db.getWritableDatabase().insert("Students", null, values);
            result = ContentUris.withAppendedId(CONTENT_AUTHORITY_URI, rowID);
        }
        else{
            throw new IllegalArgumentException("Wrong URI " + uri);
        }

        getContext().getContentResolver().notifyChange(result, null);
        Log.d("INSERT(): ", "SUCCESS");
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri))
        {
            case URI_GROUPS:
                if (selection == null)
                    selection = "groupID = groupID";
                else
                    selection = selection + " and groupID = groupID";
                choose = 1;
                break;
            case URI_GROUP_ID:
                String gID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("groupID = %s", gID);
                else
                    selection = selection + String.format(" and groupID = %s", gID);
                choose = 1;
                break;
            case URI_STUDENTS:
                break;
            case URI_STUDENT_ID:
                String sID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("studentID = %s", sID);
                else
                    selection = selection + String.format(" and studentID = %s", sID);
                choose = 2;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }

        int rowCount = 0;
        if(choose == 1)
            rowCount = db.getWritableDatabase().delete("Groups", selection, selectionArgs);
        else if (choose == 2)
            rowCount = db.getWritableDatabase().delete("Students", selection, selectionArgs);


        Log.d("RowCount: ", String.valueOf(rowCount));
        getContext().getContentResolver().notifyChange(uri,null);
        Log.d("DELETE(): ", "SUCCESS");
        return rowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri))
        {
            case URI_GROUPS:
                break;
            case URI_GROUP_ID:
                String gID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("groupID = %s", gID);
                else
                    selection = selection + String.format(" and groupID = %s", gID);
                choose = 1;
                break;
            case URI_STUDENTS:
                break;
            case URI_STUDENT_ID:
                String sID = uri.getLastPathSegment();
                if (selection == null)
                    selection = String.format("studentID = %s", sID);
                else
                    selection = selection + String.format(" and studentID = %s", sID);
                choose = 2;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }

        int rowCount = 0;
        if(choose == 1)
            rowCount = db.getWritableDatabase().update("Groups", values, selection, selectionArgs);
        else if (choose == 2)
            rowCount = db.getWritableDatabase().update("Students", values, selection, selectionArgs);

        Log.d("RowCount: ", "ZERO");
        getContext().getContentResolver().notifyChange(uri,null);
        Log.d("UPDATE(): ", "SUCCESS");
        return rowCount;
    }
}
