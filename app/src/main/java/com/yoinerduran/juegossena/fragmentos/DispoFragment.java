package com.yoinerduran.juegossena.fragmentos;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yoinerduran.juegossena.R;
import com.yoinerduran.juegossena.api.ApiDjango;
import com.yoinerduran.juegossena.funciones.Funciones;

import org.json.JSONObject;

public class DispoFragment extends Fragment {
    TextView tvTitulo;
    RecyclerView rvDispo;
    SharedPreferences session;
    public DispoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_dispo, container, false);
        //obtengo la session
        session=getActivity().getSharedPreferences("session",getContext().MODE_PRIVATE);
        //verficar sesion
        tvTitulo=vista.findViewById(R.id.tvTitulo);
        rvDispo=vista.findViewById(R.id.rvDispo);
        rvDispo.setLayoutManager(new LinearLayoutManager(getContext()));
        tvTitulo.setText("Cargando...");

        //hacer peticion de los dispositivos
        RequestParams parametros= new RequestParams();
        parametros.put("session_id",session.getString("session_id",""));
        Funciones.enviarPeticionPost(getContext(), ApiDjango.urlDatosDispo(getContext()),parametros,(statusCode, respuesta) -> {
            JSONObject res= new JSONObject(respuesta);
            Log.e("dispo",respuesta);
            tvTitulo.setText("Dispositivos #");
        }, (statusCode, error) -> {
            Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            tvTitulo.setText("Dispositivos #");
        });
        return vista;
    }
}