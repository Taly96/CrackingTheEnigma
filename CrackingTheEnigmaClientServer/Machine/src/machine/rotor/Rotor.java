package machine.rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rotor implements Serializable {

    private class Route implements Serializable {
        private final char left;

        private final char right;

        private boolean isNotch = false;

        public Route(char inputLeft, char inputRight, boolean inputNotch){
            this.left = inputLeft;
            this.right = inputRight;
            this.isNotch = inputNotch;
        }

        public Route(Route routeToCpy){
            this.left = routeToCpy.left;
            this.right = routeToCpy.right;
            this.isNotch = routeToCpy.isNotch;
        }
    }
    private int ID = 0;

    private List<Route> routes = null;

    public Rotor(int inputID) {
        this.routes = new ArrayList<>();
        this.ID = inputID;
    }

    public Rotor(Rotor rotor) {
        this.ID = rotor.ID;
        this.routes = new ArrayList<>();

        for(Route rout : rotor.routes){
            this.routes.add(new Route(rout));
        }
    }

    public int getID() {
        return this.ID;
    }

    public Integer getNotchPosFromWindow() {
        int notchDistance = 0;

        for(notchDistance = 0; notchDistance <routes.size(); notchDistance++){
            if(routes.get(notchDistance).isNotch){
                break;
            }
        }

        return notchDistance;
    }

    public String getWindowPos(){
        return (String.format("%s",this.routes.get(0).right));
    }

    public void addRoute(char left, char right, boolean isNotch){
        this.routes.add(new Route(left, right, isNotch));
    }

    public boolean setStartingPosition(String charToSetAtWindow){
        boolean isSet = false;

        for (Route route : this.routes){
            if(route.right == charToSetAtWindow.charAt(0)){
                isSet = true;
            }
        }

        while(isSet && this.routes.get(0).right != charToSetAtWindow.charAt(0)){
            this.advance();
        }

        return isSet;
    }

    public int encrypt(int charIndex, boolean isRightToLeft){
        char to = isRightToLeft?
                routes.get(charIndex).right:
                routes.get(charIndex).left;
        char from = ' ';
        int nextIndex = -1;

        for(int i = 0; i < routes.size(); i++) {
            from = isRightToLeft?
                    routes.get(i).left:
                    routes.get(i).right;

            if(to == from) {
                nextIndex = i;
                break;
            }
        }

        return nextIndex;
    }

    public void advance() {
        Route lastRoute = routes.get(0);
        routes.add(routes.size(), new Route(lastRoute));
        routes.remove(0);
    }

    public boolean isNotchAtWindowPosition(){
        return routes.get(0).isNotch;
    }
}