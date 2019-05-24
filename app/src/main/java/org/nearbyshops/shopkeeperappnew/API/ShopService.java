package org.nearbyshops.shopkeeperappnew.API;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.Model.Image;
import org.nearbyshops.shopkeeperappnew.Model.Shop;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ShopEndPoint;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {


    @POST("/api/v1/Shop")
    Call<Shop> postShop(@Header("Authorization") String headers,
                        @Body Shop shop);


    @PUT("/api/v1/Shop/UpdateBySelf")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                    @Body Shop shop);


    @DELETE("/api/v1/Shop/{id}")
    Call<ResponseBody> deleteShop(@Header("Authorization") String headers,
                                  @Path("id") int id);



    @GET("/api/v1/Shop")
    Call<ShopEndPoint> getShopEndpoint(
            @Query("DistributorID") Integer distributorID,
            @Query("LeafNodeItemCategoryID") Integer itemCategoryID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly);


    @GET("/api/v1/Shop/{id}")
    Call<Shop> getShop(@Path("id") int id);



    @GET("/api/v1/Shop/GetForShopAdmin")
    Call<Shop> getShopForShopAdmin(@Header("Authorization") String headers);




    // Image Calls

    @POST("/api/v1/Shop/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/Shop/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);

}
