package banking;

import java.util.*;

public class Account {

    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    private String cardNumber;
    private String pin;
    private List<String> cardList = new ArrayList<String>();

    public String getNewCardNumber() {
        return cardNumber;
    }

    public String getNewPin() {
        return pin;
    }

    public String newCardNumber(String cardNumber) {
        String bin = "400000";
        int lowerAccNr = 100000;
        int upperAccNr = 999999;
        int accNr = random.nextInt(upperAccNr - lowerAccNr + 1) + lowerAccNr;
        int lowerChecksum = 1000;
        int upperChecksum = 9999;
        int checksum = random.nextInt(upperChecksum - lowerChecksum + 1) + lowerChecksum;
        cardNumber = bin + String.valueOf(accNr) + String.valueOf(checksum);
        return cardNumber;
    }

    public String newPin(String pin) {
        int lower = 1000;
        int upper = 9999;
        pin = String.valueOf(random.nextInt(upper - lower + 1) + lower);

        return pin;
    }

    public void cardList(String cardNumber, String pin){
        cardList.add(cardNumber);
        cardList.add(pin);
    }

    public void pinCheck() {

        System.out.println("Enter your card number:");
        String cardNumberCheck = scanner.next();
        System.out.println("Enter your PIN:");
        String pinNumberCheck = scanner.next();

        for (String number : cardList) {
            if (number.equals(cardNumberCheck) && cardList.get(cardList.indexOf(number)+1).equals(pinNumberCheck)) {
                System.out.println("You have successfully logged in!");
                accountMenu();
            } else {
                System.out.println("Wrong card number or PIN!");
            }
        }
    }

    public void accountMenu() {

        String accountMenuSelect = "";

        while(!accountMenuSelect.equals("0")) {

            System.out.println("1. Balance \n" +
                    "2. Log out \n" +
                    "0. Exit");

            accountMenuSelect = scanner.next();

            switch (accountMenuSelect) {

                case "1":
                    System.out.println("Balance: 0");
                    break;

                case "2":
                    System.out.println("You have successfully logged out!");
                    mainMenu();
                    break;

                case "0":
                    System.out.println("Bye!");
                    break;
            }
        }
    }

    public void mainMenu() {

        String mainMenuSelect = " ";

        while (!mainMenuSelect.equals("0")) {

            System.out.println("1. Create an account \n" +
                    "2. Log into account \n" +
                    "0. Exit");

            mainMenuSelect = scanner.next();

            if (mainMenuSelect.equals("1")) {

                System.out.println("Your card has been created");
                cardNumber = newCardNumber(cardNumber);
                System.out.println("Your card number:");
                System.out.println(getNewCardNumber());
                pin = newPin(pin);
                System.out.println("Your card PIN:");
                cardList(cardNumber, pin);
                System.out.println(getNewPin());

            } else if (mainMenuSelect.equals("2")) {
                pinCheck();
            }
        }
    }

}