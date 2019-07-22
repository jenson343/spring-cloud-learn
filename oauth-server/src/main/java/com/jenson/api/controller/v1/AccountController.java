package com.jenson.api.controller.v1;

import com.jenson.domain.entity.Account;
import com.jenson.domain.repository.AccountRepository;
import com.jenson.infra.mapper.AccoutMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AccoutMapper accoutMapper;

	@GetMapping
	String test(){
		return "test";
	}

	@GetMapping("/add")
	Account addAccount(){
		Account account=new Account();
		account.setUserName("abc");
		account.setPassWord("123");
		return accountRepository.save(account);
	}

	@GetMapping("/select")
	Account selectAccount(@RequestParam Long id){
		return accoutMapper.selectAccount(id);
	}

	@GetMapping("/user")
	public Principal user(Principal user){
		return user;
	}
}
