package service;

import db.DBConn;
import dto.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class CardService {
    Card card = new Card();
    private Scanner sc = new Scanner(System.in);

    // Db 연결하기
    Connection conn = DBConn.getConnection();
    PreparedStatement psmt = null;
    String sql;



    public void insertCoin(String memberId) {
        Scanner sc = new Scanner(System.in);

        System.out.println("[잔액 충전]");
        System.out.print("충전할 금액을 입력하세요: ");
        int amount = sc.nextInt();

        sql = "UPDATE card SET balance = balance + ? WHERE member_id = ?";

        try {
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, amount);
            psmt.setString(2, memberId);
            int result = psmt.executeUpdate();

            if (result == 1) {
                System.out.println("잔액이 " + amount + "원 충전되었습니다.");
            } else {
                System.out.println("잔액 충전에 실패했습니다. 회원 정보를 확인하세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류가 발생했습니다.");
        } finally {
            try {
                if (psmt != null) psmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void viewBalance(String memberId) {
        try {
            sql = "SELECT balance FROM card WHERE member_id = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, memberId);

            var rs = psmt.executeQuery();
            if (rs.next()) {
                int balance = rs.getInt("balance");
                System.out.println("현재 잔액: " + balance + "원");
            } else {
                System.out.println("회원의 카드 정보가 없습니다.");
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("잔액 조회 중 오류가 발생했습니다.");
        }
    }

    public Card loadCardInfo(String memberId) {
        Card card = new Card();
        try {
            sql = "SELECT * FROM card WHERE member_id = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, memberId);
            var rs = psmt.executeQuery();

            if (rs.next()) {
                card.setCardNumber(rs.getString("card_id"));
                card.setMemberId(rs.getString("member_id"));
                card.setBalance(rs.getInt("balance"));
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

    public void updateBalance(Card card) {
        sql = "UPDATE card SET balance = ? WHERE card_id = ?";

        try {
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, card.getBalance());
            psmt.setString(2, card.getCardNumber());
            int result = psmt.executeUpdate();

            if (result == 1) {
                System.out.println("잔액이 DB에 반영되었습니다.");
            } else {
                System.out.println("잔액 업데이트에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("잔액 업데이트 중 오류가 발생했습니다.");
        } finally {
            try {
                if (psmt != null) psmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
