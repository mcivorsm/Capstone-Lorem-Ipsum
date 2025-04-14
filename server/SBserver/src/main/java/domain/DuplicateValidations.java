package domain;

import java.util.List;

public class DuplicateValidations {
    public static <T> boolean isDuplicate(T obj, List<T> listObj) {
        for (T o : listObj) {
            if (obj.equals(o)) return true;
        }
        return false;
    }
}
