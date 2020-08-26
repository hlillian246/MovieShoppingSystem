package edu.uci.ics.lillih1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.idm.core.PrivilegeRecords;
import edu.uci.ics.lillih1.service.idm.core.RegisterRecords;
import edu.uci.ics.lillih1.service.idm.models.PrivilegeRequestModel;
import edu.uci.ics.lillih1.service.idm.models.PrivilegeResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class PrivilegePage {
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response VerifyPrivilege(String jsonText)
    {
        ObjectMapper mapper = new ObjectMapper();
        PrivilegeRequestModel requestModel= null;
        PrivilegeResponseModel responseModel = null;
        try {
            requestModel = mapper.readValue(jsonText, PrivilegeRequestModel.class);

            if (requestModel.getPlevel() > 5 || requestModel.getPlevel() < 0)
            {
                responseModel = new PrivilegeResponseModel(-14, "Privilege level out of valid range.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!RegisterPage.validInputFormat(requestModel.getEmail(), EMAIL_REGEX))
            {
                responseModel = new PrivilegeResponseModel(-11 ,"Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (requestModel.getEmail() == null || requestModel.getEmail().isEmpty() || requestModel.getEmail().length() > 50)
            {
                responseModel = new PrivilegeResponseModel(-10 ,"Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!RegisterRecords.emailExist(requestModel.getEmail()))
            {
                responseModel = new PrivilegeResponseModel(14, "User not found.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else {
                responseModel = PrivilegeRecords.verifyPrivilege(requestModel);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
        }
        catch(JsonParseException e)
        {
            responseModel = new PrivilegeResponseModel(-3,"JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(JsonMappingException e)
        {
            responseModel = new PrivilegeResponseModel(-2,"JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(Exception e)
        {
            responseModel = new PrivilegeResponseModel(-1,"Internal Server Error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
}
