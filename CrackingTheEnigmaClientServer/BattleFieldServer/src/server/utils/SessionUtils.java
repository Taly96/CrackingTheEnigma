package server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    public static final String BATTLE = "battle";
    public static final String USERNAME = "username";

    public static final String ALLY_NAME = "team";
    public static final String TYPE = "type";

    public static final String DRAW = "draw";

    public static String getBattleFieldName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(BATTLE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getType(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(TYPE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getAllyName(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ALLY_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Integer getNumOfAssignmentsPerDraw(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(DRAW) : null;
        return sessionAttribute != null ? (Integer)sessionAttribute : null;
    }
}
