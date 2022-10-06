package com.example.lw6_births_db;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class PersonSerialize {

    private static String fileName = "ContactsInfo.json";

    static List<Person> importFromJSONExternal(Context context) {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
        StringBuilder stringBuilder = new StringBuilder();

        try (FileReader fr = new FileReader(file))
        {
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            stringBuilder.append(line);

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(stringBuilder.toString(), DataItems.class);
            return  dataItems != null ? dataItems.getApps() : new ArrayList<Person>();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private static class DataItems {
        private List<Person> persons;
        List<Person> getApps() {
            return persons;
        }
        void setApps(List<Person> persons) {
            this.persons = persons;
        }
    }
}

