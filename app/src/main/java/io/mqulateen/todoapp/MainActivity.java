package io.mqulateen.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyPreferences myPreferences;

    private ArrayAdapter<String> tasksAdapter;
    private ArrayList<String> tasks;
    private ListView listView; //declare listview from xml layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View empty = findViewById(R.id.emptyListText);
        listView = (ListView) findViewById(R.id.listItems);
        listView.setEmptyView(empty);

        myPreferences = new MyPreferences(this); //object for persistent storage
        tasks = myPreferences.retrieveList(); //get data if it exists
        populateListView();

        //floating '+' button for creating new tasks
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTaskDialog(); //display alertbox for adding a new task
            }
        });
    }

    void displayTaskDialog(){
        //input field for the dialog
        final EditText taskText = new EditText(this);

        //dialog used to prompt user to enter their task
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.item_dialog_title))
                .setMessage(getResources().getString(R.string.item_dialog_message))
                .setCancelable(false)
                .setView(taskText)
                .setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasks.add(taskText.getText().toString()); //append new task to the arraylist
                        populateListView(); //reload
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //remove dialog from view
                    }
                }).show();
    }

    //populate arrayadapter with the arraylist containing the tasks
    private void populateListView() {
        if(tasksAdapter==null){
            tasksAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.task_text, new ArrayList<>(tasks));
            listView.setAdapter(tasksAdapter);
        }
        else{
            //adapter exists so remove refresh with new list, store list to persistent storage
            tasksAdapter.clear();
            tasksAdapter.addAll(tasks);
            tasksAdapter.notifyDataSetChanged();
            myPreferences.storeList(tasks);
        }
    }

    //find the text for the selected cell, remove from the list, reload
    public void deleteItem(View view){
        TextView taskTextView = (TextView) ((View) view.getParent()).findViewById(R.id.task_text);
        String task = String.valueOf(taskTextView.getText());
        tasks.remove(task);
        populateListView();
    }
}
