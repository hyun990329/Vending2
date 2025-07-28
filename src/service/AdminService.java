package service;

import db.DBConn;
import dto.Drink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AdminService {
    private Scanner sc = new Scanner(System.in);
    ProductService productService = new ProductService();

    // Db 연결하기
    Connection conn = DBConn.getConnection();
    PreparedStatement psmt = null;
    String sql;


    public void menuInsert() {
        System.out.println("메뉴 추가 서비스");
        productService.viewMenu();
        System.out.print("추가할 제품 ID를 입력하세요 : ");
        int newProductId = sc.nextInt();
        System.out.print("추가할 제품 이름을 입력하세요 : ");
        String newProductName = sc.next();
        System.out.print("추가할 제품 가격을 입력하세요 : ");
        int newPrice = sc.nextInt();
        System.out.print("추가할 제품 제고를 입력하세요 : ");
        int newStock = sc.nextInt();

        // 비어있는 Drink 생성 후 정보 입력, List에 추가
        Drink drink = new Drink();
        drink.setProductId(newProductId);
        drink.setProductName(newProductName);
        drink.setPrice(newPrice);
        drink.setStock(newStock);
        ProductService.drinkList.add(drink);
        System.out.println("======================");
        productService.insertProduct(drink);
        System.out.println("제품이 성공적으로 추가되었습니다.");
        productService.productInit(); // 최신화
        productService.viewMenu();
    }

    public void menuUpdate() {
        System.out.println("메뉴 수정 서비스");
        productService.viewMenu();
        System.out.print("수정할 제품 번호를 입력하세요 : ");
        int updateNumber = sc.nextInt() - 1;
        if (updateNumber < 0 || updateNumber >= ProductService.drinkList.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }

        // 수정
        System.out.print("수정할 제품 이름을 입력하세요 : ");
        String updateName = sc.next();
        System.out.print("수정할 제품 가격을 입력하세요 : ");
        int updatePrice = sc.nextInt();
        System.out.print("수정할 제품 재고 수량을 입력하세요 : ");
        int updateStock = sc.nextInt();

        Drink drink = ProductService.drinkList.get(updateNumber);
        drink.setPrice(updatePrice);
        drink.setProductName(updateName);
        drink.setStock(updateStock);
        ProductService.drinkList.set(updateNumber, drink);

        // DB에 제품 수정 반영
        productService.updateProduct(drink);

        System.out.println("제품이 성공적으로 수정되었습니다.");
        productService.productInit(); // 최신화
        productService.viewMenu();
    }

    public void menuDelete() {
        productService.viewMenu();
        System.out.print("삭제할 메뉴 번호를 입력하세요 : ");
        int deleteNumber = sc.nextInt() - 1;  // 1 빼서 인덱스 맞춤

        if (deleteNumber < 0 || deleteNumber >= ProductService.drinkList.size()) {
            System.out.println("잘못된 메뉴 번호입니다.");
            return;
        }

        int productId = ProductService.drinkList.get(deleteNumber).getProductId();

        // DB에서 제품 삭제
        productService.deleteProduct(productId);
        System.out.println("정상적으로 삭제되었습니다.");
        productService.productInit(); // 최신화
        productService.viewMenu();
    }

    public void memberList() {
        sql = "SELECT * FROM member";
        try {
            psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            System.out.println("회원 목록:");
            while (rs.next()) {
                System.out.printf("ID: %s, 이름: %s, 전화: %s\n",
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("phone"));
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void memberUpdate() {
        System.out.print("수정할 회원 아이디 입력: ");
        String id = sc.next();

        System.out.print("새 이름 입력: ");
        String name = sc.next();
        System.out.print("새 전화번호 입력: ");
        String phone = sc.next();

        sql = "UPDATE member SET name = ?, phone = ? WHERE member_id = ?";
        try {
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, name);
            psmt.setString(2, phone);
            psmt.setString(3, id);
            int result = psmt.executeUpdate();
            if (result == 1) {
                System.out.println("회원 정보가 수정되었습니다");
            } else {
                System.out.println("회원 정보를 찾을 수 없습니다");
            }
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void memberDelete() {
        System.out.print("삭제할 회원 아이디 입력: ");
        String id = sc.next();

        sql = "DELETE FROM member WHERE member_id = ?";
        try {
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
            int result = psmt.executeUpdate();
            if (result == 1) {
                System.out.println("회원이 삭제되었습니다");
            } else {
                System.out.println("삭제할 회원을 찾을 수 없습니다");
            }
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productSalesSummary() {
        sql = "SELECT p.product_id, p.product_name, SUM(s.quantity) AS total_qty " +
                "FROM sale s JOIN product p ON s.product_id = p.product_id " +
                "GROUP BY p.product_id, p.product_name " +
                "ORDER BY total_qty DESC";

        try {
            psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            System.out.println("🔎 제품 별 판매 현황");
            while (rs.next()) {
                String name = rs.getString("product_name");
                int total = rs.getInt("total_qty");
                System.out.printf("제품: %s, 총 판매 수량: %d\n", name, total);
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void memberSalesSummary() {
        sql = "SELECT m.member_id, m.name, SUM(s.quantity) AS total_qty " +
                "FROM sale s JOIN member m ON s.member_id = m.member_id " +
                "GROUP BY m.member_id, m.name " +
                "ORDER BY total_qty DESC";

        try {
            psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            System.out.println("🔎 회원 별 판매 현황");
            while (rs.next()) {
                String memberId = rs.getString("member_id");
                String name = rs.getString("name");
                int total = rs.getInt("total_qty");
                System.out.printf("회원: %s(%s), 총 구매 수량: %d\n", name, memberId, total);
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
