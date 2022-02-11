package org.mvnsearch.client;

import org.junit.jupiter.api.Test;
import org.mvnsearch.UserService;

import java.lang.reflect.Method;

import static java.lang.String.join;
import static org.assertj.core.api.Assertions.assertThat;


public class DubboGenericServiceInvocationHandlerTest {
    private final DubboGenericServiceInvocationHandler serviceInvocationHandler = new DubboGenericServiceInvocationHandler("dubbo://127.0.0.1:20880/", UserService.class.getCanonicalName());

    @Test
    public void testParamsTypes() throws Exception {
        final Method authMethod = UserService.class.getMethod("auth", String.class, String.class);
        final String[] dubboMethodParamsTypes = serviceInvocationHandler.getDubboMethodParamsTypes(authMethod);
        assertThat(dubboMethodParamsTypes).isNotEmpty();
        assertThat(join(",", dubboMethodParamsTypes)).isEqualTo("java.lang.String,java.lang.String");
    }

}
