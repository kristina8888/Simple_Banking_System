package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static banking.Main.*;

public class CardDatabaseSqlite {
    private final String fileName; //ne treba definirati ime fileName-a jer je definiran u argumentima aplikacije
    Map<Integer, Account> acc = new HashMap<>();

    // konekcija prema bazi
    static Connection connectToDatabase(String fileName) {
        String url = ("jdbc:sqlite:" + fileName);
        Connection connection = null;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //konstruktor baze i stvaranje nove tablice (ako ne postoji)
    CardDatabaseSqlite(String fileName) {
        this.fileName = fileName;

        Connection connection = connectToDatabase(this.fileName);
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String createTableSql = "CREATE TABLE IF NOT EXISTS card (\n" +
                        "id INTEGER PRIMARY KEY, \n" +
                        "number VARCHAR NOT NULL, \n" +
                        "pin VARCHAR NOT NULL, \n" +
                        "balance INTEGER DEFAULT 0 \n" +
                        ");";

                statement.execute(createTableSql);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertAccountInTable(Account account) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)";

        try (Connection connection = connectToDatabase(fileName);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getNewCardNumber());
            statement.setString(2, account.getNewPin());
            statement.setInt(3, account.getBalance());

            statement.execute();

            account.setId(statement.getGeneratedKeys().getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Account> listAll() {

        String sql = "SELECT * FROM card";

        try {
            Connection connection = connectToDatabase(fileName);
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                Account account = new Account();
                account.setId(result.getInt("id"));
                account.setCardNumber(result.getString("number"));
                account.setPin(result.getString("pin"));
                account.setBalance(result.getInt("balance"));

                acc.put(account.getId(), account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }

    public void updateBalanceToAccount(int incomeAmount) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection connection = connectToDatabase(fileName);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, incomeAmount);
            preparedStatement.setString(2, cardNumberCheck);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBalance() {

        String sql = "SELECT balance FROM card WHERE number = ?";

        try (Connection connection = connectToDatabase(fileName);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumberCheck);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                if (!resultSet.isClosed()) {
                    int balance = resultSet.getInt("balance");
                    return balance;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean checkLogInAccount() {
        Connection connection = connectToDatabase(fileName);
        String sql = "SELECT number, pin FROM card WHERE number = ? AND pin = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumberCheck);
            preparedStatement.setString(2, pinNumberCheck);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                if (!resultSet.isClosed()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkCardExist(String cardNumberCheck) {
        Connection connection = connectToDatabase(fileName);
        String sql = "SELECT number, pin FROM card WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumberCheck);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                if (!resultSet.isClosed()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void executeTransferFromAccount(int amountToTransfer) {
        Connection connection = connectToDatabase(fileName);
        String sql = "UPDATE card SET balance = balance - ? WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, amountToTransfer);
            preparedStatement.setString(2, cardNumberCheck);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransferToAccount(String cardNumberToTransfer, int amountToTransfer) {
        Connection connection = connectToDatabase(fileName);
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, amountToTransfer);
            preparedStatement.setString(2, cardNumberToTransfer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String cardNumberCheck) {
        Connection connection = connectToDatabase(fileName);
        String sql = "DELETE FROM card WHERE number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumberCheck);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
