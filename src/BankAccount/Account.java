package BankAccount;

import org.json.simple.JSONObject;

public class Account {
    public String username;
    public String password;
    public long currentMoney;
    public Account(String username, String password, long currentMoney){
        this.username = username;
        this.password = password;
        this.currentMoney = currentMoney;
    }

}
