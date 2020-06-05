package com.food.table.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.food.table.model.PaymentDetail;

@Service
public class PaymentUtil {
		
	private static final String paymentKey = "as1JZtHP";

    private static final String paymentSalt = "bACZe3KkHX";
    
//    @Value("${hostname}")
    String hostname = "http://countrybroot.com/api";
    
    String sUrl = hostname+"/payment/payment-response";

    String fUrl = hostname+"/payment/payment-response";

    public PaymentDetail populatePaymentDetail(PaymentDetail paymentDetail){
        String hashString = "";
        Random rand = new Random();
        String randomId = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        String txnId = "Dev" + hashCal("SHA-256", randomId).substring(0, 12);
        paymentDetail.setTxnId(txnId);
        String hash = "";
        //String otherPostParamSeq = "phone|surl|furl|lastname|curl|address1|address2|city|state|country|zipcode|pg";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|||||||||||bACZe3KkHX";
        hashString = hashSequence.concat(paymentSalt);
        hashString = hashString.replace("key", paymentKey);
        hashString = hashString.replace("txnid", txnId);
        hashString = hashString.replace("amount", paymentDetail.getAmount());
        hashString = hashString.replace("productinfo", paymentDetail.getProductInfo());
//      TODO remove below condition 
        hashString = hashString.replace("firstname", ObjectUtils.defaultIfNull(paymentDetail.getName(), ""));
//      TODO remove below condition 
        hashString = hashString.replace("email", ObjectUtils.defaultIfNull(paymentDetail.getEmail(), ""));

        hash = hashCal("SHA-512", hashString);
        paymentDetail.setHash(hash);
        paymentDetail.setFUrl(fUrl);
        paymentDetail.setSUrl(sUrl);
        paymentDetail.setKey(paymentKey);
        return paymentDetail;
    }

    public static String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();
    }


}
