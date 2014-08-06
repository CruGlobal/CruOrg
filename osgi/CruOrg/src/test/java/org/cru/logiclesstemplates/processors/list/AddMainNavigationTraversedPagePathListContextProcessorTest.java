package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static com.xumak.base.Constants.HTML;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATHS_PROPERTY_NAME;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATH_PROPERTY_NAME;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
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
* 1.0     | 5/22/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AddMainNavigationTraversedPagePathListContextProcessorTest {

    @InjectMocks private AddMainNavigationTraversedPagePathListContextProcessor mainNavigationTraversedPagePathList;
    @Mock Page page;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExtractPathList() throws Exception {

        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));

        /*
        * CASE 0:
        * dept = 2
        * pageIterator is empty.
        */
        Iterator<Page> pageIterator = simulatePageList(0);
        when(page.listChildren(any(PageFilter.class))).thenReturn(pageIterator);
        assertNotNull(mainNavigationTraversedPagePathList.extractNavPathList(page, null, 2, 2));


        /*
        * CASE 1 :
        * depth = 2
        * pageIterator contains two elements.
        */

        HashMap<String, Object> property = new HashMap<String, Object>();
        ValueMapDecorator valueMap = new ValueMapDecorator(property);
        pageIterator = simulatePageList(2);
        when(page.listChildren(any(PageFilter.class))).thenReturn(pageIterator);
        when(page.listChildren(null)).thenReturn(pageIterator);
        when(page.getPath()).thenReturn(testPath);
        when(page.getProperties()).thenReturn(valueMap);

        //prepared data o expecting result
        Iterator<Page> expectingPageIterator = simulatePageList(3);
        Collection<Map<String, Object>> expectingResult = simulatedNavigationList(expectingPageIterator, 3);

        assertEquals(expectingResult,
            mainNavigationTraversedPagePathList.extractNavPathList(page, null, 2, 2));

    }


    /**
     * Fill Iterator of pages with simulated data.
     * @param elements
     * @return Iterator with pages.
     */
    private Iterator<Page> simulatePageList(final int elements)throws Exception{

        ArrayList<Page>  pages = new ArrayList<Page>();
        //fill array with simulated data
        for (int item = 0; item < elements; item++){
            pages.add(page);
        }
        return pages.iterator();
    }


    /**
     * Fill Collection with simulated result to extractPathListMethod
     * @param children
     * @param depth
     * @return Collection with detail data of pages.
     */
    private  Collection<Map<String, Object>> simulatedNavigationList(final Iterator<Page> children, final int depth){
        Collection<Map<String, Object>> pathList = new ArrayList<Map<String, Object>>();
        while (children.hasNext()) {
            children.next();
            Collection<Map<String, Object>> childPaths = simulatedNavigationList(children, depth - 1);
            Map<String, Object> currentPath = new HashMap<>();
            currentPath.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, testPath);
            currentPath.put(PATH_DETAILS_LIST_PATHS_PROPERTY_NAME, childPaths);
            pathList.add(currentPath);
        }
        return pathList;
    }


}
