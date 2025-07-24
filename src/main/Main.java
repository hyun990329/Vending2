package main;
import view.AdminView;
import view.UserView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        UserView userView = new UserView();
        AdminView adminView = new AdminView();

        while (true) {
            System.out.println("1. 사용자 2. 관리자 3. 종료");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    // UserView로 이동
                    userView.userScreen();
                    break;
                case 2:
                    // AdminView로 이동
                    System.out.print("관리자 비밀번호를 입력하세요 : ");
                    String passWord = sc.next();
                    if (passWord.equals("1111")) {
                        adminView.adminScreen();
                    }
                    break;
                case 3:
                    // 종료
                    return;
            }
        }
    }
}
