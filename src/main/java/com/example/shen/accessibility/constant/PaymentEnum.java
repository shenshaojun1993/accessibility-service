package com.example.shen.accessibility.constant;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 */
@AllArgsConstructor
public enum PaymentEnum {
	/**
	 * 红包
	 */
	RED_ENVELOPE(PaymentConstant.RED_ENVELOPE, "红包"),
	BALANCE(PaymentConstant.BALANCE, "余额"),
	COUPON(PaymentConstant.COUPON, "优惠券"),
	VOUCHER(PaymentConstant.VOUCHER, "代金券"),
	OTHER(PaymentConstant.OHTER, "其他");

	@Getter
	private final int type;

	@Getter
	private final String name;

	public static List<PaymentEnum> getAllPaymentWay() {
		return Arrays.stream(PaymentEnum.values()).toList();
	}
}