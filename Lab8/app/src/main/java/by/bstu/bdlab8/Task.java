package by.bstu.bdlab8;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileOutputStream;

public class Task {

    public String Date;
    public String Title;
    public String Category;

    public Task(String title, String date, String category){
        Title = title;
        Date = date;
        Category = category;
    }

    public Task() {}

    @Override
    public String toString() {
        return "Дата: " + Date + "\n" + "Таска: " + Title + "\n" + "Категория: " + Category + "\n";
    }

    public static ArrayList<String> Categories = new ArrayList<>();
    public static ArrayList<Task> Tasks= new ArrayList<>();

    public static void SerializeCategories(Context context){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element categories = doc.createElement("Categories");
            doc.appendChild(categories);
            for (String c : Categories) {
                Element newCategory = doc.createElement("Category");
                newCategory.setTextContent(c);
                categories.appendChild(newCategory);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource src = new DOMSource(doc);

            FileOutputStream fos = context.openFileOutput("categories.xml", Context.MODE_PRIVATE);
            StreamResult sr = new StreamResult(fos);
            t.transform(src, sr);
            fos.close();

        }
        catch(Exception ex){

        }
    }

    public static void SerializeTasks(Context context){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element tasks = doc.createElement("Tasks");
            doc.appendChild(tasks);
            for (Task t : Tasks) {
                Element newTask = doc.createElement("Task");
                newTask.setAttribute("Date", t.Date);
                newTask.setAttribute("Title", t.Title);
                newTask.setAttribute("Category", t.Category);
                tasks.appendChild(newTask);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource src = new DOMSource(doc);

            FileOutputStream fos = context.openFileOutput("tasks.xml", Context.MODE_PRIVATE);
            StreamResult sr = new StreamResult(fos);
            t.transform(src, sr);
            fos.close();

        }
        catch(Exception ex){

        }
    }

    public static void DeserializeCategories(File dir){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File(dir, "categories.xml");
            Document doc = builder.parse(f);
            ArrayList<String> newCategories = new ArrayList<>();
            NodeList nl = doc.getElementsByTagName("Category");
            for(int i = 0; i < nl.getLength(); i++){
                String c = nl.item(i).getTextContent();
                newCategories.add(c);
            }
            Categories = newCategories;
        }
        catch (Exception ex){
            Log.d("DeserCat(): ", ex.getMessage());
        }
    }

    public static void DeserializeTasks(File dir){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File(dir, "tasks.xml");
            Document doc = builder.parse(f);
            ArrayList<Task> newTasks = new ArrayList<>();
            NodeList nl = doc.getElementsByTagName("Task");
            for(int i = 0; i < nl.getLength(); i++){
                Task t = new Task();
                NamedNodeMap map = nl.item(i).getAttributes();
                t.Date = map.getNamedItem("Date").getNodeValue();
                t.Title = map.getNamedItem("Title").getNodeValue();
                t.Category = map.getNamedItem("Category").getNodeValue();
                newTasks.add(t);
            }
            Tasks = newTasks;

        }
        catch (Exception ex){
            Log.d("DeserTask(): ", ex.getMessage());
        }
    }
}
