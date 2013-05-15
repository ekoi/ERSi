package nl.knaw.dans.ersy.webui.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.knaw.dans.ersy.textmining.clustering.ReadVector;
import nl.knaw.dans.ersy.webui.secure.model.User;

import com.google.common.collect.Lists;

public class UserService {
	
	private static Logger LOG = LoggerFactory.getLogger(UserService.class);
    private static List<User> usersInDatabase = Lists.newArrayList(
            new User("Akmi", "akmi"),
            new User("DANS", "dans"));


    public User findByLoginAndPassword(String login, String password) {

        for (User user : usersInDatabase) {
            if(user.getPassword().equals(password) & user.getLogin().equals(login)) {
            	LOG.debug("#### " + user.getLogin() + " ####");
                return user;
            }
        }
        return null;
    }
}
