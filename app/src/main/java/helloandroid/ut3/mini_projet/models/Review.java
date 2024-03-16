package helloandroid.ut3.mini_projet.models;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Review {

    Date date;
    List<String> photos;
    float rating;
    String restaurant_id;
    String review;
    String username;

    public Review() {}
    public Review(Date date, List<String> photos, float rating, String restaurant_id, String review, String username) {
        this.date = date;
        this.photos = photos;
        this.rating = rating;
        this.restaurant_id = restaurant_id;
        this.review = review;
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        return "["+restaurant_id+"] "+formatter.format(date) +" by "+username+" : "+review+" "+rating+"/5.0";
    }
}
