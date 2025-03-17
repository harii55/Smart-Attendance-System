package com.android.attendance_automation_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    TextView signInMessage = findViewById(R.id.signInMessage);
    public void signIn(View view){
        EditText sstMail = (EditText) findViewById(R.id.sstMail);
        EditText sstRollNo = (EditText) findViewById(R.id.sstRollNo);

        Log.i("SignInButton", "Sign In Attempted");
        Log.i("Mail", sstMail.getText().toString());
        Log.i("RollNo", sstRollNo.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarkAttendance.class);
                startActivity(intent);
            }
        });
    }
}
