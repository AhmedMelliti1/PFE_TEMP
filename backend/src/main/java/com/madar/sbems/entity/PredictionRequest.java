package com.madar.sbems.entity;

public class PredictionRequest {

    private double air_temperature;
    private double dew_temperature;
    private int hour;
    private int dayofweek;
    private int month;
    private int is_weekend;
    private double cons_h_1;
    private double cons_h_24;
    private double conso_moy_6h;
    private double conso_moy_24h;

    // OBLIGATOIRE
    public PredictionRequest() {
    }

    // Getters & Setters OBLIGATOIRES
    public double getAir_temperature() {
        return air_temperature;
    }

    public void setAir_temperature(double air_temperature) {
        this.air_temperature = air_temperature;
    }

    public double getDew_temperature() {
        return dew_temperature;
    }

    public void setDew_temperature(double dew_temperature) {
        this.dew_temperature = dew_temperature;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(int dayofweek) {
        this.dayofweek = dayofweek;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getIs_weekend() {
        return is_weekend;
    }

    public void setIs_weekend(int is_weekend) {
        this.is_weekend = is_weekend;
    }

    public double getCons_h_1() {
        return cons_h_1;
    }

    public void setCons_h_1(double cons_h_1) {
        this.cons_h_1 = cons_h_1;
    }

    public double getCons_h_24() {
        return cons_h_24;
    }

    public void setCons_h_24(double cons_h_24) {
        this.cons_h_24 = cons_h_24;
    }

    public double getConso_moy_6h() {
        return conso_moy_6h;
    }

    public void setConso_moy_6h(double conso_moy_6h) {
        this.conso_moy_6h = conso_moy_6h;
    }

    public double getConso_moy_24h() {
        return conso_moy_24h;
    }

    public void setConso_moy_24h(double conso_moy_24h) {
        this.conso_moy_24h = conso_moy_24h;
    }
}
