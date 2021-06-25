package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

// screen for movie details
public class MovieDetailsActivity extends YouTubeBaseActivity {

    public static final String MOVIE_TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=%s&language=en-US";
    public static final String TAG = "MovieDetails";

    // the movie to display
    Movie movie;
    String movieKey = "";

//    // to view objects
//    TextView tvTitle;
//    TextView tvOverview;
//    RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // viewholder
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        // unwrap movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        String imageUrl = movie.getBackdropPath();
        int placeholder = R.drawable.flicks_backdrop_placeholder;

        // instantiate youtube
        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        // request for movie youtube trailer key
        String API_KEY = getString(R.string.themoviedb_api_key);
        String url = String.format(MOVIE_TRAILER_URL, movie.getId(), API_KEY);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

                    // get first instance in array of movie IDs if possible
                    if (results.length() != 0) {
                        movieKey = results.getJSONObject(0).getString("key");

                        // instantiate movie trailer
                        initMovieTrailer(youTubePlayerView);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure: " + throwable.getMessage());
            }
        });

        // set average by dividing by 2
        float voteAvg = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAvg / 2.0f);
    }

    // initializes the specified movie trailer to play
    public void initMovieTrailer (YouTubePlayerView youTubePlayerView) {
        youTubePlayerView.initialize(getString(R.string.youtube_api_key),
            new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {
                    Log.d("YOUTUBE", "onSuccess");
                    // do any work here to cue video, play video, etc.
                    youTubePlayer.cueVideo(movieKey);
                }
                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d("YOUTUBE", "onFailure: " + youTubeInitializationResult.toString());
                }
            });
    }
}