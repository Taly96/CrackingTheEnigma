package utils;

import enigma.managers.MachineManager;
import enigma.managers.UsersManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {

    private static final String MACHINE_MANAGER_ATTRIBUTE_NAME = "machineManager";

    private static final Object machineManagerLock = new Object();

    private static final String USERS_MANAGER_ATTRIBUTE_NAME = "usersManager";

    private static final Object userManagerLock = new Object();

    public static void setMachineManager(ServletContext servletContext, MachineManager newManager){
        servletContext.removeAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME);
        servletContext.setAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME, newManager);
    }

    public static UsersManager getUsersManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USERS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USERS_MANAGER_ATTRIBUTE_NAME, new UsersManager());
            }
        }
        return (UsersManager) servletContext.getAttribute(USERS_MANAGER_ATTRIBUTE_NAME);
    }

    public static MachineManager getMachineManager(ServletContext servletContext){

        return (MachineManager) servletContext.getAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME);
    }
}
