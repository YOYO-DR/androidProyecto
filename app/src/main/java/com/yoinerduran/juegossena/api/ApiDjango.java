package com.yoinerduran.juegossena.api;

import android.content.Context;

public class ApiDjango {

    //static String urlApi="https://juegossena.azurewebsites.net/api/";
    static String urlApi="http://192.168.110.39:8000/api/";
    static public String urlInicioSesion(Context context){
        return urlApi+"iniciosesionapi/";
    }
    static public String urlCerrarSesion(Context context){
        return urlApi+"cerrarsesionapi/";
    }
    static  public String urlRegistrarUsuario(Context context){
        return  urlApi+"registrarusuarionapi/";
    }
}
