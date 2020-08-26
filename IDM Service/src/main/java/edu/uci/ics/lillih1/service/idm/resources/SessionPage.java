package edu.uci.ics.lillih1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.idm.core.RegisterRecords;
import edu.uci.ics.lillih1.service.idm.core.SessionRecords;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.SessionRequestModel;
import edu.uci.ics.lillih1.service.idm.models.SessionResponseModel;
import edu.uci.ics.lillih1.service.idm.security.Token;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class SessionPage {
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response VerifySession(String jsonText)
    {
        ObjectMapper mapper = new ObjectMapper();
        SessionRequestModel requestModel = null;
        SessionResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, SessionRequestModel.class);

            if (requestModel.getSessionID() != null && requestModel.getSessionID().length() != 128)
            {
                responseModel = new SessionResponseModel(-13,
                        "Token has invalid length.", requestModel.getSessionID());
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!RegisterPage.validInputFormat(requestModel.getEmail(), EMAIL_REGEX))
            {
                responseModel = new SessionResponseModel(-11 ,"Email address has invalid format.", requestModel.getSessionID());
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (requestModel.getEmail() == null || requestModel.getEmail().isEmpty() || requestModel.getEmail().length() > 50)
            {
                responseModel = new SessionResponseModel(-10 ,"Email address has invalid length.", requestModel.getSessionID());
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!RegisterRecords.emailExist(requestModel.getEmail()))
            {
                responseModel = new SessionResponseModel(14, "User not found.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else
            {
                responseModel = SessionRecords.verifySession(requestModel);

//                ServiceLogger.LOGGER.info("Responded sessionID: " + responseModel.getSessionID());

                String text = mapper.writeValueAsString(responseModel);
                ServiceLogger.LOGGER.info("RESPONSE: " + text);

                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
        }
        catch(JsonParseException e)
        {
            responseModel = new SessionResponseModel(-3,"JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(JsonMappingException e)
        {
            responseModel = new SessionResponseModel(-2,"JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            responseModel = new SessionResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
}
