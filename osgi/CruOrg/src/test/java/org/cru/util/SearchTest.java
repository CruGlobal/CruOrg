package org.cru.util;


import com.day.cq.commons.PathInfo;
import com.day.cq.search.SimpleSearch;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static com.xumak.base.Constants.HTML;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 5/21/14     | JFlores                | Initial Creation
 * ------------------------------------------------------------------
 */
public class SearchTest {

    private Search search;
    private String query = "query-sample";
    private String URI = "/content/page/";
    @Mock private SimpleSearch simpleSearch;
    @Mock private SearchResult searchResult;
    @Mock private PathInfo pathInfo;
    @Mock private Hit hit1;
    @Mock private Hit hit2;
    @Mock private Hit hit3;
    @Mock private ResultPage resultPage1;
    @Mock private ResultPage resultPage2;
    @Mock private ResultPage resultPage3;

    MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/a/b", "11.22", HTML, null, null));
    MockResourceResolver resolver = new MockResourceResolver();
    MockResource resource = spy(new MockResource(resolver, "/a/b", NT_PAGE));

    @Before
    public void initial(){
        MockitoAnnotations.initMocks(this);

        when(request.getResource()).thenReturn(resource);
        when(request.getParameter("Query")).thenReturn("Query");
        when(request.getParameter("start")).thenReturn("start");
        doReturn(simpleSearch).when(resource).adaptTo(SimpleSearch.class);

        search = new Search(request);
    }

    @Test
    public void testGetResult() throws Exception {

        /*
         * Case #0: No results are returned.
         * No content is provided.
         */
        when(search.getResult()).thenReturn(searchResult);
        assertEquals(searchResult.getTotalMatches(), 0L);
    }

    @Test
    public void testGetPreviousPage() throws Exception {

        /*
         * Case #0: No properties page are returned.
         * No content is provided.
         *
         */
        when(search.getResult()).thenReturn(searchResult);
        assertEquals(searchResult.getTotalMatches(), 0L);


        /*
         * Case #1: A map of properties page is returned.
         * A ResultPage object is provided.
         */
        when(searchResult.getPreviousPage()).thenReturn(resultPage1);
        when(request.getRequestURI()).thenReturn(URI);
        when(simpleSearch.getQuery()).thenReturn(query);
        Map<String, Object> pageProperty = search.getPreviousPage();
        assertNotSame(pageProperty.size(), 0);
    }

    @Test
    public void testGetNextPage() throws Exception {

        /*
         * Case #0: No properties page are returned.
         * No content is provided.
         *
         */
        when(search.getResult()).thenReturn(searchResult);
        assertEquals(searchResult.getTotalMatches(), 0L);


        /*
         * Case #1: A map of properties page is returned.
         * A ResultPage object is provided.
         */
        when(searchResult.getNextPage()).thenReturn(resultPage1);
        when(request.getRequestURI()).thenReturn(URI);
        when(simpleSearch.getQuery()).thenReturn(query);
        Map<String, Object> pageProperty = search.getNextPage();
        assertNotSame(pageProperty.size(), 0);
    }

    @Test
    public void testGetResultPages() throws Exception {

        /*
         * Case #0: returns 0 results.
         * No ResultPages are provided.
         *
         */
        when(search.getResult()).thenReturn(searchResult);
        assertEquals(searchResult.getTotalMatches(), 0L);


        /*
         * Case #1: A list of pages is returned.
         * A list of three ResultPages is provided.
         */
        List<ResultPage> resultPageList = new ArrayList<ResultPage>();
        resultPageList.add(resultPage1);
        resultPageList.add(resultPage2);
        resultPageList.add(resultPage3);

        when(searchResult.getResultPages()).thenReturn(resultPageList);
        when(request.getRequestURI()).thenReturn(URI);
        when(simpleSearch.getQuery()).thenReturn(query);

        List<Map<String, Object>> listResultPagesMap = search.getResultPages();
        assertNotSame(listResultPagesMap.size(), 0);
        assertNotNull(listResultPagesMap);
    }

    @Test
    public void testGetHits() throws Exception {

        /*
         * Case #0: No property hits are returned.
         * No content is provided.
         */
        when(search.getResult()).thenReturn(searchResult);
        assertEquals(searchResult.getHits().size(), 0);


        /*
         * Case #1: A list of property hits is returned.
         * A list of three hits is provided.
         *
         */
        search.setHitsPerPage(3);

        List<Hit> hitList = new ArrayList<Hit>();
        hitList.add(hit1);
        hitList.add(hit2);
        hitList.add(hit3);

        when(searchResult.getHits()).thenReturn(hitList);
        int i = 0;

        for (Hit hit : hitList) {
            when(hit.getNode()).thenReturn(TestUtils.getNodeWithDateProperty(URI + ++i,
                    TestUtils.getCalendarDate("2014/05/23")));
        }

        List<Map<String, Object>> listMap = search.getHits();
        assertNotSame(listMap.size(), 0);
    }
}
