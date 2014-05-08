package org.cru.util;

import java.util.Calendar;
import static java.util.Calendar.*;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * DateUtils
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2/5/14      | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
public class DateUtils {

    private DateUtils(){} //prevent instantiation

    /**
     *
     * @param date the date to be compared
     * @param minDate the lower bound
     * @param maxDate the upper bound
     * @return true if date is between minDate and maxDate.
     */
    public static boolean isDateBetween(final Calendar date, final Calendar minDate, final Calendar maxDate){
        return date.after(minDate) && date.before(maxDate);
    }

    /**
     * Calculates the difference in days between {@code date1} and {@code date2}, doesn't matter what date
     * comes before.
     * @param date1 the first date
     * @param date2 the second date
     * @return the difference in days between the dates
     */
    public static int daysBetween(final Calendar date1, final Calendar date2){
        //TODO implement
        return 0;
    }

}
