package org.nearbyshops.shopkeeperappnew.API;

import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.Model.ItemCategory;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ItemCategoryEndPoint;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


/**
 * Created by sumeet on 2/4/16.
 */

public interface ItemCategoryService {



    @POST("/api/v1/ItemCategory")
    Call<ItemCategory> insertItemCategory(@Body ItemCategory itemCategory);

    @PUT("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> updateItemCategory(@Body ItemCategory itemCategory, @Path("id") int id);

    @DELETE("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> deleteItemCategory(@Path("id") int id);


    @PUT("/api/v1/ItemCategory/")
    Call<ResponseBody> updateItemCategoryBulk(@Body List<ItemCategory> itemCategoryList);




    @GET("api/v1/ItemCategory/QuerySimple")
    Call<ItemCategoryEndPoint> getItemCategoriesQuerySimple(
            @Query("ParentID") Integer parentID,
            @Query("IsDetached") Boolean parentIsNull,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset
    );


    @GET("api/v1/ItemCategory")
    Call<ItemCategoryEndPoint> getItemCategories(
            @Query("ShopID") Integer shopID,
            @Query("ParentID") Integer parentID, @Query("IsDetached") Boolean parentIsNull,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );



    @GET("/api/v1/ItemCategory/{id}")
    Call<ItemCategory> getItemCategory(@Path("id") int ItemCategoryID);




    // Deprecated

    @GET("/api/v1/ItemCategory/Deprecated")
    Call<List<ItemCategory>> getItemCategories(@Query("ParentID") int parentID, @Query("ShopID") int shopID);

    @GET("/api/v1/ItemCategory/Deprecated")
    Call<List<ItemCategory>> getItemCategories(@Query("ParentID") int parentID);



}
