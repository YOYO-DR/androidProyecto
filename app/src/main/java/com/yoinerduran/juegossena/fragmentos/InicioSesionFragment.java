package com.yoinerduran.juegossena.fragmentos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yoinerduran.juegossena.MainActivity;
import com.yoinerduran.juegossena.R;
import com.yoinerduran.juegossena.api.ApiDjango;
import com.yoinerduran.juegossena.funciones.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InicioSesionFragment extends Fragment {
    EditText etNombre,etContra;
    Button btnEntrar,btnRegistrar;
    public InicioSesionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
        etNombre=vista.findViewById(R.id.etNombre);
        etContra=vista.findViewById(R.id.etContra);
        btnEntrar=vista.findViewById(R.id.btnEntrar);
        btnRegistrar=vista.findViewById(R.id.btnRegistrar);
        //crear el arreglo de los botones que hacen alguna accion antes de hacer algo antes de la peticion
        Button[] botones=new Button[2];
        botones[0]=btnEntrar;
        botones[1]=btnRegistrar;

        //verificar si existe el session id y/o validar si esta valido en la api
        Funciones.verificarSession(getContext(),getActivity(),botones);

        //evento de boton registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cargar fragmento de registro
                Fragment fragRegistro=new RegistroFragment();
                FragmentManager fragRegistroManager=getActivity().getSupportFragmentManager();
                Funciones.crearFrag(fragRegistroManager,fragRegistro,R.id.containerInicio);
            }
        });
        //evento de boton entrar
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etNombre.getText().toString().trim();
                String contra=etContra.getText().toString();
                if (nombre.isEmpty() || contra.isEmpty()){
                    if (nombre.isEmpty()){
                        etNombre.setError("Debe ingresar el nombre de usuario");
                    }
                    if (contra.isEmpty()){
                        etContra.setError("Debe ingresar la contraseña");
                    }
                }else{
                    btnEntrar.setEnabled(false);
                    //Llamo la funcion, y en los parametros donde va cada interfaz funcional, le paso una funcion lambda (parametros)->{codigo}, y asi puedo reutilizar esta funcion para hacer varias  peticiones
                    RequestParams parametros = new RequestParams();
                    parametros.put("username", nombre);
                    parametros.put("password", contra);
                    parametros.put("action", "iniciarsesion");
                    Funciones.enviarPeticionPost(getContext(), ApiDjango.urlInicioSesion(getContext()),parametros,
                            (statusCode, respuesta) -> {
                                // Aqui se ejecuta el on success despues de la peticion
                                iniciarSesion(statusCode,respuesta);
                            },
                            (statusCode, error) -> {
                                // si sale un error
                                Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                                btnEntrar.setEnabled(true);
                            });


                }

            }
        });

        return vista;
    }



    //funcion de iniciar sesion
    private void iniciarSesion(int statusCode, String respuesta) throws JSONException {
        //verifico que la peticion haya tenido una respuesta
        if (statusCode>=200 && statusCode<=299){
            JSONObject jsonRes=new JSONObject(respuesta);
            //verifico que mp haya errores
            try {
                String sessionid = jsonRes.getString("sessionid");
                Toast.makeText(getContext(), "Inicio de sesion correcto", Toast.LENGTH_SHORT).show();
                //Creando y/o obteniendo los datos del archivo
                SharedPreferences session=getActivity().getSharedPreferences("session", getContext().MODE_PRIVATE);
                SharedPreferences.Editor sessionEditor=session.edit();
                sessionEditor.putString("session_id",sessionid);
                sessionEditor.apply();
                //activo el botón
                Intent intento = new Intent(getContext(), MainActivity.class);
                startActivity(intento);
            }catch (JSONException e){
                //si lanza error es porque no recibi un sessionid
                JSONArray errorArray=new JSONArray(jsonRes.getString("error"));
                String error = errorArray.get(0).toString();
                if (error.equals("Contraseña incorrecta")){
                    etContra.setError(error);
                } else if (error.equals("El usuario no existe")) {
                    etNombre.setError(error);
                }else{
                    Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                }
            }
            btnEntrar.setEnabled(true);
        }
    }
}