package org.mvnsearch;

import java.util.List;

public interface UserService {

    String findNickById(Integer id);

    List<String> findNicksByIds(List<Integer> ids);

    String findEmailByNick(String nick);

    boolean auth(String nick, String password);

    List<String> top10();

}
