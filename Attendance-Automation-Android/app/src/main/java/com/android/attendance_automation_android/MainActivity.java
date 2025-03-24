package com.android.attendance_automation_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://3.95.186.173:8080/";
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(android.R.color.background_dark));

        // Initializing Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);
    }
    public void signIn(View view){
        TextView signInMessage = findViewById(R.id.signInMessage);

        EditText sstMail = findViewById(R.id.sstMail);
        EditText sstPassword = findViewById(R.id.password);

        String sstMailString = sstMail.getText().toString();
        String sstPasswordString = sstPassword.getText().toString();
        String promptMessage;

        if(sstMailString.isEmpty() || sstPasswordString.isEmpty()){
            if(sstMailString.isEmpty() && sstPasswordString.isEmpty()) {
                promptMessage = "Please Enter your Credentials";
            } else if (sstMailString.isEmpty()){
                promptMessage = "Please Enter your SST Email";
            } else {
                promptMessage = "Please Enter your Password";
            }

            signInMessage.setText(promptMessage);
            signInMessage.setTextColor(Color.RED);
            return;
        }

        Log.i("SignInButton", "Sign In Attempted");
        Log.i("Mail", sstMailString);
        Log.i("Password", sstPasswordString);

        loginUser(sstMailString, sstPasswordString);
    }

    private void loginUser(String email, String password) {
        TextView signInMessage = findViewById(R.id.signInMessage);
        LoginRequest request = new LoginRequest(email, password);
        Call<LoginResponse> call = authApi.login(request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    boolean isNewUser = response.body().isNewUser();
                    Log.i("API_RESPONSE", "Token: " + token);

                    Intent intent = new Intent(MainActivity.this, MarkAttendance.class);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
                    finish();

                    if (isNewUser) {
                        Toast.makeText(MainActivity.this, "Welcome! Your account has been created.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    signInMessage.setText("Login failed. Please try again.");
                    signInMessage.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                signInMessage.setText("Network error. Please try again.");
                signInMessage.setTextColor(Color.RED);
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}
