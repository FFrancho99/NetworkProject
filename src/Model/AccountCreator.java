package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AccountCreator {

    public static void CreateAccount() throws NoSuchAlgorithmException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader("database.txt"));
        FileWriter fileWriter = new FileWriter("database.txt");

        String login = login();
        String DBLine = reader.readLine();

        while( DBLine != null){
            String[] arrayOfData = DBLine.split(":");
            if (login.equals(arrayOfData[1])){
                System.out.println("This login is already taken");
                login = login();
                reader.close();
                reader = new BufferedReader(new FileReader("database.txt"));
            }
            DBLine = reader.readLine();
        }
        fileWriter.write(login + " : ");

        String password = password();

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

    public static String login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your UserName : ");
        return scanner.nextLine();
    }
    public static String password(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Password : ");
        return scanner.nextLine();
    }
}
