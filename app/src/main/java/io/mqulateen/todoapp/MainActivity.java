package io.mqulateen.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> tasksAdapter;
    private ArrayList<String> tasks;
    private ListView listView; //declare listview from xml layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listItems);
        sharedPreferences = getSharedPreferences("mPrefs", Context.MODE_PRIVATE);

        tasks = retrieveList();
        loadTaskList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTaskDialog();
            }
        });
    }

    void displayTaskDialog(){
        final EditText taskText = new EditText(this); //input field for the dialog

        //dialog used to prompt user to enter their task
        new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("What is your task")
                .setCancelable(false)
                .setView(taskText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasks.add(taskText.getText().toString());
                        loadTaskList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void loadTaskList() {
        if(tasksAdapter==null){
            tasksAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.task_title, new ArrayList<>(tasks));
            listView.setAdapter(tasksAdapter);
        }
        else{
            tasksAdapter.clear();
            tasksAdapter.addAll(tasks);
            tasksAdapter.notifyDataSetChanged();
            storeList();
        }
    }

    public void deleteItem(View view){
        TextView taskTextView = (TextView) ((View) view.getParent()).findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        tasks.remove(task);
        loadTaskList(); //reload the list
    }

    public void storeList(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("listKey", TextUtils.join(", ", tasks)).apply();
    }

    public ArrayList<String> retrieveList(){
        String serializedList = sharedPreferences.getString("listKey", null);
        return (serializedList == null ? new ArrayList<>() : new ArrayList(Arrays.asList(TextUtils.split(serializedList, ", "))));
    }
}
