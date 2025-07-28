package service;

import db.DBConn;
import dto.Card;
import dto.Drink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public static List<Drink> drinkList = new ArrayList<>();
    Drink drink = new Drink();

    // Db 연결하기
    Connection conn = DBConn.getConnection();
    PreparedStatement psmt = null;
    String sql;

    public void productInit() {
        drinkList.clear();
        try {
            sql = "SELECT * FROM product";
            psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                Drink d = new Drink();
                d.setProductId(rs.getInt("product_id"));
                d.setProductName(rs.getString("product_name"));
                d.setPrice(rs.getInt("price"));
                d.setStock(rs.getInt("stock"));
                drinkList.add(d);
            }

            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewMenu() {
            productInit();
            System.out.println("=========================");
            drinkList.stream().forEach(x -> System.out.println(x));
            System.out.println("=========================");
    }

    public void orderSuccess(int orderNumber, Card card) {
        Drink orderDrink = drinkList.get(orderNumber);
        if (orderDrink.getStock() <= 0) {
            System.out.println("재고가 부족합니다");
            return;
        }
        if (card.getBalance() < orderDrink.getPrice()) {
            System.out.println("잔액이 부족합니다");
            return;
        }

        String orderMenu = orderDrink.getProductName();
        orderDrink.sumPrice();
        card.setBalance(card.getBalance() - orderDrink.getPrice());
        orderDrink.decreaseStock();

        CardService cardService = new CardService();
        cardService.updateBalance(card);

        insertSale(card.getMemberId(), orderDrink.getProductId(), 1, orderDrink.getPrice());
        updateProduct(orderDrink); // 재고 감소 반영

        System.out.println(orderDrink);
        System.out.println("주문이 완료 되었습니다");
        System.out.println(orderMenu + "를 받아가세요");
        System.out.println("잔액 : " + card.getBalance());
    }

    public void insertSale(String memberId, int productId, int quantity, int salePrice) {
        if (salePrice <= 0) {
            System.out.println("[판매 금액이 0 이하입니다 - 기록 안 함]");
            return;
        }

        try {
            sql = "INSERT INTO sale (member_id, product_id, quantity, sale_price) VALUES (?, ?, ?, ?)";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, memberId);
            psmt.setInt(2, productId);
            psmt.setInt(3, quantity);
            psmt.setInt(4, salePrice);
            psmt.executeUpdate();
            psmt.close();
            System.out.println("[판매 기록 저장 완료]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insertProduct(Drink drink) {
        try {
            sql = "INSERT INTO product (product_id, product_name, price, stock) VALUES (?, ?, ?, ?)";
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, drink.getProductId());
            psmt.setString(2, drink.getProductName());
            psmt.setInt(3, drink.getPrice());
            psmt.setInt(4, drink.getStock());
            psmt.executeUpdate();
            psmt.close();
            System.out.println("제품이 DB에 추가되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Drink drink) {
        try {
            sql = "UPDATE product SET product_name = ?, price = ?, stock = ? WHERE product_id = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, drink.getProductName());
            psmt.setInt(2, drink.getPrice());
            psmt.setInt(3, drink.getStock());
            psmt.setInt(4, drink.getProductId());
            psmt.executeUpdate();
            psmt.close();
            System.out.println("제품이 DB에 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productId) {
        try {
            sql = "DELETE FROM product WHERE product_id = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, productId);
            psmt.executeUpdate();
            psmt.close();
            System.out.println("제품이 DB에서 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
