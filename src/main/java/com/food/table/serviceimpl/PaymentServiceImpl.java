package com.food.table.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.constant.PaymentStatus;
import com.food.table.constant.PaymentStatusEnum;
import com.food.table.dto.Payment;
import com.food.table.model.PaymentCallback;
import com.food.table.model.PaymentDetail;
import com.food.table.repo.PaymentRepository;
import com.food.table.service.OrderService;
import com.food.table.service.PaymentService;
import com.food.table.util.PaymentUtil;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PaymentUtil paymentUtil;

    @Override
    public PaymentDetail proceedPayment(PaymentDetail paymentDetail) {
        paymentDetail = paymentUtil.populatePaymentDetail(paymentDetail);
        savePaymentDetail(paymentDetail);
        return paymentDetail;
    }

    @Override
    public String payuCallback(PaymentCallback paymentResponse) {
    	PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.TRANSACTION_FAILED;       
        Payment payment = paymentRepository.findByTxnId(paymentResponse.getOrderId());
        boolean validateSignature = paymentUtil.validateResponseSignature(paymentResponse, payment.getSignature());
        if(payment != null && validateSignature) {
            //TODO validate the hash
            PaymentStatus paymentStatus = null;
            if(paymentResponse.getTxStatus().equals("SUCCESS") || paymentResponse.getTxStatus().equals("FLAGGED") ) {
                paymentStatus = PaymentStatus.SUCCESS;
                paymentStatusEnum = PaymentStatusEnum.TRANSACTION_SUCCESSFUL;
            } else {
                paymentStatus = PaymentStatus.valueOf(paymentResponse.getTxStatus());
            }
            payment.setPaymentStatus(paymentStatus);
            payment.setPaymentMode(paymentResponse.getPaymentMode());
            payment.setTxMsg(paymentResponse.getTxMsg());
            payment.setReferenceId(paymentResponse.getReferenceId());
            paymentRepository.save(payment);
        }
        orderService.updateOrderStateAfterPayment(payment.getOrder(), paymentStatusEnum);
        return paymentStatusEnum.toString();
    }

    private void savePaymentDetail(PaymentDetail paymentDetail) {
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(paymentDetail.getOrderAmount()));
        payment.setEmail(paymentDetail.getCustomerEmail());
        payment.setName(paymentDetail.getCustomerName());
        payment.setPaymentDate(new Date());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPhone(paymentDetail.getCustomerPhone());
        payment.setProductInfo(paymentDetail.getOrderId());
        payment.setTxnId(paymentDetail.getOrderId());
        payment.setOrder(paymentDetail.getOrder());
        payment.setSignature(paymentDetail.getSignature());
        paymentRepository.save(payment);
    }    

}
