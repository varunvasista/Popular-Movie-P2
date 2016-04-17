package org.codeselect.http;

import org.codeselect.model.ReviewResult;
import org.codeselect.model.Trailer;
import org.codeselect.movieproject1.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by CodeMyMobile on 06-03-2016.
 */
public interface MovieService {

    @GET("movie/{id}/videos?api_key="+ Api.API_KEY)
    Call<Trailer> getTrailers(@Path("id") String id);

    @GET("movie/{id}/reviews?api_key="+ Api.API_KEY)
    Call<ReviewResult> getReviews(@Path("id") String id);
}
