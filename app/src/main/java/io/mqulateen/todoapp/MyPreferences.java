package io.mqulateen.todoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mqul on 23/05/2017.
 */

class MyPreferences{
    private static SharedPreferences sharedPreferences;
    private final String MY_PREF = "mPrefs";
    private final String LIST_KEY = "listKey";
    private Context mContext;

    public MyPreferences(Context c){
        mContext = c;
        sharedPreferences = mContext.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
    }

    public void storeList(ArrayList<String> tasks){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LIST_KEY, TextUtils.join(", ", tasks)).apply();
    }

    public ArrayList<String> retrieveList(){
        String serializedList = sharedPreferences.getString(LIST_KEY, null);
        return (serializedList == null ? new ArrayList<>() : new ArrayList(Arrays.asList(TextUtils.split(serializedList, ", "))));
    }
}
