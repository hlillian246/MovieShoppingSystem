package edu.uci.ics.lillih1.service.idm.core;

import edu.uci.ics.lillih1.service.idm.IDMService;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.SessionRequestModel;
import edu.uci.ics.lillih1.service.idm.models.SessionResponseModel;
import edu.uci.ics.lillih1.service.idm.security.Session;
import edu.uci.ics.lillih1.service.idm.security.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionRecords {
    public static SessionResponseModel verifySession(SessionRequestModel requestModel) throws Exception
    {
        SessionResponseModel responseModel = null;
        try
        {
//            updateSession(requestModel.getEmail(), requestModel.getSessionID());

            String query = "SELECT status, timeCreated, lastUsed, exprTime FROM sessions WHERE email = ? and sessionID = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getEmail());
            ps.setString(2,requestModel.getSessionID());

            ResultSet rs = ps.executeQuery();
//            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
            {
                int status = rs.getInt("status");
                if (status == 2)
                {
                    responseModel = new SessionResponseModel(132,"Session is closed.");
                    return responseModel;
                }
                else if (status == 3)
                {
                    responseModel = new SessionResponseModel(131,"Session is expired.");
                    return responseModel;
                }
                else if (status == 4)
                {
                    responseModel = new SessionResponseModel(133,"Session is revoked.");
                    return responseModel;
                }

                Timestamp exprTime = rs.getTimestamp("exprTime");
                Timestamp lastUsed = rs.getTimestamp("lastUsed");
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

//                ServiceLogger.LOGGER.info("exp: " + exprTime.getTime());
//                Timestamp act = new Timestamp(exprTime.getTime() + Session.SESSION_TIMEOUT);
//
//                ServiceLogger.LOGGER.info("act: " + act.getTime());
//

                if (currentTime.after(exprTime))
                {
                    setSessionStatus(requestModel.getSessionID(), "EXPIRED");
                    responseModel = new SessionResponseModel(131,"Session is expired.");
                    return responseModel;
                }
                else if (currentTime.after(new Timestamp(lastUsed.getTime() + Session.SESSION_TIMEOUT)))
                {
                    setSessionStatus(requestModel.getSessionID(), "REVOKED");
                    responseModel = new SessionResponseModel(133,"Session is revoked.");
                    return responseModel;
                }
                else if (currentTime.after(new Timestamp(exprTime.getTime() - Session.SESSION_TIMEOUT)))
                {
                    setSessionStatus(requestModel.getSessionID(), "REVOKED");
                    String sessionID = insertNewSession(requestModel.getEmail());

                    responseModel = new SessionResponseModel(130,"Session is active.", sessionID);
                    return responseModel;
                }
                else
                {
                    String sessionID = requestModel.getSessionID();
                    updateSessionLastUsed(sessionID);

                    ServiceLogger.LOGGER.info("SESSION ID: " + sessionID);

                    responseModel = new SessionResponseModel(130,"Session is active.", sessionID);
                    return responseModel;
                }
            }
            else
            {
                responseModel = new SessionResponseModel(134,"Session not found.");
                return responseModel;
            }
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve string records.");
            e.printStackTrace();

            throw e;
        }
    }

    public static String insertNewSession(String email) throws SQLException
    {
        Session session = Session.createSession(email);

        String insert = "INSERT INTO sessions(sessionID,email,status,timeCreated,lastUsed,exprTime) " +
                "VALUES(?,?,(select statusid from session_status where status = ?),?,?,?); ";

        PreparedStatement ps = IDMService.getCon().prepareStatement(insert);

        ps.setString(1, session.getSessionID().toString());
        ps.setString(2, email);
        ps.setString(3,"ACTIVE");
        ps.setTimestamp(4,session.getTimeCreated());
        ps.setTimestamp(5,session.getLastUsed());
        ps.setTimestamp(6,session.getExprTime());

        ps.execute();

        return session.getSessionID().toString();
    }


    public static String getActiveSession(String email) throws SQLException
    {
        String query = "SELECT se.sessionID FROM sessions se, session_status st " +
                "WHERE se.email = ? and se.status = st.statusid and st.status = ?;";

        PreparedStatement ps = IDMService.getCon().prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, "ACTIVE");
        ResultSet rs = ps.executeQuery();

        if (rs.next())
        {
            return rs.getString("sessionID");
        }

        return null;
    }

    public static void setSessionStatus(String sessionId, String status) throws SQLException
    {
        String query = "UPDATE sessions SET status = (select statusid from session_status where status = ?) WHERE sessionID = ?";

        PreparedStatement ps = IDMService.getCon().prepareStatement(query);

        ps.setString(1, status);
        ps.setString(2, sessionId);
        ps.execute();
    }

    public static void updateSessionLastUsed(String sessionId) throws SQLException
    {
        ServiceLogger.LOGGER.info("Update last used for SESSION ID: " + sessionId);

        String query = "UPDATE sessions SET lastUsed = ? WHERE sessionID = ?";

        PreparedStatement ps = IDMService.getCon().prepareStatement(query);

        ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        ps.setString(2, sessionId);
        ps.execute();
    }



    public static void updateSession(String email, String sessionId) throws Exception
    {
        try {
            String sessionQuery = "SELECT status, timeCreated, lastUsed, exprTime FROM sessions WHERE email = ? and sessionID = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(sessionQuery);
            ps.setString(1, email);
            ps.setString(2, sessionId);

            ResultSet rs = ps.executeQuery();
//            ServiceLogger.LOGGER.info("Query succeeded.");

            if (!rs.next()) return;

            Timestamp exprTime = rs.getTimestamp("exprTime");
            Timestamp lastUsed = rs.getTimestamp("lastUsed");

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            if (currentTime.after(new Timestamp(lastUsed.getTime() + Session.SESSION_TIMEOUT)))
            {
                setSessionStatus(sessionId, "REVOKED");
            }
            else if (currentTime.after(exprTime))
            {
                setSessionStatus(sessionId, "EXPIRED");
            }
            else if (currentTime.after(new Timestamp(exprTime.getTime() - Session.SESSION_TIMEOUT)))
            {
                setSessionStatus(sessionId, "REVOKED");
                insertNewSession(email);
            }
            else
            {
                updateSessionLastUsed(sessionId);
            }
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve string records.");
            e.printStackTrace();

            throw e;
        }
    }

}
