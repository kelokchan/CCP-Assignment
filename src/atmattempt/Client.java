/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmattempt;

/**
 *
 * @author kelok
 */
public class Client extends Thread {               //every client handles account

    Account srcAccount;
    Account destAccount;
    int depositAmt = 0;
    int withdrawalAmt = 0;
    int transferAmt = 0;

    public Client() {

    }

//    public Client(Account srcAccount, Account destAccount, int transferAmt) {
//        this.srcAccount = srcAccount;
//        this.destAccount = destAccount;
//        this.transferAmt = transferAmt;
//    }
    @Override
    public void run() {
        int sum = 0;
//        do {
//            sum = srcAccount.withdraw(withdrawalAmt)
//                    + srcAccount.deposit(depositAmt);
//            if (destAccount != null) {
//                sum += srcAccount.transfer(transferAmt, destAccount);
//            };
//        } while (sum != 3);
        while (srcAccount.withdraw(withdrawalAmt) == 0);
        while (srcAccount.deposit(depositAmt) == 0);
        if (destAccount != null) {
            while (srcAccount.transfer(transferAmt, destAccount) == 0);
        }
    }
}
