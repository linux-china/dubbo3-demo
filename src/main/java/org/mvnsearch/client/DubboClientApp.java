package org.mvnsearch.client;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.mvnsearch.AccountService;
import org.mvnsearch.UserService;

import java.util.List;

public class DubboClientApp {
    public static void main(String[] args) {
        ApplicationModel.defaultModel().getApplicationConfigManager().setApplication(new ApplicationConfig("ConsumerTest"));
        final UserService userService = DubboRemoteServiceBuilder.client(UserService.class).build();
        final AccountService accountService = DubboRemoteServiceBuilder.client(AccountService.class).build();
        System.out.println(userService.findEmailByNick("demo"));
        System.out.println(userService.auth("demo", "112"));
        final List<String> top10 = userService.top10();
        System.out.println(top10);
        System.out.println(accountService.findById(1).getEmail());
    }
}
