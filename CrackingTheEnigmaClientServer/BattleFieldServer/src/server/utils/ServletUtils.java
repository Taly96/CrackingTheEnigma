package server.utils;

import com.google.gson.Gson;
import engine.managers.AlliesManager;
import engine.managers.BattleFieldManager;
import engine.managers.UsersManager;
import jakarta.servlet.ServletContext;


public class ServletUtils {

    private static final String BATTLEFIELD_MANAGER_ATTRIBUTE_NAME = "battleFieldManager";

    private static final Object battleFieldManagerLock = new Object();

    private static final String USERS_MANAGER_ATTRIBUTE_NAME = "usersManager";

    private static final Object userManagerLock = new Object();

    private static final String ALLIES_MANAGER_ATTRIBUTE_NAME = "alliesManager";

    private static final Object alliesManagerLock = new Object();

    public final static Gson GSON_INSTANCE = new Gson();

    public static BattleFieldManager getBattleFieldManager(ServletContext servletContext){
        synchronized (battleFieldManagerLock){
            if(servletContext.getAttribute(BATTLEFIELD_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(BATTLEFIELD_MANAGER_ATTRIBUTE_NAME, new BattleFieldManager());
            }
        }
        return(BattleFieldManager)servletContext.getAttribute(BATTLEFIELD_MANAGER_ATTRIBUTE_NAME);
    }

    public static UsersManager getUsersManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USERS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USERS_MANAGER_ATTRIBUTE_NAME, new UsersManager());
            }
        }
        return (UsersManager) servletContext.getAttribute(USERS_MANAGER_ATTRIBUTE_NAME);
    }

    public static AlliesManager getAlliesManager(ServletContext servletContext) {
        synchronized (alliesManagerLock) {
            if (servletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME, new AlliesManager());
            }
        }
        return (AlliesManager) servletContext.getAttribute(ALLIES_MANAGER_ATTRIBUTE_NAME);
    }
}
