/*
 * Stores Transaction Information such as:
 * char : type
 * int : transNum
 * String : item
 * int : quantity
 * Double : amount
 * int : discount
*/
public class Transaction {

    char type;
    int transNum;
    String item;
    int quantity;
    Double price;
    int discount;

    public Transaction(char type, int transNum, String item, int quantity, Double price) {
        this.type = type;
        this.transNum = transNum;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

}
