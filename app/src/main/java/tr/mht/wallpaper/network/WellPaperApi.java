package tr.mht.wallpaper.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import tr.mht.wallpaper.model.Image;

/**
 * Created by Administrator on 8.2.2016.
 */
public class WellPaperApi {
    public static final String ENDPOINT = "https://api.unsplash.com";
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //2015-01-18 15:48:56

    public static UnsplashApi getApi() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        return retrofit.create(UnsplashApi.class);
    }

    public interface UnsplashApi {
        @GET("/photos/?client_id=98be6dadeb17e350c65a879dc72775e91adf62caa6b581d1dd8f0834bd072ab7")
        Call<List<Image>> getImages();
    }
}
