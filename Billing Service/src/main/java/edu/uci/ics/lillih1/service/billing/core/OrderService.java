package edu.uci.ics.lillih1.service.billing.core;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import edu.uci.ics.lillih1.service.billing.BillingService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;
import edu.uci.ics.lillih1.service.billing.resources.ShoppingCart;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class OrderService {


    public static String clientId = "AcpcpAKvVyDxaKZYsio-_YFhF45txb2QA8pDeNJDnfOdNvTyb9FJ10zlwOGCyhn39Ccn_eimeFmw6TZi";
    public static String clientSecret = "EMM6NvCvcm6o8mnSUzUxccV9yQQg-DZeLWypU4COF3S4VZOHM5T9NRsmFqdHoFTyDbhd0dDNi3g5uA5h";


    public static OrderPlaceResponseModel orderPlaceService(OrderPlaceRequestModel requestModel) throws Exception {
        ServiceLogger.LOGGER.info("Order place service");

        String query = "select email from customers where email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getEmail());
        ServiceLogger.LOGGER.info(ps.toString());
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return new OrderPlaceResponseModel(332, "Customer does not exist.");
        }

        String query2 = "select c.movieId as movieId, c.quantity as quantity, " +
                "p.unit_price as price, p.discount as discount " +
                "from carts c, movie_prices p where c.movieId = p.movieId and c.email = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, requestModel.getEmail());
        ServiceLogger.LOGGER.info(ps.toString());
        rs = ps.executeQuery();

        List<CartItemModel> items = new ArrayList<CartItemModel>();

        double total = 0;
        while (rs.next()) {
            CartItemModel item = new CartItemModel();
            item.setMovieId(rs.getString("movieId"));
            item.setQuantity(rs.getInt("quantity"));
            items.add(item);

            total += (rs.getFloat("price") * rs.getFloat("discount") * item.getQuantity());
        }

        if (items.size() == 0) {
            return new OrderPlaceResponseModel(341, "Shopping cart for this customer not found.");
        }

        ServiceLogger.LOGGER.info("Total amount:" + total);

        return createPayment(requestModel.getEmail(), items, total);
    }


    private static OrderPlaceResponseModel createPayment(String email, List<CartItemModel> items, double total) throws Exception
    {
        Amount amount = new Amount();
        amount.setCurrency("USD");

        String sum = String.format("%.2f", total);
        ServiceLogger.LOGGER.info("Sum:" + sum);

        amount.setTotal(sum);

        List<Transaction> transactions = new ArrayList<Transaction>();
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        URI uri = UriBuilder.fromUri(BillingService.getConfigs().getScheme() + BillingService.getConfigs().getHostName())
                    .port(BillingService.getConfigs().getPort())
                    .path(BillingService.getConfigs().getPath() + "/order/complete").build();

        ServiceLogger.LOGGER.info("Return URL: " + uri.toString());

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(uri.toString());
        redirectUrls.setCancelUrl(uri.toString());
        payment.setRedirectUrls(redirectUrls);

        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");

        ServiceLogger.LOGGER.info("Prepare Paypal call...");

        Payment createdPayment = payment.create(apiContext);

        ServiceLogger.LOGGER.info("Done Paypal call...");

        if (createdPayment == null)
        {
            ServiceLogger.LOGGER.info("Create Payment is null: ");
            return new OrderPlaceResponseModel(342, "Create payment failed.");
        }

        String redirectUrl = "";
        String token = "";
        int index = -1;

        List<Links> links = createdPayment.getLinks();

        ServiceLogger.LOGGER.info("Payment created");

        for (Links link : links)
        {
            if (link.getRel().equals("approval_url"))
            {
                redirectUrl = link.getHref();
                ServiceLogger.LOGGER.info("redirectUrl: " + redirectUrl);

                String symbol = "token=";
                index = redirectUrl.indexOf(symbol);
                if (index != -1)
                {
                    token = redirectUrl.substring(index + symbol.length());
                }

                ServiceLogger.LOGGER.info("Token: " + token);
                break;
            }
        }

        if (index == -1)
        {
            ServiceLogger.LOGGER.info("Token doesn't exist.");
            return new OrderPlaceResponseModel(342, "Create payment failed.");
        }

        String query3 = "{CALL insert_sales_transactions(?,?,?,?)}";
        CallableStatement stmt = BillingService.getCon().prepareCall(query3);

        stmt.setString(1, email);
        stmt.setString(4, token);

        for (CartItemModel item : items)
        {
            stmt.setString(2, item.getMovieId());
            stmt.setInt(3, item.getQuantity());
            stmt.execute();
        }

        ShoppingCartService.deleteCartItems(email);

        return new OrderPlaceResponseModel(3400, "Order placed successfully.", redirectUrl, token);
    }


    public static OrderCompleteResponseModel orderCompleteService(String token, String paymentId, String payerId) throws Exception
    {
        ServiceLogger.LOGGER.info("Order complete service");

        ServiceLogger.LOGGER.info("token: " + token);
        ServiceLogger.LOGGER.info("paymentId: " + paymentId);
        ServiceLogger.LOGGER.info("payerId: " + payerId);

        String query = "select count(*) as count from transactions where token = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, token);

        ResultSet rs = ps.executeQuery();

        if (!rs.next())
        {
            return new OrderCompleteResponseModel(3421, "Token not found.");
        }

        ServiceLogger.LOGGER.info("Found items: " + rs.getInt("count"));

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
        Payment createdPayment = payment.execute(apiContext, paymentExecution);

        if (createdPayment == null)
        {
            return new OrderCompleteResponseModel(3422, "Payment can not be completed.");
        }

        String transactionId = createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();

        ServiceLogger.LOGGER.info("transactionId: " + transactionId);

        String query2 = "UPDATE transactions SET transactionId = ? WHERE token = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, transactionId);
        ps.setString(2, token);
        ps.execute();

        return new OrderCompleteResponseModel(3420,"Payment is completed successfully.");
    }



    public static OrderRetrieveResponseModel orderRetrieveService (String email) throws Exception
    {
        ServiceLogger.LOGGER.info("Order retrieve service");

        String query = "select email from customers where email = ?;";
        PreparedStatement ps = BillingService.getCon().prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (!rs.next())
        {
            return new OrderRetrieveResponseModel(332, "Customer does not exist.");
        }

        String query2 = "select distinct t.transactionId as transactionId from transactions t, sales s " +
                "where t.sId = s.id and t.transactionId is not null and email = ?;";

        ps = BillingService.getCon().prepareStatement(query2);
        ps.setString(1, email);
        rs = ps.executeQuery();

        List<TransactionModel> transactions = new ArrayList<TransactionModel>();

        while (rs.next())
        {
            TransactionModel transaction = new TransactionModel();
            transaction.setTransactionId(rs.getString("transactionId"));
            transactions.add(transaction);
        }

        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");

        for (TransactionModel t : transactions)
        {
            ServiceLogger.LOGGER.info("Transaction ID: " + t.getTransactionId());


            Sale sale = Sale.get(apiContext, t.getTransactionId());

            t.setState(sale.getState());
            t.setCreate_time(sale.getCreateTime());
            t.setUpdate_time(sale.getUpdateTime());

            AmountModel amount = new AmountModel(sale.getAmount().getTotal(), sale.getAmount().getCurrency());
            t.setAmount(amount);

            TransactionFee transactionFee = new TransactionFee(sale.getTransactionFee().getValue(),sale.getTransactionFee().getCurrency());
            t.setTransaction_fee(transactionFee);

            String query3 = "select s.movieId as movieId, s.quantity as quantity, s.saleDate as saleDate, " +
                                "m.unit_price as price, m.discount as discount " +
                                "from transactions t, sales s, movie_prices m " +
                                "where t.sId = s.id and s.movieId = m.movieId and t.transactionId = ?;";
            ps = BillingService.getCon().prepareStatement(query3);
            ps.setString(1, t.getTransactionId());
            rs  = ps.executeQuery();

            List<OrderItemModel> items = new ArrayList<OrderItemModel>();
            while (rs.next())
            {
                OrderItemModel item = new OrderItemModel();

                item.setEmail(email);
                item.setMovieId(rs.getString("movieId"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnit_price(rs.getFloat("price"));
                item.setDiscount(rs.getFloat("discount"));
                item.setSaleDate(rs.getDate("saleDate").toLocalDate());
                items.add(item);
            }

            t.setItems(items);
        }

        return new OrderRetrieveResponseModel(3410, "Orders retrieved successfully.", transactions);
    }
}
