package edu.uci.ics.lillih1.service.api_gateway.threadpool;

import edu.uci.ics.lillih1.service.api_gateway.models.RequestModel;

import javax.ws.rs.HttpMethod;
import java.util.Map;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;
    private String method;
    private Map<String,Object> queryParams;
    private Map<String,Object> pathParam;

    public ClientRequest() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, Object> getPathParam() {
        return pathParam;
    }

    public void setPathParam(Map<String, Object> pathParam) {
        this.pathParam = pathParam;
    }
}
