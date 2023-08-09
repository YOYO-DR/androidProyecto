package com.yoinerduran.juegossena.funciones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yoinerduran.juegossena.MainActivity;
import com.yoinerduran.juegossena.api.ApiDjango;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class Funciones {

    //creo estas 2 interfaces funcionales que van a sert como la ejecucion de las funciones que pase como parametro
    @FunctionalInterface
    public interface SuccessFunction {
        void apply(int statusCode, String respuesta) throws JSONException;
    }

    @FunctionalInterface
    public interface FailureFunction {
        void apply(int statusCode, Throwable error);
    }
    //las paso como parametro en esta funcion y las recibira como funciones lambda y asi se ejecutaran cuando las llame en el onSuccess o onFailure, solo cuando se complete la peticion
    public static void enviarPeticionPost(Context context, String url, RequestParams parametros, SuccessFunction success, FailureFunction failure) {
        AsyncHttpClient httpCliente=new AsyncHttpClient();

        httpCliente.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = new String(responseBody);
                try {
                    success.apply(statusCode, respuesta);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failure.apply(statusCode, error);
            }
        });
    }
    // Funcion para hacer llamado a los fragmentos, como esta fuera de un activity, le paso el manager, la vista, el fragmetno y el id del contenedor
    // no le paso el View vista porque practicamente es el id del contenedor en este caso
    public static void crearFrag(FragmentManager fragManager, Fragment frag, int container) {
        FragmentTransaction fragTransa = fragManager.beginTransaction();
        fragTransa.replace(container, frag);
        fragTransa.commit();
    }
    //verifica la sesion si existe un session id y verificarlo en la api, si no existe el session id, no hace nada (para verificacion fuera e una vista que requiera iniciar sesion)
    public static void verificarSession(Context context, FragmentActivity activity, Button[] botones) {
        SharedPreferences session=activity.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session_id= session.getString("session_id","");
        if (!session_id.isEmpty()){
            Toast.makeText(context, "Verificando sesión...", Toast.LENGTH_SHORT).show();
            //desactivar los botones
            if (botones!=null){
                for (Button btn : botones){
                    btn.setEnabled(false);
                }
            }
            RequestParams parametros = new RequestParams();
            parametros.put("action", "verificariniciarsesion");
            parametros.put("session_id", session_id);
            Funciones.enviarPeticionPost(context, ApiDjango.urlInicioSesion(context),parametros,
                    (statusCode, respuesta)->{
                        JSONObject res=new JSONObject(respuesta);
                        String validacion=res.getString("validacion");
                        if (validacion.equals("true")){
                            Intent intento = new Intent(context, MainActivity.class);
                            activity.startActivity(intento);
                        }
                        //activar botones
                        if (botones!=null){
                            for (Button btn : botones){
                                btn.setEnabled(true);
                            }
                        }
                    },(statusCode, error) -> {
                        Log.e("error",error.toString());
                        Toast.makeText(context, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        //activar botones
                        if (botones!=null){
                            Toast.makeText(context, "Botones activar", Toast.LENGTH_SHORT).show();
                            for (Button btn : botones){
                                btn.setEnabled(true);
                            }
                        }
                    }
            );
        }
    }

    // Método para validar el correo electrónico
    public static boolean validarCorreo(String correo) {
        // Expresión regular para el patrón de correo electrónico
        String patronCorreo = "^[A-Za-z0-9+_.-]+@(.+)$";
        // Crea el patrón
        Pattern pattern = Pattern.compile(patronCorreo);
        // Crea el validador
        Matcher matcher = pattern.matcher(correo);
        // Verifica si el correo coincide con el patrón
        return matcher.matches();
    }
    public static String toTitle(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        char firstChar = input.charAt(0);
        return Character.toUpperCase(firstChar) + input.substring(1);
    }
}
