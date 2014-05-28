package org.cru.util;

/*
 * DESCRIPTION
 *
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 5/8/14      | JFlores                | Initial Creation
 * ------------------------------------------------------------------
 */

import org.cru.test.TestUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    @Test
    public void dateUtilsBehavior() throws Exception{
        DateTime date = null;
        DateTime minDate = null;
        DateTime maxDate = null;

        /*
         * Case #0: returns true.
         * Two corresponding date objects are provided.
         * A date between the above two dates is provided
         *
         */

        date = TestUtils.getDateTime("2014-05-01T06:01");
        minDate = TestUtils.getDateTime("2014-04-30T06:01");
        maxDate = TestUtils.getDateTime("2014-05-05T06:01");

        assertTrue(DateUtils.isDateBetween(date, minDate, maxDate));

        /*
         * Case #1: returns false.
         * Two corresponding date objects are provided.
         * A date outside the range of the above two dates is provided
         *
         */
        date = TestUtils.getDateTime("2014-05-06T06:01");
        minDate = TestUtils.getDateTime("2014-04-30T06:01");
        maxDate = TestUtils.getDateTime("2014-05-05T06:01");

        assertFalse(DateUtils.isDateBetween(date, minDate, maxDate));
    }

}
