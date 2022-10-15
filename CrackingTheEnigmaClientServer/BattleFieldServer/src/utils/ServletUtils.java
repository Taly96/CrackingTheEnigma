package utils;

import enigma.managers.BattleFieldManager;
import enigma.managers.UsersManager;
import jakarta.servlet.ServletContext;


public class ServletUtils {

    private static final String BATTLEFIELD_MANAGER_ATTRIBUTE_NAME = "battleFieldManager";

    private static final Object battleFieldManagerLock = new Object();

    private static final String USERS_MANAGER_ATTRIBUTE_NAME = "usersManager";

    private static final Object userManagerLock = new Object();

    public static final String BATTLE_FIELD = "battle";

    public static final String DATA = "data";

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
}
