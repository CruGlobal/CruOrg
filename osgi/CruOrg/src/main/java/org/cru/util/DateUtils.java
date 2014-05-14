package org.cru.util;

import org.joda.time.DateTime;

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
    public static boolean isDateBetween(final DateTime date, final DateTime minDate, final DateTime maxDate){
        return date.isAfter(minDate) && date.isBefore(maxDate);
    }

}
