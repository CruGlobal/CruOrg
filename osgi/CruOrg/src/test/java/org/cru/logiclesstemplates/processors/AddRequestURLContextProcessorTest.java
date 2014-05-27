package org.cru.logiclesstemplates.processors;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* AddRequestURLContextProcessorTest
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
public class AddRequestURLContextProcessorTest {

    @InjectMocks private AddRequestURLContextProcessor requestURL;
    private final String GLOBAL_REQUEST_URL_KEY = "page.requestURL";


    @Before
    public void init(){
        requestURL = new AddRequestURLContextProcessor();
    }


    @Test
    public void testProcess() throws Exception {
        SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);

        /*
        * CASE 0:
        * page KEY == null
        * */
        requestURL.process(request, content);
        assertFalse(content.has(GLOBAL_PAGE_CONTENT_KEY));

        /*
        * CASE 1:
        * page KEY != null
        * URL = /content/cru/test/article
        */
        StringBuffer url = new StringBuffer("/content/cru/test/article");
        Mockito.when(request.getRequestURL()).thenReturn(url);
        Map<String, String> pageMap = new HashMap<String, String>();
        content.set(GLOBAL_PAGE_CONTENT_KEY, pageMap);

        requestURL.process(request, content);
        assertEquals(url.toString(), content.get(GLOBAL_REQUEST_URL_KEY));

    }


}
