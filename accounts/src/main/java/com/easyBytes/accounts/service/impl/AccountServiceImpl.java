package com.easyBytes.accounts.service.impl;

import com.easyBytes.accounts.constants.AccountConstants;
import com.easyBytes.accounts.dto.AccountDTO;
import com.easyBytes.accounts.dto.CustomerDTO;
import com.easyBytes.accounts.entity.Account;
import com.easyBytes.accounts.entity.Customer;
import com.easyBytes.accounts.exceptions.CustomerAlreadyExistException;
import com.easyBytes.accounts.exceptions.ResourceNotFoundException;
import com.easyBytes.accounts.mapper.AccountMapper;
import com.easyBytes.accounts.mapper.CustomerMapper;
import com.easyBytes.accounts.repository.AccountRepository;
import com.easyBytes.accounts.repository.CustomerRepository;
import com.easyBytes.accounts.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Override
    public void createAccount(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> customerByMobileNumber = customerRepository.findCustomerByMobileNumber(customer.getMobileNumber());
        if(customerByMobileNumber.isPresent()){
            throw new CustomerAlreadyExistException("Customer already registered with given mobileNumber "
                    +customerDTO.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDTO fetchAccount(String mobileNumber) {
        Customer customerByMobileNumber = customerRepository.findCustomerByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobile number", mobileNumber));
        Account account = accountRepository.findAccountByCustomerId(customerByMobileNumber.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customer ID", customerByMobileNumber.getCustomerId().toString()));
        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDto(customerByMobileNumber, new CustomerDTO());
        customerDTO.setAccountsDto(AccountMapper.mapToAccountDTO(account, new AccountDTO()));
        return customerDTO;
    }

    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        boolean isUpdated = false;
        AccountDTO accountsDto = customerDTO.getAccountsDto();
        if(accountsDto !=null ){
            Account accounts = accountRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccount(accountsDto, accounts);
            accounts = accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDTO,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findCustomerByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    private Account createNewAccount(Customer savedCustomer) {
        Account account = new Account();
        account.setCustomerId(savedCustomer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        account.setAccountNumber(randomAccNumber);
        account.setAccountType(AccountConstants.SAVINGS);
        account.setBranchAddress(AccountConstants.ADDRESS);
        return account;
    }
}
