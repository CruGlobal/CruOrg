package org.cru.logiclesstemplates.processors.list;

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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Node;

import static com.xumak.base.Constants.HTML;
import static org.cru.logiclesstemplates.processors.list.AddTotalArticlesContextProcessor.ARTICLE_LONG_FORM_RESOURCE_TYPE;
import static org.cru.logiclesstemplates.processors.list.AddTotalArticlesContextProcessor.TOTAL_SIBLINGS_PROPERTY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* AddTotalArticlesContextProcessorTest
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
public class AddTotalArticlesContextProcessorTest {

    @InjectMocks private AddTotalArticlesContextProcessor totalArticles;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);
        MockResourceResolver resolver = new MockResourceResolver();

        /*
        * CASE 0:
        * Parent Main Resource == null
        */
        MockResource resource = spy(new MockResource(resolver, "/test", "cq:Page"));
        resolver.addResource(resource);
        totalArticles.process(request, content);
        assertFalse(content.has(TOTAL_SIBLINGS_PROPERTY_NAME));

        /*
        * CASE 1:
        * Parent Main Resource != null
        * 1 paragraph with resourceType diffent to article Long Form.
        */
        MockResource item0 = spy(new MockResource(resolver, "/test/item0", "testing_Resource"));
        resolver.addResource(item0);

        when(request.getResource()).thenReturn(item0);
        doReturn(TestUtils.mockGetNode(resource)).when(resource).adaptTo(Node.class);
        totalArticles.process(request, content);
        assertEquals(0, content.get(TOTAL_SIBLINGS_PROPERTY_NAME));

        /*
        * CASE 2:
        * Parent Main Resource != null
        * 1 paragraph with resourceType diffent to article Long Form.
        * 2 paragraph with resourceType equal to article Long Form.
        */
        MockResource item1 = new MockResource(resolver, "/test/item1", ARTICLE_LONG_FORM_RESOURCE_TYPE);
        MockResource item2 = new MockResource(resolver, "/test/item2", ARTICLE_LONG_FORM_RESOURCE_TYPE);
        resolver.addResource(item1);
        resolver.addResource(item2);
        request.setResource(item1);

        totalArticles.process(request, content);
        assertEquals(2, content.get(TOTAL_SIBLINGS_PROPERTY_NAME));

    }


}
