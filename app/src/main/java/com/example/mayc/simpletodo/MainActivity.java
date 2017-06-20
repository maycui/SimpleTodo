package com.example.mayc.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    //calls this when the app is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        //three arguments: reference to activity, type of item adapter will wrap, item list
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //reference to list view
        lvItems = (ListView) findViewById(R.id.lvitems); // different R classes
        lvItems.setAdapter(itemsAdapter); //wire something??

        //mock data
        //items.add("First item");
        //items.add("Second item");

        setupListViewListener();
    }

    public void onAddItem(View v) {
        //resolve edit text same way we resolved listview - reference to edittext
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // get value of edittext as a string
        String itemText = etNewItem.getText().toString();
        //add it to to do list ; add to adapter directly
        itemsAdapter.add(itemText);
        //clear field for add new item
        etNewItem.setText("");
        writeItems();
        //display a toast
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();

    }

    private void setupListViewListener() {
        //will only be executed on a long click
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list" + position);
                items.remove(position);
                //must let adapter know list changed
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }

    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
