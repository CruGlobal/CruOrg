package org.cru.util;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.cru.test.TestUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.*;

import static com.day.cq.wcm.api.NameConstants.PN_TEMPLATE;
import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.cru.test.TestUtils.*;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2014/05/20  | JFlores                | Initial Creation
 * 1.0     | 2014/06/26  | Palecio                | added testIsDayPage, testIsYearPage, testIsMonthPage
 * and testNumberOfChildren methods
 * ------------------------------------------------------------------
 */
public class PageUtilsTest {

    private Page page = mock(Page.class);
    private PageManager pageManager = mock(PageManager.class);
    private int month = 0;

    @Test
    public void testGetPageFromDate() throws Exception {

        /*
         * Case #0: the page exists for the provided structure,
         * provided page object is not null.
         *
         */
        DateTime date = TestUtils.getDateTime("2014-05-21");
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(testPath + "/" + date.getYear())).thenReturn(page);
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(anyString())).thenReturn(page);
        Page currentStructurePage = PageUtils.getPageFromDate(page, date);
        assertNotNull(currentStructurePage);

        /*
         * Case #1: the page does not exist for the provided structure,
         * provided page object is null.
         *
         */
        Page basePage = null;
        currentStructurePage = PageUtils.getPageFromDate(basePage, date);
        assertNull(currentStructurePage);

    }

    @Test
    public void testGetDayPage() throws Exception {

        /*
         * Case #0: the page exists under the day directory,
         * provided page object is not null.
         *
         */
        int day = 1;
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(anyString())).thenReturn(page);
        Page dayPage = PageUtils.getDayPage(page, day);
        assertNotNull(dayPage);

        /*
         * Case #1: the page does not exist under the day directory,
         * a null page object is produced,
         *
         */
        day = 1;
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(anyString())).thenReturn(page);
        when(pageManager.getPage(testPath + "/" + day)).thenReturn(null);
        dayPage = PageUtils.getDayPage(page, day);
        assertNotNull(dayPage);

    }

    @Test
    public void testGetMonthPage() throws Exception {

        /*
         * Case #0: the page exists under the month directory,
         * provided page object is not null.
         *
         */
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(anyString())).thenReturn(page);
        Page monthPage = PageUtils.getMonthPage(page, month);
        assertNotNull(monthPage);


        /*
         * Case #1: the page does not exist under the month directory,
         * a null page object is produced.
         *
         */
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(anyString())).thenReturn(null);
        when(pageManager.getPage(testPath + "/0")).thenReturn(page);
        monthPage = PageUtils.getMonthPage(page, month);
        assertNotNull(monthPage);
    }

    @Test
    public void testGetYearPage() throws Exception {

        /*
         * Unique case: the page exists according to a page object provided.
         *
         */
        int year = 0;
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testPath);
        when(pageManager.getPage(anyString())).thenReturn(page);
        Page yearPage = PageUtils.getYearPage(page, year);
        assertNotNull(yearPage);
    }

    @Test
    public void testGetPage() throws Exception {

        /*
         * Case #0: a child page exists according to a provided index,
         * a list of page objects is provided
         */
        List<Page> list = new ArrayList<Page>();
        list.add((mock(Page.class)));
        list.add((mock(Page.class)));
        int index = list.size() - 1;
        Iterator iterator = list.iterator();
        when(page.listChildren()).thenReturn(iterator);
        Page childPage = PageUtils.getPage(page, index);
        assertNotNull(childPage);

        /*
         * Case #1: a child page does not exist,
         * the provided index is outside of bounds.
         * A list of page objects is provided
         */
        index = 10;
        when(page.listChildren()).thenReturn(iterator);
        childPage = PageUtils.getPage(page, index);
        assertNull(childPage);

    }

    @Test
    public void testGetTemplate() throws Exception {

        /*
         * Case #0: the template property is retrieved
         * from the provided page and replaced accordingly.
         * A map object with cq:template is provided.
         */
        Map<String, Object> valueMapValues = new HashMap<String, Object>();
        valueMapValues.put(PN_TEMPLATE, testPath);
        ValueMapDecorator map = new ValueMapDecorator(valueMapValues);
        when(page.getProperties()).thenReturn(map);
        String template = PageUtils.getTemplate(page);
        assertNotNull(template);

        /*
         * Case #1: the template property is not retrieved,
         * a null page object is provided.
         * A map object with cq:template is provided.
         */
        Page page1 = null;
        when(page.getProperties()).thenReturn(map);
        template = PageUtils.getTemplate(page1);
        assertNotSame(testPath, template);

        /*
         * Case #2: the template property is not retrieved,
         * a page object is provided.
         * A null map object is provided.
         */
        map = null;
        when(page.getProperties()).thenReturn(map);
        template = PageUtils.getTemplate(page);
        assertNotSame(testPath, template);

    }

    @Test
    public void testIsMonthPage() throws Exception {

        /**
         * Case 0: Page is null
         */
        assertFalse(PageUtils.isMonthPage(null));

        /**
         * Case 1: Page name is not a valid month because it's not a number
         */
        when(page.getName()).thenReturn("lent");
        assertFalse(PageUtils.isMonthPage(page));

        /**
         * Case 2: Page name is not a valid month because it's less than 1
         */
        when(page.getName()).thenReturn("0");
        assertFalse(PageUtils.isMonthPage(page));

        /**
         * Case 3: Page name is not a valid month because it's greater than 12
         */
        when(page.getName()).thenReturn("13");
        assertFalse(PageUtils.isMonthPage(page));

        /**
         * Case 4: Page name is a valid month
         */
        when(page.getName()).thenReturn("10");
        assertTrue(PageUtils.isMonthPage(page));

    }

    @Test
    public void testIsDayPage() throws Exception {

        /**
         * Case 0: Page is null
         */
        assertFalse(PageUtils.isDayPage(null));

        /**
         * Case 1: Page name is not a valid day because it's not a number
         */
        when(page.getName()).thenReturn("day10");
        assertFalse(PageUtils.isDayPage(page));

        /**
         * Case 2: Page name is not a valid day because it's less than 1
         */
        when(page.getName()).thenReturn("0");
        assertFalse(PageUtils.isDayPage(page));

        /**
         * Case 3: Page name is not a valid day because it's greater than 31
         */
        when(page.getName()).thenReturn("32");
        assertFalse(PageUtils.isDayPage(page));

        /**
         * Case 4: Page name is a valid day
         */
        when(page.getName()).thenReturn("10");
        assertTrue(PageUtils.isDayPage(page));
    }

    @Test
    public void testIsYearPage() throws Exception {

        /**
         * Case 0: Page is null
         */
        assertFalse(PageUtils.isYearPage(null));

        /**
         * Case 1: Page name is not a valid year because it's not a number
         */
        when(page.getName()).thenReturn("devotional-life");
        assertFalse(PageUtils.isYearPage(page));

        /**
         * Case 2: Page name is not a valid year because it's length is greater than 4 (20014 is not a valid year)
         */
        when(page.getName()).thenReturn("20014");
        assertFalse(PageUtils.isYearPage(page));

        /**
         * Case 3: Page name is a valid year
         */
        when(page.getName()).thenReturn("2014");
        assertTrue(PageUtils.isYearPage(page));
    }

    @Test
    public void testNumberOfChildren() throws Exception {

        /**
         * Case 0: Page is null
         */
        assertEquals(-1, PageUtils.numberOfChildren(null));

        /**
         * Case 1: Page has no children
         */
        List<Page> list = new ArrayList<Page>();
        Iterator iterator = list.iterator();
        when(page.listChildren()).thenReturn(iterator);
        assertEquals(0, PageUtils.numberOfChildren(page));

        /**
         * Case 1: Page has 2 children
         */
        list.add((mock(Page.class)));
        list.add((mock(Page.class)));
        iterator = list.iterator();
        when(page.listChildren()).thenReturn(iterator);
        assertEquals(2, PageUtils.numberOfChildren(page));
    }




}
