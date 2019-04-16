package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ObjectEncode {


    static <T> String Encode(T input){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        StringBuilder sb = new StringBuilder();
        sb.append(gson.toJson(input));
        return sb.toString();
    }
}
