/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yo.co.ug.yopaymentssdk;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author josephtabajjwa
 */
public class YoPayments {
    String apiUsername;
    String apiPassword;
    String production_url = "https://paymentsapi1.yo.co.ug/ybs/task.php";
    String sandbox_url = "http://sandbox.yo.co.ug/services/yopaymentsdev/task.php";
    String mode = "TEST";//SET TO TEST or PRODUCTION
    String url = "";
    String pemPrivateKeyFilePath = "";
    boolean signData = false;
    HttpRequestResponse requestAndResponse;
    
    /*
    * @Param apiUsername: Obtain this from Yo! Payments dashboard
    * @Param apiPassword: Obtain this from Yo! Payments dashboard under Api Details
    * @Param mode: Set this to TEST or PRODUCTION depending on the system you are testing.
    * @oaramm signData: Set this to true if you want to sign data
    */
    public YoPayments(String apiUsername, String apiPassword, String mode, Boolean signData) {
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;
        this.mode = mode;
        if (mode.equals("TEST")) {
            this.url = this.sandbox_url;
        } else {
            this.url = this.production_url;
        }
        if (signData) {
            this.signData = true;
            //You have to call setPrivateKeyFilePath to set the path to private key file.
        }
    }
    
    
    /*
    * @Param pemPrivateKeyFilePath: This should be a valid path to the private .pem file 
    */
    public void setPrivateKeyFilePath(String pemPrivateKeyFilePath) {
        this.pemPrivateKeyFilePath = pemPrivateKeyFilePath;
    }
    
