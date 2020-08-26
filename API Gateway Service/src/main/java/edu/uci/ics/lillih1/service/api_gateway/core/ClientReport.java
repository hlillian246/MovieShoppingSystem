package edu.uci.ics.lillih1.service.api_gateway.core;

import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientReport {

    public static String fetchClientReport(String email, String sessionID, String transactionID) throws Exception
    {
        ServiceLogger.LOGGER.info("Email: " + email + ", sessionID: " + sessionID + ", transactionID: " + transactionID);

        Connection con = null;
        try {
            con = GatewayService.getConPool().requestCon();
            String query = "SELECT response FROM responses WHERE transactionID = ? ";
            if (email != null)
            {
                query += "And email = ? ";
            }
            query += ";";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, transactionID);

            int col=2;
            if (email != null)
            {
                ps.setString(col++, email);
            }

            ServiceLogger.LOGGER.info("Query: " + ps.toString());

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                String response = rs.getString("response");

                String query1 = "DELETE FROM responses WHERE transactionID = ?;";
                ps = con.prepareStatement(query1);
                ps.setString(1, transactionID);
                ps.execute();

                return response;
            }
            return null;
        }
        catch(SQLException e )
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Exception happened");
            throw e;
        }
        finally {
            GatewayService.getConPool().releaseCon(con);
        }
    }
}
