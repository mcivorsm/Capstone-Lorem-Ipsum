package domain;

import java.util.List;
import models.User;

public class DuplicateValidations {
    public static <T> boolean isDuplicate(T obj, List<T> listObj) {
        for (T o : listObj) {
            if (obj.equals(o)) return true;
        }
        return false;
    }
    public static boolean isUsernameDuplicate(User user, List<User> listUser){
        for(User u: listUser){
            if(u.getUsername().equalsIgnoreCase(user.getUsername())) return true;
        }
        return false;
    }
    public static boolean isEmailDuplicate(User user, List<User> listUser){
        for(User u: listUser){
            if(u.getEmail().equalsIgnoreCase(user.getEmail())) return true;
        }
        return false;
    }
}
