package org.mvnsearch.client;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericService;

import java.net.URI;

public class DubboDirectClientApp {
    public static void main(String[] args) {
        ApplicationModel.defaultModel().getApplicationConfigManager().setApplication(new ApplicationConfig("ConsumerTest"));
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        try {
            RpcContext.getContext().setAttachment("generic", "gson");
            final URI uri = URI.create("dubbo://127.0.0.1:20880/org.mvnsearch.AccountService?method=findById(java.lang.Integer)");
            reference.setUrl(uri.toString());
            reference.setInterface(uri.getPath().substring(1));
            reference.setGeneric("gson");
            reference.setCheck(false);
            Object responseObject = reference.get().$invoke("findById", new String[]{"java.lang.Integer"}, new Object[]{"1"});
            System.out.println(responseObject.toString());
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            reference.destroy();
        }
    }

}
