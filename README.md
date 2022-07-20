Yo Payments Java Utility
=========================================
Use this utility to integrate Yo Payments APIs in your Java Code. Follow the procedure below to test this code with your own Yo Payments credentials.
To create generate RSA keys, follow the guide in chapter 4.2 of the main API documentation.

Java Utility for Yo! Payments
=========================================
1. Clone this source code.
2. Put your keys under ./target/keys. Then, make sure you set your key variables to the right path. 
3. The main method is in YoPaymentsSample.java.
4. To test acwithdrawfunds method, in main method, un comment a line which has: //testWithdawRequest();
5. To test normal verification, in main method, uncomment the line which has: //normalVerification();
6. To test secondary verification, in main method, uncomment //secondaryVerification(); The Secondary verification uses a different public key.
7. Test other aspects like Bulk payments, Bulk Payments Check status and other parts as per the examples commented in there.

Compile and Run
=======================
After Successful compilation, run and you should see similar output as below:

Trace
========================================
Status: OK
StatusCode: 0
StatusMessage: 
ErrorMessage: 
TransactionStatus: SUCCEEDED
TransactionReference: q1XYY4fpvCxQFHeIMwZ0isNzC3oIzBbr194cOT2ksdOxmvaXLyJYIDfprArr0vzK9d7ab0af314d6f475272cecf1b2cf246
NetworkReferenceId: 348086079

****URL****
http://sandbox.yo.co.ug/services/yopaymentsdev/task.php

****Status****
200

****Request Headers****
Content-transfer-encoding: text
Content-Type: text/xml


****Request Data****
<?xml version="1.0" encoding="UTF-8"?><AutoCreate><Request><APIUsername>90003066053</APIUsername><APIPassword>do9t-IqUe-FxJW-IgUI-NV1E-fDee-YhOQ-iikQ</APIPassword><Method>acwithdrawfunds</Method><Amount>500</Amount><Account>256783086794</Account><Narrative>Java Sample</Narrative><ExternalReference>R1004-758722264</ExternalReference><PublicKeyAuthenticationNonce>-113416455</PublicKeyAuthenticationNonce><PublicKeyAuthenticationSignatureBase64>MUHqKEJ+LtM4fIn6pDv7WLxYau7TOFpVjzahvrHkS0HDlJ40WqfkEirqAGF46XZC5kXUaLf1TTJVQHJ5uU+iS5jhS2s5ZWLIEGdPVNMHA2IK4+8kNnBb+/ySFg9vquzxdwnPif8fLnlEYHxfl/hRz5J6WxNI0pXV25bPT3m0MumJhT2zP+TsZwHzPjQZGW7rDG8/Pic/BRvwuzTtBQXhnl7LK2iw+qPRBFl0mmeYFwX4S5ctlMCMlrCwftAJaWEo8PYzUJpilHhm3hZBYF+ZiTkufu8ePyXebDkmhLF6lSADxWenFmtIopFMfZQx9QmXjjX+WfaQaa50UJAnqaTlSA==</PublicKeyAuthenticationSignatureBase64></Request></AutoCreate>

****Respoonse Headers****
Cache-Control: no-cache
Server: Apache/2.2.3 (Red Hat)
Connection: close
Pragma: no-cache
Expires: Tue, 12 Mar 1910 10:45:00 GMT
Content-Length: 382
Content-transfer-encoding: text
Date: Fri, 22 May 2020 18:08:59 GMT
Content-Type: text/xml;charset=UTF-8
X-Powered-By: PHP/5.6.30


****Respones Data****
<?xml version="1.0" encoding="UTF-8"?><AutoCreate><Response><Status>OK</Status><StatusCode>0</StatusCode><TransactionStatus>SUCCEEDED</TransactionStatus><TransactionReference>q1XYY4fpvCxQFHeIMwZ0isNzC3oIzBbr194cOT2ksdOxmvaXLyJYIDfprArr0vzK9d7ab0af314d6f475272cecf1b2cf246</TransactionReference><MNOTransactionReferenceId>348086079</MNOTransactionReferenceId></Response></AutoCreate>

****Error Message****
