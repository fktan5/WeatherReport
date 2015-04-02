package net.fktan5.weatherreport;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.fktan5.weatherreport.util.SystemUiHider;

import java.io.IOException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class WeatherActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "wether";
    protected GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);
        findViewById(R.id.dummy_button).setOnClickListener(mButtonClickListener);
        buildGoogleApiClient();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask<Void, Void, String>(){
                @Override
                protected String doInBackground(Void... params) {
                    String result = null;
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(buildUrl(mLocation)).build();
                        Response response = client.newCall(request).execute();
                        result = response.body().string();
                    } catch (NullPointerException | IOException e) {
                        e.printStackTrace();
                    }

                    return result;
                }

                @Override

                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    if(result == null){
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    } else {
                        WeatherReport weatherReport = new Gson().fromJson(result, WeatherReport.class);
                        setLayoutToWeather(weatherReport);
                    }
                }
            }.execute();
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private String buildUrl(Location location) throws NullPointerException{
        return String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f", location.getLatitude(), location.getLongitude());
    }

    private void setLayoutToWeather(WeatherReport weatherReport){
        ((TextView)findViewById(R.id.text_wetherdesc)).setText(weatherReport.getBody().get(0).getDescription());
        ((TextView)findViewById(R.id.text_wetherlocname)).setText(weatherReport.getName());
        ((TextView)findViewById(R.id.text_wethermain)).setText(weatherReport.getBody().get(0).getMain());
        ((TextView)findViewById(R.id.text_wethertemp)).setText(String.valueOf(weatherReport.getMain().getTemp()));
    }
}