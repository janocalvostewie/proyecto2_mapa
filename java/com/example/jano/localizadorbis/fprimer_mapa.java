package com.example.jano.localizadorbis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

public class fprimer_mapa extends SupportMapFragment {


    /*
    *Constructor del map view que usaremos
     */
    public fprimer_mapa() {

    }


    public static fprimer_mapa newInstance() {

        return new fprimer_mapa();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

}
