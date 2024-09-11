package com.example.shen.accessibility.controller;

import com.example.shen.accessibility.model.PaymentRealStatus;
import com.example.shen.accessibility.model.QueryResponse;
import com.example.shen.accessibility.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/payment/available")
public class PaymentController {
	@Autowired
	PaymentService paymentService;

	@GetMapping("/all")
	public QueryResponse getAvailablePayment() {
		List<PaymentRealStatus> paymentRealStatusList = paymentService.getAvaliablePaymentList();
		return new QueryResponse(CollectionUtils.isEmpty(paymentRealStatusList) ? 1 : 0, paymentRealStatusList);
	}
}
