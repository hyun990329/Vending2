package dto;

public class Drink {
    private int productId;
    private String ProductName;
    private int price;
    private int stock;
    private int sales = 0;


    // 금액 누적
    public void sumPrice() {
        this.sales = sales + price;
    }

    // 재고 감소
    public void decreaseStock() {
        if (stock > 0) {
            stock--;
        }
    }

    @Override
    public String toString() {
        return "Drink{" +
                "productId='" + productId + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", sales=" + sales +
                '}';
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
}
