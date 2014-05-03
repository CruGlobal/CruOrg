package org.cru.util;

import java.util.Calendar;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * DateUtils
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2/5/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
public class DateUtils {

    private DateUtils(){}

    /**
     *
     * @param date the date to be compared
     * @param minDate the lower bound
     * @param maxDate the upper bound
     * @return true if date is between minDate and maxDate.
     */
    public static boolean isDateBetween(Calendar date, Calendar minDate, Calendar maxDate){
        return date.after(minDate) && date.before(maxDate);
    }

}
