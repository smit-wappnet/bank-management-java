package bankmanagement;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Bank {
    // private String bank_id, bank_name;
    public int balance = 0;
    public ArrayList<Customer> customers;
    public ArrayList<Account> accounts;
    public ArrayList<String> transactions;
    public File file_bank;

    // public Banking(String bank_id, String bank_name) {
    public Bank(String bank_id, String bank_name) {

        // this.bank_id = bank_id;
        // this.bank_name = bank_name;
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();

        Helper.intializePaths();
        this.transactions = new ArrayList<>();
        file_bank = new File(Helper.path_files + "bank.json");

    }

    /*
     * Account Management
     */
    public Account createAccount(String type, String customer_number_string, String name) {
        Account newAccount;
        if (type.equals("Saving")) {
            newAccount = new SavingAccount(customer_number_string, name);
        } else if (type.equals("Current")) {
            newAccount = new CurrentAccount(customer_number_string, name);
        } else {
            newAccount = new Account(customer_number_string, name);
        }
        accounts.add(newAccount);
        Helper.DEBUG(
                "New Account Created Type-" + newAccount.account_type + " Account Number-" + newAccount.account_number);
        return newAccount;
    }

    public Account getAccount(int account_number) {
        for (Account account : accounts) {
            if (account.account_number == account_number)
                return account;
        }
        return null;
    }

    public String withdraw(int account_number, int amount) {
        String response = "";
        Account account = getAccount(account_number);
        if (account != null) {
            if (balance < amount) {
                response = "Insufficient Balance in Account(Rs. " + account.balance + ").";
            } else if (account.balance - account.minimumBalance < amount) {
                response = "Withdraw Rejected due to Maintain Balance Policy";
            } else {
                Transaction transaction = new Transaction();

                transaction.amount = amount;
                transaction.message = "Withdraw";

                transaction.from_account = 0;
                transaction.from_account_string = "0000000";
                transaction.to_account = account.account_number;
                transaction.to_account_string = account.account_number_string;

                transaction.from_account_balance_before = this.balance;
                transaction.to_account_balance_before = account.balance;

                account.balance -= amount;
                this.balance -= amount;

                transaction.from_account_balance_after = this.balance;
                transaction.to_account_balance_after = account.balance;

                transaction.save();
                account.transactions.add(transaction.transaction_number_string);
                this.transactions.add(transaction.transaction_number_string);
                response = "Wirhdraw Rs. " + amount + " from Account " + account.account_number_string
                        + " is Successfully. (Avl Bal. " + account.balance + ")";
                this.save();
                account.save();
            }
        } else {
            response = "Invalid Account Number";
        }
        return response;
    }

    public String deposit(int account_number, int amount) {
        String response = "";
        Account account = getAccount(account_number);
        if (account != null) {

            Transaction transaction = new Transaction();

            transaction.amount = amount;
            transaction.message = "Diposit";

            transaction.to_account = 0;
            transaction.to_account_string = "0000000";
            transaction.from_account = account.account_number;
            transaction.from_account_string = account.account_number_string;

            transaction.from_account_balance_before = account.balance;
            transaction.to_account_balance_before = this.balance;

            account.balance += amount;
            this.balance += amount;

            transaction.from_account_balance_after = account.balance;
            transaction.to_account_balance_after = this.balance;

            transaction.save();
            account.transactions.add(transaction.transaction_number_string);
            this.transactions.add(transaction.transaction_number_string);
            response = "Deposit Rs. " + amount + " to Account " + account.account_number_string
                    + " is Successfully. (Avl Bal. " + account.balance + ")";
            this.save();
            account.save();
        } else {
            response = "Invalid Account Number";
        }
        return response;
    }

    public void transfer(String from_account, String to_account, int amount, String message) {

    }

    /*
     * Customer Management
     */
    public Customer getCustomer(int customer_number) {
        for (Customer customer : customers) {
            if (customer.customer_number == customer_number)
                return customer;
        }
        return null;
    }

    public Customer getCustomer(String customer_number_string) {
        return getCustomer(Integer.parseInt(customer_number_string));
    }

    public Customer createCustomer(String name, String city) {

        Customer newCustomer = new Customer(name, city);
        customers.add(newCustomer);

        return newCustomer;
    }

    public void open() {

        Helper.DEBUG();
        Helper.DEBUG("Bank is Going to Open");
        Helper.DEBUG();

        try {
            JsonObject jsonObject = Helper.getJsonObject(file_bank);
            this.balance = jsonObject.get("balance").getAsInt();
            JsonArray transactions = jsonObject.get("transactions").getAsJsonArray();
            for (JsonElement jsonElement : transactions) {
                this.transactions.add(jsonElement.getAsString());
            }
            Helper.DEBUG("Bank Loaded from File, Balance-" + this.balance);
        } catch (Exception e) {
            this.balance = 0;
            Helper.DEBUG("New Bank Intiated");
        }

        Helper.DEBUG(this.transactions.toString());

        File[] files;

        // Loading Existing Customers
        files = Helper.file_customers.listFiles();
        for (File file : files) {
            customers.add(new Customer(file));
        }

        // Loading Existing Accounts
        files = Helper.file_accounts.listFiles();
        for (File file : files) {
            accounts.add(Helper.getActualAccount(file));
        }

        Helper.DEBUG();
        Helper.DEBUG("Bank is now Open");
        Helper.DEBUG();
    }

    public void close() {

        Helper.DEBUG();
        Helper.DEBUG("Bank is goint to Close");
        Helper.DEBUG();

        this.save();

        for (Customer customer : customers) {
            customer.save();
        }
        for (Account account : accounts) {
            account.save();
        }
        Helper.DEBUG();
        Helper.DEBUG("Bank is closed");
        Helper.DEBUG();
    }

    public void save() {
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("balance", balance);
            JsonArray jsonArray = new JsonArray();
            JsonParser parser = new JsonParser();
            for (String transaction : transactions) {
                jsonArray.add((JsonElement) parser.parse(transaction));
            }
            jsonObject.add("transactions", jsonArray);
            Helper.saveJsonObject(file_bank, jsonObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to Save data");
        }
        Helper.DEBUG("Bank Saved to File, Balance-" + this.balance);
    }
}
