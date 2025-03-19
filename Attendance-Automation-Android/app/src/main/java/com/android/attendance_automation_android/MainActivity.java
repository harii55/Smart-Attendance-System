package com.android.attendance_automation_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://3.95.186.173:8080/attendance/auth/login";
    public void signIn(View view){
        TextView signInMessage = (TextView) findViewById(R.id.signInMessage);

        EditText sstMail = (EditText) findViewById(R.id.sstMail);
        EditText sstPassword = (EditText) findViewById(R.id.password);

        String sstMailString = sstMail.getText().toString();
        String sstPasswordString = sstPassword.getText().toString();
        String promptMessage;

        if(sstMailString.isEmpty() || sstPasswordString.isEmpty()){
            if(sstMailString.isEmpty() && sstPasswordString.isEmpty()) {
                promptMessage = "Please Enter your Credentials!";
            } else if (sstMailString.isEmpty()){
                promptMessage = "Please Enter your SST Email!";
            } else {
                promptMessage = "Please Enter your Roll No!";
            }

            signInMessage.setText(promptMessage);
            signInMessage.setTextColor(Color.RED);
            return;
        }

        Log.i("SignInButton", "Sign In Attempted");
        Log.i("Mail", sstMailString);
        Log.i("RollNo", sstPasswordString);

        new LoginTask().execute(sstMailString, sstPasswordString);
    }

    private class LoginTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            try{
                URL url = new URL("http://3.95.186.173:8080/attendance/auth/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("email", email);
                jsonRequest.put("password", password);

                OutputStream os = connection.getOutputStream();
                os.write(jsonRequest.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                Log.i("API_RESPONSE", "Response Code: " + responseCode);

                InputStream inputStream;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                } else {
                    inputStream = connection.getErrorStream();
                }

                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                Log.i("API_RESPONSE", "Response: " + response.toString());

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LoginTask", "Error: " + e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String response) {
            TextView signInMessage = findViewById(R.id.signInMessage);

            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String token = jsonResponse.getString("token");

                    Intent intent = new Intent(MainActivity.this, MarkAttendance.class);
                    intent.putExtra("TOKEN", token); // Pass token if needed
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    signInMessage.setText("Login failed. Please try again.");
                    signInMessage.setTextColor(Color.RED);
                }
            } else {
                signInMessage.setText("Invalid credentials. Please try again.");
                signInMessage.setTextColor(Color.RED);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(android.R.color.background_dark));
    }
}
