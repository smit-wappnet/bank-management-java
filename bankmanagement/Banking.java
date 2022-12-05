package bankmanagement;

import java.util.ArrayList;
import java.util.Scanner;

public class Banking {
    public Bank bank;
    public Scanner in;
    public int choice;

    public Banking() {
        in = new Scanner(System.in);
        bank = new Bank("SBI", "State Bank of India");
        bank.open();
        boolean running = true;
        while (running) {
            System.out.println("Welcome to Bank");
            System.out.println("Plesae Select Prefereble Option");
            System.out.println("1. View Bank Balance");
            System.out.println("2. Manage Customers");
            System.out.println("3. Manage Accounts");
            System.out.println("4. Manage Transaction");
            System.out.println("5. Add Deposit Entry");
            System.out.println("6. Add Withdraw Entry");
            System.out.println("7. Quit");
            choice = getChoice();
            switch (choice) {
                case 1:
                    System.out.println("Bank Balance: " + bank.balance);
                    break;
                case 2:
                    boolean customer_manage = true;
                    while (customer_manage) {
                        System.out.println();
                        System.out.println("Manage Customer");
                        System.out.println("Plesae Select Prefereble Option");
                        System.out.println("1. List Customers");
                        System.out.println("2. View Customer Details");
                        System.out.println("3. Create New Customers");
                        System.out.println("4. Back");
                        choice = getChoice();
                        switch (choice) {
                            case 1:
                                listCustomer();
                                break;
                            case 2:
                                viewCustomer();
                                break;
                            case 3:
                                createCustomer();
                                break;
                            case 4:
                                customer_manage = false;
                                break;
                            default:
                                System.out.println("Invalid Choice, Please Try Again...");
                                break;
                        }
                        dsleep(4);
                    }
                    choice = 7;
                    break;
                case 3:
                    boolean account_manage = true;
                    while (account_manage) {
                        System.out.println();
                        System.out.println("Manage Accounts");
                        System.out.println("Plesae Select Prefereble Option");
                        System.out.println("1. List Accuonts");
                        System.out.println("2. View Account Details");
                        System.out.println("3. View Account Details with Transaction");
                        System.out.println("4. Create New Account");
                        System.out.println("5. Back");
                        choice = getChoice();
                        switch (choice) {
                            case 1:
                                listAccount();
                                break;
                            case 2:
                                viewAccount();
                                break;
                            case 3:
                                viewAccountWithTransaction();
                                break;
                            case 4:
                                createAccount();
                                break;
                            case 5:
                                account_manage = false;
                                break;
                            default:
                                System.out.println("Invalid Choice, Please Try Again...");
                                break;
                        }
                        dsleep(5);
                    }
                    choice = 7;
                    break;
                case 4:
                    boolean transaction_manage = true;
                    while (transaction_manage) {
                        System.out.println();
                        System.out.println("Manage Transactions");
                        System.out.println("Plesae Select Prefereble Option");
                        System.out.println("1. List Transaction");
                        // System.out.println("2. Cancle Transaction");
                        System.out.println("3. Back");
                        choice = getChoice();
                        switch (choice) {
                            case 1:
                                viewTransaction(bank.transactions);
                                break;
                            case 2:
                                cancleTransaction();
                                break;
                            case 3:
                                transaction_manage = false;
                                break;
                            default:
                                System.out.println("Invalid Choice, Please Try Again...");
                                break;
                        }
                        dsleep(3);
                    }
                    choice = 7;
                    break;
                case 5:
                    deposit();
                    break;
                case 6:
                    withdraw();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Choice, Please Try Again...");
                    break;
            }
            dsleep(7);
        }
        bank.close();
    }

    private void viewTransaction(ArrayList<String> transactions) {
        if (transactions.size() > 0) {
            System.out.println(
                    "| Transaction Number | From Account | To Account |    Amount    |         Message         |");
            for (String transaction_number_string : transactions) {
                Transaction transaction = new Transaction(transaction_number_string);
                System.out.format("| %-18s | %-12s | %-10s | %12s | %-23s |", transaction.transaction_number_string,
                        transaction.from_account_string, transaction.to_account_string, transaction.amount,
                        transaction.message);
                System.out.println();
            }
        } else {
            System.out.println("No Transaction Found for Bank.");
        }
    }

    private void cancleTransaction() {

    }

    private void listCustomer() {
        if (bank.customers.size() > 0) {
            System.out.println("Number  " + String.format("%-10s", "Name") + String.format("%-10s", "City"));
            for (Customer customer : bank.customers) {
                System.out.println(customer.customer_number_string + "   " + String.format("%-10s", customer.name)
                        + String.format("%-10s", customer.city));
            }
        } else {
            System.out.println("No Customer Found");
        }
    }

