package org.cru.logiclesstemplates.processors.list;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.xumak.base.Constants.*;
import static org.cru.logiclesstemplates.processors.list.AddShowParagraphFlagContextProcessor.*;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Created with IntelliJ IDEA.
 * User: jlfp
 * Date: 5/12/14
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddShowParagraphFlagContextProcessorTest {

    @InjectMocks private AddShowParagraphFlagContextProcessor showParagraphFlag;
    @Mock private PageManager pageManager;
    @Mock private Page page;
    private String page_siblings_Key = "page.totalSiblings";

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);
        MockResourceResolver resolver = spy(new MockResourceResolver());
        MockResource resource = spy(new MockResource(resolver, testPath, ""));
        request.setResourceResolver(resolver);
        request.setResource(resource);
        content.set(page_siblings_Key, 2);

        //case 0: SELECTOR = NULL
        doReturn(pageManager).when(resolver).adaptTo(PageManager.class);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.adaptTo(Page.class)).thenReturn(page);
        when(page.getPath()).thenReturn(testPath);
        showParagraphFlag.process(request, content);
        assertEquals("1", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));

        //case 1: SELECTOR =2 and CURRENT_SIBLING = NULL
        request = new MockSlingHttpServletRequest("/", "2", HTML, null, null);
        request.setResourceResolver(resolver);
        request.setResource(resource);

        doReturn(pageManager).when(resolver).adaptTo(PageManager.class);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.adaptTo(Page.class)).thenReturn(page);
        when(page.getPath()).thenReturn(testPath);
        showParagraphFlag.process(request, content);
        assertEquals("2", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));
        assertEquals(testPath + ".1.html", content.getAsString(PREV_SIBLINGS_PROPERTY_NAME));

        //case 2: SELECTOR=2 AND CURRENT_SIBLING = 1
        showParagraphFlag.process(request, content);
        assertEquals("3", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));
        assertEquals("show", content.getAsString(SHOW_CONTENT_PROPERTY_NAME));
        assertEquals(testPath + ".1" + HTML_EXT, content.getAsString(PREV_SIBLINGS_PROPERTY_NAME));

        //case 3: SELECTOR=2 AND CURRENT_SIBLING=2
        content.set(GLOBAL_PAGE_CONTENT_KEY + "." + IS_EDIT_MODE, "true");
        showParagraphFlag.process(request, content);
        assertEquals("4", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));
        assertEquals("show", content.getAsString(SHOW_CONTENT_PROPERTY_NAME));
    }


}
