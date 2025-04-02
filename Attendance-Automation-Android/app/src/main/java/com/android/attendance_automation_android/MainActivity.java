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
    private IpApiService ipApiService;
    private TextView signInMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(android.R.color.background_dark));

        signInMessage = findViewById(R.id.signInMessage);

        // Initializing Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);

        // Initialize Retrofit for IP API
        Retrofit ipRetrofit = new Retrofit.Builder()
                .baseUrl("https://api64.ipify.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ipApiService = ipRetrofit.create(IpApiService.class);
    }

    public void signIn(View view) {
        EditText sstMail = findViewById(R.id.sstMail);
        EditText sstPassword = findViewById(R.id.password);

        String sstMailString = sstMail.getText().toString();
        String sstPasswordString = sstPassword.getText().toString();
        String promptMessage;

        if (sstMailString.isEmpty() || sstPasswordString.isEmpty()) {
            if (sstMailString.isEmpty() && sstPasswordString.isEmpty()) {
                promptMessage = "Please Enter your Credentials";
            } else if (sstMailString.isEmpty()) {
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
        ipApiService.getIp().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<IpResponse> call, Response<IpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userIp = response.body().getIp();
                    Log.i("USER_IP", "IP Address: " + userIp);
                    sendLoginRequest(email, password, userIp);
                } else {
                    Log.e("USER_IP", "Failed to fetch IP");
                    sendLoginRequest(email, password, "Unknown");
                }
            }

            @Override
            public void onFailure(Call<IpResponse> call, Throwable t) {
                Log.e("USER_IP", "Error fetching IP: " + t.getMessage());
                sendLoginRequest(email, password, "Unknown");
            }
        });
    }

    private void sendLoginRequest(String email, String password, String ip) {
        // Get SSID and BSSID
        String ssid = WifiHelper.getSSID(this);
        String bssid = WifiHelper.getBSSID(this);

        Log.i("WIFI_INFO", "SSID: " + ssid + ", BSSID: " + bssid);

        LoginRequest request = new LoginRequest(email, password, ip);
        Call<LoginResponse> call = authApi.login(request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Log.i("API_RESPONSE", "Token: " + token);

                    Intent intent = new Intent(MainActivity.this, MarkAttendance.class);
                    intent.putExtra("TOKEN", token);
                    intent.putExtra("SSID", ssid);
                    intent.putExtra("BSSID", bssid);
                    startActivity(intent);
                    finish();
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