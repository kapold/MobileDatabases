package by.bstu.lw17;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBase {
    private final String user = "root";
    private final String pass = "password";
    private String url = "jdbc:postgresql://??????????/?"; // вместо вопросиков айпи и имя БД,
    public Connection connection;                          // но доступ к серваку и бд НЕ ДАМ
    private boolean status;

    public DataBase(Context context)
    {
        // Добавляйте обязательно, ибо без него connection из другого потока не пашет!
        // Потраченого времени очень жаль 😭
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        connect();
        if (status)
            Toast.makeText(context, "Подключение к бд УСПЕШНО", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Подключение к бд НЕ УСПЕШНО", Toast.LENGTH_SHORT).show();

        String insertProcedure = "CREATE OR REPLACE PROCEDURE insert_data(a text, b text) \n" +
                "LANGUAGE SQL \n" +
                "AS $$ \n" +
                "    INSERT INTO groups (name) \n" +
                "        VALUES (a); \n" +
                "    INSERT INTO groups (name) \n" +
                "        VALUES (b); \n" +
                "$$; ";
        String totalFunction = "CREATE OR REPLACE FUNCTION totalRecords ()\n" +
                "RETURNS integer AS $total$\n" +
                "declare\n" +
                "\ttotal integer;\n" +
                "BEGIN\n" +
                "   SELECT count(*) into total FROM groups;\n" +
                "   RETURN total;\n" +
                "END;\n" +
                "$total$ LANGUAGE plpgsql;";
        try{
            PreparedStatement statement_proc = connection.prepareStatement(insertProcedure);
            PreparedStatement statement_func = connection.prepareStatement(totalFunction);
            statement_proc.execute();
            statement_func.execute();
            Toast.makeText(context, "Процедура и функция созданы!", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException sqlEx){
            Log.d("DATABASE constructor: ", sqlEx.getMessage());
        }
    }

    private void connect() {
        Thread thread = new Thread(() -> {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, pass);
                status = true;
                Log.d("CONNECTION CHECK: ", String.valueOf(status));
            }
            catch (Exception e) {
                status = false;
                Log.d("ExceptionLog","run:" + e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.status = false;
        }
    }

    public List<String> getTableNames(){
        List<String> tableNames = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema = 'public'");
            ResultSet rs = pst.executeQuery();
            while(rs.next())
                tableNames.add(rs.getString(3));
        }
        catch (SQLException ex) {
            Log.d("selectFromTable(): ", String.valueOf(ex.getMessage()));
        }
        return tableNames;
    }

    public String selectFromTable(String tableName){
        String result = String.format("%s:\n", tableName);
        try {
            PreparedStatement pst = connection.prepareStatement(String.format("SELECT * FROM %s", tableName));
            ResultSet rs = pst.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while(rs.next()){
                for (int i = 1; i <= columns; i++)
                    result += rs.getString(i) + "\t";
                result += "\n";
            }
        }
        catch (SQLException ex) {
            Log.d("selectFromTable(): ", String.valueOf(ex.getMessage()));
        }
        return result;
    }

    public String selectFromTableUsingPreparedQuery(String query){
        String result = "";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int columns = rs.getMetaData().getColumnCount();
            while(rs.next()){
                for (int i = 1; i <= columns; i++)
                    result += rs.getString(i) + "\t";
                result += "\n";
            }
        }
        catch (SQLException ex) {
            Log.d("selectFromTable(): ", String.valueOf(ex.getMessage()));
        }
        return result;
    }

    public void insertStudentBatch(int groupID, List<Student> students){
        String insertStudentsSql = "INSERT INTO students" + " (idGroup, name) VALUES " +
                String.format(" (%d, ?)", groupID);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertStudentsSql);
            connection.setAutoCommit(false);
            for (Student s: students) {
                preparedStatement.setString(1, s.name);
                preparedStatement.addBatch();
            }

            int[] updateCounts = preparedStatement.executeBatch();
            Log.d("InsertedBatch: " ,Arrays.toString(updateCounts));
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException ex) {
            Log.d("selectFromTable(): ", String.valueOf(ex.getMessage()));
        }
    }

    public void updateGroup(int groupID, String name){
        String sql = "UPDATE groups "
                + "SET name = ? "
                + "WHERE idGroup = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, groupID);
            ps.executeUpdate();
        }
        catch (SQLException ex) {
            Log.d("selectFromTable(): ", String.valueOf(ex.getMessage()));
        }
    }

    public void deleteStudent(int studentID){
        String sql = String.format("DELETE FROM students WHERE idStudent = %d", studentID);
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void callInsertProcedure(String name1, String name2){
        String sql = String.format("CALL insert_data('%s', '%s')", name1, name2);
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
            statement.close();
        }
        catch (SQLException sqlEx){
            Log.d("DATABASE constructor: ", sqlEx.getMessage());
        }
    }

    public int callIncrementFunction(){
        String sql = "SELECT totalRecords()";
        int count = 0;
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs =  statement.executeQuery();
            rs.next();
            count = rs.getInt(1);
        }
        catch (SQLException sqlEx){
            Log.d("DATABASE constructor: ", sqlEx.getMessage());
        }
        return count;
    }
}