package com.yoinerduran.juegossena.fragmentos;

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
import com.yoinerduran.juegossena.R;
import com.yoinerduran.juegossena.api.ApiDjango;
import com.yoinerduran.juegossena.funciones.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistroFragment extends Fragment {
    Button btnEntrar,btnRegistrar;
    EditText etNombre,etEmail,etContra,etContraConfirm;
    public RegistroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista= inflater.inflate(R.layout.fragment_registro, container, false);
        btnEntrar=vista.findViewById(R.id.btnEntrar);
        btnRegistrar=vista.findViewById(R.id.btnRegistrar);
        etNombre=vista.findViewById(R.id.etNombre);
        etEmail=vista.findViewById(R.id.etEmail);
        etContra=vista.findViewById(R.id.etContra);
        etContraConfirm=vista.findViewById(R.id.etContraConfirm);
        //crear el arreglo de los botones que hacen alguna accion antes de hacer algo antes de la peticion
        Button[] botones=new Button[2];
        botones[0]=btnEntrar;
        botones[1]=btnRegistrar;
        //verificar si existe el session id y/o validar si esta valido en la api
        Funciones.verificarSession(getContext(),getActivity(),botones);

        //evento para ir cargar el inicio de sesion
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cargar fragmento de inicio de sesión
                Fragment fragInicioSesion=new InicioSesionFragment();
                FragmentManager fragInicioManager=getActivity().getSupportFragmentManager();
                Funciones.crearFrag(fragInicioManager,fragInicioSesion,R.id.containerInicio);
            }
        });
        //evento del registro de usuario
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verficar que los inputs no esten vacios
                String nombre = etNombre.getText().toString().trim();
                String email=etEmail.getText().toString().trim();
                String contra=etContra.getText().toString();
                String contraConfirm=etContraConfirm.getText().toString();
                if (nombre.isEmpty() || email.isEmpty() || contra.isEmpty() || contraConfirm.isEmpty()){
                    if (nombre.isEmpty()) etNombre.setError("Debe ingresar un nombre de usuario");
                    if (email.isEmpty()) etEmail.setError("Debe ingresar un correo");
                    if (contra.isEmpty()) etContra.setError("Debe ingresar una contraseña");
                    if (contraConfirm.isEmpty()) etContraConfirm.setError("Debe confirmar la contraseña");
                }else {
                    //Validar el correo
                    if (!Funciones.validarCorreo(email)){
                        etEmail.setError("Debe ingresar un correo valido");
                    }else{
                        //validar si las son mayor a 8 caracteres
                        if (contra.length()<8){
                            etContra.setError("La contraseña debe ser de más de 8 caracteres");
                        }else{
                            //validar si contraseñas coinciden
                            if (!contra.equals(contraConfirm)){
                                etContraConfirm.setError("Las contraseñas no coinciden");
                            }else{
                                //mandar la peticion si todo es correcto
                                //desactivar los botones
                                for(Button boton : botones){
                                    boton.setEnabled(false);
                                }
                                RequestParams parametros=new RequestParams();
                                parametros.put("nombre",nombre);
                                parametros.put("email",email);
                                parametros.put("contra",contra);
                                parametros.put("contraConfirm",contraConfirm);
                                Funciones.enviarPeticionPost(getContext(), ApiDjango.urlRegistrarUsuario(getContext()),parametros,
                                        (statusCode, respuesta) -> {
                                            //activar los botones
                                            for(Button boton : botones){
                                                boton.setEnabled(true);
                                            }
                                            //si el registro es exitoso,
                                            registroUsuario(statusCode,respuesta);
                                        },(statusCode, error) -> {
                                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                            //activar los botones
                                            for(Button boton : botones){
                                                boton.setEnabled(true);
                                            }
                                        });
                                Toast.makeText(getContext(), "Registrar", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                }
            }
        });
        return vista;
    }

    private void registroUsuario(int statusCode, String respuesta) throws JSONException {
        if (statusCode>=200 && statusCode<=299){
            JSONObject res=new JSONObject(respuesta);
            try {
                String resString=res.getString("respuesta");
                JSONArray arrayRes=new JSONArray(resString);
                if (arrayRes.get(0).toString().equals("usuario creado")){
                    String correo=arrayRes.get(1).toString();
                    Toast.makeText(getContext(), "Correo de activación enviado ["+correo+"]", Toast.LENGTH_LONG).show();
                    //cargar fragmento de inicio de sesión
                    Fragment fragInicioSesion=new InicioSesionFragment();
                    FragmentManager fragInicioManager=getActivity().getSupportFragmentManager();
                    Funciones.crearFrag(fragInicioManager,fragInicioSesion,R.id.containerInicio);
                }

            }catch (JSONException e){
                JSONArray errores=new JSONArray(res.getString("error"));
                //entrara aqui si no recibe una clave con el valor de respuesta
                Toast.makeText(getContext(), ""+errores.get(0), Toast.LENGTH_LONG).show();
            }


        }

    }
}