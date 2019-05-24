package org.nearbyshops.shopkeeperappnew.API;

import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.Model.ShopItem;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ShopItemEndPoint;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 14/3/16.
 */
public interface ShopItemService {


    @POST("/api/v1/ShopItem")
    Call<ResponseBody> postShopItem(@Header("Authorization") String headers,
                                    @Body ShopItem shopItem);

    @PUT("/api/v1/ShopItem/UpdateByShop")
    Call<ResponseBody> putShopItem(@Header("Authorization") String headers,
                                   @Body ShopItem shopItem);

    @DELETE("/api/v1/ShopItem")
    Call<ResponseBody> deleteShopItem(@Header("Authorization") String headers,
                                      @Query("ShopID") int ShopID, @Query("ItemID") int itemID);


    @GET("/api/v1/ShopItem")
    Call<ShopItemEndPoint> getShopItemEndpoint(
            @Query("ItemCategoryID") Integer ItemCategoryID,
            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly,
            @Query("GetExtras") Boolean getExtras
    );




    @GET("/api/v1/ShopItem/ForShop")
    Call<ShopItemEndPoint> getShopItemsForShop(
            @Query("ItemCategoryID") Integer ItemCategoryID,
            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );


    // bulk update methods


    @POST("/api/v1/ShopItem/CreateBulk")
    Call<ResponseBody> createShopItemBulk(@Header("Authorization") String headers,
                                          @Body List<ShopItem> itemList);

    @POST("/api/v1/ShopItem/DeleteBulk")
    Call<ResponseBody> deleteShopItemBulk(@Header("Authorization") String headers,
                                          @Body List<ShopItem> itemList);





    // Deprecated methods


    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(@Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
                                      @Query("ItemCategoryID") Integer itemCategoryID);



    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(@Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
                                      @Query("ItemCategoryID") Integer itemCategoryID,
                                      @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero);


    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(@Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
                                      @Query("ItemCategoryID") Integer itemCategoryID,
                                      @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
                                      @Query("SortBy") String sortBy,
                                      @Query("Limit") Integer limit, @Query("Offset") Integer offset
    );

}
