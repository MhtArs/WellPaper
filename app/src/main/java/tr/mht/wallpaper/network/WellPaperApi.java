package tr.mht.wallpaper.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;
import tr.mht.wallpaper.model.PhotosResponse;

/**
 * Created by Administrator on 8.2.2016.
 */
public class WellPaperApi {
    public static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    public static final String FLICKR_API_KEY = "2faec029f64c5d12f1847171411b4aad";
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //2015-01-18 15:48:56

    public static FlickrApi getApi() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        return retrofit.create(FlickrApi.class);
    }

    public interface FlickrApi {
        @GET("?method=flickr.interestingness.getList&api_key=" + FLICKR_API_KEY + "&extras=description%2Cowner_name%2Ctags%2Co_dims%2Cviews%2Cmedia%2Curl_o&per_page=50&format=json&nojsoncallback=1")
        Call<PhotosResponse> getInterestingPhotos(@Query("page") int page, @Query("per_page") int per_page);

        @GET("?method=flickr.photos.getRecent&api_key=" + FLICKR_API_KEY + "&extras=description%2Cowner_name%2Ctags%2Co_dims%2Cviews%2Cmedia%2Curl_o&per_page=50&format=json&nojsoncallback=1")
        Call<PhotosResponse> getRecentPhotos(@Query("page") int page, @Query("per_page") int per_page);
    }
}
