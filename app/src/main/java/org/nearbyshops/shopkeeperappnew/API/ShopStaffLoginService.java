package org.nearbyshops.shopkeeperappnew.API;


import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.ModelRoles.UserEndpoint;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 30/8/17.
 */


public interface ShopStaffLoginService {



    @PUT("/api/v1/User/ShopStaffLogin/UpdateProfileStaff")
    Call<ResponseBody> updateProfileStaff(
            @Header("Authorization") String headers,
            @Body User user
    );








    @PUT("/api/v1/User/ShopStaffLogin/{UserID}")
    Call<ResponseBody> updateStaffByAdmin(
            @Header("Authorization") String headers,
            @Body User user,
            @Path("UserID") int userID
    );





    @PUT("/api/v1/User/ShopStaffLogin/UpdateStaffLocation")
    Call<ResponseBody> updateStaffLocation(
            @Header("Authorization") String headers,
            @Body ShopStaffPermissions permissions
    );








    @GET("/api/v1/User/ShopStaffLogin/GetShopStaffForShopAdmin")
    Call<UserEndpoint> getStaffForAdmin(
            @Header("Authorization") String headers,
            @Query("latCurrent") Double latPickUp, @Query("lonCurrent") Double lonPickUp,
            @Query("PermitProfileUpdate") Boolean permitProfileUpdate,
            @Query("PermitRegistrationAndRenewal") Boolean permitRegistrationAndRenewal,
            @Query("Gender") Boolean gender,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData
    );


}
