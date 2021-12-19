package Model;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AccountCreator {

    private String login;
    private String password;

    public AccountCreator(){
        this.login = null;
        this.password = null;
    }
    public AccountCreator(String login, String password){
        this.login = login;
        this.password = password;
    }

    public boolean checkLogin() throws IOException, NoSuchAlgorithmException {
        Boolean test = false;

        BufferedReader reader = new BufferedReader(new FileReader("ADD YOUR PATH TO DATABASE HERE"));

        String DBLine = reader.readLine();
        while( DBLine != null){
            String[] arrayOfData = DBLine.split(":");
            if (login.equals(arrayOfData[0])){
                test = true;
            }
            DBLine = reader.readLine();
        }
        reader.close();
        return test;
    }

    public void setLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your UserName : ");
        this.login = scanner.nextLine();
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Password : ");
        this.password = scanner.nextLine();
    }

    public String getPassword() {
        return password;
    }

    public void saveLoginPassword() throws IOException, NoSuchAlgorithmException {
        FileWriter fileWriter = new FileWriter("ADD YOUR PATH TO DATABASE HERE", true);
        fileWriter.write("\n");
        fileWriter.write(login + ":");

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }

        fileWriter.write(hexString.toString());
        fileWriter.write("\n");
        fileWriter.close();
    }
}
