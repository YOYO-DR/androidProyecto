package com.yoinerduran.juegossena.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoinerduran.juegossena.R;

public class ListaDispositivosFragment extends Fragment {

    public ListaDispositivosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =inflater.inflate(R.layout.fragment_lista_dispositivos, container, false);
        return vista;
    }
}