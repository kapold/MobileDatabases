package by.bstu.bdlab8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ListView categoriesList = findViewById(R.id.categoriesList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Task.Categories);
        categoriesList.setAdapter(adapter);
        registerForContextMenu(categoriesList);

        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                OpenTasksByCategory(position);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_category_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                deleteCategory(info.position);
                FragmentManager manager = getSupportFragmentManager();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteCategory(int position){
        Task.Categories.remove(position);
        Toast toast = Toast.makeText(this, "Категория удалена!",Toast.LENGTH_LONG);
        toast.show();
        ListView categoriesList = findViewById(R.id.categoriesList);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Task.Categories);
        categoriesList.setAdapter(adapter);
        Task.SerializeCategories(getApplicationContext());
    }

    public void addNewCategory(View view) {
        EditText categoryInput = findViewById(R.id.categoryInput);
        String newCategory = String.valueOf(categoryInput.getText());
        int temp = 0;
        if(Task.Categories.size() >= 5){
            Toast toast = Toast.makeText(this, "Категорий не может быть больше 5!",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            for (String c : Task.Categories) {
                if(c.equals(newCategory)){
                    temp++;
                }
            }
            if(temp > 0){
                Toast toast = Toast.makeText(this, "Такая категория уже существует!",Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                if(newCategory.length() <= 0){
                    Toast toast = Toast.makeText(this, "Введите категорию!",Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Task.Categories.add(newCategory);
                    Toast toast = Toast.makeText(this, "Категория добавлена!",Toast.LENGTH_LONG);
                    toast.show();
                    ListView categoriesList = findViewById(R.id.categoriesList);
                    ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Task.Categories);
                    categoriesList.setAdapter(adapter);
                    Task.SerializeCategories(getApplicationContext());
                }
            }
        }
    }

    public void OpenTasksByCategory(int pos){
        Intent intent = new Intent(this, TasksByCategoryActivity.class);
        intent.putExtra("index", pos);
        startActivity(intent);
    }
}