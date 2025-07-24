package dto;

public class Card {
    private String cardNumber;
    private String memberId;
    private int balance;

    public Card() {}

    public Card(String cardNumber, String memberId, int balance) {
        this.cardNumber = cardNumber;
        this.memberId = memberId;
        this.balance = balance;
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }
}
