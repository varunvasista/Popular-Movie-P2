package org.codeselect.movieproject1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.codeselect.adapter.TrailerAdapter;
import org.codeselect.database.DataHelper;
import org.codeselect.http.MovieService;
import org.codeselect.model.Movie;
import org.codeselect.model.Review;
import org.codeselect.model.ReviewResult;
import org.codeselect.model.Trailer;
import org.codeselect.model.Youtube;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#getInstance(Movie)} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements TrailerAdapter.ListItemClickListener, View.OnClickListener {


    private static final String ARG_EXTRA = "movie_data";
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private Movie mMovie;
    private TextView mTitleView;
    private TextView mReleaseView;
    private TextView mDetailView;
    private TextView mRatingView;
    private TextView mEmptyTrailerView;
    private RecyclerView mTrailerListView;
    private LinearLayout mReviewContainer;
    private Button favouriteBtn;
    private ImageView mThumbView;
    private MovieService service;
    private TrailerAdapter mAdapter;


    public static MovieDetailFragment getInstance(Movie item) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EXTRA, item);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable(ARG_EXTRA);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mReleaseView = (TextView) rootView.findViewById(R.id.release_date);
        mDetailView = (TextView) rootView.findViewById(R.id.overview);
        mRatingView = (TextView) rootView.findViewById(R.id.rating);
        mEmptyTrailerView = (TextView) rootView.findViewById(R.id.empty_trailer);
        mTrailerListView = (RecyclerView) rootView.findViewById(R.id.trailer_recyclerview);
        mReviewContainer = (LinearLayout) rootView.findViewById(R.id.review_container);
        mThumbView = (ImageView) rootView.findViewById(R.id.thumb);
        favouriteBtn = (Button) rootView.findViewById(R.id.favourite);
        mAdapter = new TrailerAdapter(getContext(), null);
        mAdapter.setListItemClickListener(this);
        favouriteBtn.setOnClickListener(this);
        if (getCurrentSortOrder() == MovieListFragment.FAVOURITE) {
            favouriteBtn.setVisibility(View.GONE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTrailerListView.setLayoutManager(layoutManager);
        mTrailerListView.setAdapter(mAdapter);
        setDataInView();

        return rootView;
    }

    private void setDataInView() {
        if (mMovie == null) {
            return;
        }
        if (mMovie.getOriginalTitle() != null && !mMovie.getOriginalTitle().isEmpty()) {
            mTitleView.setText(mMovie.getOriginalTitle());
        } else {
            mTitleView.setText(Api.NOT_AVAILABLE);
        }
        if (mMovie.getReleaseDate() != null && !mMovie.getReleaseDate().isEmpty()) {
            mReleaseView.setText(mMovie.getReleaseDate());
        } else {
            mReleaseView.setText(Api.NOT_AVAILABLE);
        }
        if (mMovie.getOverview() != null && !mMovie.getOverview().isEmpty()) {
            mDetailView.setText(mMovie.getOverview());
        } else {
            mDetailView.setText(Api.NOT_AVAILABLE);
        }

        String rating = String.format("%.2f/10", mMovie.getRating());
        mRatingView.setText(rating);
        Context context = getContext();
        if (context != null) {
            Glide.with(getContext()).load(Api.IMAGE_BASE_URL + mMovie.getImage())
                    .error(R.drawable.no_thumb)
                    .placeholder(R.drawable.no_thumb)
                    .into(mThumbView);
        }


        // fetch Trailers and set in view
        Call<Trailer> call = service.getTrailers(String.valueOf(mMovie.getId()));
        call.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                Trailer body = response.body();
                if (body != null) {
                    List<Youtube> trailerList = body.getResults();
                    mAdapter.refereshAdapter(trailerList);
                    showHideEmptyTrailerView();
                }

            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Log.d(TAG, "Error");
                showHideEmptyTrailerView();
            }
        });

        //fetch reviews and set in view  // we show maximum of 2 reviews
        Call<ReviewResult> callReview = service.getReviews(String.valueOf(mMovie.getId()));
        callReview.enqueue(new Callback<ReviewResult>() {
            @Override
            public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                ReviewResult result = response.body();
                List<Review> reviews = result.getResults();
                inflateReviewList(reviews);
            }

            @Override
            public void onFailure(Call<ReviewResult> call, Throwable t) {
                Log.d(TAG, "Error");
                addEmptyView();
            }
        });
    }

    public int getCurrentSortOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getInt(MovieListFragment.SORT_BY, MovieListFragment.POPULARITY);
    }

    private void inflateReviewList(List<Review> reviews) {
        if (reviews.size() == 0) {
            addEmptyView();
            return;
        }
        for (int i = 0; i < reviews.size(); i++) {
            Context context = getContext();
            if (context == null) {
                return;
            }
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            String reviewText = "";
            reviewText += "<b>" + (i + 1) + "</b>" + ".<br/>" + "<b>" + getString(R.string.auther_prefix) + "</b>" + reviews.get(i).getAuthor();
            reviewText += "<br/>" + "<b>" + getString(R.string.comment_prefix) + "</b>" + reviews.get(i).getContent();
            textView.setText(Html.fromHtml(reviewText));

            mReviewContainer.addView(textView);
            if (i == 1) {
                break;
            }
        }
    }

    private void showHideEmptyTrailerView() {
        if (mAdapter.getItemCount() > 0) {
            mEmptyTrailerView.setVisibility(View.GONE);
        } else {
            mEmptyTrailerView.setVisibility(View.VISIBLE);
        }
    }

    // Adds empty view for review
    private void addEmptyView() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        String reviewText = getString(R.string.no_review_available);
        textView.setGravity(Gravity.CENTER);
        textView.setText(reviewText);
        mReviewContainer.addView(textView);
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + mAdapter.getItem(position).getKey()));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.no_application_found);
            builder.setPositiveButton(R.string.ok, null);
            builder.show();
        }
    }

    @Override
    public void onClick(View v) {
       DataHelper.getInstance(getContext()).addToFavourite(mMovie);
        Toast.makeText(getContext(), String.format(getString(R.string.added_to_fav),mMovie.getTitle()), Toast.LENGTH_SHORT).show();
    }
}
