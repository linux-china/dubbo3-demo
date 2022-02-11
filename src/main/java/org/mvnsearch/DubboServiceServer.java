package org.mvnsearch;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.mvnsearch.impl.AccountServiceImpl;
import org.mvnsearch.impl.UserServiceImpl;

import java.util.concurrent.CountDownLatch;

public class DubboServiceServer {
    public static void main(String[] args) throws Exception {
        ApplicationModel.defaultModel().getApplicationConfigManager().setApplication(new ApplicationConfig("user-service-provider"));
        exposeDubboService(UserService.class, new UserServiceImpl());
        exposeDubboService(AccountService.class, new AccountServiceImpl());
        System.out.println("Dubbo server started: dubbo://localhost:20880 with AccountService and UserService!");
        new CountDownLatch(1).await();
    }

    public static <T> void exposeDubboService(Class<T> serviceInterface, T impl) {
        ServiceConfig<T> service = new ServiceConfig<>();
        service.setInterface(serviceInterface);
        service.setRef(impl);
        service.export();
    }
}
