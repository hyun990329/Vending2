package service;

import db.DBConn;
import dto.Card;
import dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Scanner;
import java.sql.ResultSet;

public class UserService {
    private Scanner sc = new Scanner(System.in);
    Member member = new Member();
    Card card = new Card();
    CardService cardService = new CardService();
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

        Member loginMember = checkLogin(id, pw);
        if (loginMember != null) {
            this.member = loginMember;
            this.card = cardService.loadCardInfo(member.getMemberId());
            System.out.println("로그인 성공! " + loginMember.getName() + "님 환영합니다.");
            return 1;
        } else {
            System.out.println("로그인 실패!");
            return 0;
        }
    }
    public Member getMember() {
        return member;
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

            boolean cardResult = insertCard(id);
            if (cardResult) {
                System.out.println("카드 등록이 완료 되었습니다");
            } else {
                System.out.println("회원가입은 성공했지만 카드 등록은 실패했습니다");
            }

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
        String sql = "INSERT INTO member (member_id, password, name, phone) VALUES (?, ?, ?, ?)";
        PreparedStatement psmt = null;

        try {
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, member.getMemberId());
            psmt.setString(2, member.getPassword());
            psmt.setString(3, member.getName());
            psmt.setString(4, member.getPhone());

            int result = psmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psmt != null) psmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean insertCard(String memberId) {
        PreparedStatement psSelect = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        ResultSet keyRs = null;

        try {
            conn.setAutoCommit(false);

            // 1. 회원 존재 확인
            sql = "SELECT * FROM member WHERE member_id = ?";
            psSelect = conn.prepareStatement(sql);
            psSelect.setString(1, memberId);
            rs = psSelect.executeQuery();

            if (!rs.next()) {
                System.out.println("회원이 존재하지 않아 카드를 등록할 수 없습니다.");
                return false;
            }

            // 2. 카드 INSERT
            sql = "INSERT INTO card (member_id, balance) VALUES (?, 0)";
            psInsert = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            psInsert.setString(1, memberId);
            int result = psInsert.executeUpdate();

            if (result != 1) {
                conn.rollback();
                System.out.println("카드 등록 실패 (insert 실패)");
                return false;
            }

            // 3. 생성된 카드 ID 가져오기
            keyRs = psInsert.getGeneratedKeys();
            int cardId = -1;
            if (keyRs.next()) {
                cardId = keyRs.getInt(1);
            }

            if (cardId == -1) {
                conn.rollback();
                System.out.println("카드 ID를 가져오지 못했습니다.");
                return false;
            }

            // 4. member 테이블에 card_id 업데이트
            sql = "UPDATE member SET card_id = ? WHERE member_id = ?";
            psUpdate = conn.prepareStatement(sql);
            psUpdate.setInt(1, cardId);
            psUpdate.setString(2, memberId);
            int updateResult = psUpdate.executeUpdate();

            if (updateResult != 1) {
                conn.rollback();
                System.out.println("member 테이블에 card_id 업데이트 실패");
                return false;
            }

            conn.commit();
            System.out.println("카드 등록 성공!");
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
                if (rs != null) rs.close();
                if (keyRs != null) keyRs.close();
                if (psSelect != null) psSelect.close();
                if (psInsert != null) psInsert.close();
                if (psUpdate != null) psUpdate.close();
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
            productService.orderSuccess(orderNumber - 1 , card);
            System.out.println(orderNumber);
        }
    }

    public void charge () {
        // 카드 잔액 충전
        CardService cardService = new CardService();
        cardService.insertCoin(member.getMemberId());
    }
}

