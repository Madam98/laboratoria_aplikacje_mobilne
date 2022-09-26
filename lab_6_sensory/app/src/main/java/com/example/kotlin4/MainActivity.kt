package com.example.kotlin4

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), SensorEventListener, LocationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        temperature = findViewById(R.id.temperatureText)
        light = findViewById(R.id.lightText)
        geoLocation1 = findViewById(R.id.geoLocationText1)
        geoLocation2 = findViewById(R.id.geoLocationText2)
        position = findViewById(R.id.postionText)
        humidity = findViewById(R.id.humidityText)
        pressure = findViewById(R.id.pressureText)
        setUpSensor()
        setUpLocation()
        timer = timer("Temperatura",daemon = false,period = TimeUnit.MINUTES.toMillis(1)){
            notifyTemp()
        }
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    var brightness: Sensor? = null
    var hotness: Sensor? = null
    var rotations: Sensor? = null
    var wetness: Sensor? = null
    var push: Sensor? = null
    var timer: Timer? = null
    private lateinit var temperature: TextView
    private lateinit var light: TextView
    private lateinit var geoLocation1: TextView
    private lateinit var geoLocation2: TextView
    private lateinit var position: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView
    var currTemp: Float = 0f

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, hotness, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, rotations, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, wetness, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, push, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer?.purge()
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event:SensorEvent){
        if(event.sensor?.type== Sensor.TYPE_AMBIENT_TEMPERATURE){
            currTemp = event.values[0]
            temperature.text = event.values[0].toString()
        }
        if(event.sensor?.type== Sensor.TYPE_ACCELEROMETER){
            position.text = "X: "+event.values[0]+" Y: "+event.values[1]+" Z: "+event.values[2]
        }
        if(event.sensor?.type== Sensor.TYPE_LIGHT){
            light.text = event.values[0].toString()
        }
        if(event.sensor?.type== Sensor.TYPE_PRESSURE){
            pressure.text = event.values[0].toString()
        }
        if(event.sensor?.type== Sensor.TYPE_RELATIVE_HUMIDITY){
            humidity.text = event.values[0].toString()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onLocationChanged(p0: Location) {
        geoLocation1.text = p0.latitude.toString()
        geoLocation2.text = p0.longitude.toString()
    }

    private fun setUpSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        hotness = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        rotations = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        wetness = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        push = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }

    private fun setUpLocation(){
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),2)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    private fun notifyTemp(){
        val channelId = "i.apps.notifications"
        val description = "description"
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Temperatura")
                .setContentText("Obecna temperatura urządzenia: $currTemp")
        }
        else{
            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Temperatura")
                .setContentText("Obecna temperatura urządzenia: $currTemp")
        }
        notificationManager.notify(1234,builder.build())
    }
}