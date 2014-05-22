package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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

import java.util.HashMap;
import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static org.cru.logiclesstemplates.processors.dailycontent.AbstractAddDailyContentPagePathContextProcessor.*;
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
* 1.0     | 5/20/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AddTodaysPagePathContextProcessorTest {

    @InjectMocks private AddTodaysPagePathContextProcessor todaysPagePath;
    @Mock private Resource resource;
    @Mock private ResourceResolver resolver;
    @Mock private PageManager pageManager;
    @Mock private Page page;
    private final String CONTENT_TODAY_PATH = "content.today";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Ignore
    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, "html", null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel contentModel = spy(new MockTemplateContentModel(request, response));
        Map<String, Object> content = new HashMap();
        String testingPath = "/cru/test/today";
        content.put(DEFAULT_PATH, testingPath);
        contentModel.set(RESOURCE_CONTENT_KEY, content);

        /*
        * CASE 0:
        * Resource != null
        * today path != null
        */
       when(request.getResource()).thenReturn(resource);
       when(request.getResourceResolver()).thenReturn(resolver);
       when(resource.getResourceResolver()).thenReturn(resolver);
       when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
       when(resolver.map(anyString())).thenReturn(testingPath);

        todaysPagePath.process(request, contentModel);
        assertTrue(contentModel.has(CONTENT_TODAY_PATH));
        assertEquals(testingPath, contentModel.get(CONTENT_TODAY_PATH));

    }
}
