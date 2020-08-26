package edu.uci.ics.lillih1.service.api_gateway.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.lillih1.service.api_gateway.models.idm.*;
import edu.uci.ics.lillih1.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.lillih1.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
@Path("idm")
public class IDMEndpoints {
    @Path("register")
    @OPTIONS
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest0(String jsonText) {
        return Response.status(Status.NO_CONTENT)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }


    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to register user.");

        ServiceLogger.LOGGER.info("jonText: " + jsonText);

        RegisterUserRequestModel requestModel;
        try {
            requestModel = (RegisterUserRequestModel) ModelValidator.verifyModel(jsonText, RegisterUserRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RegisterUserResponseModel.class);
        }

        ObjectMapper mapper = new ObjectMapper();
        String text = null;
        try {
            text = mapper.writeValueAsString(requestModel);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("REQUEST: " + text);


        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserRegister());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setEmail(requestModel.getEmail());

        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to login user.");

        ServiceLogger.LOGGER.info("Request: \n" + jsonText);

        LoginUserRequestModel requestModel;
        try {
            requestModel = (LoginUserRequestModel) ModelValidator.verifyModel(jsonText, LoginUserRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e,LoginUserResponseModel.class);
        }

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserLogin());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setEmail(requestModel.getEmail());
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .header("transactionID", transactionID)
                .build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to verify session.");
        SessionRequestModel requestModel;
        try {
            requestModel = (SessionRequestModel) ModelValidator.verifyModel(jsonText, SessionRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, SessionResponseModel.class);
        }

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setMethod(HttpMethod.POST);

        cr.setEmail(headers.getHeaderString("email"));
        cr.setSessionID(headers.getHeaderString("sessionID"));

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .header("transactionID", transactionID)
                .build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(@Context HttpHeaders headers,String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to check privilege.");
        PrivilegeRequestModel requestModel;
        try {
            requestModel = (PrivilegeRequestModel) ModelValidator.verifyModel(jsonText, PrivilegeRequestModel .class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, PrivilegeResponseModel.class);
        }

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserPrivilegeVerify());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);

        cr.setEmail(headers.getHeaderString("email"));
        cr.setSessionID(headers.getHeaderString("sessionID"));
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .header("transactionID", transactionID)
                .build();
    }
}
