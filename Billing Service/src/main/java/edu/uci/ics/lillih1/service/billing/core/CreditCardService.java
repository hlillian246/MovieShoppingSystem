package edu.uci.ics.lillih1.service.billing.core;

import edu.uci.ics.lillih1.service.billing.BillingService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.glassfish.grizzly.http.util.Header.Date;

public class CreditCardService {

    public static CreditCardInsertResponseModel insertService(CreditCardInsertRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Credit card insert service");

        String query = "select id from creditcards where id = ?;";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getId());

        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return new CreditCardInsertResponseModel(325, "Duplicate insertion.");
        }

        String query2 = "insert into creditcards(id, firstName, lastName, expiration) VALUES (?,?,?,?);";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getId());
        ps.setString(2, requestModel.getFirstName());
        ps.setString(3, requestModel.getLastName());
        java.sql.Date sqlDate = java.sql.Date.valueOf(requestModel.getExpiration());
        ps.setDate(4, sqlDate);

        ps.execute();

        return new CreditCardInsertResponseModel(3200, "Credit card inserted successfully.");
    }


    public static CreditCardUpdateResponseModel updateService(CreditCardUpdateRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Credit card update service");

        String query = "select id from creditcards where id = ?;";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CreditCardUpdateResponseModel(324, "Credit card does not exist.");
        }

        String query2 = "update creditcards set firstName = ?, lastName = ?, expiration = ? where id = ?;";
        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getFirstName());
        ps.setString(2, requestModel.getLastName());
        java.sql.Date sqlDate = java.sql.Date.valueOf(requestModel.getExpiration());
        ps.setDate(3, sqlDate);
        ps.setString(4, requestModel.getId());
        ps.execute();

        return new CreditCardUpdateResponseModel(3210, "Credit card updated successfully.");
    }


    public static CreditCardDeleteResponseModel deleteService(CreditCardDeleteRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Credit card delete service");

        String query = "select id from creditcards where id = ?;";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CreditCardDeleteResponseModel(324, "Credit card does not exist.");
        }

        String query2 = "delete from creditcards where id = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getId());
        ps.execute();

        return new CreditCardDeleteResponseModel(3220, "Credit card deleted successfully.");
    }


    public static CreditCardRetrieveResponseModel retrieveService(CreditCardRetrieveRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Credit card retrieve service");

        String query2 = "select id, firstName, lastName, expiration from creditcards where id = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getId());
        ResultSet rs = ps.executeQuery();

        if (!rs.next())
        {
            return new CreditCardRetrieveResponseModel(324, "Credit card does not exist.");
        }

        CreditCardModel creditcard = new CreditCardModel();
        creditcard.setId(rs.getString("id"));
        creditcard.setFirstName(rs.getString("firstName"));
        creditcard.setLastName(rs.getString("lastName"));

        creditcard.setExpiration(rs.getDate("expiration").toLocalDate());

        return new CreditCardRetrieveResponseModel(3230, "Credit card retrieved successfully.", creditcard);
    }

}
