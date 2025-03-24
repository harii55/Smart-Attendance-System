package com.android.attendance_automation_android;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IpApiService {
    @GET("?format=json")
    Call<IpResponse> getIp();
}