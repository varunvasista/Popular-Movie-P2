package org.codeselect.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.codeselect.movieproject1.Api;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CodeMyMobile on 06-02-2016
 */

public class Movie implements Parcelable {
    private int id;
    private String originalTitle;
    private String title;
    private String image;
    private String overview;
    private double rating;
    private String releaseDate;

    public String getImage() {
        return image;
    }

    public Movie() {
    }

    public Movie(JSONObject movie) throws JSONException{
        this.id = movie.getInt(Api.JSON.ID);
        this.originalTitle = movie.getString(Api.JSON.ORIGINAL_TITLE);
        this.title = movie.getString(Api.JSON.TITLE);
        this.image = movie.getString(Api.JSON.POSTER_IMAGE);
        this.overview = movie.getString(Api.JSON.OVERVIEW);
        this.rating = movie.getDouble(Api.JSON.RATING);
        this.releaseDate = movie.getString(Api.JSON.RELEASE_DATE);
    }

    private Movie(Parcel parcel) {
        id = parcel.readInt();
        originalTitle = parcel.readString();
        title = parcel.readString();
        overview = parcel.readString();
        releaseDate = parcel.readString();
        rating = parcel.readDouble();
        image = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String title) {
        this.originalTitle = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(rating);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
