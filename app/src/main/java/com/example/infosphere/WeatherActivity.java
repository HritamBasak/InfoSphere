package com.example.infosphere;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.infosphere.databinding.ActivityWeatherBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherActivity extends AppCompatActivity {
    ActivityWeatherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LocalDateTime current = null;
        String formatted=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            current = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, EEEE, HH:mm:ss"); // Format pattern
            formatted = current.format(formatter);
        }
        binding.dateShow.setText(formatted);
        binding.getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
//                getDataHourly();
            }
        });
    }
//    public void getDataHourly() {
//        String api = "e1c5d1523fe242d4bdf086e2f888ce85";
//        String city = binding.etCity.getText().toString();
//        if (city.isEmpty()) {
//            Toast.makeText(WeatherActivity.this, "City Name cannot be empty!", Toast.LENGTH_SHORT).show();
//        } else {
//            String url = "https://pro.openweathermap.org/data/2.5/forecast/hourly?q="+city+"&appid="+api;
//            RequestQueue queue = Volley.newRequestQueue(this);
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try{
//                                JSONArray array=response.getJSONArray("list");
//                                for(int i=0;i<array.length();i++)
//                                {
//                                    JSONObject obj=array.getJSONObject(i);
//                                    String time = obj.getString("dt_txt");
//                                    String sub_time=time.substring(time.indexOf(" ")+1,time.length());
//
//                                    JSONObject mainObject = obj.getJSONObject("main");
//                                    double temp = mainObject.getDouble("temp");
//                                    double temp_in_celcius = temp - 273.15;
//                                    temp = Double.parseDouble(String.format("%.2f", temp_in_celcius));
//
//                                    JSONArray weatherArray = obj.getJSONArray("weather");
//                                    JSONObject conditionObject = weatherArray.getJSONObject(0);
//                                    String condition = conditionObject.getString("main");
//
//                                    switch (i)
//                                    {
//                                        case 0:
//                                            binding.timeOne.setText(sub_time);
//                                            binding.tempOne.setText(Double.toString(temp)+"°C");
//                                            setDrawable(binding.first,condition);
//                                            break;
//                                        case 1:
//                                            binding.timeTwo.setText(sub_time);
//                                            binding.tempTwo.setText(Double.toString(temp)+"°C");
//                                            setDrawable(binding.first,condition);
//                                            break;
//                                        case 2:
//                                            binding.timeThree.setText(sub_time);
//                                            binding.tempThree.setText(Double.toString(temp)+"°C");
//                                            setDrawable(binding.first,condition);
//                                            break;
//                                        case 3:
//                                            binding.timeFour.setText(sub_time);
//                                            binding.tempFour.setText(Double.toString(temp)+"°C");
//                                            setDrawable(binding.first,condition);
//                                            break;
//                                    }
//
//                                }
//                            }
//                             catch (JSONException e) {
//                                Log.e("WeatherActivity", "JSON parsing error: " + e.getMessage());
//                                Toast.makeText(WeatherActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
//                                Toast.makeText(WeatherActivity.this, "Sorry,We could not find the city's data!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e("WeatherActivity", "Error: " + error.getMessage());
//                                Toast.makeText(WeatherActivity.this, "Network error", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//            queue.add(jsonObjectRequest);
//        }
//    }
//    public void setDrawable(ImageView imageView,String condition)
//    {
//        switch (condition)
//        {
//            case "Rain":
//                imageView.setImageResource(R.drawable.rain);
//                break;
//            case "Clouds":
//                imageView.setImageResource(R.drawable.clouds);
//                break;
//            case "Clear":
//                imageView.setImageResource(R.drawable.clear);
//                break;
//            default:
//                imageView.setImageResource(R.drawable.resize_weather);
//                break;
//        }
//    }
    public void getData() {
        String api = "e1c5d1523fe242d4bdf086e2f888ce85";
        String city = binding.etCity.getText().toString();if (city.isEmpty()) {
            Toast.makeText(WeatherActivity.this, "City Name cannot be empty!", Toast.LENGTH_SHORT).show();
        } else {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + api;
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Main weather data
                                JSONObject jsonObject = response.getJSONObject("main");
                                double temp = jsonObject.getDouble("temp");
                                double temp_in_celcius = temp - 273.15;
                                temp = Double.parseDouble(String.format("%.2f", temp_in_celcius));
                                binding.temp.setText(Double.toString(temp) + "°C");

                                double feels_like = jsonObject.getDouble("feels_like");
                                double feels_like_in_celcius = feels_like - 273.15;
                                feels_like = Double.parseDouble(String.format("%.2f", feels_like_in_celcius));
                                binding.feelsLiketemp.setText(Double.toString(feels_like) + "°C");

                                double humidity = jsonObject.getDouble("humidity");
                                binding.hum.setText(Double.toString(humidity) + "%");

                                double pressure = jsonObject.getDouble("pressure");
                                binding.pre.setText(Double.toString(pressure) + "hPa");

                                // Weather condition and description
                                JSONArray jsonArray = response.getJSONArray("weather");
                                JSONObject weatherobj = jsonArray.getJSONObject(0);
                                String cond = weatherobj.getString("main");
                                String desc = weatherobj.getString("description");
                                String optimized_desc=Character.toUpperCase(desc.charAt(0))+desc.substring(1);
                                binding.condition.setText(cond);
                                binding.description.setText(optimized_desc);

                                // Wind data
                                JSONObject jsonObject1 = response.getJSONObject("wind");
                                double speed = jsonObject1.getDouble("speed");
                                binding.spe.setText(Double.toString(speed) + "m/s");

                                // Gust data (check if available)
                                if (jsonObject1.has("gust")) {
                                    double gust = jsonObject1.getDouble("gust");
                                    binding.gus.setText(Double.toString(gust) + "m/s");
                                } else {
                                    binding.gus.setText("N/A");
                                }

                                // Visibility
                                int visibility = response.getInt("visibility");
                                binding.vis.setText(Integer.toString(visibility) + "m");

//                                JSONObject jsonObject2=response.getJSONObject("sys");
//                                String country=jsonObject2.getString("country");
//                                binding.country.setText(country);

                                switch (cond)
                                {
                                    case "Rain":
                                        binding.image.setImageResource(R.drawable.rain);
                                        break;
                                    case "Clear":
                                        binding.image.setImageResource(R.drawable.clear);
                                        break;
                                    case "Clouds":
                                        binding.image.setImageResource(R.drawable.clouds);
                                        break;
                                    case "Haze":
                                        binding.image.setImageResource(R.drawable.fog);
                                        break;
                                    case "Snow":
                                        binding.image.setImageResource(R.drawable.snowhouse);
                                        break;
                                }
                            } catch (JSONException e) {
                                Log.e("WeatherActivity", "JSON parsing error: " + e.getMessage());
                                Toast.makeText(WeatherActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error.networkResponse!=null && error.networkResponse.statusCode==404)
                            {
                                Toast.makeText(WeatherActivity.this, "Sorry,We could not find the city's data!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.e("WeatherActivity", "Error: " + error.getMessage());
                                Toast.makeText(WeatherActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            queue.add(jsonObjectRequest);
        }
    }
}