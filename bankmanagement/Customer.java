package bankmanagement;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.*;

public class Customer {
    public File file_customer;
    public int customer_number;
    public String customer_number_string;
    public String name;
    public String city;
    public ArrayList<String> accounts;

    public Customer(File file) {
        this.file_customer = file;
        JsonObject jsonObject = Helper.getJsonObject(file);
        try {
            this.customer_number = jsonObject.get("customer_number").getAsInt();
            this.customer_number_string = jsonObject.get("customer_number_string").getAsString();
            this.name = jsonObject.get("name").getAsString();
            this.city = jsonObject.get("city").getAsString();
            this.accounts = new ArrayList<>();
            JsonArray accounts = jsonObject.get("accounts").getAsJsonArray();
            for (JsonElement jsonElement : accounts) {
                this.accounts.add(jsonElement.getAsString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Helper.DEBUG(
                "Customer Loaded From File, Customer Number-" + this.customer_number_string + ", Name-" + this.name);
    }

    public Customer(String name, String city) {
        this.customer_number = Helper.getNewCustomerNumber();
        this.customer_number_string = Helper.getCustomerNumberString(this.customer_number);
        this.file_customer = new File(Helper.path_customers + customer_number_string + ".json");
        this.name = name;
        this.city = city;
        this.accounts = new ArrayList<>();
    }

    public void save() {
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("customer_number", customer_number);
            jsonObject.addProperty("customer_number_string", customer_number_string);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("city", city);
            JsonArray jsonArray = new JsonArray();
            JsonParser parser = new JsonParser();
            for (String transaction : accounts) {
                jsonArray.add((JsonElement) parser.parse(transaction));
            }
            jsonObject.add("accounts", jsonArray);
            Helper.saveJsonObject(file_customer, jsonObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to Save data");
        }
        Helper.DEBUG("Customer Saved to File, Customer Number-" + this.customer_number_string + ", Name-" + this.name);
    }
}
