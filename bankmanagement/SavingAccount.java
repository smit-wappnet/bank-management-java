package bankmanagement;

import java.io.File;

public class SavingAccount extends Account {
    public SavingAccount(File file){
        super(file);
    }
    public SavingAccount(String customer_number_string, String name) {
        super(customer_number_string, name);
        this.account_type = "Saving";
    }
}
