package service;

import db.DBConn;
import dto.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class CardService {
    Card card = new Card();

    // Db 연결하기
    Connection conn = DBConn.getConnection();
    PreparedStatement psmt = null;
    String sql;



    private Scanner sc = new Scanner(System.in);

    public void insertCoin(String memberId) {
        int cardId = 0;
        int currentBalance = 0;

        try {
            sql = "SELECT card_id FROM member WHERE member_id = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, memberId);
            var rs = psmt.executeQuery();

            if (rs.next()) {
                cardId = rs.getInt("card_id");
            } else {
                System.out.println("해당 회원이 존재하지 않습니다");
                return;
            }
            rs.close();
            psmt.close();

            sql = "SELECT balance FROM card WHERE card_id = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, cardId);
            rs = psmt.executeQuery();

            if (rs.next()) {
                currentBalance = rs.getInt("balance");
            } else {
                System.out.println("해당 회원의 카드 정보가 없습니다");
                return;
            }
            rs.close();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int chargeCoin = 0;
        while (true) {
            System.out.println("충전할 금액을 입력하세요");
            chargeCoin = sc.nextInt();

            int updateBalance = currentBalance + chargeCoin;

            try {
                sql = "update card set balance = ? where member_id = ?";
                psmt = conn.prepareStatement(sql);
                psmt.setInt(1, updateBalance);
                psmt.setInt(2, cardId);
                int result = psmt.executeUpdate();
                psmt.close();

                if (result > 0) {
                    System.out.println("잔액 : " + updateBalance);
                    currentBalance = updateBalance;
                } else {
                    System.out.println("잔액 업데이트 실패");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateBalance(Card card) {

    }
}
