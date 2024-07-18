package com.easyBytes.accounts.mapper;

import com.easyBytes.accounts.dto.AccountDTO;
import com.easyBytes.accounts.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDTO accountDTO, Account account){
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setBranchAddress(account.getBranchAddress());
        return account;
    }

    public static AccountDTO mapToAccountDTO(Account account, AccountDTO accountDTO){
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setBranchAddress(account.getBranchAddress());
        return accountDTO;
    }

}
