package com.nativemodulesdemo.modules;

import android.widget.Toast;

import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.entities.Entity;
import com.apigee.sdk.data.client.response.ApiResponse;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.nativemodulesdemo.baas.ApiBAAS;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class AndroidCallbackModule extends ReactContextBaseJavaModule {

    public static final String MODULE_NAME="AndroidCallback";

    private ApiBAAS apiBAAS;

    public AndroidCallbackModule(ReactApplicationContext reactContext) {
        super(reactContext);
        apiBAAS=ApiBAAS.getInstance(reactContext);
    }


    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        return super.getConstants();
    }


    @ReactMethod
    public void startCountdown(int durationMillis,Callback errorCallback, Callback finishedCallback){
        try {
            Thread.sleep(durationMillis);
            finishedCallback.invoke("countdown finished ("+durationMillis+" ms)");
        } catch (InterruptedException e) {
            errorCallback.invoke(e.toString());
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void getEntitiesAsync(String type, String query, final Callback errorCallback, final Callback apiResponseCallback){
        apiBAAS.getApigeeDataClient().getEntitiesAsync(type, query, new ApiResponseCallback() {
            @Override
            public void onResponse(ApiResponse apiResponse) {
                try{
                    List<Entity> entities=apiResponse.getEntities();
                    apiResponseCallback.invoke(new JSONObject(apiResponse.toString()).toString(2));
                }catch(Exception e){
                    errorCallback.invoke(e.getMessage());
                }
            }

            @Override
            public void onException(Exception e) {
                errorCallback.invoke(e.getMessage());
            }
        });
    }

}
