package com.example.weatherapp;

import static java.lang.Double.parseDouble;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class CityWeather extends AppCompatActivity {

    ImageView iView;
    ImageView iFirstDay;
    ImageView iSecondDay;
    ImageView iThirdDay;

    boolean gps = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_weather);
        callWebService();

        iView = findViewById(R.id.icon);
        iFirstDay = findViewById(R.id.iconFirstDay);
        iSecondDay = findViewById(R.id.iconSecondDay);
        iThirdDay = findViewById(R.id.iconThirdDay);

    }

    public void callWebService() {
        //CURRENT
        final TextView textViewCityName = findViewById(R.id.cityName);


        //RECIBIMOS LA CIUDAD
        Intent intent = getIntent();

        if(intent.hasExtra("lat")){
            gps=true;
            forecast(intent.getStringExtra("lat"),intent.getStringExtra("lon"));
        }else{
            String city = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (city.equals("")) error();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",CL&appid=bdb869f5f7a8bb464e27d9c9eb5f86d7&units=metric";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                String cod = responseJSON.getString("cod");
                                System.out.println("cod"+cod);

                                if (cod.equals("200")) {
                                    JSONObject main = responseJSON.getJSONObject("main");
                                    JSONObject coord = responseJSON.getJSONObject("coord");
                                /*JSONArray weather = responseJSON.getJSONArray("weather");
                                JSONObject wtr = weather.getJSONObject(0);
                                String weather_main = wtr.getString("main");

                                String icon = wtr.getString("icon");*/
                                    String cityName = responseJSON.getString("name");

                                    String lat = coord.getString("lat");
                                    String lon = coord.getString("lon");


                                    textViewCityName.setText(cityName);




                                    forecast(lat, lon);
                                }else{
                                    error();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error();

                }
            });
            queue.add(stringRequest);
        }

    }

    public void forecast(String lat, String lon){
        //CURRENT
        final TextView textViewCityName = findViewById(R.id.cityName);
        final TextView textViewTemp = findViewById(R.id.temp);
        final TextView textViewTempMax = findViewById(R.id.temp_max);
        final TextView textViewTempMin = findViewById(R.id.temp_min);
        final TextView textViewWeather = findViewById(R.id.weather);
        //FORECAST
        //FIRST DAY
        final TextView textViewTemperaturesFirstDay = findViewById(R.id.temperatureFirstDay);
        final TextView textViewWeatherFirstDay = findViewById(R.id.weatherFirstDay);
        final TextView textViewFirstDay = findViewById(R.id.firstDay);

        //SECOND DAY
        final TextView textViewTemperaturesSecondDay = findViewById(R.id.temperatureSecondDay);
        final TextView textViewWeatherSecondDay = findViewById(R.id.weatherSecondDay);
        final TextView textViewSecondDay = findViewById(R.id.SecondDay);

        //THIRD DAY
        final TextView textViewTemperaturesThirdDay = findViewById(R.id.temperatureThirdDay);
        final TextView textViewWeatherThirdDay = findViewById(R.id.weatherThirdDay);
        final TextView textViewThirdDay = findViewById(R.id.ThirdDay);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url2 = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly&appid=bdb869f5f7a8bb464e27d9c9eb5f86d7&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try {
                            //FORECAST
                            JSONObject responseJSON = new JSONObject(response);
                            JSONArray daily = responseJSON.getJSONArray("daily");

                            if(gps) textViewCityName.setText("Your Location");

                            //CURRENT
                            JSONObject current= responseJSON.getJSONObject("current");
                            String tempCurrent= current.getString("temp");
                            JSONArray currentWeather= current.getJSONArray("weather");
                            JSONObject wtrCurrent= currentWeather.getJSONObject(0);
                            JSONObject currentDay= daily.getJSONObject(0);
                            JSONObject currentTemp= currentDay.getJSONObject("temp");

                            String mainWtr= wtrCurrent.getString("main");
                            String currentMin= currentTemp.getString("min");
                            String currentMax= currentTemp.getString("max");
                            String icon = wtrCurrent.getString("icon");

                            DecimalFormat df = new DecimalFormat("##");
                            textViewWeather.setText(mainWtr);

                            textViewTemp.setText(df.format(parseDouble(tempCurrent))+"°C");
                            textViewTempMin.setText(df.format(parseDouble(currentMin)) + "°C");
                            textViewTempMax.setText(df.format(parseDouble(currentMax)) + "°C");

                            String urlImage = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
                            Glide.with(getApplicationContext()).load(urlImage).into(iView);


                            RelativeLayout layout = (RelativeLayout) findViewById(R.id.Layout);
                            switch (icon) {
                                case "01d":
                                    layout.setBackgroundResource(R.drawable.clearskyday);
                                    break;
                                case "01n":
                                    layout.setBackgroundResource(R.drawable.clearskynight);
                                    break;
                                case "02d":
                                    layout.setBackgroundResource(R.drawable.fewcloudsday);
                                    break;
                                case "02n":
                                    layout.setBackgroundResource(R.drawable.fewcloudsnight);
                                    break;
                                case "03d":
                                    layout.setBackgroundResource(R.drawable.fewcloudsday);
                                    break;
                                case "03n":
                                    layout.setBackgroundResource(R.drawable.fewcloudsnight);
                                    break;
                                case "04d":
                                    layout.setBackgroundResource(R.drawable.brokencloudsday);
                                    break;
                                case "04n":
                                    layout.setBackgroundResource(R.drawable.brokencloudsnight);
                                    break;
                                case "09d":
                                    layout.setBackgroundResource(R.drawable.showerrainday);
                                    break;
                                case "09n":
                                    layout.setBackgroundResource(R.drawable.showerrainnight);
                                    break;
                                case "10d":
                                    layout.setBackgroundResource(R.drawable.showerrainday);
                                    break;
                                case "10n":
                                    layout.setBackgroundResource(R.drawable.showerrainnight);
                                    break;
                                case "11d":
                                    layout.setBackgroundResource(R.drawable.thunderstormday);
                                    break;
                                case "11n":
                                    layout.setBackgroundResource(R.drawable.thunderstormnight);
                                    break;
                                case "13d":
                                    layout.setBackgroundResource(R.drawable.snowday);
                                    break;
                                case "13n":
                                    layout.setBackgroundResource(R.drawable.snownight);
                                    break;
                                case "50d":
                                    layout.setBackgroundResource(R.drawable.mistday);
                                    break;
                                case "50n":
                                    layout.setBackgroundResource(R.drawable.mistnight);
                                    break;
                                default:
                                    layout.setBackgroundResource(R.drawable.backgrounddark);
                            }


                            //FIRST DAY
                            JSONObject firstDay = daily.getJSONObject(1);
                            JSONObject tempFirstDay = firstDay.getJSONObject("temp");
                            JSONArray weatherFirstDay = firstDay.getJSONArray("weather");
                            JSONObject weatherFirstOBJ = weatherFirstDay.getJSONObject(0);

                            String temperaturesFirstDay = df.format(parseDouble(tempFirstDay.getString("max")))+ "/" + df.format(parseDouble(tempFirstDay.getString("min")));
                            String weatherFDay = weatherFirstOBJ.getString("main");
                            String iconFirstDay = weatherFirstOBJ.getString("icon");
                            String dtFirst = firstDay.getString("dt");
                            String dtF= truncateTemp(conseguirDiaDeSemana(dtFirst),4);

                            textViewTemperaturesFirstDay.setText(temperaturesFirstDay+ "°C");
                            textViewWeatherFirstDay.setText(weatherFDay);
                            textViewFirstDay.setText(dtF);

                            String urlIconFirstDay = "https://openweathermap.org/img/wn/"+iconFirstDay+"@2x.png";
                            Glide.with(getApplicationContext()).load(urlIconFirstDay).into(iFirstDay);

                            //SECOND DAY
                            JSONObject secondDay = daily.getJSONObject(2);
                            JSONObject tempSecondDay = secondDay.getJSONObject("temp");
                            JSONArray weatherSecondDay = secondDay.getJSONArray("weather");
                            JSONObject weatherSecondOBJ = weatherSecondDay.getJSONObject(0);

                            String temperaturesSecondDay = df.format(parseDouble(tempSecondDay.getString("max")))+ "/" + df.format(parseDouble(tempSecondDay.getString("min")));
                            String weatherSDay = weatherSecondOBJ.getString("main");
                            String iconSecondDay = weatherSecondOBJ.getString("icon");
                            String dtSecond = secondDay.getString("dt");
                            String dtS= truncateTemp(conseguirDiaDeSemana(dtSecond),4);


                            textViewTemperaturesSecondDay.setText(temperaturesSecondDay+ "°C");
                            textViewWeatherSecondDay.setText(weatherSDay);
                            textViewSecondDay.setText(dtS);

                            String urlIconSecondDay = "https://openweathermap.org/img/wn/"+iconSecondDay+"@2x.png";
                            Glide.with(getApplicationContext()).load(urlIconSecondDay).into(iSecondDay);

                            //THIRD DAY
                            JSONObject thirdDay = daily.getJSONObject(3);
                            JSONObject tempThirdDay = thirdDay.getJSONObject("temp");
                            JSONArray weatherThirdDay = thirdDay.getJSONArray("weather");
                            JSONObject weatherThirdOBJ = weatherThirdDay.getJSONObject(0);


                            String temperaturesThirdDay = df.format(parseDouble(tempThirdDay.getString("max")))+ "/" + df.format(parseDouble(tempThirdDay.getString("min")));
                            String weatherTDay = weatherThirdOBJ.getString("main");
                            String iconThirdDay = weatherThirdOBJ.getString("icon");
                            String dtThird = thirdDay.getString("dt");
                            String dtT= truncateTemp(conseguirDiaDeSemana(dtThird),4);

                            textViewTemperaturesThirdDay.setText(temperaturesThirdDay+ "°C");
                            textViewWeatherThirdDay.setText(weatherTDay);
                            textViewThirdDay.setText(dtT);

                            String urlIconThirdDay = "https://openweathermap.org/img/wn/"+iconThirdDay+"@2x.png";
                            Glide.with(getApplicationContext()).load(urlIconThirdDay).into(iThirdDay);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }

    public static String truncateTemp(String value, int length) {
        length = value.length()==5? length : length-1;
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String conseguirDiaDeSemana(String dt){
        int fechaEpoch = Integer.parseInt(dt);
        System.out.println(fechaEpoch);
        Date date = new Date(fechaEpoch);
        Instant fecha = Instant.ofEpochSecond(fechaEpoch);
        LocalDate fecha2 = fecha.atZone(ZoneOffset.UTC).toLocalDate();
        System.out.println(fecha2);
        DayOfWeek dayOfWeek = fecha2.getDayOfWeek();
        System.out.println(dayOfWeek);
        String word =dayOfWeek.toString();
        word = word.charAt(0) + word.substring(1).toLowerCase();

        return word;
    }

    public void goError(){
        Intent intentGoError = new Intent(this, Error.class);
        startActivity(intentGoError);
    }

    public void error(){
        finish();
        goError();
    }



}