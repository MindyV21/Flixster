package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

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

//        // resolve the view objects
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvOverview = (TextView) findViewById(R.id.tvOverview);
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        // unwrap movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        // set average by dividing by 2
        float voteAvg = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAvg / 2.0f);


    }
}