    //TODO: implement transactionCheck Status
    /*
     * @Param bulkPaymentRequestIdentifier: The Unique ID returned by Yo Payments gateway
     * @Param privateBulkPaymentRequestId: The unique private ID you sent in the initial xml
     */
    public YoPayments.YoPaymentsResponse runAcCheckBulkPaymentStatus(
            String bulkPaymentRequestIdentifier,
            String privateBulkPaymentRequestId) {

        String xmlData = getAcCheckBulkPaymentStatusXml(
                bulkPaymentRequestIdentifier,
                privateBulkPaymentRequestId
        );

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");

        HttpRequestResponse rs = YoPaymentsUtils.doHttpRequest("POST", url, xmlData, headers);
        this.requestAndResponse = rs;

        //Now parse the response
        if (!rs.response.isEmpty()) {
            try {
                YoPaymentsResponse res = new YoPaymentsResponse(rs.response);
                //Now you can use the response, check the fields

                return res;

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;
    }

    /*
    * @Param transactionReference: This is the transaction reference which was returned in original request.
    * 
    */
    public YoPayments.YoPaymentsResponse runAcTransactionCheckStatus(String transactionReference) {
        return null;
    }

    /*
     * @Param bulkPaymentRequestIdentifier: The Unique ID returned by Yo Payments gateway
     * @Param privateBulkPaymentRequestId: The unique private ID you sent in the initial xml
     */
    private String getAcCheckBulkPaymentStatusXml(String bulkPaymentRequestIdentifier,
                                                  String privateBulkPaymentRequestId) {

        String rXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        rXml += "<AutoCreate>"
                + "<Request>"
                + "<APIUsername>"+this.apiUsername+"</APIUsername>"
                + "<APIPassword>"+this.apiPassword+"</APIPassword>"
                + "<Method>accheckbulkpaymentstatus</Method>";
        if (!bulkPaymentRequestIdentifier.isEmpty()) {
            rXml += "<BulkPaymentRequestIdentifier>"
                    + bulkPaymentRequestIdentifier
                    +"</BulkPaymentRequestIdentifier>";
        }
        if (!privateBulkPaymentRequestId.isEmpty()) {
            rXml += "<PrivateBulkPaymentRequestId>"
                    +privateBulkPaymentRequestId
                    +"</PrivateBulkPaymentRequestId>";
        }
        rXml += "</Request>"
                + "</AutoCreate>";


        return rXml;
    }


    /*
     * @Param account: This is the mobile money number to send money to.
     * @Param amount: The amount to send.
     * @Param narrative: Simple description about the transaction.
     * @Param reference: This is the external reference.
     */
    public YoPayments.YoPaymentsResponse runAcDepositFunds(String account, String amount, String narrative,
                                                           String reference) throws IOException {

        String xmlData = getAcDepositFundsXml(account, amount, narrative, reference);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");

        HttpRequestResponse rs = YoPaymentsUtils.doHttpRequest("POST", url, xmlData, headers);
        this.requestAndResponse = rs;

        //Now parse the response
        if (!rs.response.isEmpty()) {
            try {
                YoPaymentsResponse res = new YoPaymentsResponse(rs.response);
                //Now you can use the response, check the fields

                return res;

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;

    }


    /*
     * @Param account: This is the mobile money number to send money to.
     * @Param amount: The amount to send.
     * @Param narrative: Simple description about the transaction.
     * @Param reference: This is the external reference.
     */
    private String getAcDepositFundsXml(String account, String amount, String narrative,
                                         String reference) throws IOException {

        String rXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        rXml += "<AutoCreate>"
                + "<Request>"
                + "<APIUsername>"+this.apiUsername+"</APIUsername>"
                + "<APIPassword>"+this.apiPassword+"</APIPassword>"
                + "<Method>acdepositfunds</Method>"
                + "<NonBlocking>TRUE</NonBlocking>"
                + "<Amount>"+amount+"</Amount>"
                + "<Account>"+account+"</Account>"
                + "<Narrative>"+narrative+"</Narrative>"
                + "<ExternalReference>"+reference+"</ExternalReference>";
        rXml += "</Request>"
                + "</AutoCreate>";


        return rXml;
    }
    
    
    
    /*
    * @Param account: This is the mobile money number to send money to.
    * @Param amount: The amount to send.
    * @Param narrative: Simple description about the transaction.
    * @Param reference: This is the external reference.
    */
    public YoPayments.YoPaymentsResponse runAcWithdrawFunds(String account, String amount, String narrative,
                                                            String reference) throws IOException {
         
            String xmlData = getAcWithdrawFundsXml(account, amount, narrative, reference);
        
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/xml");
            headers.put("Content-transfer-encoding", "text");
            
            HttpRequestResponse rs = YoPaymentsUtils.doHttpRequest("POST", url, xmlData, headers);
            this.requestAndResponse = rs;
            
            //Now parse the response
            if (!rs.response.isEmpty()) {
                try {
                    YoPaymentsResponse res = new YoPaymentsResponse(rs.response);
                    //Now you can use the response, check the fields
                    
                    return res;
                    
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
            return null;
            
    }
    
    
    /*
    * @Param account: This is the mobile money number to send money to.
    * @Param amount: The amount to send.
    * @Param narrative: Simple description about the transaction.
    * @Param reference: This is the external reference.
    */
    private String getAcWithdrawFundsXml(String account, String amount, String narrative, 
            String reference) throws IOException {
        String nonce = YoPaymentsUtils.getRandomNumericString();
        String data = this.apiUsername+amount+account+narrative+reference+nonce;
        String sha1 = YoPaymentsUtils.generateSha1String(data);
        String signatureBase64 = "";
        
        if (this.signData) {
            try {
                PrivateKey privKey = YoPaymentsUtils.getPrivateKeyFromFile(this.pemPrivateKeyFilePath);
                Signature sign = Signature.getInstance("SHA1withRSA");
                sign.initSign(privKey);
                sign.update(sha1.getBytes());
                byte[] realSig = sign.sign();
                signatureBase64 = Base64.getEncoder().encodeToString(realSig);
            } catch (NoSuchAlgorithmException ex) {

                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return null;
            } catch (URISyntaxException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return null;
            } catch (InvalidKeyException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return null;
            } catch (SignatureException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return null;
            }
        }
        
        String rXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        rXml += "<AutoCreate>"
                + "<Request>"
                + "<APIUsername>"+this.apiUsername+"</APIUsername>"
                + "<APIPassword>"+this.apiPassword+"</APIPassword>"
                + "<Method>acwithdrawfunds</Method>"
                + "<Amount>"+amount+"</Amount>"
                + "<Account>"+account+"</Account>"
                + "<Narrative>"+narrative+"</Narrative>"
                + "<ExternalReference>"+reference+"</ExternalReference>";
              if (this.signData) {
                rXml += "<PublicKeyAuthenticationNonce>"+nonce+"</PublicKeyAuthenticationNonce>";
                rXml += "<PublicKeyAuthenticationSignatureBase64>"+signatureBase64+"</PublicKeyAuthenticationSignatureBase64>";
              } else {
                  rXml += "<PublicKeyAuthenticationNonce></PublicKeyAuthenticationNonce>";
                  rXml += "<PublicKeyAuthenticationSignatureBase64></PublicKeyAuthenticationSignatureBase64>";
              }
              rXml += "</Request>"
            + "</AutoCreate>";
                
        
        return rXml;
    }

    /*
     * @Param account: This is the mobile money number to send money to.
     * @Param amount: The amount to send.
     * @Param narrative: Simple description about the transaction.
     * @Param reference: This is the external reference.
     */
    public YoPayments.YoPaymentsResponse runAcCreateBulkpayment(String name,
                                                                String description,
                                                                String groupwidePaymentNotificationText,
                                                                String privateBulkPaymentRequestId,
                                                                ArrayList<Beneficiary> beneficiaries) throws IOException {

        String xmlData = getAcCreateBulkPaymentsXml(name,
                description,
                groupwidePaymentNotificationText,
                privateBulkPaymentRequestId,
                beneficiaries
        );

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");

        HttpRequestResponse rs = YoPaymentsUtils.doHttpRequest("POST", url, xmlData, headers);
        this.requestAndResponse = rs;

        //Now parse the response
        if (!rs.response.isEmpty()) {
            try {
                YoPaymentsResponse res = new YoPaymentsResponse(rs.response);
                //Now you can use the response, check the fields

                return res;

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;

    }


    /*
     * @Param Name: The name of bulk payments.
     * @Param Description: Simple description about the transaction.
     * @Param GroupwidePaymentNotificationText: Simple group description about the transaction.
     * @Param PrivateBulkPaymentRequestId: This is the unique external reference.
     * @Param beneficiaries: An array list of beneficiaries
     */
    private String getAcCreateBulkPaymentsXml(String name, String description,
                                              String groupwidePaymentNotificationText,
                                         String privateBulkPaymentRequestId,
                                              ArrayList<Beneficiary> beneficiaries) throws IOException {

        String signatureBase64 = "";
        String nonce = YoPaymentsUtils.getRandomNumericString();
        if (this.signData) {
            String data = this.apiUsername + name + description + groupwidePaymentNotificationText
                    + privateBulkPaymentRequestId + nonce;

            String benListString = "";
            for (int i = 0; i < beneficiaries.size(); i++) {
                benListString += beneficiaries.get(i).Amount
                        + beneficiaries.get(i).Account
                        + beneficiaries.get(i).AccountType;
            }
            String dataToSign = data + benListString;
            String sha1 = YoPaymentsUtils.generateSha1String(dataToSign);


            if (this.signData) {
                try {
                    PrivateKey privKey = YoPaymentsUtils.getPrivateKeyFromFile(this.pemPrivateKeyFilePath);
                    Signature sign = Signature.getInstance("SHA1withRSA");
                    sign.initSign(privKey);
                    sign.update(sha1.getBytes());
                    byte[] realSig = sign.sign();
                    signatureBase64 = Base64.getEncoder().encodeToString(realSig);
                } catch (NoSuchAlgorithmException ex) {

                    Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    return null;
                } catch (URISyntaxException ex) {
                    Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeySpecException ex) {
                    Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    return null;
                } catch (InvalidKeyException ex) {
                    Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    return null;
                } catch (SignatureException ex) {
                    Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    return null;
                }
            }
        }

        String rXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        rXml += "<AutoCreate>"
                + "<Request>"
                + "<APIUsername>"+this.apiUsername+"</APIUsername>"
                + "<APIPassword>"+this.apiPassword+"</APIPassword>"
                + "<Method>accreatebulkpayment</Method>"
                + "<Name>"+name+"</Name>"
                + "<Description>"+description+"</Description>"
                + "<GroupwidePaymentNotificationText>"+groupwidePaymentNotificationText+"</GroupwidePaymentNotificationText>"
                + "<PrivateBulkPaymentRequestId>"+privateBulkPaymentRequestId+"</PrivateBulkPaymentRequestId>";
        if (this.signData) {
            rXml += "<PublicKeyAuthenticationNonce>"+nonce+"</PublicKeyAuthenticationNonce>";
            rXml += "<PublicKeyAuthenticationSignatureBase64>"+signatureBase64+"</PublicKeyAuthenticationSignatureBase64>";
        } else {
            //If you don't have public key configured on your account, don't include these fields
            //rXml += "<PublicKeyAuthenticationNonce></PublicKeyAuthenticationNonce>";
            //rXml += "<PublicKeyAuthenticationSignatureBase64></PublicKeyAuthenticationSignatureBase64>";
        }
        rXml += "<Beneficiaries>";
        for (int i = 0; i < beneficiaries.size(); i++) {
            Beneficiary ben = beneficiaries.get(i);
            rXml += "<Beneficiary>";
            rXml += "<Amount>"+ben.Amount+"</Amount>";
            rXml += "<AccountNumber>"+ben.Account+"</AccountNumber>";
            rXml += "<Name>"+ben.Name+"</Name>";
            rXml += "<AccountType>"+ben.AccountType+"</AccountType>";
            rXml += "<EmailAddress>"+ben.EmailAddress+"</EmailAddress>";
            rXml += "</Beneficiary>";
        }
        rXml += "</Beneficiaries>";
        rXml += "</Request>"
                + "</AutoCreate>";

        return rXml;
    }
    
    class YoPaymentsResponse {
        String responseXml;
        String statusCode = "";
        String status = "";
        String transactionReference = "";
        String statusMessage = "";
        String errorMessage = "";
        String mnoTransactionReferenceId = "";
        String transactionStatus = "";
        String bulkPaymentRequestIdentifier = "";
        String txType = "SINGLE PAYMENTS"; //Can be set to SINGLE PAYMENT or BULK PAYMENTS
        String activityStatus = "";
        String pauseReason = "";
        String authorizationStatus = "";
        String timeLeftToRun = "";
        String numberOfBeneficiaries = "";
        String numberOfBeneficiariesPaid = "";
        String numberOfBeneficiariesUnpaid = "";
        String percentProgress = "";
        ArrayList<Beneficiary> beneficiaries = new ArrayList<>();
                
        
        public YoPaymentsResponse(String responseXml) throws ParserConfigurationException {
            this.responseXml = responseXml;
            this.parse();
        }
        
        public void parse() throws ParserConfigurationException {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            try {
                ByteArrayInputStream input = new ByteArrayInputStream(this.responseXml.getBytes("UTF-8"));
                
                Document doc = dBuilder.parse(input);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Response");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (eElement.getElementsByTagName("Status") != null 
                                && eElement.getElementsByTagName("Status").item(0) != null) {

                                this.status = eElement.getElementsByTagName("Status").item(0).getTextContent();
                        }
                        if (eElement.getElementsByTagName("ActivityStatus") != null
                                && eElement.getElementsByTagName("ActivityStatus").item(0) != null) {
                            this.activityStatus = eElement.getElementsByTagName("ActivityStatus").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("PauseReason") != null
                                && eElement.getElementsByTagName("PauseReason").item(0) != null) {
                            this.pauseReason = eElement.getElementsByTagName("PauseReason").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("AuthorizationStatus") != null
                                && eElement.getElementsByTagName("AuthorizationStatus").item(0) != null) {
                            this.authorizationStatus = eElement.getElementsByTagName("AuthorizationStatus").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("TimeLeftToRun") != null
                                && eElement.getElementsByTagName("AuthorizationStatus").item(0) != null) {
                            this.timeLeftToRun = eElement.getElementsByTagName("TimeLeftToRun").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("numberOfBeneficiaries") != null
                                && eElement.getElementsByTagName("numberOfBeneficiaries").item(0) != null) {
                            this.numberOfBeneficiaries = eElement.getElementsByTagName("numberOfBeneficiaries").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("NumberOfBeneficiariesUnpaid") != null
                                && eElement.getElementsByTagName("NumberOfBeneficiariesUnpaid").item(0) != null) {
                            this.numberOfBeneficiariesUnpaid = eElement.getElementsByTagName("NumberOfBeneficiariesUnpaid").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("NumberOfBeneficiariesPaid") != null
                                && eElement.getElementsByTagName("NumberOfBeneficiariesPaid").item(0) != null) {
                            this.numberOfBeneficiariesPaid = eElement.getElementsByTagName("NumberOfBeneficiariesPaid").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        if (eElement.getElementsByTagName("PercentProgress") != null
                                && eElement.getElementsByTagName("PercentProgress").item(0) != null) {
                            this.percentProgress = eElement.getElementsByTagName("PercentProgress").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }
                        //
                        if (eElement.getElementsByTagName("StatusCode") != null 
                                && eElement.getElementsByTagName("StatusCode").item(0) != null) {
                                this.statusCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent();
                                
                        }
                        if (eElement.getElementsByTagName("StatusMessage") != null 
                                && eElement.getElementsByTagName("StatusMessage").item(0) != null) {
                                this.statusMessage = eElement.getElementsByTagName("StatusMessage").item(0).getTextContent();
                                
                        }
                        if (eElement.getElementsByTagName("TransactionReference") != null
                                && eElement.getElementsByTagName("TransactionReference").item(0) != null) {
                            this.transactionReference = eElement.getElementsByTagName("TransactionReference").item(0).getTextContent();
                            
                        }  
                        if (eElement.getElementsByTagName("MNOTransactionReferenceId") != null && 
                                eElement.getElementsByTagName("MNOTransactionReferenceId").item(0) != null) {
                                this.mnoTransactionReferenceId = eElement.getElementsByTagName("MNOTransactionReferenceId").item(0).getTextContent();
                                
                        }
                        if (eElement.getElementsByTagName("TransactionStatus") != null 
                                && eElement.getElementsByTagName("TransactionStatus").item(0) != null) {
                                this.transactionStatus = eElement.getElementsByTagName("TransactionStatus").item(0).getTextContent();
                           
                        }

                        if (eElement.getElementsByTagName("BulkPaymentRequestIdentifier") != null
                                && eElement.getElementsByTagName("BulkPaymentRequestIdentifier").item(0) != null) {
                            this.bulkPaymentRequestIdentifier = eElement.getElementsByTagName("BulkPaymentRequestIdentifier").item(0).getTextContent();
                        }

                        if (eElement.getElementsByTagName("ActivityStatus") != null
                                && eElement.getElementsByTagName("ActivityStatus").item(0) != null) {
                            this.activityStatus = eElement.getElementsByTagName("ActivityStatus").item(0).getTextContent();
                            this.txType = "BULK PAYMENTS";
                        }

                        if (eElement.getElementsByTagName("Beneficiaries") != null
                                && eElement.getElementsByTagName("Beneficiaries").item(0) != null) {
                            NodeList benList = eElement.getElementsByTagName("Beneficiary");
                            for (int j=0; j < benList.getLength(); j++) {
                                Element eValue = (Element) benList.item(j);
                                String amount = "";
                                String AccountNumber = "";
                                String Name = "";
                                String AccountType = "";
                                String EmailAddress = "";
                                String Status = "";
                                String LowLevelStatus = "";
                                String LowLevelErrorMessage = "";
                                String LowLevelErrorMessageNegative = "";
                                String ProviderReference = "";

                                if (eValue.getElementsByTagName("Amount") != null
                                        && eValue.getElementsByTagName("Amount").item(0) != null) {
                                    amount = eValue.getElementsByTagName("Amount").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("AccountNumber") != null
                                        && eValue.getElementsByTagName("AccountNumber").item(0) != null) {
                                    AccountNumber = eValue.getElementsByTagName("AccountNumber").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("Name") != null
                                        && eValue.getElementsByTagName("Name").item(0) != null) {
                                    Name = eValue.getElementsByTagName("Name").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("AccountType") != null
                                        && eValue.getElementsByTagName("AccountType").item(0) != null) {
                                    AccountType = eValue.getElementsByTagName("AccountType").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("EmailAddress") != null
                                        && eValue.getElementsByTagName("EmailAddress").item(0) != null) {
                                    EmailAddress = eValue.getElementsByTagName("EmailAddress").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("Status") != null
                                        && eValue.getElementsByTagName("Status").item(0) != null) {
                                    Status = eValue.getElementsByTagName("Status").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("LowLevelStatus") != null
                                        && eValue.getElementsByTagName("LowLevelStatus").item(0) != null) {
                                    LowLevelStatus = eValue.getElementsByTagName("LowLevelStatus").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("LowLevelErrorMessage") != null
                                        && eValue.getElementsByTagName("LowLevelErrorMessage").item(0) != null) {
                                    LowLevelErrorMessage = eValue.getElementsByTagName("LowLevelErrorMessage").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("LowLevelErrorMessageNegative") != null
                                        && eValue.getElementsByTagName("LowLevelErrorMessageNegative").item(0) != null) {
                                    Status = eValue.getElementsByTagName("LowLevelErrorMessageNegative").item(0).getTextContent();
                                }
                                if (eValue.getElementsByTagName("ProviderReference") != null
                                        && eValue.getElementsByTagName("ProviderReference").item(0) != null) {
                                    ProviderReference = eValue.getElementsByTagName("ProviderReference").item(0).getTextContent();
                                }

                                Beneficiary b = new Beneficiary(
                                        amount,
                                        AccountNumber,
                                        Name,
                                        AccountType,
                                        EmailAddress
                                );
                                b.setStatus(Status);
                                b.setLowLevelErrorMessage(LowLevelErrorMessage);
                                b.setLowLevelStatus(LowLevelStatus);
                                b.setLowLevelErrorMessageNegative(LowLevelErrorMessageNegative);
                                b.setProviderReference(ProviderReference);
                                beneficiaries.add(b);
                            }

                            this.txType = "BULK PAYMENTS";
                        }
                        //NodeList valueList = labTest.getElementsByTagName("value");
                        break;
                    }
                }
            } catch (SAXException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
