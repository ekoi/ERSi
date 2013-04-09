package nl.knaw.dans.ersy.webui.service;

import java.util.List;

import nl.knaw.dans.ersy.webui.secure.model.User;

import com.google.common.collect.Lists;

public class UserService {

    private static List<User> usersInDatabase = Lists.newArrayList(
            new User("John", "john"),
            new User("Akmi", "akmi"),
            new User("Tom", "tom"),
            new User("Steven", "steven"));


    public User findByLoginAndPassword(String login, String password) {

        for (User user : usersInDatabase) {
            if(user.getPassword().equals(password) & user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }
}
