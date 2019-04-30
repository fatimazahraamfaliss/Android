package com.example.like.miniprojetandroid;

import java.util.Date;

public class UserInformation {

    public String name;
    public String url;
    public String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public float rating;
  //  public Date date;
    public double latitude;
    public double longitude;

    public UserInformation(){

    }
    public UserInformation(String name, double latitude, double longitude, String url,String desc,float rating){
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.url = url;
        this.desc = desc;
        this.rating = rating;
      //  this.date = date;



    }

    public String toString() {
        return name + " " + desc + " " + longitude + " Lat: " + longitude + " Lng: " + rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() { return url; }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}