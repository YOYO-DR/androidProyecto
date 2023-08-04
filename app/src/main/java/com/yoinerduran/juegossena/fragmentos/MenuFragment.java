package com.yoinerduran.juegossena.fragmentos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yoinerduran.juegossena.InicioActivity;
import com.yoinerduran.juegossena.R;
import com.yoinerduran.juegossena.api.ApiDjango;
import com.yoinerduran.juegossena.funciones.Funciones;

import org.json.JSONArray;
import org.json.JSONObject;

public class MenuFragment extends Fragment {
    ImageButton btnInicio,btnSalirSesion;
    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        View vista=inflater.inflate(R.layout.fragment_menu, container, false);
        btnInicio=vista.findViewById(R.id.btnInicio);
        btnSalirSesion=vista.findViewById(R.id.btnSalirSesion);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Cargar inicio", Toast.LENGTH_SHORT).show();
                //obtengo el fragmento inicio
                //Fragment fragInicioSesion=new IniciarSesionFragment();
                //FragmentManager fragInicioManager=getSupportFragmentManager();
                //Funciones.crearFrag(fragInicioManager,fragInicioSesion,R.id.contentInicio);
            }
        });
        btnSalirSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtengo el session id
                SharedPreferences session=getActivity().getSharedPreferences("session", getContext().MODE_PRIVATE);
                String session_id=session.getString("session_id","");
                SharedPreferences.Editor sessionEditor=session.edit();
                if (session_id.isEmpty()){
                    Intent intento = new Intent(getContext(), InicioActivity.class);
                    startActivity(intento);
                }else{
                    RequestParams parametros = new RequestParams();
                    parametros.put("session_id",session_id);
                    Funciones.enviarPeticionPost(getContext(), ApiDjango.urlCerrarSesion(getContext()),parametros,
                            (statusCode, respuesta) -> {
                                JSONArray res=new JSONObject(respuesta).getJSONArray("respuesta");
                                String resVS=res.get(0).toString();
                                if (resVS.equals("true")){
                                    //limpio el session id
                                    sessionEditor.clear();
                                    //aplico los cambios
                                    sessionEditor.apply();
                                    //lo redirijo al inicio de sesión
                                    Intent intento = new Intent(getContext(), InicioActivity.class);
                                    startActivity(intento);
                                }
                                Toast.makeText(getContext(), "Cerrando sesión", Toast.LENGTH_SHORT).show();
                            },(statusCode, error) -> {
                                Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                            }
                    );
                }
            }
        });
        return vista;
    }
}