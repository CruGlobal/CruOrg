package org.cru.logiclesstemplates.processors.list;

import mockit.Mocked;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xumak.extended.contextprocessors.lists.ListConstants.PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME;
import static org.cru.logiclesstemplates.processors.list.AddQueriedFilterContextProcessor.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* AddQueriedFilterContextProcessorTest
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/12/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
public class AddQueriedFilterContextProcessorTest {

    @Mocked
    private AddQueriedFilterContextProcessor queriedFilter;


    @Before
    public void init(){
        queriedFilter  = new AddQueriedFilterContextProcessor();
    }


    @Test
    public void testProcess() throws Exception {

        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/", null, "html", null, null);
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);


        /*
        * CASE 0:
        * list.pages == null
        */
        queriedFilter.process(request, content);
        assertFalse(content.has(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME));

        /*
        * CASE 1:
        * list.pages != null
        * title Filter = null
        * description Filter = null
        * image Filter = NONE
        */
        List<Map<String, String>> completePageDetails = simulatePagesList(3, true, true, 0);
        List<Map<String, String>> partialPageDetails = simulatePagesList(3, false, false, 2);
        content.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, completePageDetails);
        content.set(IMAGE_CONTENT_KEY_NAME, IMAGE_VALUE_NONE);
        queriedFilter.process(request, content);
        assertEquals(partialPageDetails, content.get(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME));

        /*
        * CASE 2:
        * list.pages != null
        * title Filter = true
        * description Filter = null
        * image Filter = FIRST
        */
        completePageDetails = simulatePagesList(3, true, true, 0);
        partialPageDetails = simulatePagesList(3, true, false, 1);
        content.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, completePageDetails);
        content.set(TITLE_CONTENT_KEY_NAME, true);
        content.set(IMAGE_CONTENT_KEY_NAME, IMAGE_VALUE_FIRST);
        queriedFilter.process(request, content);
        assertEquals(partialPageDetails, content.get(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME));

        /*
        * CASE 3:
        * list.pages != null
        * title Filter = true
        * description Filter = true
        * image Filter = ALL
        */
        completePageDetails = simulatePagesList(3, true, true, 0);
        content.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, completePageDetails);
        content.set(TITLE_CONTENT_KEY_NAME, true);
        content.set(DESCRIPTION_CONTENT_KEY_NAME, true);
        content.set(IMAGE_CONTENT_KEY_NAME, "ALL");
        queriedFilter.process(request, content);
        assertEquals(completePageDetails, content.get(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME));

    }


    /**
     * Fill list of page with simulated data.
     * @param elements
     * @param titleFilter
     * @param descriptionFilter
     * @param imageFilter
     * @return list with specific page data.
     */
    private List<Map<String, String>> simulatePagesList(final int elements, final boolean titleFilter,
                                                     final boolean descriptionFilter,
                                                     final int imageFilter)throws Exception{

        List<Map<String, String>> allPageDetailList = new ArrayList<Map<String, String>>();
        //fill List with simulated data
        for (int item = 0; item < elements; item++){
            allPageDetailList.add((Map) simulatePageDetails(item, titleFilter, descriptionFilter, imageFilter));
        }
        return allPageDetailList;
    }


    /**
     * Fill map with simulated data of page.
     * @param item
     * @param titleFilter
     * @param descriptionFilter
     * @param imageFilter
     * @return list with specific page data.
     */
    private Map<String, String> simulatePageDetails(final int item,  final boolean titleFilter,
                                               final boolean descriptionFilter, final int imageFilter)throws Exception {

        Map<String, String> pageDetails = new HashMap<String, String>();

        if (titleFilter) {
            pageDetails.put(TITLE_REMOVE_KEY_NAME, TITLE_REMOVE_KEY_NAME + item);
        }

        if (descriptionFilter) {
            pageDetails.put(DESCRIPTION_REMOVE_KEY_NAME, DESCRIPTION_REMOVE_KEY_NAME + item);
        }

        if ((imageFilter == 0) || ((imageFilter == 1) && (item == 0))) {
            pageDetails.put(IMAGE_REMOVE_KEY_NAME, IMAGE_REMOVE_KEY_NAME + item);
        }

        return pageDetails;
    }


}
