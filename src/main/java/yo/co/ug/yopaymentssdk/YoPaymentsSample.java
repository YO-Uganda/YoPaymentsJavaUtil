/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yo.co.ug.yopaymentssdk;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josephtabajjwa
 */
public class YoPaymentsSample {
    
    //Set here the correcct path where the certificate was stored.
    static String PUBLIC_KEY_PATH = Paths.get(".")
            .toAbsolutePath()
            .normalize().toString()+"/target/keys/Yo_Uganda_Public_Certificate.crt";
    
    static String PUBLIC_KEY_PATH_SECONDARY = Paths.get(".")
            .toAbsolutePath()
            .normalize().toString()+"/target/keys/Yo_Uganda_Secondary_Public_Certificate.crt";
    
    static String PRIVATE_KEY_PATH = Paths.get(".")
            .toAbsolutePath()
            .normalize().toString()+"/target/keys/private_key.pem";
    
    public static void main(String[] args) throws IOException {
        
        //Test WithdrawRequest
        //testWithdawRequest();

        //Uncomment the following to Test Deposit Request
        //testDepositRequest();

        //Uncomment the following line to test Bulk Payments
        //testBulkPayments();

        //Uncomment the line below to test Bulk Payments Check status
        //testCheckBulkPaymentStatus();
        
        /*Uncomment the line below to test normalVerification*/
        //normalVerification();
        
        
        /*Uncomment the line below to test secondary verification*/
        //secondaryVerification();

        /*Uncomment the line below to test check balance API*/
        //testBalance();

        /*Uncomment the line below to test get ministatent*/
        //testMinistament();

        /*Uncomment the line below to test send mobile Airtime*/
        //testsendAirtimeMobile();

        /*Uncomment the line below to test pause bulk payment*/
        //testPauseBulkPayment();

        /*Uncomment the line below to test resume bulk payment*/
        //testResumeBulkPayment();

        /*Uncomment the line below to test cancel bulk payment*/
        testCancelBulkPayment();
    }
    
    static void testWithdawRequest() throws IOException {
        /*YoPayments yp = new YoPayments(
                "90003066053", 
                "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ", 
                "TEST", 
                true);*/
        
        YoPayments yp = new YoPayments(
                "90009487675", 
                "1254783734", 
                "TEST", 
                true);
        
        //Now set the private key path
        yp.setPrivateKeyFilePath(PRIVATE_KEY_PATH);
        
        //Initiate a withdraw request
        YoPayments.YoPaymentsResponse r = yp.runAcWithdrawFunds(
                "256783086794", 
                "500", 
                "Java Sample", 
                "R1004-"+YoPaymentsUtils.getRandomNumericString());
        
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
            System.out.println("TransactionStatus: "+r.transactionStatus);
            System.out.println("TransactionReference: "+r.transactionReference);
            System.out.println("NetworkReferenceId: "+r.mnoTransactionReferenceId);
        }
        