    private void viewCustomer() {
        int customer_number;
        System.out.print("Enter Customer Number: ");
        customer_number = in.nextInt();
        Customer customer = bank.getCustomer(customer_number);
        if (customer != null) {
            System.out.println(
                    "Customer Number : " + customer.customer_number + " (" + customer.customer_number_string + ")");
            System.out.println("Name : " + customer.name);
            System.out.println("City : " + customer.city);
        } else {
            System.out.println("Invalid Customer Number.");
        }
    }

    private void createCustomer() {
        String name, city;
        System.out.print("Enter Name: ");
        name = in.next();
        System.out.print("Enter City: ");
        city = in.next();

        Customer customer = bank.createCustomer(name, city);
        System.out.println("New Customer Created, Number-" + customer.customer_number_string);
    }

    private void listAccount() {
        if (bank.accounts.size() > 0) {
            System.out.println(
                    "| Account Number | Account Type | Customer Number |     Account Name     |   Balance   |");
            for (Account account : bank.accounts) {
                System.out.format("| %-14s | %-12s | %-15s | %-20s | %11d |", account.account_number_string,
                        account.account_type,
                        account.customer_number_string, account.name, account.balance);
                System.out.println();
            }
        } else {
            System.out.println("No Accounts Found");
        }
    }

    private void viewAccount() {
        int account_number;
        System.out.print("Enter Account Number: ");
        account_number = in.nextInt();
        Account account = bank.getAccount(account_number);
        if (account != null) {
            System.out.println("Account Number  : " + account.account_number);
            System.out.println("Customer Number : " + account.customer_number_string);
            System.out.println("Account Type    : " + account.account_type);
            System.out.println("Name            : " + account.name);
            System.out.println("Balance         : " + account.balance);
        } else {
            System.out.println("Invalid Account Number");
        }
    }

    private void viewAccountWithTransaction() {
        int account_number;
        System.out.print("Enter Account Number: ");
        account_number = in.nextInt();
        Account account = bank.getAccount(account_number);
        if (account != null) {
            System.out.println("Account Number  : " + account.account_number);
            System.out.println("Customer Number : " + account.customer_number_string);
            System.out.println("Account Type    : " + account.account_type);
            System.out.println("Name            : " + account.name);
            System.out.println("Balance         : " + account.balance);

            viewTransaction(account.transactions);
        } else {
            System.out.println("Invalid Account Number");
        }
    }

    private void createAccount() {
        String type, customer_number_string, name;
        System.out.print("Enter Customer Number: ");
        customer_number_string = in.next();
        if (bank.getCustomer(customer_number_string) != null) {
            System.out.print("Enter Account Type   : ");
            type = in.next();
            System.out.print("Enter Account Name   : ");
            name = in.next();
            Account newAccount = bank.createAccount(type, customer_number_string, name);
            System.out.println("Account Created, Number-" + newAccount.account_number_string);
        } else {
            System.out.println("Invalid Customer Number.");
            System.out.println("Please Try Again...");
        }
    }

    private void deposit() {
        int account_number, amount;
        System.out.print("Enter Account Number: ");
        account_number = in.nextInt();
        System.out.print("Enter Amount: ");
        amount = in.nextInt();

        if (bank.getAccount(account_number) == null) {
            System.out.println("Invalid Account Number");
            System.out.println("Please Try Again");
        } else if (amount <= 0) {
            System.out.println("Transaction Amount Should be Minimum 1 Rupee.");
            System.out.println("Please Try Again");
        } else {
            System.out.println(bank.deposit(account_number, amount));
        }
    }

    private void withdraw() {
        int account_number, amount;
        System.out.print("Enter Account Number: ");
        account_number = in.nextInt();
        System.out.print("Enter Amount: ");
        amount = in.nextInt();

        if (bank.getAccount(account_number) == null) {
            System.out.println("Invalid Account Number");
            System.out.println("Please Try Again");
        } else if (amount <= 0) {
            System.out.println("Transaction Amount Should be Minimum 1 Rupee.");
            System.out.println("Please Try Again");
        } else {
            System.out.println(bank.withdraw(account_number, amount));
        }
    }

    public int getChoice() {
        System.out.print("Enter Your Choice: ");
        return in.nextInt();
    }

    public void dsleep(int n) {
        if (choice != n)
            sleep(3000);
    }

    public void sleep(int ms) {
        System.out.println();
        System.out.print("Waiting... ");
        for (int i = ms; i > 0; i -= 500) {
            System.out.print(i / 500 + "  ");
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        System.out.println();
        System.out.println();
    }

}
