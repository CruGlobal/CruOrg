package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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

import java.util.HashMap;
import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static org.cru.logiclesstemplates.processors.dailycontent.AddYesterdaysPagePathContextProcessor.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
public class AddYesterdaysPagePathContextProcessorTest {

    @InjectMocks private AddYesterdaysPagePathContextProcessor yesterdaysPagePath;
    @Mock private Resource resource;
    @Mock private ResourceResolver resolver;
    @Mock private PageManager pageManager;
    @Mock private Page page;
    private final String CONTENT_IS_YESTERDAY_PATH = "content.isYesterdayDefaultPath";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, "html", null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel contentModel = spy(new MockTemplateContentModel(request, response));
        Map<String, Object> content = new HashMap();
        String testingPath = "/cru/test/yesterday";
        content.put(DEFAULT_PATH, testingPath);
        contentModel.set(RESOURCE_CONTENT_KEY, content);

        /*
        * CASE 0:
        * DEFAULT PATH == IS YESTERDAY PATH
        */
        when(request.getResource()).thenReturn(resource);
        when(request.getResourceResolver()).thenReturn(resolver);
        when(resource.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(resolver.map(anyString())).thenReturn(testingPath);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(pageManager.getPage(anyString())).thenReturn(page);
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testingPath);
        when(contentModel.get(CURRENT_RESOURCE_KEY)).thenReturn(resource);

        yesterdaysPagePath.process(request, contentModel);
        assertTrue(contentModel.has(CONTENT_IS_YESTERDAY_PATH));
        assertEquals(Boolean.TRUE.toString(), contentModel.getAsString(CONTENT_IS_YESTERDAY_PATH));


         /*
        * CASE 1:
        * DEFAULT PATH != IS YESTERDAY PATH
        */
        String testingMap = "/cru/yesterday";
        content.put(DEFAULT_PATH, testingMap);
        when(resolver.map(anyString())).thenReturn(testingMap);

        yesterdaysPagePath.process(request, contentModel);
        assertTrue(contentModel.has(CONTENT_IS_YESTERDAY_PATH));
        assertEquals(Boolean.FALSE.toString(), contentModel.getAsString(CONTENT_IS_YESTERDAY_PATH));

    }


}
