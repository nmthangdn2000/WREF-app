package thang.com.wref.Retrofits;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import thang.com.wref.Models.HarvesthelperModel;

public interface HarvesthelperRetrofit {
    @GET("api/harvesthelper/{id}")
    Call<HarvesthelperModel> getByID(
            @Header("Authorization") String auth,
            @Path("id") String id
    );
}
