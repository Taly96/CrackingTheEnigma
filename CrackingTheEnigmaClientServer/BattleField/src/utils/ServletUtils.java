package utils;

import enigma.machine.InventoryManager;
import enigma.machine.MachineManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {

    private static final Object inventoryManagerLock = new Object();

    private static final Object machineManagerLock = new Object();

    private static final String INVENTORY_MANAGER_ATTRIBUTE_NAME = "inventoryManager";

    private static final String MACHINE_MANAGER_ATTRIBUTE_NAME = "machineManager";

    public static InventoryManager getInventoryManager(ServletContext servletContext){
        synchronized (inventoryManagerLock){
            if(servletContext.getAttribute(INVENTORY_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(INVENTORY_MANAGER_ATTRIBUTE_NAME, new InventoryManager());
            }
        }

        return (InventoryManager) servletContext.getAttribute(INVENTORY_MANAGER_ATTRIBUTE_NAME);
    }

    public static MachineManager getMachineManager(ServletContext servletContext) {
        synchronized (machineManagerLock){
            if(servletContext.getAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME) == null){
                servletContext.setAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME, new MachineManager());
            }
        }

        return (MachineManager) servletContext.getAttribute(MACHINE_MANAGER_ATTRIBUTE_NAME);
    }
}
