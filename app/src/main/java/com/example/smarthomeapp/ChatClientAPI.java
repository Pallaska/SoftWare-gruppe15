package com.example.smarthomeapp;
import okhttp3.*;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatClientAPI {
    // Endpoint er ikke laget ennå, tar det i neste sprint
    private String URL = "http://localhost:3000";
    private OkHttpClient httpClient = new OkHttpClient();

    // Metoden sender melding ved å lage et JSON objekt
    // som går inn i request body og sendes med newCall
    public String sendMelding(String melding) {
        JSONObject jsonObject = new JSONObject();

        // Har ikke lagt til noe verdi for "sender" siden
        // det ikke er noe behov for å spore brukere
        try {
            jsonObject.put("sender", "");
            jsonObject.put("melding", melding);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Lager et RequestBody objekt med meldingen
        RequestBody requestBody = RequestBody.create(jsonObject.toString(),
                MediaType.parse("application/json"));

        // Lager et request med URL/request body
        Request request = new Request.Builder().url(URL).post(requestBody).build();

        // Sender request og får svar som et Response objekt
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}