package com.yoinerduran.juegossena.api;

import android.content.Context;

public class ApiDjango {
    //local
    //static public String url="http://192.168.110.39:8000";
    //static public String urlMedia="http://192.168.110.39:8000";

    //web
    static public String url="https://juegossena.azurewebsites.net";
    static public String urlMedia="";

    static String urlApi=url+"/api/";
    static public String urlInicioSesion(Context context){
        return urlApi+"iniciosesionapi/";
    }
    static public String urlCerrarSesion(Context context){
        return urlApi+"cerrarsesionapi/";
    }
    static  public String urlRegistrarUsuario(Context context){
        return  urlApi+"registrarusuarionapi/";
    }
    static public String urlDatosUsuario(Context context){
        return  urlApi+"datosusuarioapi/";
    }
    static public String urlDatosDispo(Context context){
        return  urlApi+"datosdispositivosapi/";
    }
}
