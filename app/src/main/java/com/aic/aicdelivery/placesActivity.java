package com.aic.aicdelivery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class placesActivity extends AppCompatActivity implements AsyncResponse, OnMapReadyCallback, LocationListener {
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText txtaddress;
    Location l = null;
    PlacesClient placesClient;
    HMCoreData myDB;
    IntroManager intomanager;
    String cityg, addressg, latlngu;
    ProgressBar progressBar20;
    GoogleMap mMap;
    Button btnset;
    LocationManager mlocationManager;
    String newaddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new HMCoreData(this);
        intomanager = new IntroManager(this);
        setContentView(R.layout.fragment_fr_googleplaces);
        txtaddress = findViewById(R.id.txtaddress);

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.maps_api_key));
        }

        placesClient = Places.createClient(getApplicationContext());
        //final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)  getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);
        autocompleteSupportFragment.setPlaceFields(fieldList);
        progressBar20 = findViewById(R.id.progressBar20);
        btnset = findViewById(R.id.btnnext);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {


                final LatLng latlng = place.getLatLng();

                performAddressChange(latlng.latitude, latlng.longitude);
                Log.i("Debugging", "Latitude:" + latlng.latitude + " Longitude:" + latlng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (intomanager.getAddressType().equals("N")) {
                    intomanager.setGeoLocation(latlngu);
                    intomanager.setAddressLandMark(txtaddress.getText().toString());
                } else if (intomanager.getProfile()) {
                    intomanager.setLocation(txtaddress.getText().toString());
                    intomanager.setLatLon(latlngu);
                } else {
                    intomanager.setLocation(txtaddress.getText().toString());
                    intomanager.setLatLon(latlngu);

                }*/
                //  intomanager.setAddressLandMark(addressg);

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", HMConstants.appmerchantid);
                    jsonMain.put("mdevice", intomanager.getDevice());
                    jsonMain.put("geolocation", latlngu);
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getApplicationContext(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMFetchCity", jsonParam.toString()};
                Log.i("Debugging ", "Fetch City  " + jsonParam.toString());
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getApplicationContext(), progressBar20, "SSMFetchCity");
                    aasyncTask.delegate = (AsyncResponse) placesActivity.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getApplicationContext(), e.getMessage());
                    return;
                }
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);

    }

    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    l = location;
                    performAddressChange(l.getLatitude(), l.getLongitude());
                }
            }
        });
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("Debugging", "Fetchcity while firsttime  " + output);

        JSONObject myjson = new JSONObject(output);
        if (!myjson.getString("statuscode").equals("000")) {
            myDB.showToast(getApplicationContext(), myjson.getString("statusdesc"));
            return;
        } else {
            try {
                mlocationManager.removeUpdates(this);
                mlocationManager = null;
            } catch (Exception e) {
//             //
            }
//            Log.i("Debugging", "Profile ---------------> " + intomanager.getProfile());
//            Log.i("Debugging", "Address Type ---------------> " + intomanager.getAddressType());

            /*if (intomanager.getAddressType().equals("N")) {
                intomanager.setCityOnce(myjson.getString("city"));*/
            Intent intent = new Intent();
            intent.putExtra("keyLocation", txtaddress.getText().toString());
            intent.putExtra("keyLatLon", latlngu);
//                intent.putExtra("keyCity", intomanager.getCity());
//                intent.putExtra("keyCityName", intomanager.getCityName());
            setResult(RESULT_OK, intent);
            //       clickevent.clickEventItem();
            //         clickevent.clickEventItem(txtaddress.getText().toString(),latlngu);
            /*} else if (intomanager.getProfile()) {
                intomanager.setCity(myjson.getString("city"));
                intomanager.setCityName(myjson.getString("cityname"));
                Log.i("Debugging", "City Code " + intomanager.getCity());
                Log.i("Debugging", "City Name " + intomanager.getCityName());
                Log.i("Debugging", "Address " + intomanager.getLocation());
                Log.i("Debugging", "Geo location" + intomanager.getLatLon());
                //     clickevent.clickEventItem();
                //    clickevent.clickEventItem(txtaddress.getText().toString(),latlngu);
                Intent intent = new Intent();
                intent.putExtra("keyLocation", txtaddress.getText().toString());
                intent.putExtra("keyLatLon", latlngu);
                intent.putExtra("keyCity", intomanager.getCity());
                intent.putExtra("keyCityName", intomanager.getCityName());
                setResult(RESULT_OK, intent);
            } else {
                intomanager.setCity(myjson.getString("city"));
                intomanager.setCityName(myjson.getString("cityname"));
                Intent intent = new Intent(placesActivity.this, MainActivity.class);
                startActivity(intent);
            }*/
            finish();


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    performAddressChange(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                }

            });

            //      LatLng sydney = new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
            //      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
            //      performAddressChange(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
            //    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    Log.i("Debugging", "Running Second Camera");
                    performAddressChange(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                    newaddress = "";
                    mlocationManager.removeUpdates(placesActivity.this);
                }


            });

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fetchLastLocation();
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            fm.getMapAsync(this);

              /*  LatLng sydney = new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
                performAddressChange(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude);*/
        }
    }

    public void performAddressChange(Double lat, Double lon) {
        try {
            latlngu = lat + "," + lon;
            Log.i("Debugging", "New Location----------->  " + latlngu);
        } catch (Exception e) {
            myDB.showToast(getApplicationContext(), "Invalid location select. Please try again" + latlngu);
            return;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                addressg = addresses.get(0).getAddressLine(0);
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        addressg = adr.getAddressLine(0);
                        cityg = adr.getLocality();

                        break;
                    }
                }
                //        Log.i("Debugging", "Address  while Geocode " + addressg);
                //        Log.i("Debugging", "Geolocation while Geocoder  " + cityg);
            }
            cityg = addresses.get(0).getAddressLine(1);

            txtaddress.setText(addressg);
            newaddress = addressg;
        } catch (Exception e) {
            //  myDB.showToast(getApplicationContext(), "Invalid location select. Please try again");
            return;
        }

        //    }

    }

    @Override
    public void onLocationChanged(Location location) {
        //      performAddressChange(location.getLatitude(),location.getLongitude());
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        performAddressChange(location.getLatitude(), location.getLongitude());
        Log.i("Debugging", "Running Onloaction changed");
        mlocationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Debugging", "Running onStatusChanged changed");
        try {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        } catch (Exception e) {
//             //
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Debugging", "Running onProviderEnabled changed");
        try {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        } catch (Exception e) {
//             //
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Debugging", "Running onProviderDisabled changed");
        try {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        } catch (Exception e) {
//             //
        }

    }

   /*public interface ClickEvent {
        void clickEventItem(String value, String value1);
      //  void clickEventItem();
    }

    ClickEvent clickevent;

    public void setClickEvent(ClickEvent event) {
        Log.i("Debugging", "<---------------------------------Set Click Event------------------------------------>");
        this.clickevent = event;
    }*/

}
