package util;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ResponseUtil {
       public static JsonObject getJson(String state, String message, JsonArray...data){
           JsonObjectBuilder response = Json.createObjectBuilder();//create object
           response.add("state", state);
           response.add("message", message);
           if(data.length>0) {
               response.add("data", data[0]);
           }else{
               response.add("data", "");
           }
           return response.build();
       }
}
