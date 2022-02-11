package org.mvnsearch.client;


import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class DubboRemoteServiceBuilder<T> {
    public static final Map<String, String> CONSUMED_SERVICES = new HashMap<>();
    private String serviceName;
    private Class<T> serviceInterface;
    private String baseUrl = "dubbo://localhost:20880/";

    public static <T> DubboRemoteServiceBuilder<T> client(Class<T> serviceInterface) {
        DubboRemoteServiceBuilder<T> builder = new DubboRemoteServiceBuilder<T>();
        builder.serviceInterface = serviceInterface;
        builder.serviceName = serviceInterface.getCanonicalName();
        return builder;
    }

    public DubboRemoteServiceBuilder<T> serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public DubboRemoteServiceBuilder<T> baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public T build() {
        CONSUMED_SERVICES.put(this.serviceName, this.baseUrl);
        DubboGenericServiceInvocationHandler proxy = new DubboGenericServiceInvocationHandler(baseUrl, serviceName);
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class[]{serviceInterface},
                proxy);
    }
}
