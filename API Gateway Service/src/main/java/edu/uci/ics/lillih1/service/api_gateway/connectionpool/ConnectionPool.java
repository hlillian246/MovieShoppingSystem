package edu.uci.ics.lillih1.service.api_gateway.connectionpool;

import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.xml.ws.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static edu.uci.ics.lillih1.service.api_gateway.GatewayService.ANSI_RED;
import static edu.uci.ics.lillih1.service.api_gateway.GatewayService.ANSI_RESET;

public class ConnectionPool {
    LinkedList<Connection> connections;
    String driver;
    String url;
    String username;
    String password;
    int numCons;

    private Lock lock = new ReentrantLock();
    private Condition notFull = this.lock.newCondition();
    private Condition notEmpty = this.lock.newCondition();


    public ConnectionPool(int numCons, String driver, String url, String username, String password) {
        this.connections = new LinkedList<Connection>();
        this.numCons = numCons;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;

        ServiceLogger.LOGGER.info("numCons: " + numCons);
        ServiceLogger.LOGGER.info("Driver: " + driver);
        ServiceLogger.LOGGER.info("url: " + url);
        ServiceLogger.LOGGER.info("user: " + username);
        ServiceLogger.LOGGER.info("pass: " + password);

        int totalCons = 0;

        try {
            Class.forName(driver);
            for (int i = 0; i < numCons; i++) {
                Connection con = createConnection();
                if (con != null) {
                    connections.add(con);
                    totalCons++;
                }
            }
        }
        catch(ClassNotFoundException e)
        {
            ServiceLogger.LOGGER.severe(ANSI_RED + "Unable to load driver into memory." + ANSI_RESET);
        }

        ServiceLogger.LOGGER.info("Total connections: " + totalCons);
    }

    public synchronized Connection requestCon()
    {
        while (connections.isEmpty()) {
            try {
                ServiceLogger.LOGGER.info("No DB connections available");

                synchronized (connections) {
                    ServiceLogger.LOGGER.info("WAIT a connection");
                    connections.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ServiceLogger.LOGGER.info("GOT a connection");
        return connections.pop();
    }

    public void releaseCon(Connection con) {
        synchronized (connections) {
            ServiceLogger.LOGGER.info("Notify release");
            connections.add(con);
            connections.notifyAll();
        }
    }

    private Connection createConnection() {
        try
        {
            Connection connection = DriverManager.getConnection(url,username,password);
            return connection;
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));

        }
        return null;
    }
}
