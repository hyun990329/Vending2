package dto;

public class Sale {
    private int saleId;
    private String memberId;
    private int productId;
    private int stack;
    private int salePrice;

    public Sale() {}

    public Sale(int saleId, String memberId, int productId, int quantity, int salePrice) {
        this.saleId = saleId;
        this.memberId = memberId;
        this.productId = productId;
        this.stack = quantity;
        this.salePrice = salePrice;
    }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getStack() { return stack; }
    public void setStack(int stack) { this.stack = stack; }

    public int getSalePrice() { return salePrice; }
    public void setSalePrice(int salePrice) { this.salePrice = salePrice; }
}
