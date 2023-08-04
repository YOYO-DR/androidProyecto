package com.yoinerduran.juegossena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.yoinerduran.juegossena.fragmentos.InicioSesionFragment;
import com.yoinerduran.juegossena.funciones.Funciones;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //cargar fragmento de inicio de sesión
        Fragment fragInicioSesion=new InicioSesionFragment();
        FragmentManager fragInicioManager=getSupportFragmentManager();
        Funciones.crearFrag(fragInicioManager,fragInicioSesion,R.id.containerInicio);
    }
}