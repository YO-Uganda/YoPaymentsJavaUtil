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
    String sandbox_url = "https://sandbox.yo.co.ug/services/yopaymentsdev/task.php";
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

        //Check your YoPayments account balance
	public YoPayments.YoPaymentsResponse getBalance() throws IOException{
		String xmlData="<?xml version='1.0' encoding='UTF-8'?>";
		xmlData +="<AutoCreate>"
		      +"<Request>"
			  +"<APIUsername>"+this.apiUsername+"</APIUsername>"
			  +"<APIPassword>"+this.apiPassword+"</APIPassword>"
			  +"<Method>acacctbalance</Method>"
			  +"</Request>"
			  +"</AutoCreate>";
		
		//Setting headers for the request
	    Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");
	    HttpRequestResponse  rs=YoPaymentsUtils.doHttpRequest("POST",url,xmlData,headers);
		 this.requestAndResponse = rs;
		if(!rs.response.isEmpty()){
            try{
                YoPaymentsResponse res= new YoPaymentsResponse(rs.response);
                return res;

            }catch(ParserConfigurationException ex){
            Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);

            
            }
		}
        
        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;

	}

    /**
	* getMiniStatement gets a list of transactions which were carried out on 
    *your account during a certain period of time .
    * @param String startDate: This is the date and time for which
    *the transaction should be queried. If specified, format: YYYY-MM-DD HM:MM:SS.
    * @param String endDate: This is the date and time for which
    * the transaction should be queried. If specified, format: YYYY-MM-DD HM:MM:SS.
    * @param String transactionStatus: . If specified, the only valid values are: 
    * FAILED, PENDING, SUCCEEDED and INDETERMINATE.
    * @param String currencyCode: Currency code for the transaction.
    * @param String resultSetLimit:Specifies the maximum number of results that
    * should be allowed. By default, the maximum is 15.
    * @param transactionEntryDesignation:This parameter can take any of three possible 
    * values; TRANSACTION, CHARGES,ANY.
    * @param externalReference:ExternalReference parameter that you provided with 
    *your withdraw or deposit request.
	*return the statement
	*/
    public YoPayments.YoPaymentsResponse getMiniStatement(
        String startDate,
        String endDate,
        String transactionStatus,
        String currencyCode,
        String resultSetLimit, 
        String transactionEntryDesignation,
        String externalReference
    ) throws IOException{
        String xmlData="<?xml version='1.0' encoding='UTF-8'?>";
           xmlData +="<AutoCreate>"
                    +"<Request>"
                    +"<APIUsername>"+this.apiUsername+"</APIUsername>"
                    +"<APIPassword>"+this.apiPassword+"</APIPassword>"
                    +"<Method>acgetministatement</Method>";
        if(!startDate.isEmpty()){
            xmlData +="<StartDate>"+startDate+"</StartDate>";
        }
        if(!endDate.isEmpty()){
            xmlData+="<EndDate>"+endDate+"</EndDate>";
        }
        if(!transactionStatus.isEmpty()){
            xmlData+="<TransactionStatus>"+transactionStatus+
            "</TransactionStatus>";
        }
        if(!currencyCode.isEmpty()){
            xmlData+="<CurrencyCode>"+currencyCode+
            "</CurrencyCode>";
        }
        if(!resultSetLimit.isEmpty()){
            xmlData+="<ResultSetLimit>"+resultSetLimit+
            "</ResultSetLimit>";
        }
        if(!transactionEntryDesignation.isEmpty()){
            xmlData+="<TransactionEntryDesignation>"+transactionEntryDesignation+
            "</TransactionEntryDesignation>";
        }
        if(!externalReference.isEmpty()){
            xmlData+="<ExternalReference>"+externalReference+
            "</ExternalReference>";
        }
        xmlData +="</Request>"
                +"</AutoCreate>";
        //Setting headers for the request
	    Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");
	    HttpRequestResponse  rs=YoPaymentsUtils.doHttpRequest("POST",url,xmlData,headers);
		 this.requestAndResponse = rs;
		if(!rs.response.isEmpty()){
            try{
                YoPaymentsResponse res= new YoPaymentsResponse(rs.response);
                return res;

            }catch(ParserConfigurationException ex){
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
 
            }
		}
        
        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;
        
    }

    /**
	* sendAirtimetakes transaction input fields to send mobile airtime  
	* 
	* @param String account:	This is the mobile money number of the receiver.
	* @param String amount:	This is the amount being sent.
	* @param String narrative:	This is the description of the transaction.
	* @param String externalReference: 	This is the unique tx id from our Database.
	* 
	* 
	* Returns : YoPaymentsResponse object. 
	*/

    public YoPayments.YoPaymentsResponse sendAirtimeMobile(
        String account,
        String amount,
        String narrative,
        String externalReference) throws IOException{
        String xmlData ="<?xml version='1.0' encoding='UTF-8'?>";
            xmlData +="<AutoCreate>"
                    +"<Request>"
                    +"<APIUsername>"+this.apiUsername+"</APIUsername>"
                    +"<APIPassword>"+this.apiPassword+"</APIPassword>"
                    +"<Method>acsendairtimemobile</Method>"
                    +"<NonBlocking>TRUE</NonBlocking>"
                    +"<Amount>"+amount+"</Amount>"
                    +"<Account>"+account+"</Account>"
                    +"<Narrative>"+narrative+"</Narrative>";
        if(!externalReference.isEmpty()){
            xmlData +="<ExternalReference>"+externalReference+"</ExternalReference>";

        }
        xmlData +="</Request>"
                +"</AutoCreate>";
        //Setting headers for the request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");
        HttpRequestResponse  rs=YoPaymentsUtils.doHttpRequest("POST",url,xmlData,headers);
         this.requestAndResponse = rs;
        if(!rs.response.isEmpty()){
            try{
                YoPaymentsResponse res= new YoPaymentsResponse(rs.response);
                return res;
            }catch(ParserConfigurationException ex){
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);
  
            }
        }
        
        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;
       
    }

    /**
	* pauseBulkPayment takes in parameters used to pause a running bulk payment.
    * @param String bulkPaymentRequestIdentifier; This uniquely identifies
    * the bulk payment that was created and returned from the gateway in the
    * parameter.
    * @param String pauseReason: This the reason why the bulk payment is being paused. 
	*/

    public YoPayments.YoPaymentsResponse pauseBulkPayment(
        String bulkPaymentRequestIdentifier,
        String pauseReason) throws IOException{
        String xmlData ="<?xml version='1.0' encoding='UTF-8'?>";
            xmlData +="<AutoCreate>"
                    +"<Request>"
                    +"<APIUsername>"+this.apiUsername+"</APIUsername>"
                    +"<APIPassword>"+this.apiPassword+"</APIPassword>"
                    +"<Method>acpausebulkpayment</Method>"
                    +"<BulkPaymentRequestIdentifier>"+bulkPaymentRequestIdentifier+"</BulkPaymentRequestIdentifier>"
                    +"<PauseReason>"+pauseReason+"</PauseReason>"
                    +"</Request>"
                    +"</AutoCreate>";
        //Setting headers for the request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");
        HttpRequestResponse  rs=YoPaymentsUtils.doHttpRequest("POST",url,xmlData,headers);
         this.requestAndResponse = rs;
        if(!rs.response.isEmpty()){
            try{
                YoPaymentsResponse res= new YoPaymentsResponse(rs.response);
                return res;

            }catch(ParserConfigurationException ex){
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);

                
            }
        }
        
        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;
       
    }
    
    /**
	* resumeBulkPayment takes in one parameter used to resume a previously 
	* paused bulk payment.
    * @param String bulkPaymentRequestIdentifier; This uniquely identifies
    * the bulk payment that was created and returned from the gateway in the
    * parameter.
	*/
    public YoPayments.YoPaymentsResponse resumeBulkPayment(
        String bulkPaymentRequestIdentifier) throws IOException{
        String xmlData ="<?xml version='1.0' encoding='UTF-8'?>";
            xmlData +="<AutoCreate>"
                    +"<Request>"
                    +"<APIUsername>"+this.apiUsername+"</APIUsername>"
                    +"<APIPassword>"+this.apiPassword+"</APIPassword>"
                    +"<Method>acresumebulkpayment</Method>"
                    +"<BulkPaymentRequestIdentifier>"+bulkPaymentRequestIdentifier+"</BulkPaymentRequestIdentifier>"
                    +"</Request>"
                    +"</AutoCreate>";
        //Setting headers for the request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");
        HttpRequestResponse  rs=YoPaymentsUtils.doHttpRequest("POST",url,xmlData,headers);
         this.requestAndResponse = rs;
        if(!rs.response.isEmpty()){
            try{
                YoPaymentsResponse res= new YoPaymentsResponse(rs.response);
                return res;

            }catch(ParserConfigurationException ex){
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);

                
            }
        }
        
        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;
       
    }

    /**
	*  cancelBulkPayment takes in one parameter used to cancel a running 
	* bulk payment.
    * @param String bulkPaymentRequestIdentifier; This uniquely identifies
    * the bulk payment that was created and returned from the gateway in the
    * parameter. 
	*/

    public YoPayments.YoPaymentsResponse cancelBulkPayment(
        String bulkPaymentRequestIdentifier) throws IOException{
        String xmlData ="<?xml version='1.0' encoding='UTF-8'?>";
            xmlData +="<AutoCreate>"
                    +"<Request>"
                    +"<APIUsername>"+this.apiUsername+"</APIUsername>"
                    +"<APIPassword>"+this.apiPassword+"</APIPassword>"
                    +"<Method>accancelbulkpayment</Method>"
                    +"<BulkPaymentRequestIdentifier>"+bulkPaymentRequestIdentifier+"</BulkPaymentRequestIdentifier>"
                    +"</Request>"
                    +"</AutoCreate>";
        //Setting headers for the request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml");
        headers.put("Content-transfer-encoding", "text");
        HttpRequestResponse  rs=YoPaymentsUtils.doHttpRequest("POST",url,xmlData,headers);
         this.requestAndResponse = rs;
        if(!rs.response.isEmpty()){
            try{
                YoPaymentsResponse res= new YoPaymentsResponse(rs.response);
                return res;

            }catch(ParserConfigurationException ex){
                Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, null, ex);

                
            }
        }
        
        Logger.getLogger(YoPayments.class.getName()).log(Level.SEVERE, rs.toString(), "");
        return null;
       
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
        ArrayList<Balance> balances = new ArrayList<>();
        String totalTransactions;
        String returnedTransactions;
        ArrayList<Transaction> transactions=new ArrayList<>();
                
        
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

                        if (eElement.getElementsByTagName("Balance") != null 
                            && eElement.getElementsByTagName("Balance").item(0) != null) {
                            NodeList balList = eElement.getElementsByTagName("Currency");
                            for (int j=0; j < balList.getLength(); j++) {
                                Element eValue = (Element) balList.item(j);
                                String code = "";
                                String balance = "";

                                if (eValue.getElementsByTagName("Code") != null
                                        && eValue.getElementsByTagName("Code").item(0) != null) {
                                    code = eValue.getElementsByTagName("Code").item(0).getTextContent();
                                    
                                }
                                if (eValue.getElementsByTagName("Balance") != null
                                        && eValue.getElementsByTagName("Balance").item(0) != null) {
                                    balance = eValue.getElementsByTagName("Balance").item(0).getTextContent();
                                    }
                                    Balance bal =new Balance(
                                        code,
                                        balance
                                    );
                                    balances.add(bal);
                            }
                        }	
                        if(eElement.getElementsByTagName("TotalTransactions") !=null &&
                            eElement.getElementsByTagName("TotalTransactions").item(0) !=null
                            ){
                            totalTransactions= eElement.getElementsByTagName("TotalTransactions").item(0).getTextContent();
                                
                            }
                        if(eElement.getElementsByTagName("ReturnedTransactions") !=null &&
                            eElement.getElementsByTagName("ReturnedTransactions").item(0) !=null
                            ){
                            returnedTransactions= eElement.getElementsByTagName("ReturnedTransactions").item(0).getTextContent();
                                
                            }	
                        if(eElement.getElementsByTagName("Transactions")!=null 
                            && eElement.getElementsByTagName("Transactions").item(0)!=null){
                            NodeList transactionList=eElement.getElementsByTagName("Transaction");
                            for(int j=0; j<transactionList.getLength(); j++){
                                Element eValue= (Element) transactionList.item(j);
                                String transactionSystemId="";
                                String transactionReference="";
                                String transactionStatus="";
                                String initiationDate="";
                                String completionDate="";
                                String narrativeBase64="";
                                String currency="";
                                String amount="";
                                String balance="";
                                String generalType="";
                                String detailedType="";
                                String beneficiaryMsisdn="";
                                String beneficiaryBase64="";
                                String senderMsisdn="";
                                String senderBase64="";
                                String base64TransactionExternalReference="";
                                String transactionEntryDesignation="";
                                if(eValue.getElementsByTagName("TransactionSystemId")!=null
                                && eValue.getElementsByTagName("TransactionSystemId").item(0)!=null){
                                transactionSystemId=eValue.getElementsByTagName("TransactionSystemId").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("TransactionReference")!=null
                                && eValue.getElementsByTagName("TransactionReference").item(0)!=null){
                                transactionReference=eValue.getElementsByTagName("TransactionReference").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("TransactionStatus")!=null
                                && eValue.getElementsByTagName("TransactionStatus").item(0)!=null){
                                transactionStatus=eValue.getElementsByTagName("TransactionStatus").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("InitiationDate")!=null
                                && eValue.getElementsByTagName("InitiationDate").item(0)!=null){
                                initiationDate=eValue.getElementsByTagName("InitiationDate").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("CompletionDate")!=null
                                && eValue.getElementsByTagName("CompletionDate").item(0)!=null){
                                completionDate=eValue.getElementsByTagName("CompletionDate").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("NarrativeBase64")!=null
                                && eValue.getElementsByTagName("NarrativeBase64").item(0)!=null){
                                narrativeBase64=eValue.getElementsByTagName("NarrativeBase64").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("Currency")!=null
                                && eValue.getElementsByTagName("Currency").item(0)!=null){
                                currency=eValue.getElementsByTagName("Currency").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("Amount")!=null
                                && eValue.getElementsByTagName("Amount").item(0)!=null){
                                amount=eValue.getElementsByTagName("Amount").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("Balance")!=null
                                && eValue.getElementsByTagName("Balance").item(0)!=null){
                                balance=eValue.getElementsByTagName("Balance").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("GeneralType")!=null
                                && eValue.getElementsByTagName("GeneralType").item(0)!=null){
                                generalType=eValue.getElementsByTagName("GeneralType").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("DetailedType")!=null
                                && eValue.getElementsByTagName("DetailedType").item(0)!=null){
                                detailedType=eValue.getElementsByTagName("DetailedType").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("BeneficiaryMsisdn")!=null
                                && eValue.getElementsByTagName("BeneficiaryMsisdn").item(0)!=null){
                                beneficiaryMsisdn=eValue.getElementsByTagName("BeneficiaryMsisdn").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("BeneficiaryBase64")!=null
                                && eValue.getElementsByTagName("BeneficiaryBase64").item(0)!=null){
                                beneficiaryBase64=eValue.getElementsByTagName("BeneficiaryBase64").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("SenderMsisdn")!=null
                                && eValue.getElementsByTagName("SenderMsisdn").item(0)!=null){
                                senderMsisdn=eValue.getElementsByTagName("SenderMsisdn").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("SenderBase64")!=null
                                && eValue.getElementsByTagName("SenderBase64").item(0)!=null){
                                senderBase64=eValue.getElementsByTagName("SenderBase64").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("Base64TransactionExternalReference")!=null
                                && eValue.getElementsByTagName("Base64TransactionExternalReference").item(0)!=null){
                                base64TransactionExternalReference=eValue.getElementsByTagName("Base64TransactionExternalReference").item(0).getTextContent();

                                }
                                if(eValue.getElementsByTagName("TransactionEntryDesignation")!=null
                                && eValue.getElementsByTagName("TransactionEntryDesignation").item(0)!=null){
                                transactionEntryDesignation=eValue.getElementsByTagName("TransactionEntryDesignation").item(0).getTextContent();

                                }
                                Transaction t=new Transaction(
                                            transactionSystemId,
                                            transactionReference,
                                            transactionStatus,
                                            initiationDate,
                                            completionDate,
                                            narrativeBase64,
                                            currency,
                                            amount
                                );
                                t.setBalance(balance);
                                t.setGeneralType(generalType);
                                t.setDetailedType(detailedType);
                                t.setBeneficiaryMsisdn(beneficiaryMsisdn);
                                t.setBeneficiaryBase64(beneficiaryBase64);
                                t.setSenderMsisdn(senderMsisdn);
                                t.setSenderBase64(senderBase64);
                                t.setBase64TransactionExternalReference(base64TransactionExternalReference);
                                t.setTransactionEntryDesignation(transactionEntryDesignation);
                                transactions.add(t);

                            }

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
