package yo.co.ug.yopaymentssdk;

class Beneficiary{
    String Amount = "";
    String Account = "";
    String Name = "";
    String AccountType = "";
    String EmailAddress = "";
    String Status = "";
    String LowLevelStatus = "";
    String LowLevelErrorMessage = "";
    String LowLevelErrorMessageNegative = "";
    String ProviderReference = "";



    public Beneficiary(String Amount, String Account, String Name, String AccountType, String Email) {
        this.Amount = Amount;
        this.Account = Account;
        this.Name = Name;
        this.AccountType = AccountType;
        this.EmailAddress = Email;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLowLevelStatus() {
        return LowLevelStatus;
    }

    public void setLowLevelStatus(String lowLevelStatus) {
        LowLevelStatus = lowLevelStatus;
    }

    public String getLowLevelErrorMessage() {
        return LowLevelErrorMessage;
    }

    public void setLowLevelErrorMessage(String lowLevelErrorMessage) {
        LowLevelErrorMessage = lowLevelErrorMessage;
    }

    public String getLowLevelErrorMessageNegative() {
        return LowLevelErrorMessageNegative;
    }

    public void setLowLevelErrorMessageNegative(String lowLevelErrorMessageNegative) {
        LowLevelErrorMessageNegative = lowLevelErrorMessageNegative;
    }

    public String getProviderReference() {
        return ProviderReference;
    }

    public void setProviderReference(String providerReference) {
        ProviderReference = providerReference;
    }
}