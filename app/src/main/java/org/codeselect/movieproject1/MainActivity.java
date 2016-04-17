package org.codeselect.movieproject1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.codeselect.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieListFragment.ShowDetailListener{


    private boolean mDualPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDualPane = (findViewById(R.id.detail_container) != null);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MovieListFragment.getInstance(), null)
                    .commit();
        }

        if (mDualPane) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_container);
            if (fragment != null) {
                findViewById(R.id.empty_detail).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onShowDetail(Movie item) {
        if (mDualPane) {
            Fragment fragment = MovieDetailFragment.getInstance(item);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragment, null).commit();
        }else{
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_DATA, item);
            startActivity(intent);
        }
    }
}
