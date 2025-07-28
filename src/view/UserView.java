package view;

import dto.Member;
import service.CardService;
import service.UserService;

import java.util.Scanner;

public class UserView {

    private Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();
    CardService cardService = new CardService();

    public void userScreen() {

        while (true) {
            System.out.println("1. 로그인 2. 회원가입 3. 돌아가기");
            int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1:
                    // 로그인
                    int loginResult = userService.login();
                    if (loginResult == 1) {
                        afterLogin();
                    }
                    break;
                case 2:
                    // 회원가입
                    userService.signUp(sc);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("잘 못 입력 하셨습니다");
                    return;
            }
        }
    }

    public void afterLogin() {
        while (true) {
            System.out.println("1. 구매 2. 잔액충전 3. 잔액조회 4. 돌아가기");
            int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1:
                    // 구매 화면 띄우기
                    userService.buy();
                    break;
                case 2:
                    // 잔액 충전 화면 띄우기
                    userService.charge();
                    break;
                case 3:
                    // 잔액 조회
                    cardService.viewBalance(userService.getMember().getMemberId());
                    break;
                case 4:
                    return;
                default:
                    System.out.println("잘 못 입력 하셨습니다");
                    return;
            }
        }
    }
}
