/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmattempt;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author kelok
 */
public class ATMAttempt {

    public static Scanner sc = new Scanner(System.in);

    public static ArrayList<Account> accounts;
    public static Client[] clients;

    public static void main(String[] args) {
        // TODO code application logic here
        int n;

        System.out.println("ATM Attempt!");
        System.out.println("How many accounts? : ");
        n = sc.nextInt();
        accounts = initilizeAccount(n);
        clients = new Client[n];

        for (int i = 0; i < n; i++) {
            clients[i] = new Client();
        }

        for (Iterator<Account> i = accounts.iterator(); i.hasNext();) {
            Account account = i.next();
            System.out.println("Account " + account.id + ":");
            if (account.enterPIN()) {     //successful PIN
                showMenu(account);
            } else {
                accounts.remove(account);                        //delete from list
            }
        }

        for (Client client : clients) {
            client.start();
        }

        try {
            for (Client client : clients) {
                client.join();
            }

            System.out.println("Transaction all done...");

            for (int i = 0; i < accounts.size(); i++) {
                System.out.println("Account ID : " + i + " current balance: " + accounts.get(i).bal);
            }
        } catch (InterruptedException ex) {

        }
    }

    public static ArrayList<Account> initilizeAccount(int count) {
        ArrayList<Account> accounts = new ArrayList<>();
        System.out.println("Accounts ");
        System.out.println("================");
        for (int i = 0; i < count; i++) {
            System.out.println("Enter initial balance for account " + i + ":");
            int bal = sc.nextInt();
            int randomPIN = (int) (Math.random() * 9000) + 1000;
            Account account = new Account(i, bal, randomPIN);
            accounts.add(account);
        }

        for (Account acc : accounts) {
            acc.showDetails();
        }
        return accounts;
    }

    public static void showMenu(Account account) {
        String input;
        do {
            System.out.println("Menu");
            System.out.println("==========");
            System.out.println("1. Balance enquiry");
            System.out.println("2. Change PIN");
            System.out.println("3. Withdrawal");
            System.out.println("4. Deposit");
            System.out.println("5. Transfer");
            System.out.println("Input choice 1-5");
            input = sc.next();

            switch (input) {
                case "1":
                    account.getBal();
                    break;
                case "2":
                    changePIN(account);
                    break;
                case "3":
                    int withAmt = withdrawalInput();
                    clients[account.id].withdrawalAmt = withAmt;
                    break;
                case "4":
                    int depAmt = depositInput();
                    clients[account.id].depositAmt = depAmt;
                    break;
                case "5":
                    initilizeTransfer(account);
                    break;
                default:
                    System.out.println("Invalid selection");
            }
            System.out.println("Continue? (y/n): ");
            input = sc.next();
        } while (input.equalsIgnoreCase("y"));
    }

    public static void changePIN(Account account) {
        System.out.println("Enter new PIN: ");
        int newPIN;
        while (true) {
            try {
                newPIN = sc.nextInt();
                if (String.valueOf(newPIN).length() == 4) {
                    break;
                } else {
                    System.out.println("Invalid length of PIN. Re-enter");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid new PIN. Re-enter");
                sc.nextLine();
            }
        }
        account.pin = newPIN;
        System.out.println("New PIN is " + account.pin);
    }

    public static int withdrawalInput() {
        System.out.println("You may withdraw only in multiples of RM20. Enter amount: ");
        int amount;
        while (true) {
            try {
                amount = sc.nextInt();
                if (amount % 20 == 0) {
                    break;
                } else {
                    System.out.println("Invalid amount. Re-enter");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid amount. Re-enter");
                sc.nextLine();
            }
        }
        return amount;
    }

    public static int depositInput() {
        int selection;
        int amount;
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Select deposit type: ");
                System.out.println("1. Cash");
                System.out.println("2. Envelope");
                selection = sc.nextInt();

                switch (selection) {
                    case 1:
                        System.out.println("Enter amount: ");
                        amount = sc.nextInt();
                        if (amount > 0) {
                            return amount;
                        } else {
                            System.out.println("Invalid amount.");
                        }
                        break;
                    case 2:
                        System.out.println("Reading envelope in the dispenser...");
                        System.out.println("Pending verification from bank. This process may take a few days.");
                        break;

                    default:
                        System.out.println("Invalid selection.");
                        break;
                }
                break;
            } catch (Exception ex) {
                System.out.println("Invalid input. Re-enter");
                sc.nextLine();
            }
        }
        return 0;
    }

    public static void initilizeTransfer(Account account) {
        int destAccountID;
        int amt = 0;
        destAccountID = 0;
        while (true) {
            System.out.println("Enter destination account id from account " + account.id + " (Input own account id or -1 if no transfer) ");
            try {
                destAccountID = sc.nextInt();
                if (destAccountID < accounts.size()) {
                    if (destAccountID == -1 || destAccountID == account.id) {
                        //self transfer
                        break;
                    } else {
                        System.out.println("Enter transfer amount: ");
                        amt = sc.nextInt();
                        clients[account.id].srcAccount = accounts.get(account.id);
                        clients[account.id].destAccount = accounts.get(destAccountID);
                        clients[account.id].transferAmt = amt;
                        break;
                    }
                } else {
                    System.out.println("Invalid destination account ID.");
                }
            } catch (Exception ex) {
                System.out.println("Invalid input.");
                sc.nextLine();
            }
        }
    }

//    public static void initilizeTransfer(Account account) {
//        int destAccountID;
//        int amt = 0;
//        destAccountID = 0;
//        do {
//            System.out.println("Enter destination account id from account " + account.id + " (Input own account id or -1 if no transfer) ");
//            destAccountID = sc.nextInt();
//            if (destAccountID < accounts.size()) {
//                if (destAccountID == -1 || destAccountID == account.id) {
//                    //self transfer
//                } else {
//                    System.out.println("Enter transfer amount: ");
//                    amt = sc.nextInt();
//                    clients[account.id].srcAccount = accounts.get(account.id);
//                    clients[account.id].destAccount = accounts.get(destAccountID);
//                    clients[account.id].transferAmt = amt;
//                }
//            } else {
//                System.out.println("Invalid destination account ID.");
//            }
//        } while (!checkInput(destAccountID, accounts.get(account.id), accounts, amt));
//    }
//
//    public static boolean checkInput(int destID, Account src, ArrayList<Account> allAccounts, int amount) {
//        if (destID >= allAccounts.size() || amount < 0 || amount > src.bal) {
//            System.out.println("Error input");
//            return false;
//        }
//        return true;
//    }
}
