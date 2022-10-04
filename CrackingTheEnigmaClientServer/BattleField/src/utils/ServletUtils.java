package utils;

import enigma.engine.managers.MachineManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {

    private static final String MACHINE_MANAGER_ATTRIBUTE_NAME = "machineManager";

    public static void setMachineManager(ServletContext servletContext, MachineManager newManager){
        servletContext.removeAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME);
        servletContext.setAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME, newManager);
    }
}
