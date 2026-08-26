package com.order.client.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User 
{
	private Long id;
	private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private LocalDateTime createdAt;
}
