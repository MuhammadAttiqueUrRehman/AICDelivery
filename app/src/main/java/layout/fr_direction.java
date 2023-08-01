package layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdelivery.DirectionsJSONParser;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_direction extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    //  MapView mapView;
    IntroManager intromanager;
    LatLng latlng;
    public fr_direction() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intromanager = new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_direction, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Direction");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tvDistanceDuration = (TextView) v.findViewById(R.id.tv_distance_time);
        // Initializing
        markerPoints = new ArrayList<LatLng>();

        //  markerPoints.add(getArguments().getString("lat") + "," +  getArguments().getString("lon"));
        SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Getting Map for the SupportMapFragment
        fm.getMapAsync(this);


        return v;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getAllProviders();
            Location bestLocation = null;
            Log.d("Debugging", "Providers Check " + providers.size());
            for (String provider : providers) {
                Log.d("Debugging", "Providers Name " + provider.toString());
                Location l = mLocationManager.getLastKnownLocation(provider);

                if (bestLocation == null) {
                    bestLocation = l;
                }
            }

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
         //   try {
                Double dlat = (Double) bestLocation.getLatitude();
                Double dlon = (Double) bestLocation.getLongitude();
                latlng = new LatLng(dlat, dlon);
       //     }catch (Exception e){
       //         return;
       //     }


            //      mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            //        @Override
            //      public void onMapClick(LatLng point) {
            // Already two locations
            //    if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
            //    }

            // Adding new item to the ArrayList
            MarkerOptions options, options1;
            markerPoints.add(latlng);
            options = new MarkerOptions();
            options.position(latlng);
            LatLng tlatlng = new LatLng(Double.parseDouble(getArguments().getString("lat")), Double.parseDouble(getArguments().getString("lon")));

            markerPoints.add(tlatlng);
            options1 = new MarkerOptions();
            options1.position(tlatlng);
            /**
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             */
//                if (markerPoints.size() == 1) {
            Log.i("Debugging", "Origin " + markerPoints.toString());
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else if (markerPoints.size() == 2) {
            Log.i("Debugging", "Destination " + markerPoints.toString());
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //              }

            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options);
            mMap.addMarker(options1);

            // Checks, whether start and end locations are captured
            if (markerPoints.size() >= 2) {
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(options.getPosition());
                builder.include(options1.getPosition());
                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.animateCamera(cu);
                Toast.makeText(getContext(), "Tap on the Destination Marker  or Red Color marker to invoke navigation.", Toast.LENGTH_LONG).show();
            }
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getAllProviders();
            Location bestLocation = null;
            for (String provider : providers) {
                Log.d("Debugging", "Providers Name " + provider.toString());
                Location l = mLocationManager.getLastKnownLocation(provider);

                if (bestLocation == null) {
                    bestLocation = l;
                }
            }

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       //     try {
                Double dlat = (Double) bestLocation.getLatitude();
                Double dlon = (Double) bestLocation.getLongitude();
                latlng = new LatLng(dlat, dlon);
        //    }catch (Exception e){
         //       return;
         //   }
            markerPoints.clear();
            mMap.clear();
            // Adding new item to the ArrayList
            MarkerOptions options, options1;
            markerPoints.add(latlng);
            options = new MarkerOptions();
            options.position(latlng);
            LatLng tlatlng = new LatLng(Double.parseDouble(getArguments().getString("lat")), Double.parseDouble(getArguments().getString("lon")));

            markerPoints.add(tlatlng);
            options1 = new MarkerOptions();
            options1.position(tlatlng);

            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            Log.i("Debugging", "Destination " + markerPoints.toString());
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //              }

            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options);
            mMap.addMarker(options1);

            // Checks, whether start and end locations are captured
            if (markerPoints.size() >= 2) {
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(options.getPosition());
                builder.include(options1.getPosition());
                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.animateCamera(cu);
                Toast.makeText(getContext(), "Tap on the Destination Marker  or Red Color marker to invoke navigation.", Toast.LENGTH_LONG).show();
            }
        }
        return;
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String apikey="key=" + HMConstants.DEVELOPER_KEY;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + apikey;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.i("Debugging", "URL " + url);
        return url;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }

            tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }



}

