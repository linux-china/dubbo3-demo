package org.mvnsearch.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.mvnsearch.Account;
import org.mvnsearch.AccountService;

@DubboService(interfaceClass = AccountService.class)
public class AccountServiceImpl implements AccountService {
    @Override
    public Account findById(Integer id) {
        return new Account(id, "wayne_wildshb@column.ps");
    }
}
