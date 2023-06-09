package repositories;

import config.DatabaseConfiguration;
import exceptions.ClientNotFoundException;
import model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static constants.Constants.*;

public class ClientRepository {
    private final DatabaseConfiguration databaseConfiguration;

    public DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public ClientRepository(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }


    public Optional<Client> findClientByEmail(String email){
        Client client = null;
        try{
            PreparedStatement preparedStatement = databaseConfiguration.getConnection().prepareStatement(QUERY_GET_CLIENT_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                client = new Client(resultSet.getInt("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"));
                System.out.println("Client found!");
            }
            else{
                throw new ClientNotFoundException("Client with email " + email + " doesn't exist!");
            }
        }
        catch (ClientNotFoundException clientNotFoundException){
            System.out.println(clientNotFoundException.getMessage());
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return Optional.ofNullable(client);
    }

    public Client getClientById(int id){
        Client client = null;
        try{
            PreparedStatement preparedStatement = databaseConfiguration.getConnection().prepareStatement(QUERY_GET_CLIENT_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                client = new Client(resultSet.getInt("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"));
                System.out.println("Client found!");
            }
            else{
                throw new ClientNotFoundException("Client with id " + id + " doesn't exist!");
            }
        }
        catch (ClientNotFoundException clientNotFoundException){
            System.out.println(clientNotFoundException.getMessage());
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return client;
    }

    public Map<String, Client> getClients(){
        Map<String, Client> clients = new TreeMap<>();
        try{
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_GET_ALL_CLIENTS);
            while(resultSet.next()){
                Client client = new Client(resultSet.getInt("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"));
                clients.put(client.getEmail(), client);
            }
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
        return clients;
    }

    public void addClient(Client client){
        try{
            PreparedStatement preparedStatement = databaseConfiguration.getConnection().prepareStatement(QUERY_INSERT_CLIENT);
            preparedStatement.setInt(1, client.getId());
            preparedStatement.setString(2, client.getFirstName());
            preparedStatement.setString(3, client.getLastName());
            preparedStatement.setString(4, client.getPhoneNumber());
            preparedStatement.setString(5, client.getEmail());
            preparedStatement.execute();
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void deleteClient(Client client){
        try{
            PreparedStatement preparedStatement = databaseConfiguration.getConnection().prepareStatement(QUERY_DELETE_CLIENT);
            preparedStatement.setInt(1, client.getId());
            preparedStatement.execute();
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void updateClient(Client oldClient, Client newClient){
        try{
            PreparedStatement preparedStatement = databaseConfiguration.getConnection().prepareStatement(QUERY_UPDATE_CLIENT);
            preparedStatement.setInt(1, newClient.getId());
            preparedStatement.setString(2, newClient.getFirstName());
            preparedStatement.setString(3, newClient.getLastName());
            preparedStatement.setString(4, newClient.getPhoneNumber());
            preparedStatement.setString(5, newClient.getEmail());
            preparedStatement.setInt(6, oldClient.getId());
            preparedStatement.execute();
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }
    }
}
