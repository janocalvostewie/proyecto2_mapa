package com.example.jano.localizadorbis;

import android.content.Context;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private static final int PERMISO = 1;
    private fprimer_mapa miMapa;
    double latitud, longitud, diferencia;
    double marcalatitud=42.211;
    double marcalongitud=-8.737;
    LatLng miCasaVigo;

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

        recogeUbicacion();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        solicitarPermiso();

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        miCasaVigo = new LatLng(marcalatitud,marcalongitud);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        creaPuntoInteres(57);

    }

    /*
    *Copiado de un Tutorial
     */
    public void solicitarPermiso(){
        int checkPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkPerm == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISO);
            }
        }
    }

    /*
    *Método para crear el punto de interés que habrá que localizar
     */
    public void creaPuntoInteres(int radio){

        //Añadimos una marca en las coordenadas indicadas
        LatLng puntoInteres = new LatLng(generaCoordenada(marcalatitud),generaCoordenada(marcalongitud));

        //Creamos un punto dentro de un círculo
        mMap.addMarker(new MarkerOptions().position(puntoInteres).title("Punto Interes"));
        CircleOptions circleOptions = new CircleOptions()
                .center(miCasaVigo)
                .radius(radio)
                .strokeColor(Color.parseColor("#0D47A1"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        mMap.addCircle(circleOptions);

    }

    /*
    *Copiado de una página tutorial tal cual
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISO) {

            if (permissions.length > 0 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }
        }
    }


    /*
     *Recoge la localización actual del dispositivo
      */
    public void recogeUbicacion(){
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Hubo que añadir el android. para que cogiese el Manifest
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }

    LocationListener locLis= new LocationListener() {

        @Override
        public void onLocationChanged(Location loc) {

            compararDistancia(loc);
            verDistancia(loc);

        }
        public void compararDistancia(Location loc){
            double distancia = verDistancia(loc);
            if(distancia <= 20.00){
                creaPuntoInteres(56);
            }

        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS activado",
                    Toast.LENGTH_SHORT).show();
        }


        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS desactivado",
                    Toast.LENGTH_SHORT).show();
        }
    };


    /*
    *Necesitamos un número aleatorio para crear las coordenadas
     */
    public double generaNumero(){

        double resultado=0;
        resultado=(Math.random()*(31-10)+10)/100000;

        return resultado;
    }

    /*
    *Genera unas coordenadas aleatorias para crear una marca dentro de un circulo
    * Se empleará para crear el punto de interés que hay que localizar
     */
    public double generaCoordenada(double coor){
        double resultado=0;
        int temp = (Math.random() <= 0.5) ? 1 : 2;
        if(temp==1){resultado=coor+generaNumero();}
        else{resultado=coor-generaNumero();}

        return resultado;
    }


    public double verDistancia(Location loc) {

        latitud = loc.getLatitude();
        longitud = loc.getLongitude();
        Location myLocation = new Location("YO");
        myLocation.setLatitude(latitud);
        myLocation.setLongitude(longitud);
        Location markerLocation = new Location("Punto Interés");
        markerLocation.setLatitude(marcalatitud);
        markerLocation.setLongitude(marcalongitud);
        diferencia = myLocation.distanceTo(markerLocation);

        return diferencia;
    }

}