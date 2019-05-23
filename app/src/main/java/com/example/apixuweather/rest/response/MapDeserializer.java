package com.example.apixuweather.rest.response;

import com.example.apixuweather.rest.response.maps.ResponseBaseMap;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MapDeserializer<T extends ResponseBaseMap> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = (JsonObject) json;

        String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : null;
        if (status == null || !status.equals("OK")){
            throw new JsonParseException(json.toString());
        }

        return new Gson().fromJson(json, typeOfT);
    }
}
