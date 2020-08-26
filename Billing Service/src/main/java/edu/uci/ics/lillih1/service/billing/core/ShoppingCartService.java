package edu.uci.ics.lillih1.service.billing.core;

import edu.uci.ics.lillih1.service.billing.BillingService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartService {

    public static CartInsertResponseModel insertService(CartInsertRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Shopping cart insert service");

        String query = "select id from carts where email = ? and movieId = ?";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getEmail());
        ps.setString(2, requestModel.getMovieId());

        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return new CartInsertResponseModel(311, "Duplicate insertion.");
        }

        String query2 = "insert into carts(email, movieId, quantity) VALUES (?,?,?);";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getEmail());
        ps.setString(2, requestModel.getMovieId());
        ps.setInt(3, requestModel.getQuantity());
        ps.execute();

        return new CartInsertResponseModel(3100, "Shopping cart item inserted successfully.");
    }

    public static CartUpdateResponseModel updateService(CartUpdateRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Shopping cart update service");

        String query = "select id from carts where email = ? and movieId = ?";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getEmail());
        ps.setString(2, requestModel.getMovieId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CartUpdateResponseModel(312, "Shopping item does not exist.");
        }

        String query2 = "update carts set quantity = ? where email = ? and movieId = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setInt(1, requestModel.getQuantity());
        ps.setString(2, requestModel.getEmail());
        ps.setString(3, requestModel.getMovieId());
        ps.execute();

        return new CartUpdateResponseModel(3110, "Shopping cart item updated successfully.");
    }

    public static CartDeleteResponseModel deleteService(CartDeleteRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Shopping cart delete service");

        String query = "select id from carts where email = ? and movieId = ?";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getEmail());
        ps.setString(2, requestModel.getMovieId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CartDeleteResponseModel(312, "Shopping item does not exist.");
        }

        String query2 = "delete from carts where email = ? and movieId = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getEmail());
        ps.setString(2, requestModel.getMovieId());
        ps.execute();

        return new CartDeleteResponseModel(3120, "Shopping cart item deleted successfully.");
    }


    public static CartRetrieveResponseModel retrieveService(CartRetrieveRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Shopping cart retrieve service");

//        String query = "select id from carts where email = ?";
//
//        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
//        ps.setString(1, requestModel.getEmail());
//
//        ResultSet rs = ps.executeQuery();
//        if (!rs.next())
//        {
//            return new CartRetrieveResponseModel(312, "Shopping item does not exist.");
//        }

        String query = "select email, movieId, quantity from carts where email = ?;";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getEmail());
        ResultSet rs = ps.executeQuery();


        List<CartItemModel> items = new ArrayList<CartItemModel>();

        int count = 0;
        while (rs.next())
        {
            CartItemModel item = new CartItemModel();
            item.setEmail(rs.getString("email"));
            item.setMovieId(rs.getString("movieId"));
            item.setQuantity(rs.getInt("quantity"));
            items.add(item);
            count++;
        }

        if (count == 0)
        {
            return new CartRetrieveResponseModel(312, "Shopping item does not exist.");
        }

        return new CartRetrieveResponseModel(3130, "Shopping cart retrieved successfully.", items);
    }


    public static CartClearResponseModel clearService(CartClearRequestModel requestModel) throws Exception {
        ServiceLogger.LOGGER.info("Shopping cart clear service");
        deleteCartItems(requestModel.getEmail());

        return new CartClearResponseModel(3140, "Shopping cart cleared successfully.");
    }


    public static void deleteCartItems(String email) throws Exception {
        ServiceLogger.LOGGER.info("Delete cart items for email: " + email);

        String query = "delete from carts where email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, email);
        ps.execute();
    }
}
