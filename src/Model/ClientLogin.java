package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ClientLogin {
    public static boolean userLogin() throws NoSuchAlgorithmException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader("database.txt"));
        String login = login();
        String password = password();

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }

        String hashedPassword = hexString.toString();

        String DBLine = reader.readLine();
        while(DBLine != null){
            String[] arrayOfData = DBLine.split(":");
            if(login.equals(arrayOfData[0])){
                if(hashedPassword.equals(arrayOfData[1])){
                    return true;
                }
                System.out.println("Wrong password for this username");
                return false;
            }
            DBLine = reader.readLine();
        }
        System.out.println("This username doesnt exist");
        return false;
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
