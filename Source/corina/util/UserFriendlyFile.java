package corina.util;

import java.io.File;

// just like a java.io.File except toString() returns its name, not its path.
// used in a popup or list, where i want to know the path but users should
// see only the name.
public class UserFriendlyFile extends File {
    public UserFriendlyFile(String f) {
        super(f);
    }
    public String toString() {
        return getName();
    }
}
