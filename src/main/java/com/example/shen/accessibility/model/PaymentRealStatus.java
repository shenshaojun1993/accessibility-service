package com.example.shen.accessibility.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
public class PaymentRealStatus {
	private int type;
	private String name;
	private boolean available;
}