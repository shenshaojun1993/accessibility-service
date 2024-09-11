package com.example.shen.accessibility.model;

import lombok.*;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
public class QueryResponse {
    private int code;
    private List<PaymentRealStatus> availableServices;
}