package service;

import dto.Card;
import dto.Drink;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public static List<Drink> drinkList = new ArrayList<>();
    Drink drink = new Drink();

    public void productInit() {
        Drink water = new Drink();
        water.setProductId(1);
        water.setProductName("물");
        water.setPrice(700);
        water.setStock(10);
        drinkList.add(water);

        Drink coffee = new Drink();
        coffee.setProductId(2);
        coffee.setProductName("커피");
        coffee.setPrice(1200);
        coffee.setStock(10);
        drinkList.add(coffee);

        Drink coke = new Drink();
        coke.setProductId(3);
        coke.setProductName("콜라");
        coke.setPrice(1300);
        coke.setStock(10);
        drinkList.add(coke);
    }

    public void viewMenu() {
        productInit();
        System.out.println("=========================");
        drinkList.stream().forEach(x -> System.out.println(x));
        System.out.println("=========================");
        System.out.println("9. 돌아가기");
    }

    public void orderSuccess(int orderNumber, Card card) {
        Drink orderDrink = ProductService.drinkList.get(orderNumber);

        if (orderDrink.getStock() <= 0) {
            System.out.println("재고가 부족합니다");
            return;
        } else {
            if (card.getBalance() < orderDrink.getPrice()) {
                System.out.println("잔액이 부족합니다");
                return;
            } else {
                orderDrink = ProductService.drinkList.get(orderNumber);
                String orderMenu = orderDrink.getProductName();
                orderDrink.sumPrice();
                card.setBalance(card.getBalance() - orderDrink.getPrice());
                drink.decreaseStock();
                System.out.println(orderDrink);
                System.out.println("주문이 완료 되었습니다");
                System.out.println(orderMenu + "를 받아가세요");
                System.out.println("잔액 : " + card.getBalance());
            }
        }
    }


}
