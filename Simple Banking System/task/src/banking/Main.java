package banking;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Account account = new Account(args[1]);
        CardDatabaseSqlite dao = new CardDatabaseSqlite(args[1]);

        String selectStartMenu = " ";
        String cardNumberCheck;
        String pinNumberCheck;
        int money;

        while (!selectStartMenu.equals("0")) {

            startMenu();
            selectStartMenu = scanner.next();

            switch (selectStartMenu) {
                case "1": //create new card
                    System.out.println("Your card has been created");
                    account.createNewCardNumber();
                    System.out.println("Your card number:");
                    System.out.println(account.getNewCardNumber());
                    account.crateNewPin();
                    System.out.println("Your card PIN:");
                    dao.insertAccountInTable(account);
                    dao.listAll().put(account.getId(), account);
                    System.out.println(account.getNewPin());
                    break;

                case "2": // log into account
                    System.out.println("Enter your card number:");
                    cardNumberCheck = scanner.next();
                    System.out.println("Enter your PIN:");
                    pinNumberCheck = scanner.next();

                    if (!dao.checkLogInAccount(cardNumberCheck, pinNumberCheck)) {
                        System.out.println("Wrong card number or PIN number!");
                    } else if (dao.checkLogInAccount(cardNumberCheck, pinNumberCheck)) {
                        System.out.println("You have successfully logged in!");

                        String selectAccountMenu = "";

                        while (!selectAccountMenu.equals("0")) {

                            System.out.println("1. Balance \n" +
                                    "2. Add income \n" +
                                    "3. Do transfer \n" +
                                    "4. Close account \n" +
                                    "5. Log out \n" +
                                    "0. Exit");

                            selectAccountMenu = scanner.next();

                            switch (selectAccountMenu) {
                                case "1": //Balance
                                    System.out.println(dao.printCurrentBalance(cardNumberCheck));
                                    break;
                                case "2": //add income
                                    System.out.println("Enter income: ");
                                    money = scanner.nextInt();
                                    dao.updateBalanceOnAccount(money, cardNumberCheck);
                                    System.out.println("Income was added!");

                                    break;
                                case "3": //do transfer
                                    System.out.println("Enter card number: ");
                                    String cardNumberToTransfer = scanner.next();

                                    if (!account.checkLuhnAlgorithm(cardNumberToTransfer)) {
                                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                                    } else if (!dao.checkCardExist(cardNumberToTransfer)) {
                                        System.out.println("Such card does not exist.");
                                    } else if (cardNumberCheck.equals(cardNumberToTransfer)) {
                                        System.out.println("You can't transfer money to the same account!");
                                    } else if (dao.checkCardExist(cardNumberToTransfer)) {

                                        System.out.println("Enter how much money you want to transfer: ");
                                        money = scanner.nextInt();
                                        if (dao.printCurrentBalance(cardNumberCheck) >= money) {
                                            dao.updateBalanceOnAccount(-money, cardNumberCheck);
                                            dao.updateBalanceOnAccount(money, cardNumberToTransfer);
                                            System.out.println("Success!");
                                        } else {
                                            System.out.println("Not enough money!");
                                        }

                                    } else {
                                        System.out.println("Wrong input");
                                    }

                                    break;
                                case "4": //Close account
                                    dao.deleteAccount(cardNumberCheck);
                                    System.out.println("The account has been closed!");
                                    selectAccountMenu = "0";
                                    break;
                                case "5": //Log out
                                    System.out.println("You have successfully logged out!");
                                    selectAccountMenu = "0";
                                    break;
                                case "0": // exit program
                                    System.out.println("Bye!");
                                    selectStartMenu = "0";
                                    break;
                                default:
                                    System.out.println("Wrong input!");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Wrong input!");
                    }

                    break;
                case "0":
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Wrong input!");
                    break;
            }
        }
    }

    public static void startMenu() {
        System.out.println("1. Create an account \n" +
                "2. Log into account \n" +
                "0. Exit");
    }
}