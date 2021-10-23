package BankAccount;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
public class init  {

    //Is correct password
    public static boolean checkPassword(String inputpassword,String password){
        if(inputpassword.equals(password)){
            return true;
        }
        return false;
    }
    //Check UserName
    public static boolean checkUsername(String username,String usernamedata){
        if ( username.equals(usernamedata)){
                return true;
            }
        return  false;
    }
    //log in
    public static Account login (JSONArray data,String username, String pw){

        for( int i = 0;i<data.size();i++){
            JSONObject object = (JSONObject) data.get(i);
            long money = (long) object.get("money");
            Account account = new Account(object.get("username").toString(),object.get("password").toString(),money);
            boolean checkUsername =checkUsername(username, object.get("username").toString());
            boolean checkPws =checkPassword(pw,object.get("password").toString());
            if(checkUsername && checkPws){
                return account;
            }
        }
        return null;
    }
    //log in
    public static int findIndex (JSONArray data, String username){
        for( int i = 0;i<data.size();i++){
            JSONObject object = (JSONObject) data.get(i);
            if(checkUsername(username,object.get("username").toString())){
                return i;
            }
        }
        return -1;
    }
    //check tien trong tai khoan rut
    public static boolean checkEnoughMoney (long currentMoney, long withdrawMoney ){
        if(withdrawMoney<=currentMoney){
            return true;
        }
        return false;
    }
    //nhap rut tien + update tien trong tk
    public static long updateCurrency(int choose,Account account, String username, String pw){
        long currency = 0;
        Scanner in = new Scanner(System.in);
        if ( choose ==1){
            System.out.println("Vui long nhap tien");
            long withdraw = in.nextLong();
            boolean check = checkEnoughMoney(account.currentMoney,withdraw);
            if ( check){
                currency = account.currentMoney - withdraw;
            }
            else{
                return -1;
            }

        }
        if (choose==2) {
            System.out.println("Vui long tien gui");
            long deposit = in.nextLong();
            currency = account.currentMoney + deposit;
        }

        return currency;
    }
    public static void main(String[] args) {
        Object obj;
        Scanner in = new Scanner(System.in);
        System.out.println("Nhap user name: ");
        String username = in.nextLine();
        System.out.println("Nhap password: ");
        String pw = in.nextLine();


        try {
            obj = new JSONParser().parse(new FileReader("../Bank/src/BankAccount/data.json"));
            JSONArray data = (JSONArray) obj;
            Account account = login(data,username,pw);
            if (account == null){
                System.out.println("Wrong ");
            }
            else{
                System.out.println("Vui long chon thao tac: [1]Rut tien ; [2]Gui tien");
                int choose = in.nextInt();
                long currency = updateCurrency(choose,account,username,pw);
                if ( currency ==-1){
                    System.out.println("SO tien trong tai khoan khong du de rut - leu leu ");
                }
                else {
                    account = new Account(username, pw, currency);
                    int i = findIndex(data, account.username);
                    JSONObject updateObjectMoney = (JSONObject) data.get(i);
                    updateObjectMoney.put("money", account.currentMoney);
                    data.set(i, updateObjectMoney);
                    //Write JSON file
                    try (FileWriter file = new FileWriter("../Bank/src/BankAccount/data.json")) {

                        //We can write any JSONArray or JSONObject instance to the file
                        file.write(data.toJSONString());
                        file.flush();
                        file.close();
                        System.out.println("Write succeed!");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
