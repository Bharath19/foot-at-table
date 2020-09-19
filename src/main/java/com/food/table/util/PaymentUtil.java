package com.food.table.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.food.table.constant.ApplicationConstants;
import com.food.table.model.PaymentCallback;
import com.food.table.model.PaymentDetail;

@Component
public class PaymentUtil {

	@Value("${cashfree.appId}")
	private String appId;

	@Value("${cashfree.secretKey}")
	private String secretKey;

	@Value("${hostname}")
	private String hostname;

	private String sUrl = "/payment/payment-response";

	private String fUrl = "/payment/payment-response";

	public PaymentDetail populatePaymentDetail(PaymentDetail paymentDetail) {
		Random rand = new Random();
		String randomId = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
		String orderId = "TRN_" + paymentDetail.getOrderId() + "_" + hashCal("SHA-256", randomId).substring(0, 12);
		paymentDetail.setOrderId(orderId);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("appId", appId);
		postData.put("orderId", paymentDetail.getOrderId());
		postData.put("orderAmount", paymentDetail.getOrderAmount());
		postData.put("orderCurrency", ApplicationConstants.CURRENCY);
		postData.put("orderNote", paymentDetail.getOrderNote());
		postData.put("customerName", paymentDetail.getCustomerName());
		postData.put("customerEmail", paymentDetail.getCustomerEmail());
		postData.put("customerPhone", paymentDetail.getCustomerPhone());
		postData.put("returnUrl", sUrl);
		postData.put("notifyUrl", null);
		String data = "";
		SortedSet<String> keys = new TreeSet<String>(postData.keySet());
		for (String key : keys) {
			data = data + key + postData.get(key);
		}
		Mac sha256_HMAC = null;
		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key_spec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key_spec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		String signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
		paymentDetail.setSignature(signature);
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

	private String getsUrl() {
		return hostname + sUrl;
	}

	private String getfUrl() {
		return hostname + fUrl;
	}

	public boolean validateResponseSignature(PaymentCallback paymentResponse, String signature) {
		LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();

		postData.put("orderId", paymentResponse.getOrderId());
		postData.put("orderAmount", paymentResponse.getOrderAmount());
		postData.put("referenceId", paymentResponse.getReferenceId());
		postData.put("txStatus", paymentResponse.getTxStatus());
		postData.put("paymentMode", paymentResponse.getPaymentMode().toString());
		postData.put("txMsg", paymentResponse.getTxMsg());
		postData.put("txTime", paymentResponse.getTxTime());

		String data = "";
		Set<String> keys = postData.keySet();

		for (String key : keys) {
			data = data + postData.get(key);
		}
		Mac sha256_HMAC = null;
		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key_spec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key_spec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		String responseSignature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));

		return responseSignature.equalsIgnoreCase(signature);
	}
}
