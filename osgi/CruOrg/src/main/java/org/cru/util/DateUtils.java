package org.cru.util;

import org.joda.time.DateTime;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * DateUtils
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2014/05/02  | palecio                | Initial Creation
 * 1.0     | 2014/05/14  | palecio                | Joda Time refactoring
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
