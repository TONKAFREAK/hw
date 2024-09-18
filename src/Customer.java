import java.util.ArrayList;

/*
 * Stores Customer Infiormation such as:
 * Int : Customer Number
 * String : Name
 * Double : Balance
 * Transaction[] : Transactions
*/
public class Customer {
    int customerNum;
    String name;
    Double balance;
    ArrayList<Transaction> transactions;


    public Customer(int customerNum, String name, Double balance) {
        this.customerNum = customerNum;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction); 
        System.out.println("Transaction added: " + transaction);
    }

    public Transaction getTransaction(int index) {
        return transactions.get(index);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String toString() {
        return "Account information: " + customerNum + " " + name + " " + balance +" "+transactions.get(1).discount;
    }

}