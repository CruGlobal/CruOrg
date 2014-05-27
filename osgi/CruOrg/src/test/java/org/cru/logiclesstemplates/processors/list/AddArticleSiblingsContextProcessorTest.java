package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.cru.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static com.xumak.base.Constants.*;
import static org.cru.logiclesstemplates.processors.list.AddArticleSiblingsContextProcessor.*;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* 
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/14/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AddArticleSiblingsContextProcessorTest {

    @InjectMocks private AddArticleSiblingsContextProcessor articleSiblings;
    @Mock PageManager pageManager;
    @Mock Page page;

    private final String testPathItem1 = testPath + "/item1";
    private final String testPathArticle1 = testPathItem1 + "/article";
    private final String testPathItem2 = testPath + "/item2";
    private final String testPathItem3 = testPath + "/item3";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));
        SlingHttpServletResponse response = mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);
        MockResourceResolver resolver = spy(new MockResourceResolver());

        /*
        * CASE 0:
        * Parent Main Resource == null
        */
        MockResource resource = spy(new MockResource(resolver, testPath, NT_PAGE));
        resolver.addResource(resource);
        request.setResource(resource);
        articleSiblings.process(request, content);
        assertFalse(content.has(TOTAL_SIBLINGS_PROPERTY_NAME));
        assertFalse(content.has(ALL_SIBLINGS_PROPERTY_NAME));

        /*
        * CASE 1:
        * Parent Main Resource != null
        * 1 paragraph with resourceType diffent to article Long Form.
        * 2 paragraph with resourceType equal to article Long Form.
        */
        MockResource item1 = spy(new MockResource(resolver, testPathItem1, ARTICLE_LONG_FORM_RESOURCE_TYPE));
        MockResource itemArticle1 = spy(new MockResource(resolver, testPathArticle1, ARTICLE_LONG_FORM_RESOURCE_TYPE));
        MockResource item2 = spy(new MockResource(resolver, testPathItem2, ARTICLE_LONG_FORM_RESOURCE_TYPE));
        MockResource item3 = spy(new MockResource(resolver, testPathItem3 , NT_PAGE));
        resolver.addResource(item1);
        resolver.addResource(item2);
        resolver.addResource(item3);
        resolver.addResource(itemArticle1);
        request.setResource(itemArticle1);

        when(request.getResourceResolver()).thenReturn(resolver);
        doReturn(pageManager).when(resolver).adaptTo(PageManager.class);
        doReturn(TestUtils.mockGetNode(resource)).when(resource).adaptTo(Node.class);
        doReturn(TestUtils.mockGetNode(item1)).when(item1).adaptTo(Node.class);
        doReturn(TestUtils.mockGetNode(item2)).when(item2).adaptTo(Node.class);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.adaptTo(Page.class)).thenReturn(page); //TODO
        when(page.getPath()).thenReturn(testPathItem1);
        articleSiblings.process(request, content);
        assertEquals(3, content.get(TOTAL_SIBLINGS_PROPERTY_NAME));
        assertEquals(mockParagraphDetailList(testPathItem1, 2), content.get(ALL_SIBLINGS_PROPERTY_NAME));

    }


    /**
     * Fill arrayList of paragraph detail list with simulated data.
     * @param currentPath
     * @param elements
     * @return arrayList paragraph detail list.
     */
    private List<Map<String, String>> mockParagraphDetailList(final String currentPath, final int elements){
        List<Map<String, String>> allParagraphDetailList = new ArrayList<Map<String, String>>();
        String path = "";
        Map <String, String > detail;
        for (int items = 1; items <= elements; items++){
            path = currentPath + "." + items + HTML_EXT;
            detail = new HashMap<String, String>();
            detail.put(PATH, path);
            allParagraphDetailList.add(detail);
        }
        return allParagraphDetailList;
    }


}
