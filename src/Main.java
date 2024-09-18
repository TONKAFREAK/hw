import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

    public static int globalTransNum = (int)(Math.random() * 327);
    public static int lastDiscountIndex = 0;

    /*
     * REads Cusnomer info from the given file
     * returns array of customer Objects
    */
    public static Customer[] readCustomers(File file) {

        try {

            boolean isDuplicate = false;

            Scanner accReader = new Scanner(file);

            Customer[] customers = new Customer[7];

            int i = 0;
        
            while (accReader.hasNext()) {
                isDuplicate = false;
                String line = accReader.nextLine();
                String[] lineSplit = line.split(",");

                //System.out.println(lineSplit[0] + " " + lineSplit[1] + " " + lineSplit[2]);

                if (lineSplit[0].length() != 4) {
                    System.out.println("Invalid Customer Number: " + lineSplit[0] + " must be 4 digits.");
                    continue;
                }

                if (lineSplit[1].length() > 20) {
                    System.out.println("Invalid Customer Name: " + lineSplit[1] + " must be less than 20 characters.");
                    continue;
                }

                if (lineSplit.length != 3) {
                    System.out.println("Missing required account information for customer, please check your master.txt file.");
                    continue;
                    
                }

                if (customers != null) {

                    for (int j = 0; j < customers.length; j++) {

                        if (customers[j] != null && customers[j].customerNum == Integer.parseInt(lineSplit[0])) {
                            System.out.println("Duplicate Customer Number: " + lineSplit[0]);
                            isDuplicate = true;
                        }
                    }
                }
                
                if (!isDuplicate) {

                    Customer customer = new Customer(Integer.parseInt(lineSplit[0]), lineSplit[1], Double.parseDouble(lineSplit[2]));

                    customers[i] = customer;

                } else {

                    continue;
                }

                i++;
                
            }
            accReader.close();
            return customers;
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return null;
        }
        

    }

    /*
     * searches the array of customer object for the given customer NUmber than returns the index of the customer
    */
    public static int getCustomerIndex(Customer[] customers, int customerNum) {
        if (customers == null) return -1;
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] != null && customers[i].customerNum == customerNum) {
                return i;
            }
        }
        return -1;
    }
    

    /*
     * Reads the discounds from the given file.
     * Remembers the last discount index
     * returns the next discount
    */
    public static int getDiscount(File file){

        try {

        Scanner discReader = new Scanner(file);

        int discountIndex = 0;
        while (discReader.hasNext()) {
            int discount = discReader.nextInt();
            //System.out.println(discount);

            if ( discountIndex == lastDiscountIndex) {
                lastDiscountIndex++;
                //System.out.println("Discount: " + discount);
                discReader.close();
                return discount;
            }

            discountIndex++;

        }
        
        discReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    /*
     * Reads the transactions from the given transtactions file
     * Adds the transcations to the customers object if the customer number matches.
    */
    public static void readTransactions(File transFile, Customer[] customers, File discountsFile) {

        try {

        Scanner transReader = new Scanner(transFile);

        while (transReader.hasNext()) {
            String line = transReader.nextLine();
            String[] lineSplit = line.split(",");

            char type = lineSplit[0].charAt(0);

            int transNum = globalTransNum;
            globalTransNum+=1;

            int customerNum = Integer.parseInt(lineSplit[1]);

            Transaction transaction;
            int customerIndex = getCustomerIndex(customers, customerNum);

            if (customerIndex == -1) {
                System.out.println("Invalid Customer Number: " + customerNum);
                continue;
            }

            if (type == 'P') {
                double price = Double.parseDouble(lineSplit[2]);
                transaction = new Transaction(type, transNum, null, 0, price);
                int discount = getDiscount(discountsFile);
                //System.out.println(discount);
                transaction.setDiscount(discount);
                
                
                customers[customerIndex].addTransaction(transaction);
            } else {
                String item = lineSplit[2];
                int quantity = Integer.parseInt(lineSplit[3]);
                Double price = Double.parseDouble(lineSplit[4]);
                transaction = new Transaction(type, transNum, item, quantity, price);
                customers[customerIndex].addTransaction(transaction);
            }

        }

        transReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * Writes the Invoice to the given file.
     * gets all the necessary information from the customer object
    */    
    public static void printInvoices(Customer[] customers, File invoicesFile) {

        try {
            FileWriter fw = new FileWriter(invoicesFile);
            
            for (int i = 0; i < customers.length; i++) {

                fw.write("Name: "+customers[i].name + "\t" + "Account Number: "+customers[i].customerNum + "\n\n"
                + "\t\t\t\t\tPrevious Balance: $" + customers[i].balance + "\n\n");

                for (int j = 0; j < customers[i].transactions.size(); j++) {

                    if (customers[i].getTransaction(j).type == 'O') {

                        fw.write("Transaction number: "+ customers[i].getTransaction(j).transNum + "\t\t\t" + 
                                    "Item Ordered: " + customers[i].getTransaction(j).item + "\t\t"+
                                    "Quantity: " + customers[i].getTransaction(j).quantity + "\t\t"+
                                    
                                    "Order Amount: $" + String.format("%.2f", customers[i].getTransaction(j).quantity * customers[i].getTransaction(j).price) + "\n");
                        customers[i].balance += customers[i].getTransaction(j).quantity * customers[i].getTransaction(j).price;
                    } else {

                        double givenDiscount = customers[i].getTransaction(j).getDiscount() * customers[i].getTransaction(j).price / 100;
                        fw.write("Transaction number: "+ customers[i].getTransaction(j).transNum + "\t\t\t" +
                                    "Payment Amount: $"+ String.format("%.2f", customers[i].getTransaction(j).price )+"\t\t"+ 
                                    "Payment w discount: $"+ String.format("%.2f",givenDiscount + customers[i].getTransaction(j).price)+" (with discount of "+customers[i].getTransaction(j).getDiscount()+"%)" + "\n");

                        customers[i].balance -= givenDiscount + customers[i].getTransaction(j).price;
                    }

                }

                fw.write("\n\t\t\t\t\tBalance Due: $" + String.format("%.2f", customers[i].balance) + "\n\n");
            }

            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    File masterFile = new File("master.txt");
    File transactionsFile = new File("transactions.txt");
    File discountsFile = new File("discounts.txt");
    File invoicesFile = new File("invoices.txt");

    Customer[] customers = readCustomers(masterFile);
    readTransactions(transactionsFile, customers, discountsFile);
    //System.out.println(customers[0].toString());
    printInvoices(customers, invoicesFile);
    
    }
}
