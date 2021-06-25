package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

// information for one movie in a details screen
@Parcel
public class Movie {

    public static final String MOVIE_TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=%s&language=en-US";

    Integer id;
    Double voteAverage;
    String backdropPath;
    String posterPath;
    String title;
    String overview;

    public static final String TAG = "Movie";

    public Movie() {}

    // method exception signature so whoever calls Movie has to deal with possible exceptions
    public Movie(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        voteAverage = jsonObject.getDouble("vote_average");
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
    }

    // iterates through JsonArray and constructs a Movie for each
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public static void playTrailer() {

    }

    public Integer getId() {
        return id;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() { // fetching all available sizes with API append to base url and add posterPath
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
