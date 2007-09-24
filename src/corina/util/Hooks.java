package corina.util;

import java.util.List;
import java.util.ArrayList;

// a list of hooks
public class Hooks implements Runnable {

    private List hooks;

    public Hooks() {
	hooks = new ArrayList();
    }

    public void addHook(Runnable hook) {
	if (!hooks.contains(hook))
	    hooks.add(hook);
    }

    public void removeHook(Runnable hook) {
	hooks.remove(hook);
    }

    public void run() {
	for (int i=0; i<hooks.size(); i++) {
	    Runnable h = (Runnable) hooks.get(i);
	    h.run();
	}
    }
}
