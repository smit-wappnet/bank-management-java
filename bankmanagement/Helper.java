package bankmanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.JsonParser;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class Helper {

    public static int customer_number_length = 5;
    public static int account_number_length = 7;
    public static int transaction_number_length = 10;

    public static String path_files = "";
    public static String path_customers = "";
    public static String path_accounts = "";
    public static String path_transactions = "";

    public static File file_files;
    public static File file_customers;
    public static File file_accounts;
    public static File file_transactions;

    public static void intializePaths() {
        path_files = System.getProperty("user.dir") + "\\files\\";
        path_customers = path_files + "customers\\";
        path_accounts = path_files + "accounts\\";
        path_transactions = path_files + "transactions\\";

        file_files = new File(path_files);
        file_customers = new File(path_customers);
        file_accounts = new File(path_accounts);
        file_transactions = new File(path_transactions);

        Helper.fixPath(file_files);
        Helper.fixPath(file_customers);
        Helper.fixPath(file_accounts);
        Helper.fixPath(file_transactions);
    }

    public static void fixPath(File file) {
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to Intialize Bank Server");
            System.exit(0);
        }
    }

    /*
     * Transaction Utilities
     * getNewTransactionNumber() -> int
     * getNewTransactionNumberString() ->String
     * getTransactionNumber(String number) -> int
     * getTransactionNumberString(int number) -> String
     * getTransactionNumber(File file) -> int
     */
    public static int getNewTransactionNumber() {
        int transaction_number = 1;
        File[] transactions = file_transactions.listFiles();
        if (transactions.length > 0) {
            File last_transaction = transactions[transactions.length - 1];
            transaction_number = Integer.parseInt(last_transaction.getName().replaceFirst(".json", "")) + 1;
        }
        return transaction_number;
    }

    public static String getNewTransactionNumberString() {
        return getTransactionNumberString(getNewAccountNumber());
    }

    public static int getTransactionNumber(String transaction_number_string) {
        return Integer.parseInt(transaction_number_string);
    }

    public static String getTransactionNumberString(int transaction_number) {
        String transaction_number_string = String.format("%0" + transaction_number_length + "d", transaction_number);
        return transaction_number_string;
    }

    public static int getTransactionNumber(File file_transaction) {
        int transaction_number = Integer.parseInt(file_transaction.getName().replaceFirst(".json", ""));
        return transaction_number;
    }
    /*
     * Transaction Utilities - Over
     */

    /*
     * Customer Utilities
     * getNewCustomerNumber() -> int
     * getNewCustomerNumberString() -> String
     * getCustomerNumberString(int Number) -> String
     * getCustomerNumber(String number) -> int
     * getCustomerNumber(File file) -> int
     */
    public static int getNewAccountNumber() {
        int account_number = 1;
        File accounts[] = file_accounts.listFiles();
        if (accounts.length > 0) {
            File last_account = accounts[accounts.length - 1];
            account_number = Integer.parseInt(last_account.getName().replaceFirst(".json", "")) + 1;
        }
        return account_number;
    }

    public static String getNewAccountNumberString() {
        return getAccountNumberString(getNewAccountNumber());
    }

    public static String getAccountNumberString(int account_number) {
        String account_number_string = String.format("%0" + account_number_length + "d", account_number);
        return account_number_string;
    }

    public static int getAccountNumber(String account_number_string) {
        int account_number = Integer.parseInt(account_number_string);
        return account_number;
    }

    public static int getAccountNumber(File file_account) {
        int account_number = Integer.parseInt(file_account.getName().replaceFirst(".json", ""));
        return account_number;
    }

    public static Account getActualAccount(File file) {
        JsonObject jsonObject = Helper.getJsonObject(file);
        String type = jsonObject.get("account_type").getAsString();
        if (type.equals("Saving")) {
            return new SavingAccount(file);
        } else if (type.equals("Current")) {
            return new CurrentAccount(file);
        } else {
            return new Account(file);
        }
    }
    /*
     * Account Utilities - Over
     */

    /*
     * Customer Utilities
     * getNewCustomerNumber() -> int
     * getNewCustomerNumberString() -> String
     * getCustomerNumberString(int Number) -> String
     * getCustomerNumber(String number) -> int
     * getCustomerNumber(File file) -> int
     */
    public static int getNewCustomerNumber() {
        int customer_number = 1;
        File customers[] = file_customers.listFiles();
        if (customers.length > 0) {
            File last_customer = customers[customers.length - 1];
            customer_number = Integer.parseInt(last_customer.getName().replaceFirst(".json", "")) + 1;
        }
        return customer_number;
    }

    public static String getNewCustomerNumberString() {
        return getCustomerNumberString(getNewCustomerNumber());
    }

    public static String getCustomerNumberString(int customer_number) {
        String customer_number_string = String.format("%0" + customer_number_length + "d", customer_number);
        return customer_number_string;
    }

    public static int getCustomerNumber(String customer_number_string) {
        int customer_number = Integer.parseInt(customer_number_string);
        return customer_number;
    }

    public static int getCustomerNumber(File file_customer) {
        int customer_number = Integer.parseInt(file_customer.getName().replaceFirst(".json", ""));
        return customer_number;
    }
    /*
     * Customer Utilities - Over
     */

    /*
     * JSON & Files Utilities
     * getJsonObject(File file) -> JsonObject
     * saveJsonObject(File file, JsonObject jsonObject) -> void
     * getFileContent(File file) -> String
     */
    public static JsonObject getJsonObject(File file) {
        JsonObject jsonObject = new JsonObject();
        try {
            JsonParser parser = new JsonParser();
            String content = getFileContent(file);
            if (!(parser.parse(content) instanceof JsonNull)) {
                jsonObject = (JsonObject) parser.parse(content);
            }
        } catch (Exception e) {
            // Failed to Load File Because of it's Null.
        }
        return jsonObject;
    }

    public static void saveJsonObject(File file, JsonObject jsonObject) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (Exception e) {
            Helper.DEBUG(e.toString());
        }
    }

    public static String getFileContent(File file) {
        String content = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line;
            }
            bufferedReader.close();
        } catch (Exception e) {

        }
        return content;
    }
    /*
     * JSON & Files Utilities - Over
     */

    /*
     * Debug Utilities
     * DEBUG(String message) -> void
     */
    public static void DEBUG() {
        // System.out.println();
    }

    public static void DEBUG(String message) {
        // System.out.println("D: " + message);
    }
    /*
     * Debug Utilities - Over
     */
}
