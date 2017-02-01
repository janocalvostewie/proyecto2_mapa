package com.example.jano.localizadorbis;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
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
       // LatLng miCasaVigo = new LatLng(42.21107305348766,-8.737963736057281);
        double latitud=42.211;
        double longitud=-8.737;
        LatLng miCasaVigo = new LatLng(latitud,longitud);
        //LatLng miCasaVigo = new LatLng(capturaLatitud(),capturaLatitud());
        double numAleatorio=generaNumero();
        int radio=56;

        // Controles UI
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Añadimos una marca en las coordenadas indicadas
       LatLng puntoInteres = new LatLng(generaCoordenada(latitud),generaCoordenada(longitud));
       // LatLng puntoInteres = new LatLng(capturaLatitud(),capturaLongitud());
       mMap.addMarker(new MarkerOptions().position(puntoInteres).title("Punto Interes"));
        CircleOptions circleOptions = new CircleOptions()
                .center(miCasaVigo)
                .radius(radio)
                .strokeColor(Color.parseColor("#0D47A1"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        mMap.addCircle(circleOptions);
/*
        Posicionamos las cámara en las coordenadas indicadas
         CameraPosition cameraPosition = CameraPosition.builder()
               .target(miCasaVigo)
             .zoom(15)
           .build();

         mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

    }

    public double generaNumero(){
        double resultado=0;
        resultado=(Math.random()*(31-10)+10)/100000;

        return resultado;
    }

    public double generaCoordenada(double coor){
        double resultado=0;
        int temp = (Math.random() <= 0.5) ? 1 : 2;
        if(temp==1){resultado=coor+generaNumero();}
        else{resultado=coor-generaNumero();}

        return resultado;
    }
public double capturaLatitud(){

    LocationManager lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    //double longitude = location.getLongitude();
    double lat = location.getLatitude();
    return lat;
}
    public double capturaLongitud(){

        LocationManager lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lon = location.getLongitude();
        //double lat = location.getLatitude();
        return lon;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

       }
    }
}
