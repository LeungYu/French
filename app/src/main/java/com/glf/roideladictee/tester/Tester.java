package com.glf.roideladictee.tester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glf.roideladictee.R;
import com.glf.roideladictee.TranslateWindow.TranslatorFrame;

public class Tester extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        final EditText test_input=(EditText)findViewById(R.id.test_input);
        Button test_search=(Button)findViewById(R.id.test_search);
        test_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tester.this, TranslatorFrame.class);
                String str=test_input.getText().toString();
                intent.putExtra("target", str);
                System.out.println("单词："+str);
                startActivity(intent);
            }
        });
    }
}
