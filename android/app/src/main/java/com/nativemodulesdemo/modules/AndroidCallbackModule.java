package com.nativemodulesdemo.modules;

import android.widget.Toast;

import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.entities.Entity;
import com.apigee.sdk.data.client.response.ApiResponse;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.nativemodulesdemo.baas.ApiBAAS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class AndroidCallbackModule extends ReactContextBaseJavaModule {

    public static final String MODULE_NAME = "AndroidCallback";

    private ApiBAAS apiBAAS;

    public AndroidCallbackModule(ReactApplicationContext reactContext) {
        super(reactContext);
        apiBAAS = ApiBAAS.getInstance(reactContext);
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
    public void startCountdown(int durationMillis, Callback errorCallback, Callback finishedCallback) {
        try {
            Thread.sleep(durationMillis);
            finishedCallback.invoke("countdown finished (" + durationMillis + " ms)");
        } catch (InterruptedException e) {
            errorCallback.invoke(e.toString());
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void getEntitiesAsync(String type, String query, final Callback errorCallback, final Callback apiResponseCallback) {
        apiBAAS.getApigeeDataClient().getEntitiesAsync(type, query, new ApiResponseCallback() {
            @Override
            public void onResponse(ApiResponse apiResponse) {
                try {
                    List<Entity> entities = apiResponse.getEntities();
                    // send a callback with our JS-ready object
                    apiResponseCallback.invoke(extractEntities(apiResponse));
                    // TODO RECREATE WITH THIS METHOD
//                    apiResponseCallback.invoke(apiResponse.getEntities());
                    Entity entity = apiResponse.getFirstEntity();
                } catch (Exception e) {
                    errorCallback.invoke(e.getMessage());
                }
            }

            @Override
            public void onException(Exception e) {
                errorCallback.invoke(e.getMessage());
            }
        });
    }

    /**
     * Extracts entities from the api response and collects them into a WritableArray, which is
     * passed to the JS (JS sees this as an object)
     * @param apiResponse
     * @return
     * @throws JSONException
     */
    public static WritableArray extractEntities(ApiResponse apiResponse) throws JSONException {
        WritableArray entities = Arguments.createArray();
        JSONObject responseJSON = new JSONObject(apiResponse.toString());
        JSONArray entitiesJSON = responseJSON.getJSONArray("entities");
        for (int i = 0; i < entitiesJSON.length(); i++) {
            JSONObject entityJSON = entitiesJSON.getJSONObject(i);
            WritableMap entity = Arguments.createMap();
            // FIXME no magic strings!
            entity.putString("type", entityJSON.getString("type"));
            entity.putString("uuid", entityJSON.getString("uuid"));
            entity.putString("username", entityJSON.getString("username"));
            entities.pushMap(entity);
        }
        return entities;
    }


}
