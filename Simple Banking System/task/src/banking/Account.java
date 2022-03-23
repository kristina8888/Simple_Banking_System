package banking;

import java.util.*;

public class Account {

    Random random = new Random();

    private String cardNumber;
    private String pin;
    static Map<String, Account> accountMap = new HashMap<>();


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
}