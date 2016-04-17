package org.codeselect.movieproject1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.codeselect.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "movie_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        Movie item = intent.getParcelableExtra(EXTRA_DATA);

        if (savedInstanceState == null) {
            Fragment fragment = MovieDetailFragment.getInstance(item);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, null).commit();
        }

    }
}
