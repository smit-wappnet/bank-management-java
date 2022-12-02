package bankmanagement;

import java.io.File;

public class CurrentAccount extends Account {
    public CurrentAccount(File file) {
        super(file);
    }

    public CurrentAccount(String customer_number_string, String name) {
        super(customer_number_string, name);
        this.account_type = "Current";
        this.maxWithdwal = 100000;
        this.minimumBalance = 5000;
    }
}
