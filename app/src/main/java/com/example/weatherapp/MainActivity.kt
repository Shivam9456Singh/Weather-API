package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.DateFormat
import java.util.Calendar
// 9ee15678b0bba0bfb151e4321420ab3c
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
       val retrofit = Retrofit.Builder()
           .addConverterFactory(GsonConverterFactory.create())
           .baseUrl("https://api.openweathermap.org/data/2.5/")
           .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData("jaipur", "9ee15678b0bba0bfb151e4321420ab3c", "metric" )
        response.enqueue(object : Callback<WeatherApp>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Your existing code to set UI elements
                        val temperature = responseBody.main.temp.toString()
                        val humidity = responseBody.main.humidity
                        val windSpeed = responseBody.wind.speed
                        val sunRise = responseBody.sys.sunrise
                        val sunSet = responseBody.sys.sunset
                        val seaLevel = responseBody.main.pressure
                        val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                        val maxTemp = responseBody.main.temp_max
                        val minTemp = responseBody.main.temp_min
                        Log.d("TAG", "onResponse: $temperature")
                        binding.temp.text = "$temperature °C"
                        binding.weather.text = condition
                        binding.maxTemp.text = "Max temp: $maxTemp °C"
                        binding.minTemp.text = "Min temp: $minTemp °C"
                        binding.humidity.text = "$humidity %"
                        binding.windSpeed.text = "$windSpeed m/s"
                        binding.sunRise.text = "$sunRise"
                        binding.sunSet.text = "$sunSet"
                        binding.sea.text = "$seaLevel hPa"

                    } else {
                        Log.e("WeatherApp", "Response body is null")
                    }
                } else {
                    Log.e("WeatherApp", "Response unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Log.e("WeatherApp", "Failed to fetch weather data: ${t.message}")
                // You might want to show a toast or a dialog to inform the user about the error.
            }

        })

    }
}