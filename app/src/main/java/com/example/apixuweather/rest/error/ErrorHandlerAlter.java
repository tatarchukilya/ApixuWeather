package com.example.apixuweather.rest.error;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

import static com.example.apixuweather.utils.Const.I_TAG;

public class ErrorHandlerAlter {
    public static final String TAG = I_TAG + ErrorHandler.class.getSimpleName();

    public static final String[] APIXU = {
            "API key not provided.",
            "Parameter 'q' not provided.",
            "API request url is invalid",
            "No location found matching parameter 'q'",
            "API key provided is invalid",
            "API key has exceeded calls per month quota.",
            "API key has been disabled.",
            "Internal application error."
    };

    public static String[] MAP = {
            "OVER_QUERY_LIMIT",
            "REQUEST_DENIED",
            "INVALID_REQUEST",
            "UNKNOWN_ERROR"
    };

    public static int handelError(Throwable throwable) {
        Log.i(TAG, "handler error");
        int resId;
        /*Нет подключения*/
        if (throwable instanceof UnknownHostException) {
            Log.i(TAG, "no internet connection");
            return R.string.no_network_connection;
        }

      /*  if (throwable instanceof SSLException || throwable instanceof ConnectException){
            Log.i(TAG, throwable.getMessage());
            return R.string.failed_update_no_connection;
        }*/

        /*Ошибка google geocode. Т.к. google всегда шлет код 200, retrofit игнорирует ошибки,
         * поэтому, если статус ответа не ОК, ошибка генерируется руками при десириализации*/
        if (throwable instanceof JsonParseException) {
            return parseException(throwable.getMessage());
        }

        /*Ошибки apixu.  */
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if (!httpException.response().isSuccessful()) {
                try {
                    return parseException(httpException.response().errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG, throwable.toString());
        return  R.string.failed_update_no_connection;
    }

    private static int parseException(String exception) {
        JsonObject json = new JsonParser().parse(exception).getAsJsonObject();
        if (json.has("error")) {
            return handleApixuError(createError(json, ApixuException.class));
        }

        if (json.has("error_message")) {
            return handleMapError(createError(json, MapException.class));
        }

        return R.string.failed_update_no_connection;
    }

    private static  <E> E createError(JsonObject response, Class<E> errorClass) {
        return new Gson().fromJson(response, errorClass);
    }

    private static int handleApixuError(@NotNull ApixuException error) {
        Log.e(TAG, error.getError().getMessage() + " " + error.getError().getCode());
        return R.string.failed_apixu;
    }

    private static int handleMapError(@NotNull MapException error) {
        Log.i(TAG, error.getStatus() + " " + error.getErrorMessage());
        return R.string.failed_geo;
    }
}
