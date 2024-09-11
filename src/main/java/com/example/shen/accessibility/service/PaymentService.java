package com.example.shen.accessibility.service;

import com.example.shen.accessibility.model.PaymentRealStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public interface PaymentService {
	/**
	 * 返回所有可用的支付方式列表
	 *
	 * @return 所有可用支付方式列表
	 */
	List<PaymentRealStatus> getAvaliablePaymentList();
}