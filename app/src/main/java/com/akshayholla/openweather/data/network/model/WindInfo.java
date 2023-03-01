package com.akshayholla.openweather.data.network.model;

import com.google.gson.annotations.SerializedName;

public class WindInfo {
    public float getSpeed() {
        return speed;
    }

    public float getAngle() {
        return angle;
    }

    @SerializedName("speed")
    float speed;
    @SerializedName("deg")
    float angle;
}