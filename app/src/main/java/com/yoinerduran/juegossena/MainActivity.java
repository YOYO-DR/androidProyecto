package com.yoinerduran.juegossena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.yoinerduran.juegossena.api.ApiDjango;
import com.yoinerduran.juegossena.fragmentos.DispoFragment;
import com.yoinerduran.juegossena.fragmentos.MenuFragment;
import com.yoinerduran.juegossena.funciones.Funciones;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvNombre;
    ImageView ivUsuario;
    SharedPreferences session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNombre=findViewById(R.id.tvNombre);
        tvNombre.setText("Cargando...");
        ivUsuario=findViewById(R.id.ivUsuario);
        session=getSharedPreferences("session", Context.MODE_PRIVATE);
        //hacer peticion para pedir los valores del usuario
        RequestParams parametros = new RequestParams();
        parametros.put("session_id",session.getString("session_id",""));
        Funciones.enviarPeticionPost(this, ApiDjango.urlDatosUsuario(this),parametros,(statusCode, respuesta) -> {
            //obtengo el username y link de la imagen del usuario
                JSONObject res=new JSONObject(respuesta);
            try{
                JSONObject usuario=res.getJSONObject("usuario");
                String username=Funciones.toTitle(usuario.getString("username"));
                String tiene_imagen=usuario.getString("tiene_imagen");
                String imagen = null;
                if (tiene_imagen.equals("true")){
                    imagen=ApiDjango.urlMedia+usuario.getString("imagen");
                }else{
                    imagen=usuario.getString("imagen");
                }

                tvNombre.setText("Hola, "+username+".");
                Picasso.with(this).load(imagen).into(ivUsuario);
            }catch (JSONException e){
                String error=res.getString("error");
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }

        },(statusCode, error) -> {
            Log.e("error",error.toString());
        });
        //crear y cargar fragmento del menu
        Fragment fragMenu=new MenuFragment();
        FragmentManager fragMenuManager=getSupportFragmentManager();
        Funciones.crearFrag(fragMenuManager,fragMenu,R.id.containerMenu);

        //cargar fragmento de los dispositivos
        Fragment fragDispo=new DispoFragment();
        FragmentManager fragDispoManager=getSupportFragmentManager();
        Funciones.crearFrag(fragDispoManager,fragDispo,R.id.containerFrag);
    }
}