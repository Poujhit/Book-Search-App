package com.poujhit.findingauthorname;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static TextView output;
    EditText input;
    String inputString=null;
    ConnectivityManager MyConn;
    NetworkInfo networkInfo;
    ImageView b1;
    ImageView b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output= findViewById(R.id.result);
        input= findViewById(R.id.input);
        MyConn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        b1= findViewById(R.id.imageView3);
        b2= findViewById(R.id.imageView4);

    }

    public void searchitem(View view) {
        networkInfo= MyConn.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()) {
            inputString = input.getText().toString();
            new Find_Author(this).execute(inputString);
            KeyBoardClose(b1);

        }
        else {
            KeyBoardClose(b1);
            Toast.makeText(getApplicationContext(), "Connect To the Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void KeyBoardClose(View view){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void DeveloperPage(View view) {
        Intent i= new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(i);
    }
}
