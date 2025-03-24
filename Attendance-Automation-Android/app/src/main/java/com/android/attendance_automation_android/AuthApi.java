package com.android.attendance_automation_android;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("attendance/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
