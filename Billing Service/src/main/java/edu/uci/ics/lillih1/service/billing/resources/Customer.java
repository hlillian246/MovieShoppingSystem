package edu.uci.ics.lillih1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.billing.core.CustomerService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;
import edu.uci.ics.lillih1.service.billing.utils.Utils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("customer")
public class Customer {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";
    private final String CC_REGEX = "^[0-9]+$";


    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String jsonText) {
        ServiceLogger.LOGGER.info("Received customer insert request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CustomerInsertRequestModel requestModel = null;
        CustomerInsertResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CustomerInsertRequestModel.class);
            String ccId = requestModel.getCcId();

            if (!Utils.validCCFormat(ccId))
            {
                responseModel = new CustomerInsertResponseModel(321, "Credit card ID has invalid length.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if (!Utils.validInputFormat(ccId, CC_REGEX))
            {
                responseModel = new CustomerInsertResponseModel(322, "Credit card ID has invalid value.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

            responseModel = CustomerService.insertService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CustomerInsertResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CustomerInsertResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CustomerInsertResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received customer update request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CustomerUpdateRequestModel requestModel = null;
        CustomerUpdateResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CustomerUpdateRequestModel.class);
            String ccId = requestModel.getCcId();
            if (!Utils.validCCFormat(ccId))
            {
                responseModel = new CustomerUpdateResponseModel(321, "Credit card ID has invalid length.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if (!Utils.validInputFormat(ccId, CC_REGEX))
            {
                responseModel = new CustomerUpdateResponseModel(322, "Credit card ID has invalid value.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

            responseModel = CustomerService.updateService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CustomerUpdateResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CustomerUpdateResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CustomerUpdateResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }

    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(String jsonText) {
        ServiceLogger.LOGGER.info("Received customer retrieve request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CustomerRetrieveRequestModel requestModel = null;
        CustomerRetrieveResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try
        {
            requestModel = mapper.readValue(jsonText, CustomerRetrieveRequestModel.class);
            responseModel = CustomerService.retrieveService(requestModel);
            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CustomerRetrieveResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CustomerRetrieveResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CustomerRetrieveResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }

    }



}
