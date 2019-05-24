package org.nearbyshops.shopkeeperappnew.MarketDetail.API;


import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.ModelReviewMarket.FavouriteMarket;
import org.nearbyshops.shopkeeperappnew.ModelReviewMarket.FavouriteMarketEndpoint;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 2/4/16.
 */



public interface FavouriteMarketService {



    @GET("/api/v1/FavouriteMarket")
    Call<FavouriteMarketEndpoint> getFavouriteMarkets(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @POST("/api/v1/FavouriteMarket")
    Call<FavouriteMarket> insertFavouriteItem(
            @Header("Authorization") String headers,
            @Body FavouriteMarket item
    );

    @DELETE("/api/v1/FavouriteMarket")
    Call<ResponseBody> deleteFavouriteItem(@Header("Authorization") String headers,
                                           @Query("ItemID") Integer itemID,
                                           @Query("EndUserID") Integer endUserID);

}