        //Request Trace
        System.out.println(yp.requestAndResponse);
        
    }

    static void testDepositRequest() throws IOException {
        YoPayments yp = new YoPayments(
                "90003066053",
                "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
                "TEST",
                true);

        //Now set the private key path
        yp.setPrivateKeyFilePath(PRIVATE_KEY_PATH);

        //Initiate a withdraw request
        YoPayments.YoPaymentsResponse r = yp.runAcDepositFunds(
                "256783086794",
                "500",
                "Java Sample",
                "R1004-"+YoPaymentsUtils.getRandomNumericString());

        //Display the response
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
            System.out.println("TransactionStatus: "+r.transactionStatus);
            System.out.println("TransactionReference: "+r.transactionReference);
            System.out.println("NetworkReferenceId: "+r.mnoTransactionReferenceId);
        }

        //Request Trace
        System.out.println(yp.requestAndResponse);

    }

    static void testCheckBulkPaymentStatus() throws IOException {
        YoPayments yp = new YoPayments(
                "90003066053",
                "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
                "TEST",
                true);

        //Now set the private key path
        yp.setPrivateKeyFilePath(PRIVATE_KEY_PATH);

        //Initiate a withdraw request
        YoPayments.YoPaymentsResponse r = yp.runAcCheckBulkPaymentStatus(
                "2022072017BULKPAY3491",
                "BULK001--133586828");

        //Display the response
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
            System.out.println("TransactionStatus: "+r.transactionStatus);
            System.out.println("TransactionReference: "+r.transactionReference);
            System.out.println("NetworkReferenceId: "+r.mnoTransactionReferenceId);
            System.out.println("NetworkReferenceId: "+r.mnoTransactionReferenceId);

            System.out.println("ActivityStatus: "+r.activityStatus);
            System.out.println("PauseReason: "+r.pauseReason);
            System.out.println("AuthorizationStatus: "+r.authorizationStatus);
            System.out.println("TimeLeftToRun: "+r.timeLeftToRun);
            System.out.println("NumberOfBeneficiaries: "+r.numberOfBeneficiaries);
            System.out.println("NumberOfBeneficiariesPaid: "+r.numberOfBeneficiariesPaid);
            System.out.println("NumberOfBeneficiariesUnpaid: "+r.numberOfBeneficiariesUnpaid);
            System.out.println("PercentProgress: "+r.percentProgress);
            System.out.println("Beneficariies:\n=================\n\n");
            for (Beneficiary b : r.beneficiaries)  {
                System.out.println("Beneficary:\n=================\n\n");
                System.out.println("Beneficariy Account: "+b.getAccount());
                System.out.println("Beneficariy Account Type: "+b.getAccountType());
                System.out.println("Beneficariy Email: "+b.getEmailAddress());
                System.out.println("Beneficariy Status: "+b.getStatus());
                System.out.println("Beneficariy Low Level Status: "+b.getLowLevelStatus());
                System.out.println("Beneficariy Low Level Error: "+b.getLowLevelErrorMessage());
                System.out.println("Beneficariy Low Level Message Negative: "+b.getLowLevelErrorMessageNegative());
                //System.out.println("\n=================\n\n");
            }

        }

        //Request Trace
        System.out.println(yp.requestAndResponse);

    }

    static void testBulkPayments() throws IOException {
        YoPayments yp = new YoPayments(
                "90003066053",
                "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
                "TEST",
                true);


        //Now set the private key path
        yp.setPrivateKeyFilePath(PRIVATE_KEY_PATH);

        ArrayList<Beneficiary> beneficiaries = new ArrayList<>();
        beneficiaries.add(new Beneficiary(
                "1000",
                "256783086794",
                "Joseph Tabajjwa",
                "MOBILE MONEY",
                ""
        ));
        beneficiaries.add(new Beneficiary(
                "2000",
                "256788355547",
                "Joseph Tabajjwa",
                "MOBILE MONEY",
                ""
        ));
        /*
        * NOTE: For Internal transfer, set AccountType to 'YO PAYMENTS',
        * Account to Yo payments a/c no. and email to beneficiary email
        */

        //Initiate a withdraw request
        YoPayments.YoPaymentsResponse r = yp.runAcCreateBulkpayment(
                "Salary Payments",
                "Salary Payments",
                "Thanks for your service",
                "BULK001-"+YoPaymentsUtils.getRandomNumericString(),
                beneficiaries);

        //Display the response
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
            System.out.println("TransactionStatus: "+r.transactionStatus);
            System.out.println("TransactionReference: "+r.transactionReference);
            System.out.println("BulkPaymentRequestIdentifier: "+r.bulkPaymentRequestIdentifier);
        }

        //Request Trace
        System.out.println(yp.requestAndResponse);

    }
    
    static void normalVerification() {
        String datetime = "2014-02-07 14:48:07";
        String amount = "1000";
        String narrative = "SpinApp Userid:7 Number:256783086794";
        String network_ref = "1327659406";
        String external_ref = "";
        String msisdn = "256783086794";
        String signatureBase64 = "05b4cTk+IDhI8aqRhsFR2zXbbl9xfWJPHO+WAn/sSWCCB0zQeePvqjUTONk6w8wcaue0YbCO2cd1ER3l0K8aJUj8Ob7Ixl7o5cNsYwCHu8cDenBFxUL8UBnlSxZAkOXf/vi47rwT3Eon9KpPJxJISLnp1vyVJgkWAH9GFsX1zLY33314sekJ1KFzPxY55vkTaUic9BfpIKsj+L4XFcgHpnJHqA20byAEE8uYmdrrSbwlCnEdqJx3ROE3gxMS/M0gAwPcjZFziawAfFaUARogFmrkRA9KKjA9XLPMvN8tN8vNwVbg8xV5p/K4pmBA3Z4DtnJAaYAeUXvgW8Dij+UDdw==";
        
        String signedData = datetime+amount+narrative+network_ref+external_ref
                +msisdn;
        
        //First read the public key from the file
        String pKeyString = YoPaymentsUtils.readAllBytesFromFile(PUBLIC_KEY_PATH);
        if (pKeyString == null) {
            Logger.getLogger(YoPaymentsSample.class.getName())
                    .log(Level.SEVERE, "Couldn't read PK from file", "");
            return;
        }
        
        try {
            Signature sign = Signature.getInstance("SHA1withRSA");
            
            //Obtain the public key resource
            PublicKey publicKey = YoPaymentsUtils.getPublicKeyFromBase64String(pKeyString);
            //PublicKey publicKey = YoPaymentsUtils.getKey(PUBLIC_KEY_PATH);
            if (publicKey==null) {
                System.out.print("PublicKey must not be null\n");
                return;
            }
            
            sign.initVerify(publicKey);
            sign.update(signedData.getBytes());
            
            //Try to decode base64 to byte array
            byte[] signature_content;
            try{
                signature_content = Base64.getDecoder().decode(signatureBase64);
            } catch (Exception e) {
                Logger.getLogger(YoPaymentsSample.class.getName())
                        .log(Level.SEVERE, e.getMessage(), "");
                return;
            }

            //Now check the signature
            if (signature_content.length < 256) {
                System.out.print("Invalid Base64 signature data\n");
                return;
            }

            if (!sign.verify(signature_content)) {
                System.out.print("Signature verification FAILED\n");
                return;
            }

            System.out.print("Signature verification PASSED\n");
            

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(YoPaymentsSample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(YoPaymentsSample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        } catch (SignatureException ex) {
            Logger.getLogger(YoPaymentsSample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }
    }

    /*
    * Secondary verification is done for transactions
    * that are reposted the second time. The public key to use
    * is different.
    * */
    static void secondaryVerification() {
        String datetime = "2020-04-07 23:34:34";
        String amount = "15000";
        String narrative = "8218000126135 EMPLOYEE";
        String network_ref = "9322253676";
        String external_ref = "";
        String msisdn = "256774003539";
        String signatureBase64 = "Dlg+HD/O9xIS0JP1WyGgJizDZPhe67QB5lJIH4PRPtTyr1tkIbZN9ZDwUifmZOmbUfSEXxS10BaZsJuiMjayZcLfRdPhcVr4CQ/riMzoKuduqGRPRHGeYsvo+ZWFTPt6/vhaGVozPxhyflAEMhzZZYyMPRyBrflIZSWVVLIcgxzrzjd5h46kzF11IU1FMvXpHIz3q+xNdK3uO6igyQHRdlCqt+mcccTTdQ4bZDJ3MWfKGP2bUgOgjME1NAAwsOkIZHNgQHdpyzu/VFFJRsBUyMzoge7VPLwhEBLbYbiM0AqEIXRGfWVk19t9OkQ8frutyQT+PEVMAHfkBF3NuJFH3g==";
        
        String signedData = datetime+amount+narrative+network_ref+external_ref
                +msisdn;
        
        //First read the public key from the file
        String pKeyString = YoPaymentsUtils.readAllBytesFromFile(PUBLIC_KEY_PATH_SECONDARY);
        if (pKeyString == null) {
            Logger.getLogger(YoPaymentsSample.class.getName())
                    .log(Level.SEVERE, "Couldn't read PK from file", "");
            return;
        }
        
        try {
            Signature sign = Signature.getInstance("SHA1withRSA");
            
            //Obtain the public key resource
            PublicKey publicKey = YoPaymentsUtils.getPublicKeyFormPEMFormat(pKeyString);
            //PublicKey publicKey = YoPaymentsUtils.getKey(PUBLIC_KEY_PATH);
            if (publicKey==null) {
                System.out.print("PublicKey must not be null\n");
                return;
            }
            
            sign.initVerify(publicKey);
            sign.update(signedData.getBytes());
            
            //Try to decode base64 to byte array
            byte[] signature_content;
            try{
                signature_content = Base64.getDecoder().decode(signatureBase64);
            } catch (Exception e) {
                Logger.getLogger(YoPaymentsSample.class.getName())
                        .log(Level.SEVERE, e.getMessage(), "");
                return;
            }

            //Now check the signature
            if (signature_content.length < 256) {
                System.out.print("Invalid Base64 signature data\n");
                return;
            }

            if (!sign.verify(signature_content)) {
                System.out.print("Signature verification FAILED\n");
                return;
            }
            
            
            System.out.print("Signature verification PASSED\n");
            

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(YoPaymentsSample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(YoPaymentsSample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        } catch (SignatureException ex) {
            Logger.getLogger(YoPaymentsSample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }
    }

    //Test Balance
    static void testBalance() throws IOException{
        YoPayments yp = new YoPayments(
            "90003066053",
            "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
            "TEST",
            true
        );
        YoPayments.YoPaymentsResponse r=yp.getBalance();
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
            //System.out.println("Balances: ");
            for(Balance bal: r.balances){
                System.out.println("Balance\n=================\n\n");
                System.out.println("Currency Code: "+ bal.getCode());
                System.out.println("Balance: " + bal.getBalance());
                System.out.print("\n");
            }
        }
        //Request Trace
        System.out.println(yp.requestAndResponse);
    }

    //Test get_ministatement
    static void testMinistament() throws IOException{
        YoPayments yp = new YoPayments(
            "90003066053",
            "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
            "TEST",
            true
        );
        String startDate="";
        String endDate="";
        String transactionStatus="SUCCEEDED";
        String currencyCode="";
        String resultSetLimit="";
        String transactionEntryDesignation="";
        String externalReference="";
        YoPayments.YoPaymentsResponse r=yp.getMiniStatement(
            startDate,
            endDate,
            transactionStatus,
            currencyCode,
            resultSetLimit, 
            transactionEntryDesignation,
            externalReference

        );
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("TotalTransactions: "+r.totalTransactions);
            System.out.println("ReturnedTransactions: "+r.returnedTransactions);
            for(Transaction t: r.transactions){
                System.out.println("Transaction\n=================\n\n");
                System.out.println("TransactionSystemId: "+ t.getTransactionSystemId());
                System.out.println("TransactionReference: " + t.getTransactionReference());
                System.out.println("TransactionStatus: " + t.getTransactionStatus());
                System.out.println("InitiationDate: " + t.getInitiationDate());
                System.out.println("CompletionDate: " + t.getCompletionDate());
                System.out.println("NarrativeBase64: " + t.getNarrativeBase64());
                System.out.println("Currency: " + t.getCurrency());
                System.out.println("Amount: " + t.getAmount());
                System.out.println("Balance: " + t.getBalance());
                System.out.println("GeneralType: " + t.getGeneralType());
                System.out.println("DetailedType: " + t.getDetailedType());
                System.out.println("BeneficiaryMsisdn: " + t.getBeneficiaryMsisdn());
                System.out.println("BeneficiaryBase64: " + t.getBeneficiaryBase64());
                System.out.println("SenderMsisdn: " + t.getSenderMsisdn());
                System.out.println("SenderBase64: " + t.getSenderBase64());
                System.out.println("Base64TransactionExternalReference: " + t.getBase64TransactionExternalReference());
                System.out.println("TransactionEntryDesignation: " + t.getTransactionEntryDesignation());
                System.out.print("\n");
            }
        }
        //Request Trace
        //System.out.println(yp.requestAndResponse);
    }

    //Test SendAirtime
    static void testsendAirtimeMobile() throws IOException{
        YoPayments yp = new YoPayments(
            "90003066053",
            "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
            "TEST",
            true
        );
        String account="256772003456";
        String amount="2000";
        String narrative="Airtime";
        String externalReference="RT45";
        YoPayments.YoPaymentsResponse r=yp.sendAirtimeMobile(
            account,
            amount,
            narrative,
            externalReference

        );
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
            System.out.println("TransactionStatus: "+r.transactionStatus);
            System.out.println("TransactionReference: "+r.transactionReference);
            System.out.println("NetworkReferenceId: "+r.mnoTransactionReferenceId);
        }
        //Request Trace
        System.out.println(yp.requestAndResponse);
    }
    //Test pauseBulkPaymente
    static void testPauseBulkPayment() throws IOException{
        YoPayments yp = new YoPayments(
            "90003066053",
            "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
            "TEST",
            true
        );
        String bulkPaymentRequestIdentifier="2023010311BULKPAY3542";
        String pauseReason="Trial";
        YoPayments.YoPaymentsResponse r=yp.pauseBulkPayment(
            bulkPaymentRequestIdentifier,
            pauseReason
        );
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
        }
        //Request Trace
        System.out.println(yp.requestAndResponse);
    }

    //Test pauseBulkPaymente
    static void testResumeBulkPayment() throws IOException{
        YoPayments yp = new YoPayments(
            "90003066053",
            "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
            "TEST",
            true
        );
        String bulkPaymentRequestIdentifier="2023010311BULKPAY3542";
        YoPayments.YoPaymentsResponse r=yp.resumeBulkPayment(
            bulkPaymentRequestIdentifier
        );
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
        }
        //Request Trace
        System.out.println(yp.requestAndResponse);
    }

    //Test pauseBulkPaymente
    static void testCancelBulkPayment() throws IOException{
        YoPayments yp = new YoPayments(
            "90003066053",
            "do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ",
            "TEST",
            true
        );
        String bulkPaymentRequestIdentifier="2023010311BULKPAY3542";
        YoPayments.YoPaymentsResponse r=yp.cancelBulkPayment(
            bulkPaymentRequestIdentifier
        );
        //Display the response 
        if (r != null) {
            System.out.println("Status: "+r.status);
            System.out.println("StatusCode: "+r.statusCode);
            System.out.println("StatusMessage: "+r.statusMessage);
            System.out.println("ErrorMessage: "+r.errorMessage);
        }
        //Request Trace
        System.out.println(yp.requestAndResponse);
    }


    
}
