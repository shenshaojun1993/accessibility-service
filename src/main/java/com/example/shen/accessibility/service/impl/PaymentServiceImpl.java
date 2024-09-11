package com.example.shen.accessibility.service.impl;

import com.example.shen.accessibility.cache.PaymentCache;
import com.example.shen.accessibility.model.PaymentRealStatus;
import com.example.shen.accessibility.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class PaymentServiceImpl implements PaymentService {
	@Override
	public List<PaymentRealStatus> getAvaliablePaymentList() {
		return PaymentCache.getAvailableList();
	}
}