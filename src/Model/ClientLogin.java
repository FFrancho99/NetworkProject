package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ClientLogin {

    private String login;
    private String password;

    public ClientLogin(){
        this.login = null;
        this.password = null;
    }

    public ClientLogin(String login, String password){
        this.login = login;
        this.password = password;
    }

    public ClientLogin(String login){
        this.login = login;
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

    public boolean checkPassword() throws NoSuchAlgorithmException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader("ADD YOUR PATH TO DATABASE HERE"));

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
            if(login.equals(arrayOfData[0]) && hashedPassword.equals(arrayOfData[1])){
                return true;
            }
            DBLine = reader.readLine();
        }
        return false;
    }

    public void setLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your UserName : ");
        this.login = scanner.nextLine();
    }

    public String getLogin(){
        return this.login;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Password : ");
        this.password = scanner.nextLine();
    }
}
