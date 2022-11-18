package com.example.linventario;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class api_linventario {

    private static final String API_URL = "192.168.0.7:8080";
    private static final String USER_POST = "/PSM/register_inc.php";

    public static void createUser(String name, String email, String password) {
        if (!(name.isEmpty() || email.isEmpty() || password.isEmpty())) {

            HashMap<String, String> data = new HashMap<String, String>();
            data.put("nombre", name);
            data.put("correo", email);
            data.put("contrasena", password);
            data.put("v_contrasena", password);

            JSONObject datos_toSend = new JSONObject((Map<String, String>) data);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL + USER_POST, datos_toSend, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                int error = response.getInt("error");
                                if (error == 0) {
                                    Log.println(Log.INFO, "Api action", "Se creó un usuario");
                                }
                            } catch (Exception e) {
                                Log.println(Log.ERROR, "Api insert user error", e.getMessage());
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.println(Log.ERROR, "Iternal server error", error.getMessage());
                        }
                    });
        }
    }


}
