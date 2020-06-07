package com.food.table.serviceimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.constant.PaymentStatus;
import com.food.table.constant.PaymentStatusEnum;
import com.food.table.dto.Payment;
import com.food.table.model.PaymentCallback;
import com.food.table.model.PaymentDetail;
import com.food.table.repo.OrderRepository;
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
        Payment payment = paymentRepository.findByTxnId(paymentResponse.getTxnid());
        if(payment != null) {
            //TODO validate the hash
            PaymentStatus paymentStatus = null;
            if(paymentResponse.getStatus().equals("failure")){
                paymentStatus = PaymentStatus.Failed;
            }else if(paymentResponse.getStatus().equals("success")) {
                paymentStatus = PaymentStatus.Success;
                paymentStatusEnum = PaymentStatusEnum.TRANSACTION_SUCCESSFUL;
            }
            payment.setPaymentStatus(paymentStatus);
            payment.setMihpayId(paymentResponse.getMihpayid());
            payment.setMode(paymentResponse.getMode());
            paymentRepository.save(payment);
        }
        orderService.updateOrderStateAfterPayment(payment.getOrder(), paymentStatusEnum);
        return paymentStatusEnum.toString();
    }

    private void savePaymentDetail(PaymentDetail paymentDetail) {
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(paymentDetail.getAmount()));
        payment.setEmail(paymentDetail.getEmail());
        payment.setName(paymentDetail.getName());
        payment.setPaymentDate(new Date());
        payment.setPaymentStatus(PaymentStatus.Pending);
        payment.setPhone(paymentDetail.getPhone());
        payment.setProductInfo(paymentDetail.getProductInfo());
        payment.setTxnId(paymentDetail.getTxnId());
        payment.setOrder(paymentDetail.getOrder());
        paymentRepository.save(payment);
    }

}
