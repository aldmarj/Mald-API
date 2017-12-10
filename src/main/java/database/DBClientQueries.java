/**
 *
 */
package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import models.Client;

/**
 * Class to contain helper functions for interaction with the database. Each instance should only be
 * used once.
 *
 * @author Lawrence
 */
public final class DBClientQueries extends DBQueries {

    /**
     * CLASS CONSTRUCTOR
     */
    public DBClientQueries() throws NoDataStoreConnectionException {
        super();
    }

    /**
     * Creates a client in the database with the given name.
     *
     * @param client - the new client to add
     * @throws BadKeyException - If the given key is invalid.
     * @throws NoDataStoreConnectionException - If a connection cannot be made to the store.
     */
    public void createClient(Client client)
        throws BadKeyException, NoDataStoreConnectionException {
        try {
            createClientSQL(client, this);
        } catch (SQLIntegrityConstraintViolationException e) {
            this.handleIntegrityConstaitViolation(e);
        } catch (SQLException e) {
            this.handleSQLException(e);
        } finally {
            this.closeConnection();
        }
    }

    /**
     * Returns a client by its given clientId. Returns null if no client is found.
     *
     * @param clientId - The ID of the client to find.
     * @return The requested client.
     * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
     */
    public Client getClient(int clientId) throws NoDataStoreConnectionException {
        Client result = null;

        try {
            result = getClientSQL(clientId, this);
        } catch (SQLException e) {
            this.handleSQLException(e);
        } finally {
            this.closeResultSet();
            this.closeConnection();
        }

        return result;
    }

    /**
     * Returns all of the clients.
     *
     * @param businessTag The businessTag to limit the results
     * @return All clients within a business.
     * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
     */
    public ArrayList<Client> getAllClients(final String businessTag)
        throws NoDataStoreConnectionException {
        ArrayList<Client> result = new ArrayList<Client>();

        try {
            result = getAllClientsSQL(businessTag, this);
        } catch (SQLException e) {
            this.handleSQLException(e);
        } finally {
            this.closeResultSet();
            this.closeConnection();
        }

        return result;
    }

    /**
     * Creates a client in the database with the given client and DB runner.
     *
     * @param client - the client to create in the database.
     * @param queryRunner - the DB query runner.
     * @throws SQLException if the DB cannot be reached.
     * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
     */
    public static void createClientSQL(Client client, DBQueries queryRunner)
        throws SQLException, SQLIntegrityConstraintViolationException {
        String query = "INSERT INTO BusinessClient(clientName, businessTag) VALUES (?, ?);";

        final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
        stmt.setString(1, client.getClientName());
        stmt.setString(2, client.getBusinessTag());

        stmt.executeUpdate();
    }

    /**
     * Gets a client from the database with the given clientId and DB runner.
     *
     * @param clientId the client id to search for in the database.
     * @param queryRunner - the DB query runner.
     * @throws SQLException if the DB cannot be reached.
     * @throws NoDataStoreConnectionException if the DB cannot be reached.
     */
    public static Client getClientSQL(int clientId, DBQueries queryRunner)
        throws SQLException, NoDataStoreConnectionException {
        Client result = null;

        String query = "Select clientName, businessTag FROM BusinessClient WHERE BusinessClient.clientId = ?;";

        final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
        stmt.setInt(1, clientId);

        queryRunner.resultSet = stmt.executeQuery();
        while (queryRunner.resultSet.next()) {
            result = new Client(
                clientId,
                queryRunner.resultSet.getString("clientName"),
                queryRunner.resultSet.getString("businessTag"));
        }

        return result;
    }


    /**
     * Gets all clients from the database.
     *
     * @param businessTag The businessTag to limit the results
     * @param queryRunner the DB query runner.
     * @throws SQLException if the DB cannot be reached.
     * @throws NoDataStoreConnectionException if the DB cannot be reached.
     */
    public static ArrayList<Client> getAllClientsSQL(final String businessTag,
        final DBQueries queryRunner)
        throws SQLException, NoDataStoreConnectionException {
        ArrayList<Client> result = new ArrayList<Client>();

        String query = "Select clientId, clientName, businessTag FROM BusinessClient WHERE BusinessClient.businessTag = ?;";

        final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
        stmt.setString(1, businessTag);

        queryRunner.resultSet = stmt.executeQuery();
        while (queryRunner.resultSet.next()) {
            result.add(new Client(
                queryRunner.resultSet.getInt("clientId"),
                queryRunner.resultSet.getString("clientName"),
                queryRunner.resultSet.getString("businessTag")));
        }

        return result;
    }
}
