package bankmanagement;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.*;

public class Account {
    // Independent Attributes
    public int account_number;
    public String account_number_string;
    public int customer_number;
    public String customer_number_string;
    public String name;
    public int balance = 0;
    public boolean blocked = false;
    public File file_account;
    public ArrayList<String> transactions;

    // Dependent Attributes
    public boolean canDeposit = true;
    public boolean canWithdrawal = true;
    public String account_type = "Default";
    public int maxWithdwal = 20000;
    public int minimumBalance = 0;

    public Account() {
        transactions = new ArrayList<>();
    }

    public Account(File file) {
        this();
        this.file_account = file;
        JsonObject jsonObject = Helper.getJsonObject(file);
        try {
            this.account_type = jsonObject.get("account_type").getAsString();
            this.account_number = jsonObject.get("account_number").getAsInt();
            this.account_number_string = jsonObject.get("account_number_string").getAsString();
            this.customer_number = jsonObject.get("customer_number").getAsInt();
            this.customer_number_string = jsonObject.get("customer_number_string").getAsString();
            this.name = jsonObject.get("name").getAsString();
            this.balance = jsonObject.get("balance").getAsInt();
            this.blocked = jsonObject.get("blocked").getAsBoolean();
            JsonArray transactions = jsonObject.get("transactions").getAsJsonArray();
            for (JsonElement jsonElement : transactions) {
                this.transactions.add(jsonElement.getAsString());
            }
            Helper.DEBUG("Account  Loaded From File, Account  Number-" + this.account_number_string + ", Type-"
                    + this.account_type + ", Name-" + this.name);
        } catch (Exception e) {
            Helper.DEBUG("Error while Loading Account");
        }
    }

    public Account(String customer_number_string, String name) {
        this();
        this.account_number = Helper.getNewAccountNumber();
        this.account_number_string = Helper.getAccountNumberString(account_number);
        this.customer_number = Helper.getCustomerNumber(customer_number_string);
        this.customer_number_string = Helper.getCustomerNumberString(this.customer_number);
        this.name = name;
        this.file_account = new File(Helper.path_accounts + account_number_string + ".json");
        this.save();
    }

    public void save() {
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("account_type", account_type);
            jsonObject.addProperty("account_number", account_number);
            jsonObject.addProperty("account_number_string", account_number_string);
            jsonObject.addProperty("customer_number", customer_number);
            jsonObject.addProperty("customer_number_string", customer_number_string);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("balance", balance);
            jsonObject.addProperty("blocked", blocked);

            JsonArray jsonArray = new JsonArray();
            JsonParser parser = new JsonParser();
            for (String transaction : transactions) {
                jsonArray.add((JsonElement) parser.parse(transaction));
            }
            jsonObject.add("transactions", jsonArray);
            Helper.saveJsonObject(file_account, jsonObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to Save data");
        }
        Helper.DEBUG("Account  Saved to File, Account  Number-" + this.account_number_string + ", Type-"
                + this.account_type + ", Name-" + this.name);
    }
}
