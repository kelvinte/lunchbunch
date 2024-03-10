package sg.okayfoods.lunchbunch.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {


    public static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static LocalDate convert(String date){

        return LocalDate.parse(date,ISO_LOCAL_DATE);
    }
}
