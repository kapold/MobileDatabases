package com.example.lw8_db;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class XmlSerializator {
    private static String fileName = "tasksInfo.xml";

    public void SerializeToXml(Context context, List<Taska> dataList)
    {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "root");

            for (int j = 0; j < dataList.size(); j++) {
                serializer.startTag(null, "task");

                serializer.startTag(null, "value");
                serializer.text(dataList.get(j).value);
                serializer.endTag(null, "value");

                serializer.startTag(null, "category");
                serializer.text(dataList.get(j).category);
                serializer.endTag(null, "category");

                serializer.startTag(null, "date");
                serializer.text(dataList.get(j).date);
                serializer.endTag(null, "date");

                serializer.endTag(null, "task");
            }
            serializer.endTag(null, "root");
            serializer.endDocument();
            serializer.flush();

            fos.close();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка в сериализации", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Taska> DeserializeFromXml(Context context)
    {
        List<Taska> arrayResult = new ArrayList<>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            fis = context.openFileInput(fileName);
            isr = new InputStreamReader(fis);

            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);

            String data;
            data = new String(inputBuffer);

            isr.close();
            fis.close();

            /*
             * Converting the String data to XML format so
             * that the DOM parser understands it as an XML input.
            */
            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));

            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            NodeList items = null;
            Document dom;

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            dom = db.parse(is);

            // Normalize the document
            dom.getDocumentElement().normalize();
            items = dom.getElementsByTagName("task");
            for (int i = 0; i < items.getLength(); i++) {
                Taska taska = new Taska();
                Node item = items.item(i);
                NodeList parametres = item.getChildNodes();
                for (int j = 0; j < parametres.getLength(); j++) {
                    Node parametr = parametres.item(j);
                    if (parametr.getNodeName().equals("value"))
                        taska.value = parametr.getFirstChild().getNodeValue();
                    if (parametr.getNodeName().equals("category"))
                        taska.category = parametr.getFirstChild().getNodeValue();
                    if (parametr.getNodeName().equals("date"))
                        taska.date = parametr.getFirstChild().getNodeValue();
                }
                arrayResult.add(taska);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка в десериализации", Toast.LENGTH_SHORT).show();
        }
        return arrayResult != null ? arrayResult : new ArrayList<>();
    }

    public List<Taska> DeserializeFromXml_XPath(Context context, String category)
    {
        List<Taska> arrayResult = new ArrayList<>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try{
            fis = context.openFileInput(fileName);
            isr = new InputStreamReader(fis);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fis);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/Tasks/Task[@category=" + "'" + category + "'" + "]";
            arrayResult = (List<Taska>) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

            isr.close();
            fis.close();
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return arrayResult;
    }
}
