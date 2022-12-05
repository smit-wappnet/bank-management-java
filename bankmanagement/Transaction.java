package bankmanagement;

import java.io.File;
import java.util.Date;

import com.google.gson.JsonObject;

public class Transaction {
    public Date date;
    public int transaction_number;
    public String transaction_number_string;
    public int from_account;
    public String from_account_string;
    public int to_account;
    public String to_account_string;
    public int from_account_balance_before;
    public int from_account_balance_after;
    public int to_account_balance_before;
    public int to_account_balance_after;
    public int amount;
    public String message;
    public File file_transaction;

    public Transaction() {
        this.date = new Date();
        this.transaction_number = Helper.getNewTransactionNumber();
        this.transaction_number_string = Helper.getTransactionNumberString(this.transaction_number);
        System.out.println(Helper.path_transactions + this.transaction_number_string + ".json");
        this.file_transaction = new File(Helper.path_transactions + this.transaction_number_string + ".json");
        System.out.println(transaction_number_string);
        System.out.println(date.toString());
    }

    public Transaction(String transaction_number_string) {
        this.transaction_number_string = transaction_number_string;
        this.transaction_number = Helper.getTransactionNumber(this.transaction_number_string);
        this.file_transaction = new File(Helper.path_transactions + this.transaction_number_string + ".json");

        JsonObject jsonObject = Helper.getJsonObject(file_transaction);
        try {
            this.from_account = jsonObject.get("from_account").getAsInt();
            this.from_account_string = jsonObject.get("from_account_string").getAsString();
            this.to_account = jsonObject.get("to_account").getAsInt();
            this.to_account_string = jsonObject.get("to_account_string").getAsString();
            this.from_account_balance_before = jsonObject.get("from_account_balance_before").getAsInt();
            this.from_account_balance_after = jsonObject.get("from_account_balance_after").getAsInt();
            this.to_account_balance_before = jsonObject.get("to_account_balance_before").getAsInt();
            this.to_account_balance_after = jsonObject.get("to_account_balance_after").getAsInt();
            this.amount = jsonObject.get("amount").getAsInt();
            this.message = jsonObject.get("message").getAsString();
            // Helper.DEBUG("Transaction Loaded From File, Account  Number-" + this.transaction_number);
        } catch (Exception e) {
            Helper.DEBUG("Error while Loading Account");
        }
    }

    public void save() {
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("date", date.toString());
            jsonObject.addProperty("transaction_number", transaction_number);
            jsonObject.addProperty("transaction_number_string", transaction_number_string);
            jsonObject.addProperty("from_account", from_account);
            jsonObject.addProperty("from_account_string", from_account_string);
            jsonObject.addProperty("to_account", to_account);
            jsonObject.addProperty("to_account_string", to_account_string);
            jsonObject.addProperty("from_account_balance_before", from_account_balance_before);
            jsonObject.addProperty("from_account_balance_after", from_account_balance_after);
            jsonObject.addProperty("to_account_balance_before", to_account_balance_before);
            jsonObject.addProperty("to_account_balance_after", to_account_balance_after);
            jsonObject.addProperty("amount", amount);
            jsonObject.addProperty("message", message);

            Helper.saveJsonObject(file_transaction, jsonObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to Save data");
        }
        Helper.DEBUG("Transaction Saved to File, Number-" + this.transaction_number_string);
    }

}
