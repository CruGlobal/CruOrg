package org.cru.logiclesstemplates.processors.list;


import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.xumak.base.Constants.*;
import static org.cru.logiclesstemplates.processors.list.AddShowParagraphFlagContextProcessor.CURRENT_SIBLING_PROPERTY_NAME;
import static org.cru.logiclesstemplates.processors.list.AddShowParagraphFlagContextProcessor.SHOW_CONTENT_PROPERTY_NAME;
import static org.junit.Assert.assertEquals;



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

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/", null, HTML, null, null);
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);

        content.set("page.totalSiblings", 2);

        //case 0: SELECTOR = NULL
        //showParagraphFlag.process(request, content);
        //assertEquals("1", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));

        //case 1: SELECTOR =2 and CURRENT_SIBLING = NULL
        request = new MockSlingHttpServletRequest("/", "2", "html", null, null);
        showParagraphFlag.process(request, content);
        assertEquals("2", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));

        //case 2: SELECTOR=2 AND CURRENT_SIBLING = 1
        showParagraphFlag.process(request, content);
        assertEquals("3", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));
        assertEquals("show", content.getAsString(SHOW_CONTENT_PROPERTY_NAME));

        //case 3: SELECTOR=2 AND CURRENT_SIBLING=2
        content.set(GLOBAL_PAGE_CONTENT_KEY + "." + IS_EDIT_MODE, "true");
        showParagraphFlag.process(request, content);
        assertEquals("4", content.getAsString(CURRENT_SIBLING_PROPERTY_NAME));
        assertEquals("show", content.getAsString(SHOW_CONTENT_PROPERTY_NAME));
    }


}
