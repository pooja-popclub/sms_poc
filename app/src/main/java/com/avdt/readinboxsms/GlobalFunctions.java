package com.avdt.readinboxsms;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.avdt.common.SharedPrefsGetSet;
import com.avdt.retorfit.ApiResponse;
import com.avdt.retorfit.ApiResponse1;
import com.avdt.retorfit.SmsApi;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalFunctions {
    static String app_name = "pop_poc";
    public static void sendDataToServer(MainActivity mainActivity, boolean checkAndRequestPermissions, List<JsonObject> creditEntities) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("source", "SMS");
        map.put("permission", checkAndRequestPermissions);
        map.put("data", creditEntities);

        final Call<ApiResponse> call = initRetrofit(mainActivity)
                .create(SmsApi.class)
                .sendSmsList(map);
        call.enqueue(new CancelableCallback<ApiResponse>("TAG") {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("Success",""+map);
                    //mainActivity.showResponse("Response Code: "+response.code()+"\nResponse Status: "+response.message() +"\nResponse Body: "+response.body());
                    mainActivity.showResponse(response.body());
                } else {
                    Log.d("Error",""+map);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Failure",""+map);
            }
        });
    }

    public static void callLoginApi(MainActivity mainActivity,String mobileNumber) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone_number", mobileNumber);
        final Call<ApiResponse> call = initRetrofit(mainActivity)
                .create(SmsApi.class)
                .loginApi(map);
        call.enqueue(new CancelableCallback<ApiResponse>("TAG") {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("Success",""+map);
                    //mainActivity.showResponse("Response Code: "+response.code()+"\nResponse Status: "+response.message() +"\nResponse Body: "+response.body());
                    mainActivity.showLoginResponse(response.body());
                } else {
                    Log.d("Error",""+map);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Failure",""+map);
            }
        });
    }

    public static void initializeSSLContext(Context mContext) {
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static Retrofit initRetrofit(Context mContext) {

        //SSL Context - For Handling SSL Exception
        initializeSSLContext(mContext);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.followRedirects(false);
        final File baseDir = mContext.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            httpClient.cache(new Cache(mContext.getApplicationContext().getCacheDir(), 10 * 1024 * 1024));
        }

        //Setting time out for okHTTP
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(true);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder builder = request.newBuilder();
               if (!TextUtils.isEmpty(SharedPrefsGetSet.getToken(mContext))) {
                   builder.addHeader("Authorization", "Bearer " + SharedPrefsGetSet.getToken(mContext));
                   //builder.addHeader("os_type", "android");
               }
                return chain.proceed(builder.build());

            }
        }).build();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }


        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl("http://a9e4dcfcff44241c9a35b08d356bc670-c0aaec70b15db883.elb.ap-south-1.amazonaws.com")
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    public static void getDataFromTheServer(@NotNull MainActivity mainActivity) {
        final Call<ApiResponse1> call = initRetrofit(mainActivity)
                .create(SmsApi.class)
                .getDataFromTheServer();
        call.enqueue(new CancelableCallback<ApiResponse1>("TAG") {
            @Override
            public void onResponse(Call<ApiResponse1> call, Response<ApiResponse1> response) {
                if (response.code() == 200 && response.body() != null) {
                    //mainActivity.showResponse("Response Code: "+response.code()+"\nResponse Status: "+response.message() +"\nResponse Body: "+response.body());
                    mainActivity.showResponseOfGet(response.body());
                    Log.d("Error",""+response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse1> call, Throwable t) {
                //Log.d("Failure",""+map);
            }
        });
    }

    public static void calculateCoins(@NotNull MainActivity mainActivity, HashMap<String, Object> map) {
        final Call<ApiResponse1> call = initRetrofit(mainActivity)
                .create(SmsApi.class)
                .calculateCoins(map);
        call.enqueue(new CancelableCallback<ApiResponse1>("TAG") {
            @Override
            public void onResponse(Call<ApiResponse1> call, Response<ApiResponse1> response) {
                if (response.code() == 200 && response.body() != null) {
                    mainActivity.showResponseOfGet(response.body());
                    Log.d("Error",""+response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse1> call, Throwable t) {
                //Log.d("Failure",""+map);
            }
        });
    }

    public static void getCateInformation(@NotNull MainActivity mainActivity) {
        final Call<ApiResponse> call = initRetrofit(mainActivity)
                .create(SmsApi.class)
                .getCateInformation();
        call.enqueue(new CancelableCallback<ApiResponse>("TAG") {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    mainActivity.showSettingDetails(response.body());
                    Log.d("Error",""+response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                //Log.d("Failure",""+map);
            }
        });
    }

    public static String getSharedPrefs(Context c, String key,
                                        String default_value) {
        if (c == null) {
            return default_value;
        } else {
            SharedPreferences prefs = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE);
            return prefs.getString(key, default_value);
        }
    }

    public static void setSharedPrefs(Context c, String key, String value) {

        SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }
    public static void clearSharedPrefs(Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                Context.MODE_PRIVATE).edit();
        //editor.commit();
        editor.clear();
        editor.apply();
    }
}
