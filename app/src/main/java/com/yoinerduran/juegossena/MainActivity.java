package com.yoinerduran.juegossena;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //crear y cargar fragmento del menu, y el fragmento de inicio de sesión
        //menu
        Fragment fragMenu=new MenuFragment();
        FragmentManager fragMenuManager=getSupportFragmentManager();
        Funciones.crearFrag(fragMenuManager,fragMenu,R.id.containerMenu);
    }
}