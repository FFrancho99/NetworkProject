package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AccountCreator {
    private String login;
    private String password;

    AccountCreator(String login, String password){
        this.login = login;
        this.password = password;
    }

    private void CreateAccount() throws NoSuchAlgorithmException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your UserName : ");
        this.login = scanner.nextLine();
        FileWriter fileWriter = new FileWriter("database.txt");
        fileWriter.write(login + " ");
        System.out.println("Enter your Password : ");
        this.password = scanner.nextLine();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        fileWriter.write(hexString.toString());
        fileWriter.close();
    }
}
