package org.mvnsearch.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.mvnsearch.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DubboService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public String findNickById(Integer id) {
        return "nick: " + id;
    }

    @Override
    public List<String> findNicksByIds(List<Integer> ids) {
        return ids.stream().map(this::findNickById).collect(Collectors.toList());
    }

    @Override
    public String findEmailByNick(String nick) {
        return "demo@example.com";
    }

    @Override
    public boolean auth(String nick, String password) {
        return false;
    }

    @Override
    public List<String> top10() {
        return Arrays.asList("first", "second");
    }
}
