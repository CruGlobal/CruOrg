package org.cru.logiclesstemplates.processors;

import com.day.cq.search.SimpleSearch;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.jcr.MockNode;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.day.cq.search.Predicate.PARAM_EXCERPT;
import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static com.day.cq.wcm.api.commands.WCMCommand.PAGE_TITLE_PARAM;
import static com.xumak.base.Constants.*;
import static org.cru.test.TestUtils.testPath;
import static org.cru.util.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchContextProcessorTest {

    @InjectMocks private SearchContextProcessor searchContextProcessor;
    @Mock private SimpleSearch simpleSearch;
    @Mock private SearchResult searchResult;
    @Mock private ResultPage resultPage1;
    @Mock private ResultPage resultPage2;
    @Mock private Hit hit1;
    @Mock private Hit hit2;
    private final String nodePath = "/node";

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest(testPath, "1", HTML, null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockResourceResolver resolver = new MockResourceResolver();
        MockResource resource = spy(new MockResource(resolver, testPath, NT_PAGE));
        request.setResource(resource);

        //Prepare Basic Data
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);
        Map<String, Object> global = new HashMap<String, Object>();
        global.put("pagesToSearch", testPath);
        global.put("resultsPerPage", "3");
        global.put("nextText", "Next");
        global.put("previousText", "Previous");
        global.put("noResultsText", "Your search - <strong>{0}</strong> - did not match any documents.");
        global.put("statisticsText", "<strong>{1} results</strong> found for {0}");
        content.set(GLOBAL_PROPERTIES_KEY, global);


        /*
        * case 0: Not Results for search.
        * Provided Query and start parameters.
        */
        when(request.getResource()).thenReturn(resource);
        when(request.getParameter("Query")).thenReturn("Query");
        when(request.getParameter("start")).thenReturn("1");
        doReturn(simpleSearch).when(resource).adaptTo(SimpleSearch.class);
        /*
        searchContextProcessor.process(request, content);
        assertTrue(global.containsKey("resultPages"));
        assertTrue(global.containsKey("escapedQuery"));
        assertTrue(global.containsKey("resultPages"));
        assertTrue(global.containsKey("showPagination"));
        assertTrue(global.containsKey("previousPage"));
        assertTrue(global.containsKey("nextPage"));
        assertTrue(global.containsKey("hits"));
        */
        /*
        * Case 1:
        *
        */


        List<Hit> hitList = new ArrayList<Hit>();
        hitList.add(hit1);
        hitList.add(hit2);

        List<ResultPage> resultPageList = new ArrayList<ResultPage>();
        resultPageList.add(resultPage1);
        resultPageList.add(resultPage2);


        MockNode node1 = spy(new MockNode(nodePath + 1, NT_PAGE));
        MockNode node2 = spy(new MockNode(nodePath + 2, NT_PAGE));


        doReturn(searchResult).when(simpleSearch).getResult();
        when(searchResult.getTotalMatches()).thenReturn(2L);
        when(searchResult.getExecutionTime()).thenReturn("2");

        doReturn(hitList).when(searchResult).getHits();
        doReturn(resultPageList).when(searchResult).getResultPages();

        when(hit1.getNode()).thenReturn(node1);
        when(hit1.getTitle()).thenReturn(PAGE_TITLE_PARAM + 1);
        when(hit1.getExcerpt()).thenReturn(PARAM_EXCERPT + 1);

        when(hit2.getNode()).thenReturn(node2);
        when(hit2.getTitle()).thenReturn(PAGE_TITLE_PARAM + 2);
        when(hit2.getExcerpt()).thenReturn(PARAM_EXCERPT + 2);

        when(resultPage1.getIndex()).thenReturn(0L);
        when(resultPage2.getIndex()).thenReturn(1L);

        when(node1.isNodeType(NT_PAGE)).thenReturn(true);
        when(node2.isNodeType(NT_PAGE)).thenReturn(true);

        when(request.getContextPath()).thenReturn(testPath);
        when(request.getRequestURI()).thenReturn(testPath + ".1.html");
        when(simpleSearch.getQuery()).thenReturn("Query");

        searchContextProcessor.process(request, content);


        ArrayList<Map <String, Object>> expectedResults = expectedResults(2);
        assertEquals(expectedResults, global.get("resultPages"));


        ArrayList<Map <String, Object>> expectedHits = expectedHits(2);
        assertEquals(expectedHits, global.get("hits"));
        assertEquals(TRUE, global.get("showPagination"));
        assertEquals(1L, global.get("startIndex"));
        assertEquals(2L, global.get("numberOfHits"));
        assertEquals(2L, global.get("totalMatches"));
        assertEquals("2", global.get("executionTime"));

    }


    /**
     * Fill Collection with simulated result to hits
     * @param elements
     * @return Collection with detail data of hits.
     */
    private ArrayList<Map <String, Object>> expectedHits(final int elements){
        ArrayList<Map<String, Object>> hits = new ArrayList<Map<String, Object>>();
        Map<String, Object> hitsMap;
        for (int item = 0; item < elements; item++){
            hitsMap = new HashMap<String, Object>();
            hitsMap.put(URL_KEY, testPath + nodePath + item + HTML_EXT);
            hitsMap.put(PAGE_TITLE_PARAM, PAGE_TITLE_PARAM + item);
            hitsMap.put(PARAM_EXCERPT, PARAM_EXCERPT + item);
            hits.add(hitsMap);
        }
        return hits;
    }


    /**
     * Fill Collection with simulated result to Search Results
     * @param elements
     * @return Collection with detail data of results.
     */
    private ArrayList<Map <String, Object>> expectedResults(final int elements){
        ArrayList<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap;
        for (int item = 1; item <= elements; item++){
            resultMap = new HashMap<String, Object>();
            resultMap.put(URL_KEY, testPath + ".0.Query" + HTML_EXT);
            resultMap.put(PAGE_NUMBER_KEY, item);
            resultMap.put(DEFAULT_CURRENT_PAGE_NAME, FALSE);
            results.add(resultMap);
        }
        return results;
    }


}
