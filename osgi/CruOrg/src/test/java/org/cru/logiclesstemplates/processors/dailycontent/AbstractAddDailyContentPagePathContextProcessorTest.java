package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.testing.sling.MockResource;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static org.cru.logiclesstemplates.processors.dailycontent.AbstractAddDailyContentPagePathContextProcessor.*;
import static org.cru.logiclesstemplates.processors.dailycontent.AddContentServletPathContextProcessor.CURRENT_RESOURCE_KEY;
import static org.cru.logiclesstemplates.processors.dailycontent.AddContentServletPathContextProcessor.DEFAULT_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* 
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/19/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AbstractAddDailyContentPagePathContextProcessorTest {

    @InjectMocks private AddContentServletPathContextProcessor contentServletPath;
    @Mock private ResourceResolver resolver;
    @Mock private PageManager pageManager;
    @Mock private Page page;

    public final String testingPath = "/cru/test";
    private final String DEFAULT = "default";
    private MockResource resource;
    private String dateTimeStr;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        resource = spy(new MockResource(resolver, testingPath, NT_PAGE));
        dateTimeStr = new DateTime().toString();
    }


    @Test
    public void testGetPeriodicalPagePath() throws Exception {

        Map<String, Object> content = new HashMap();
        content.put(DEFAULT_PATH, testingPath);
        content.put(CURRENT_RESOURCE_KEY, resource);

        /* CASE 0:
        *  StartDate is Empty
        *  EndDate is Empty
        */
        when(resource.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(pageManager.getPage(anyString())).thenReturn(page);
        when(page.getPath()).thenReturn(testingPath);
        assertEquals(testingPath, contentServletPath.getPeriodicalPagePath(dateTimeStr, content));


         /* CASE 1:
        *  StartDate = Yesterday
        *  EndDate = tomorrow
        *  PageList contains three elements
        */

        Iterator<Page> iterator = getPathIterator(3);
        DateTime startDate = new DateTime().minusDays(1);
        DateTime endDate = new DateTime().plusDays(1);
        content.put(START_DATE, startDate.toString());
        content.put(END_DATE, endDate.toString());
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.listChildren()).thenReturn(iterator);
        assertEquals(testingPath, contentServletPath.getPeriodicalPagePath(dateTimeStr, content));

    }


    @Test
    public void testGetDatePagePath() throws Exception {

        /*
        * CASE 0:
        * datetime = today
        */
        DateTime dateTime = new DateTime();
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testingPath);
        when(pageManager.getPage(anyString())).thenReturn(page);
        assertEquals(testingPath, contentServletPath.getDatePagePath(dateTime, page));

    }


    @Test
    public void testGetDailyContentPath() throws Exception {
        Map<String, Object> content = new HashMap();
        content.put(CURRENT_RESOURCE_KEY, resource);

        /*
        * CASE 0:
        * content.DisplayPeriodicallyKey is Empty.
        */
        when(resource.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(pageManager.getPage(anyString())).thenReturn(page);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.getPath()).thenReturn(testingPath);
        when(page.getPageManager()).thenReturn(pageManager);
        assertEquals(testingPath, contentServletPath.getDailyContentPath(dateTimeStr, content));

        /*
        * CASE 1:
        * content.DisplayPeriodicallyKey is not Empty.
        * startDate  = yesterday date
        * EndDate = tomorrow date
        */
        Iterator<Page> iterator = getPathIterator(3);
        DateTime startDate = new DateTime().minusDays(1);
        DateTime endDate = new DateTime().plusDays(1);
        content.put(START_DATE, startDate.toString());
        content.put(END_DATE, endDate.toString());
        content.put(DISPLAY_PERIODICALLY_KEY, Boolean.TRUE.toString());
        content.put(DEFAULT_PATH, testingPath);
        when(page.listChildren()).thenReturn(iterator);
        assertEquals(testingPath, contentServletPath.getDailyContentPath(dateTimeStr, content));

    }


    @Test
    public void testGetDate() throws Exception {
        Map<String, Object> content = new HashMap();
        DateTime startDate = new DateTime().minusDays(1);
        DateTime endDate = new DateTime().plusDays(1);


        /*
        * case 0:
        * which == START_DATE
        * content.START_DATE is not exist
        */
        assertNotNull(contentServletPath.getDate(START_DATE, content));

        /*
        * case 0.5:
        * which == START_DATE
        * content.START_DATE is exist
        */
        content.put(START_DATE, startDate.toString());
        DateTime expectedDate = startDate;
        assertEquals(expectedDate, contentServletPath.getDate(START_DATE, content));


        /*
        * case 1:
        * which == END_DATE
        *  content.END_DATE is exist
        */
        content.put(END_DATE, endDate.toString());
        expectedDate = endDate.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        assertEquals(expectedDate, contentServletPath.getDate(END_DATE, content));


        /*
        * case 2:
        * which == YESTERDAY
        */
        expectedDate = contentServletPath.getDate(YESTERDAY, content);
        assertEquals(startDate.getDayOfMonth(), expectedDate.getDayOfMonth());
        assertEquals(startDate.getMonthOfYear(), expectedDate.getMonthOfYear());
        assertEquals(startDate.getYear(), expectedDate.getYear());

        /*
        * case 3:
        * which == TOMORROW
        */
        expectedDate = contentServletPath.getDate(TOMORROW, content);
        assertEquals(endDate.getDayOfMonth(), expectedDate.getDayOfMonth());
        assertEquals(endDate.getMonthOfYear(), expectedDate.getMonthOfYear());
        assertEquals(endDate.getYear(), expectedDate.getYear());

        /*
        * case 4:
        * which == DEFAULT
        */
        assertNotNull(contentServletPath.getDate(DEFAULT, content));

    }



    @Test
    public void testGetDefaultPath() throws Exception {
        Map<String, Object> content = new HashMap();
        /*
        * CASE 0: Content not container DefaultPath key
        */
        assertEquals("", contentServletPath.getDefaultPath(content));

        /*
        * CASE 1:
        * content.defaultpath != null
        * content.currentResource != null
        */
        final String defaultPath = "/test";
        content.put(DEFAULT_PATH, defaultPath);
        content.put(CURRENT_RESOURCE_KEY, resource);

        when(resource.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(pageManager.getPage(defaultPath)).thenReturn(page);
        assertEquals(defaultPath, contentServletPath.getDefaultPath(content));

    }


    /**
     * Fill iterator <Page> based in number of elements.
     * @param elements
     * @return Iterator <Pages>
     */
    private Iterator<Page> getPathIterator(final int elements){
        ArrayList<Page> pageList = new ArrayList<Page>();
        for (int item = 0; item < elements; item++){
            pageList.add(page);
        }
        return pageList.iterator();
    }


}
