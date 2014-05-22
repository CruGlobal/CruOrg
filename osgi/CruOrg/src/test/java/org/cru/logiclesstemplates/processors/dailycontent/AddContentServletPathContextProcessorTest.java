package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.commons.testing.sling.MockResource;
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

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static com.day.cq.wcm.api.NameConstants.PN_TEMPLATE;
import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static org.cru.logiclesstemplates.processors.dailycontent.AddContentServletPathContextProcessor.SERVLET_PATH_KEY;
import static org.cru.util.PageUtils.ARTICLE_TEMPLATE_PATH;
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
public class AddContentServletPathContextProcessorTest {

    @InjectMocks private AddContentServletPathContextProcessor contentServletPath;
    @Mock private ResourceResolver resolver;
    @Mock private PageManager pageManager;
    @Mock private Page page;

    public final String testingPath = "/cru/test";
    public final String resourceJsonPath = "/cru/test.contentmodel.page.json";
    public final String CONTENT_SERVLET_PATH_KEY = RESOURCE_CONTENT_KEY + "." + SERVLET_PATH_KEY;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, "html", null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);

        MockResource resource = spy(new MockResource(resolver, testingPath, NT_PAGE));
        MockTemplateContentModel contentModel = spy(new MockTemplateContentModel(request, response));
        request.setResource(resource);
        Map<String, Object> content = new HashMap();
        contentModel.set(RESOURCE_CONTENT_KEY, content);

        /*
        * CASE 0:
        * Resource != null
        */
        Map<String, Object> valueMapValues = new HashMap<String, Object>();
        valueMapValues.put(PN_TEMPLATE, ARTICLE_TEMPLATE_PATH);
        ValueMapDecorator map = new ValueMapDecorator(valueMapValues);
        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        when(resolver.map(anyString())).thenReturn(resourceJsonPath);
        when(resource.getResourceResolver()).thenReturn(resolver);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(pageManager.getPage(anyString())).thenReturn(page);
        when(page.getPageManager()).thenReturn(pageManager);
        when(page.getPath()).thenReturn(testingPath);
        when(page.getContentResource()).thenReturn(resource);
        when(page.getProperties()).thenReturn(map);
        contentServletPath.process(request, contentModel);

        assertTrue(contentModel.has(CONTENT_SERVLET_PATH_KEY));
        assertEquals(resourceJsonPath, contentModel.get(CONTENT_SERVLET_PATH_KEY));

    }


}
