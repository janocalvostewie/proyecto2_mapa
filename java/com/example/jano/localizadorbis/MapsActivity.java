package com.example.jano.localizadorbis;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //Creamos un atributo de la clase fragment_mapa
    private fprimer_mapa miMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Con el FragmentManager y el fragmento que hemos creado, hacemos que se muestre
        //Primero instanciamos un objeto del tipo de fragment y con el FragmentManager lo vinculamos al layout 'map'
        //de la aplicación
        miMapa = fprimer_mapa.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, miMapa)
                .commit();
        // Registrar escucha onMapReadyCallback
        miMapa.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Tras buscar latitud y longitud de mi direccion las he añadido
        LatLng miCasaVigo = new LatLng(42.226136, -8.707154);
        //Añadimos una marca en las coordenadas indicadas
        mMap.addMarker(new MarkerOptions()
            .position(miCasaVigo)
            .title("Mi casa"));

        //Posicionamos las cámara en las coordenadas indicadas
    CameraPosition cameraPosition = CameraPosition.builder()
            .target(miCasaVigo)
            .zoom(15)
            .build();

    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
