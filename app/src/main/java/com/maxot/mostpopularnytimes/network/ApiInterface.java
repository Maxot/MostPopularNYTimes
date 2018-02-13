package com.maxot.mostpopularnytimes.network;



import com.maxot.mostpopularnytimes.model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("{type}/{section}/{time-period}.json")
    Call<ArticleResponse> getArticles(@Path("type") String most,@Path("section") String section, @Path("time-period") int timePeriod, @Query("api_key") String apiKey);
}
