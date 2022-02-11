package org.mvnsearch.client;

import com.google.gson.Gson;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DubboGenericServiceInvocationHandler implements InvocationHandler {
    private static final Gson gson = new Gson();
    private static final String[] EMPTY_STRING_ARRAY = new String[]{};
    private final String baseUrl;
    private final String serviceName;
    private final Map<Method, String[]> dubboMethodParamsTypes = new HashMap<>();
    private final Map<Method, ReferenceConfig<GenericService>> referenceConfigStore = new HashMap<>();

    public DubboGenericServiceInvocationHandler(String baseUrl, String serviceName) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl;
        } else {
            this.baseUrl = baseUrl + "/";
        }
        this.serviceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        String[] paramsTypes = getDubboMethodParamsTypes(method);
        RpcContext.getClientAttachment().setAttachment("generic", "gson");
        ReferenceConfig<GenericService> reference = getReferenceConfig(method, paramsTypes);
        final Object result = reference.get().$invoke(methodName, paramsTypes, convertArgsToJson(args));
        // simple converter, please use carefully
        if (result != null) {
            if (result instanceof Map) {
                return gson.fromJson(gson.toJsonTree(result, Map.class), method.getReturnType());
            } else if (result instanceof List) {
                return gson.fromJson(gson.toJsonTree(result, List.class), method.getGenericReturnType());
            }
        }
        return result;
    }

    private ReferenceConfig<GenericService> getReferenceConfig(Method method, String[] paramsTypes) {
        if (referenceConfigStore.containsKey(method)) {
            return referenceConfigStore.get(method);
        }
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        String dubboServiceUrl = baseUrl + serviceName + "?method=" + method.getName() + "(" + String.join(",", paramsTypes) + ")";
        reference.setUrl(dubboServiceUrl);
        reference.setInterface(serviceName);
        reference.setGeneric("gson");
        reference.setCheck(false);
        referenceConfigStore.put(method, reference);
        return reference;
    }

    public String[] convertArgsToJson(Object[] args) {
        if (args == null || args.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        String[] argsJson = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                argsJson[i] = "";
            } else if (arg instanceof String) {
                argsJson[i] = (String) arg;
            } else {
                argsJson[i] = gson.toJson(arg);
            }
        }
        return argsJson;
    }

    public String[] getDubboMethodParamsTypes(Method method) {
        if (dubboMethodParamsTypes.containsKey(method)) {
            return dubboMethodParamsTypes.get(method);
        }
        String[] paramsStringTypes;
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            paramsStringTypes = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                final Class<?> parameterType = parameterTypes[i];
                final String canonicalName = parameterType.getCanonicalName();
                paramsStringTypes[i] = canonicalName;
            }
            return paramsStringTypes;
        } else {
            paramsStringTypes = EMPTY_STRING_ARRAY;
        }
        dubboMethodParamsTypes.put(method, paramsStringTypes);
        return paramsStringTypes;
    }
}
