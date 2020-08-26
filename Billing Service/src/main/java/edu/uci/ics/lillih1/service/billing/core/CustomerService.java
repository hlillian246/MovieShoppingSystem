package edu.uci.ics.lillih1.service.billing.core;

import edu.uci.ics.lillih1.service.billing.BillingService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;
import edu.uci.ics.lillih1.service.billing.resources.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerService {

    public static CustomerInsertResponseModel insertService (CustomerInsertRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Customer insert service");

        String query = "select id from creditcards where id = ?;";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getCcId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CustomerInsertResponseModel(331, "Credit card ID not found.");
        }

        String query1 = "select email from customers where email = ?;";

        ps = BillingService.getCon().prepareStatement(query1);
        ps.setString(1, requestModel.getEmail());

        rs = ps.executeQuery();
        if (rs.next())
        {
            return new CustomerInsertResponseModel(333, "Duplicate insertion.");
        }

        String query2 = "insert into customers(email, firstName, lastName, ccID, address) VALUES (?,?,?,?,?);";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getEmail());
        ps.setString(2, requestModel.getFirstName());
        ps.setString(3, requestModel.getLastName());
        ps.setString(4,requestModel.getCcId());
        ps.setString(5,requestModel.getAddress());
        ps.execute();

        return new CustomerInsertResponseModel(3300, "Customer inserted successfully.");
    }

    public static CustomerUpdateResponseModel updateService(CustomerUpdateRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Customer update service");

        String query = "select id from creditcards where id = ?;";

        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getCcId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CustomerUpdateResponseModel(331, "Credit card ID not found.");
        }

        String query1 = "select email from customers where email = ?;";
        ps = BillingService.getCon().prepareStatement(query1);
        ps.setString(1, requestModel.getEmail());

        rs = ps.executeQuery();
        if (!rs.next())
        {
            return new CustomerUpdateResponseModel(332, "Customer does not exist.");
        }

        String query2 = "update customers set firstName = ?, lastName = ?, ccId = ?, address = ? where email = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1,requestModel.getFirstName());
        ps.setString(2,requestModel.getLastName());
        ps.setString(3,requestModel.getCcId());
        ps.setString(4,requestModel.getAddress());
        ps.setString(5,requestModel.getEmail());
        ps.execute();

        return new CustomerUpdateResponseModel(3310,"Customer updated successfully.");
    }

    public static CustomerRetrieveResponseModel retrieveService(CustomerRetrieveRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Customer retrieve service");

        String query = "select email, firstName, lastName, ccId, address from customers where email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1,requestModel.getEmail());
        ResultSet rs = ps.executeQuery();

        if (!rs.next())
        {
            return new CustomerRetrieveResponseModel(332, "Customer does not exist.");
        }

        CustomerModel customer = new CustomerModel();
        customer.setEmail(rs.getString("email"));
        customer.setFirstName(rs.getString("firstName"));
        customer.setLastName(rs.getString("lastName"));
        customer.setCcId(rs.getString("ccId"));
        customer.setAddress(rs.getString("address"));

        return new CustomerRetrieveResponseModel(3320,"Customer retrieved successfully.", customer);
    }


}
