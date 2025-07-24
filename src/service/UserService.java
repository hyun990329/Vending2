package service;

import db.DBConn;
import dto.Card;
import dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Scanner;

public class UserService {
    private Scanner sc = new Scanner(System.in);
    Member member = new Member();
    Card card = new Card();
    ProductService productService = new ProductService();
    // Db 연결하기
    Connection conn = DBConn.getConnection();
    PreparedStatement psmt = null;
    String sql;

    public int login() {
        System.out.println("[로그인]");
        System.out.print("아이디: ");
        String id = sc.next();
        System.out.print("비밀번호: ");
        String pw = sc.next();

        Member member = checkLogin(id, pw);
        if (member != null) {
            System.out.println("로그인 성공! " + member.getName() + "님 환영합니다.");
        } else {
            System.out.println("로그인 실패!");
        }
        return 0;
    }

    public void signUp(Scanner sc) {
        System.out.println("[회원가입]");
        System.out.print("아이디: ");
        String id = sc.nextLine();
        System.out.print("비밀번호: ");
        String pw = sc.nextLine();
        System.out.print("이름: ");
        String name = sc.nextLine();
        System.out.print("전화번호: ");
        String phone = sc.nextLine();
        System.out.print("카드번호 : ");
        String creditNumber = sc.nextLine();

        Member member = new Member();
        member.setMemberId(id);
        member.setPassword(pw);
        member.setName(name);
        member.setPhone(phone);
        member.setCreditNumber(creditNumber);

        boolean result = insertMember(member);
        if (result) {
            System.out.println("회원가입 성공!");
        } else {
            System.out.println("회원가입 실패!");
        }
    }

    public Member checkLogin(String id, String pw) {
        try {
            sql = "SELECT * FROM member WHERE member_id = ? AND password = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
            psmt.setString(2, pw);
            var rs = psmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setPassword(rs.getString("password"));
                member.setName(rs.getString("name"));
                member.setPhone(rs.getString("phone"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    member.setCreatedAt(ts.toLocalDateTime());
                } else {
                    member.setCreatedAt(null);
                }
                return member;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertMember(Member member) {
        String memberInsertSql = "INSERT INTO member (member_id, password, name, phone) VALUES (?, ?, ?, ?)";
        String cardInsertSql = "INSERT INTO card (member_id, balance) VALUES (?, 0)";
        String memberUpdateSql = "UPDATE member SET card_id = ? WHERE member_id = ?";

        PreparedStatement psMemberInsert = null;
        PreparedStatement psCardInsert = null;
        PreparedStatement psMemberUpdate = null;

        try {
            conn.setAutoCommit(false);

            // 1) member insert (card_id 없이)
            psMemberInsert = conn.prepareStatement(memberInsertSql);
            psMemberInsert.setString(1, member.getMemberId());
            psMemberInsert.setString(2, member.getPassword());
            psMemberInsert.setString(3, member.getName());
            psMemberInsert.setString(4, member.getPhone());
            int resultMember = psMemberInsert.executeUpdate();

            if (resultMember != 1) {
                conn.rollback();
                return false;
            }

            // 2) card insert
            psCardInsert = conn.prepareStatement(cardInsertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            psCardInsert.setString(1, member.getMemberId());
            int resultCard = psCardInsert.executeUpdate();

            if (resultCard != 1) {
                conn.rollback();
                return false;
            }

            // 3) 생성된 card_id 받기
            var rs = psCardInsert.getGeneratedKeys();
            int cardId = -1;
            if (rs.next()) {
                cardId = rs.getInt(1);
            }
            rs.close();

            if (cardId == -1) {
                conn.rollback();
                return false;
            }

            // 4) member update card_id
            psMemberUpdate = conn.prepareStatement(memberUpdateSql);
            psMemberUpdate.setInt(1, cardId);
            psMemberUpdate.setString(2, member.getMemberId());
            int updateResult = psMemberUpdate.executeUpdate();

            if (updateResult != 1) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (psMemberInsert != null) psMemberInsert.close();
                if (psCardInsert != null) psCardInsert.close();
                if (psMemberUpdate != null) psMemberUpdate.close();
                conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void buy () {
        // 음료 구매
        productService.viewMenu();
        System.out.println("주문할 번호를 입력하세요");
        int orderNumber = sc.nextInt();

        if (orderNumber == 9) {
            return;
        } else {
            productService.orderSuccess(orderNumber, card);
            System.out.println(orderNumber);
        }
    }

    public void charge () {
        // 카드 잔액 충전
        CardService cardService = new CardService();
        cardService.insertCoin(member.getMemberId());
    }
}

