package com.example.lw5_db;

import android.util.Log;
import java.io.File;
import java.io.RandomAccessFile;

public class HashTable {
    public static void SaveCouple(String key, String value, File file)
    {
        try{
            while(value.length() != 10)
                value += "#";
            while(key.length() != 5)
                key += "#";

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            int readHash = 0,
                position = 0,
                hash = GetHashCode(key);

            do{
                raf.seek(position);
                readHash = raf.read() - 48;

                if(readHash == -49){
                    raf.seek(raf.length());
                    raf.writeBytes(String.valueOf(hash) + key + value + "@@@@@");
                }

                if(!(readHash == hash))
                    position += 21;
                else{
                    byte[] readKey = new byte[5];
                    raf.read(readKey);
                    String readKeyStr = new String(readKey);

                    // Если есть похожий ключ, перезаписываем
                    if(readKeyStr.equals(key)){
                        raf.writeBytes(value);
                        break;
                    }
                    else{
                        raf.skipBytes(10);
                        byte[] readLink = new byte[5];
                        raf.read(readLink);
                        String readLinkStr = new String(readLink);
                        if(readLinkStr.equals("@@@@@")) {
                            long link = raf.length();
                            String linkStr = String.valueOf(link);

                            while(linkStr.length() != 5)
                                linkStr += "@";

                            raf.seek(raf.getFilePointer() - 5);
                            raf.writeBytes(linkStr);
                            raf.seek(raf.length());
                            String message = String.valueOf(hash) + key + value + "@@@@@";
                            raf.writeBytes(message);
                            break;
                        }
                        else{
                            readLinkStr = readLinkStr.replace("@", "");
                            position = Integer.parseInt(readLinkStr);
                        }
                    }
                }
            }
            while(readHash != -49);
            raf.close();
        }
        catch(Exception ex){
            Log.d("SaveCouple(Exception): ", ex.getMessage());
        }
    }

    public static String GetValue(String key, File file)
    {
        String value = "";
        try{
            while(key.length() != 5)
                key += "#";

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            int readHash = 0,
                hash = GetHashCode(key),
                position = 0;

            do{
                raf.seek(position);
                readHash = raf.read() - 48;

                if(!(readHash == hash))
                    position = position + 21;
                else{
                    byte[] readKey = new byte[5];
                    raf.read(readKey);
                    String readKeyStr = new String(readKey);

                    if(readKeyStr.equals(key)){
                        byte[] readValue = new byte[10];
                        raf.read(readValue);
                        value = new String(readValue);
                        break;
                    }
                    else{
                        raf.skipBytes(10);
                        byte[] readLink = new byte[5];
                        raf.read(readLink);
                        String readLinkStr = new String(readLink);
                        if(readLinkStr.equals("@@@@@")){
                            value = "";
                            break;
                        }
                        else{
                            readLinkStr = readLinkStr.replace("@", "");
                            position = Integer.parseInt(readLinkStr);
                        }
                    }
                }
            }
            while(readHash != -49);
            raf.close();
        }
        catch(Exception ex){
            Log.d("GetValue(Exception): ", ex.getMessage());
        }
        value = value.replace("#", "");
        return value;
    }

    private static int GetLastChar(int number)
    {
        if(number / 10 == 0)
            return number;
        return GetLastChar(number / 10);
    }

    private static int GetHashCode(String input)
    {
        int hashCode = input.hashCode();
        int output = GetLastChar(hashCode);
        if(output < 0)
            output *= -1;
        return output;
    }
}