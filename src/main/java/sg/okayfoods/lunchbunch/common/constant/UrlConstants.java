package sg.okayfoods.lunchbunch.common.constant;

public class UrlConstants {
    private UrlConstants(){

    }
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String SESSION="/session";
    public static final String SUGGESTIONS=SESSION+"/{id}/suggestion";
}
