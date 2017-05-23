package com.murano500k.crackvigenere;

import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Intent.CATEGORY_OPENABLE;
import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private TextView textInput;
    private TextView textKey;
    private TextView textOut;
    private static final int REQUEST_FILE = 543;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textInput=(TextView) findViewById(R.id.text_input);
        textKey=(TextView) findViewById(R.id.text_key);
        textOut=(TextView) findViewById(R.id.text_output);
        progress=(ProgressBar)findViewById(R.id.progressBar);
        progress.setVisibility(GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });

    }

    private void selectFile(){
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType("text/plain");
        intent.addCategory(CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_FILE){
            if(resultCode==RESULT_OK){
                tryCrackFile(data);
            }else Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void tryCrackFile(Intent data) {
        progress.setVisibility(View.VISIBLE);
        ParcelFileDescriptor fd;
        try {
            fd=getContentResolver().openFileDescriptor(data.getData(),"r");
            printOutput(decryptInput(readFile(fd)));
        } catch (Exception e) {
            e.printStackTrace();
            progress.setVisibility(View.GONE);
        }

    }

    private String readFile(ParcelFileDescriptor fileDescriptor) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while(br.ready()){
            sb.append(br.readLine());
        }
        return sb.toString();
    }

    private Result decryptInput(String in){
        textInput.setText(in);
        return new DeVigenere(null).crack(in);
    }
    private void printOutput(Result result){
        textKey.setText(result.key);
        textOut.setText(result.text);
        progress.setVisibility(GONE);
    }
}
