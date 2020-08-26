package edu.uci.ics.lillih1.service.api_gateway.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.api_gateway.models.idm.SessionRequestModel;
import edu.uci.ics.lillih1.service.api_gateway.models.idm.SessionResponseModel;
import edu.uci.ics.lillih1.service.api_gateway.threadpool.ClientRequest;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

public class UserSessionValidation {

    public static SessionResponseModel userSessionValidation(String email, String sessionID) throws Exception
    {
        ServiceLogger.LOGGER.info("email: " + email + ", sessionID: " + sessionID);

        ClientRequest cr = new ClientRequest();

        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());

        SessionRequestModel requestModel = new SessionRequestModel();
        requestModel.setEmail(email);
        requestModel.setSessionID(sessionID);

        cr.setRequest(requestModel);
        cr.setMethod(HttpMethod.POST);

        Response response = DispatchClientRequest.sendMacroServiceRequest(cr, -1);

        ServiceLogger.LOGGER.info("Received status: " + response.getStatus());

        String jsonText = response.readEntity(String.class);
        ServiceLogger.LOGGER.info("jsonText: " + jsonText);

        ObjectMapper mapper = new ObjectMapper();

        SessionResponseModel responseModel = mapper.readValue(jsonText, SessionResponseModel.class);

        return responseModel;
    }
}
