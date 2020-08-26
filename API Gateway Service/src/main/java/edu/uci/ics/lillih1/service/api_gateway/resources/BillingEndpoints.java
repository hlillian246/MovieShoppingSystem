package edu.uci.ics.lillih1.service.api_gateway.resources;

import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.core.UserSessionValidation;
import edu.uci.ics.lillih1.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.lillih1.service.api_gateway.models.billing.*;
import edu.uci.ics.lillih1.service.api_gateway.models.idm.SessionResponseModel;
import edu.uci.ics.lillih1.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.lillih1.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert card.");
        CartInsertRequestModel requestModel;
        try {
            requestModel = (CartInsertRequestModel) ModelValidator.verifyModel(jsonText, CartInsertRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CartInsertResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "transactionID, delay")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        try {

            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "transactionID, delay")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "transactionID, delay")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();

        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartInsert());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to update cart.");
        CartUpdateRequestModel requestModel;
        try {
            requestModel = (CartUpdateRequestModel) ModelValidator.verifyModel(jsonText, CartUpdateRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CartUpdateResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "transactionID, delay")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartUpdate());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to delete cart.");
        CartDeleteRequestModel requestModel;
        try {
            requestModel = (CartDeleteRequestModel) ModelValidator.verifyModel(jsonText, CartDeleteRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CartDeleteResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartDelete());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve cart.");
        CartRetrieveRequestModel requestModel;
        try {
            requestModel = (CartRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CartRetrieveRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CartRetrieveResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        ServiceLogger.LOGGER.info("email: " + email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartRetrieve());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to clear cart.");
        CartClearRequestModel requestModel;
        try {
            requestModel = (CartClearRequestModel) ModelValidator.verifyModel(jsonText, CartClearRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CartClearResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        ServiceLogger.LOGGER.info("email: " + email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartClear());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("cart/clear")
    @OPTIONS
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest0 (String jsonText) {
        return Response.status(Status.OK)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to credit card insert.");
        CreditCardInsertRequestModel requestModel;
        try {
            requestModel = (CreditCardInsertRequestModel) ModelValidator.verifyModel(jsonText, CreditCardInsertRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CreditCardInsertResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ServiceLogger.LOGGER.info("creditcard/insert sessoinID: " + sessionID);

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcInsert());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to credit card update.");
        CreditCardUpdateRequestModel requestModel;
        try {
            requestModel = (CreditCardUpdateRequestModel) ModelValidator.verifyModel(jsonText, CreditCardUpdateRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CreditCardUpdateResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .entity(response).build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcUpdate());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to credit card delete.");
        CreditCardDeleteRequestModel requestModel;
        try {
            requestModel = (CreditCardDeleteRequestModel) ModelValidator.verifyModel(jsonText, CreditCardDeleteRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CreditCardDeleteResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcDelete());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to credit card retrieve.");
        CreditCardRetrieveRequestModel requestModel;
        try {
            requestModel = (CreditCardRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRetrieveRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CreditCardRetrieveRequestModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcRetrieve());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to customer insert.");
        CustomerInsertRequestModel requestModel;
        try {
            requestModel = (CustomerInsertRequestModel) ModelValidator.verifyModel(jsonText, CustomerInsertRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CustomerInsertResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerInsert());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request for customer update.");
        CustomerUpdateRequestModel requestModel;
        try {
            requestModel = (CustomerUpdateRequestModel) ModelValidator.verifyModel(jsonText, CustomerUpdateRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CustomerUpdateResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerUpdate());// set the request model
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to customer retrieve.");
        CustomerRetrieveRequestModel requestModel;
        try {
            requestModel = (CustomerRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CustomerRetrieveRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, CustomerRetrieveResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerRetrieve());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("Received request to place order.");
        OrderPlaceRequestModel requestModel;
        try {
            requestModel = (OrderPlaceRequestModel) ModelValidator.verifyModel(jsonText, OrderPlaceRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, OrderPlaceResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderPlace());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }


    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, @QueryParam("email") String email) {

        ServiceLogger.LOGGER.info("Received request to retrieve order.");

        String sessionID = headers.getHeaderString("sessionID");
        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderRetrieve());

        Map<String,Object> queryParams = new HashMap<String,Object>();
        queryParams.put("email", email);
        cr.setQueryParams(queryParams);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }
}
