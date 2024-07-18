package com.easyBytes.accounts.service;

import com.easyBytes.accounts.dto.CustomerDTO;

public interface IAccountService {

    void createAccount(CustomerDTO customerDTO);

    CustomerDTO fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDTO customerDTO);

    boolean deleteAccount(String mobileNumber);
}
