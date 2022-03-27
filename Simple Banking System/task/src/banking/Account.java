package banking;

import java.util.*;

public class Account {

    Random random = new Random();

    private String cardNumber;
    private String pin;
    private int balance;
    private int id;

    public Account() {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public Account(String arg) {

    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Account(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public String getNewCardNumber() {
        return cardNumber;
    }

    public String getNewPin() {
        return pin;
    }

    public String createNewCardNumber() {
        String bin = "400000";
        int lowerAccNr = 100000000;
        int upperAccNr = 999999999;
        int accNr = random.nextInt(upperAccNr - lowerAccNr + 1) + lowerAccNr;
        String digits = bin + accNr;

        int sum = 0;
        boolean alternate = false;

        for (int i = digits.length() - 1; i >= 0; --i) {
            int digit = Character.getNumericValue(digits.charAt(i));
            digit = (alternate = !alternate) ? (digit * 2) : digit;
            digit = (digit > 9) ? (digit - 9) : digit;
            sum += digit;
        }
        int lastDigit = (sum * 9) % 10;

        cardNumber = digits + String.valueOf(lastDigit);

        return cardNumber;
    }

    public String crateNewPin() {
        int lower = 1000;
        int upper = 9999;
        pin = String.valueOf(random.nextInt(upper - lower + 1) + lower);

        return pin;
    }

    public boolean checkLuhnAlgorithm(String cardNumberCheck) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumberCheck.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(cardNumberCheck.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}