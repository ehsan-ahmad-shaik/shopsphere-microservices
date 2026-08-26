package com.order.client;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.order.client.dto.User;


@FeignClient(name = "User-Service")
public interface UserClient 
{
	@GetMapping("/user/{id}")
	public Optional<User> getUserById(@PathVariable("id") Long id);
}
