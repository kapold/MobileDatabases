package by.bstu.bdlab8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class TasksByCategoryActivity extends AppCompatActivity {

    private int index;
    private String Category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_by_category);

        Bundle extras = getIntent().getExtras();
        index = extras.getInt("index");
        Category = Task.Categories.get(index);

        getTasksByCategory();
    }

    public void getTasksByCategory(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File(getFilesDir(), "tasks.xml");
            Document doc = builder.parse(f);
            ArrayList<Task> choseTasks = new ArrayList<>();

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();
            XPathExpression xpe = xp.compile("/Tasks/Task[@Category = '" + Category +  "']");

            NodeList nl = (NodeList)xpe.evaluate(doc, XPathConstants.NODESET);
            for(int i = 0; i < nl.getLength(); i++){
                Task newTask = new Task();
                newTask.Title = nl.item(i).getAttributes().getNamedItem("Title").getNodeValue();
                newTask.Date = nl.item(i).getAttributes().getNamedItem("Date").getNodeValue();
                newTask.Category = nl.item(i).getAttributes().getNamedItem("Category").getNodeValue();
                choseTasks.add(newTask);
            }

            ListView taskList = findViewById(R.id.tasksByCategoryList);
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, choseTasks);
            taskList.setAdapter(adapter);
        }
        catch (Exception ex){
            Log.d("Xpath():", ex.getMessage());
        }
    }
}