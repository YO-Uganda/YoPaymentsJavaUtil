package yo.co.ug.yopaymentssdk;

public class Transaction{
    String TransactionSystemId;
    String TransactionReference;
    String TransactionStatus;
    String InitiationDate;
    String CompletionDate;
    String NarrativeBase64;
    String Currency;
    String Amount;
    String Balance;
    String GeneralType;
    String DetailedType;
    String BeneficiaryMsisdn;
    String BeneficiaryBase64;
    String SenderMsisdn;
    String SenderBase64;
    String Base64TransactionExternalReference;
    String TransactionEntryDesignation;
    public Transaction(
            String TransactionSystemId,
            String TransactionReference,
            String TransactionStatus,
            String InitiationDate,
            String CompletionDate,
            String NarrativeBase64,
            String Currency,
            String Amount){
        this.TransactionSystemId=TransactionSystemId;
        this.TransactionReference=TransactionReference;
        this.TransactionStatus=TransactionStatus;
        this.InitiationDate=InitiationDate;
        this.CompletionDate=CompletionDate;
        this.NarrativeBase64=NarrativeBase64;
        this.Currency=Currency;
        this.Amount=Amount;
    }
    public void setTransactionSystemId(String transactionSystemId){
        TransactionSystemId=transactionSystemId;
        
    }
    public String getTransactionSystemId(){
        return TransactionSystemId;
    }
    public void setTransactionReference(String transactionReference){
        TransactionReference=transactionReference;
        
    }
    public String getTransactionReference(){
        return TransactionReference;
    }
    public void setTransactionStatus(String transactionStatus){
        TransactionStatus=transactionStatus;
        
    }
    public String getTransactionStatus(){
        return TransactionStatus;
    }
    public void setInitiationDate(String initiationDate){
        InitiationDate=initiationDate;
        
    }
    public String getInitiationDate(){
        return InitiationDate;
    }
    public void setCompletionDate(String completionDate){
        CompletionDate=completionDate;
        
    }
    public String getCompletionDate(){
        return CompletionDate;
    }
    public void setNarrativeBase64(String narrativeBase64){
        NarrativeBase64=narrativeBase64;
        
    }
    public String getNarrativeBase64(){
        return NarrativeBase64;
    }
    public void setCurrency(String currency){
        Currency=currency;
        
    }
    public String getCurrency(){
        return Currency;
    }
    public void setAmount(String amount){
        Amount=amount;
        
    }
    public String getAmount(){
        return Amount;
    }
    public void setBalance(String balance){
        Balance=balance;
        
    }
    public String getBalance(){
        return Balance;
    }
    public void setGeneralType(String generalType){
        GeneralType=generalType;
        
    }
    public String getGeneralType(){
        return GeneralType;
    }
    public void setDetailedType(String detailedType){
        DetailedType=detailedType;
        
    }
    public String getDetailedType(){
        return DetailedType;
    }
    public void setBeneficiaryMsisdn(String beneficiaryMsisdn){
        BeneficiaryMsisdn=beneficiaryMsisdn;
        
    }
    public String getBeneficiaryMsisdn(){
        return BeneficiaryMsisdn;
    }
        public void setBeneficiaryBase64(String beneficiaryBase64){
        BeneficiaryBase64=beneficiaryBase64;
        
    }
    public String getBeneficiaryBase64(){
        return BeneficiaryBase64;
    }
    public void setSenderMsisdn(String senderMsisdn){
        SenderMsisdn=senderMsisdn;
        
    }
    public String getSenderMsisdn(){
        return SenderMsisdn;
    }
    public void setSenderBase64(String senderBase64){
        SenderBase64=senderBase64;
        
    }
    public String getSenderBase64(){
        return SenderBase64;
    }
    public void setBase64TransactionExternalReference(String base64TransactionExternalReference){
        Base64TransactionExternalReference=base64TransactionExternalReference;
        
    }
    public String getBase64TransactionExternalReference(){
        return Base64TransactionExternalReference;
    }
    public void setTransactionEntryDesignation(String transactionEntryDesignation){
        TransactionEntryDesignation=transactionEntryDesignation;
        
    }
    public String getTransactionEntryDesignation(){
        return TransactionEntryDesignation;
    }
    


}