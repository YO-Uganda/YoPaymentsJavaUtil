package yo.co.ug.yopaymentssdk;

public class Balance{
    String Code;
    String Balance;
    public Balance(String Code, String Balance){
        this.Code=Code;
        this.Balance=Balance;
    }
    public void setCode(String code){
        Code=code;
        
    }
    public String getCode(){
        return Code;
    }
    public void setBalance(String balance){
        Balance=balance;
        
    }
    public String getBalance(){
        return Balance;
    }


}