package view;

import service.AdminService;
import service.ProductService;

import java.util.Scanner;

public class AdminView {
    AdminService adminService = new AdminService();
    ProductService productService = new ProductService();
    private Scanner sc = new Scanner(System.in);


    public void adminScreen() {

        while (true) {
            System.out.println("1. 자판기 관리 2. 회원 관리 3. 판매 관리 4. 돌아가기");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    // 자판기 관리
                    vendingManagement();
                    break;
                case 2:
                    // 회원 관리
                    memberManagement();
                    break;
                case 3:
                    // 판매 관리
                    salesManagement();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("잘 못 입력 하셨습니다");
                    return;
            }
        }
    }

    public void vendingManagement() {
        while (true) {
            System.out.println("1. 추가 2. 수정 3. 삭제 4. 조회 5. 돌아가기");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    // 제품 추가
                    adminService.menuInsert();
                    break;
                case 2:
                    // 제품 수정
                    adminService.menuUpdate();
                    break;
                case 3:
                    // 제품 삭제
                    adminService.menuDelete();
                    break;
                case 4:
                    // 제품 조회
                    productService.viewMenu();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘 못 입력 하셨습니다");
                    return;
            }
        }
    }


    public void memberManagement() {
        while (true) {
            System.out.println("1. 수정 2. 삭제 3. 조회 4. 돌아가기");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    //  회원 정보 수정
                    adminService.memberUpdate();
                    break;
                case 2:
                    // 회원 정보 삭제
                    adminService.memberDelete();
                    break;
                case 3:
                    // 회원 정보 조회
                    adminService.memberList();
                    break;
                case 4:
                    // 돌아가기
                    return;
                default:
                    System.out.println("잘 못 입력 하셨습니다");
                    return;
            }
        }
    }


    public void salesManagement() {
        while (true) {
            System.out.println("1. 제품 별 판매 현황 2. 회원 별 판매 현황 3. 돌아가기");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    //  제품 별 판매 현황
                    adminService.productSalesSummary();
                    break;
                case 2:
                    // 회원 별 판매 현황
                    adminService.memberSalesSummary();
                    break;
                case 3:
                    // 돌아가기
                    return;
                default:
                    System.out.println("잘 못 입력 하셨습니다");
                    return;
            }
        }
    }
}
