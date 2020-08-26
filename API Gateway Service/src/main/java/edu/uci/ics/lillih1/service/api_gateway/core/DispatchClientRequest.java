package edu.uci.ics.lillih1.service.api_gateway.core;

import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.api_gateway.threadpool.ClientRequest;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DispatchClientRequest {

    public static void dispatchClientRequest(ClientRequest request, int id) throws Exception {
        Response response = sendMacroServiceRequest(request, id);
        persistResponse(request, id, response);
    }

    public static Response sendMacroServiceRequest(ClientRequest request, int id) throws Exception
    {
        ServiceLogger.LOGGER.info("Thread " + id + " sending request to micro services.");

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

        if (request.getPathParam() != null)
        {
            webTarget = webTarget.resolveTemplates(request.getPathParam());
        }

        if (request.getQueryParams() != null) {
            for (String key : request.getQueryParams().keySet()) {
                webTarget = webTarget.queryParam(key, request.getQueryParams().get(key));
            }
        }

        ServiceLogger.LOGGER.info("URI: " + webTarget.getUri());

        // Create an InvocationBuilder to create the HTTP request
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("email", request.getEmail());
        invocationBuilder.header("sessionID", request.getSessionID());
        invocationBuilder.header("transactionID", request.getTransactionID());

        Response response = null;
        if (request.getMethod() == HttpMethod.GET)
        {
            // Send the request and save it to a Response
            response = invocationBuilder.get();
        }
        else if (request.getMethod() == HttpMethod.POST)
        {
            // Send the request and save it to a Response
            response = invocationBuilder.post(Entity.entity(request.getRequest(),MediaType.APPLICATION_JSON));
        }
        else if (request.getMethod() == HttpMethod.PUT)
        {
            // Send the request and save it to a Response
            response = invocationBuilder.put(Entity.entity(request.getRequest(),MediaType.APPLICATION_JSON));
        }
        else if (request.getMethod() == HttpMethod.DELETE)
        {
            // Send the request and save it to a Response
            response = invocationBuilder.delete();
        }
        else
        {
            ServiceLogger.LOGGER.info("Unsupported method: " + request.getMethod());
            throw new RuntimeException("Unsupported method: " + request.getMethod());
        }

        return response;
    }


    private static void persistResponse(ClientRequest request, int id, Response response) throws Exception
    {
//        ServiceLogger.LOGGER.info("Received status: " + response.getStatus());

        String jsonText = response.readEntity(String.class);
        ServiceLogger.LOGGER.info("Thread " + id + " got response jsonText: " + jsonText);

        Connection con = null;
        try
        {
            con = GatewayService.getConPool().requestCon();
            if (con == null)
            {
                ServiceLogger.LOGGER.info("ERROR: Can't get connection.");
                return;
            }

            String query = "INSERT INTO responses VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, request.getTransactionID());
            ps.setString(2, request.getEmail());
            ps.setString(3, request.getSessionID());
            ps.setString(4, jsonText);
            ps.setInt(5, response.getStatus());
            ps.execute();
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.info("Exception happened. ");
            throw e;
        }
        finally {
            GatewayService.getConPool().releaseCon(con);
        }
    }
}
