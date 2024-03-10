package sg.okayfoods.lunchbunch.common.constant;

public class UrlConstants {
    private UrlConstants(){

    }
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LUNCH_PLAN="/lunch-plan";
    public static final String LUNCH_PLAN_WEBSOCKET="/lunch-plan-websock";
    public static final String SUGGESTION = LUNCH_PLAN + "/{uuid}/suggestion";
}
