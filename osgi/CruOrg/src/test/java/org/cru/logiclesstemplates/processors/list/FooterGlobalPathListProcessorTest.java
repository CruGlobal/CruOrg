package org.cru.logiclesstemplates.processors.list;

import mockit.Mocked;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATH_LIST_CONTEXT_PROPERTY_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* FooterGlobalPathListProcessorTest
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

public class FooterGlobalPathListProcessorTest {
    private static final Logger log = LoggerFactory.getLogger(FooterGlobalPathListProcessorTest.class);

    @Mocked
    private FooterGlobalPathListProcessor footerGlobalPathList;


    @Before
    public void init(){
        footerGlobalPathList = new FooterGlobalPathListProcessor();
    }

    @Ignore
    @Test
    public void testProcess() throws Exception {

        SlingHttpServletRequest request = Mockito.mock(SlingHttpServletRequest.class);
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);

        //prepare data
        String[] pathRefs = new String[]{"content/test/cru", "content/test/cru/article"};

        //prepare result data
        Collection<String> pathList = new ArrayList();
        pathList.addAll(Arrays.asList(pathRefs));

        /*
        * CASE 0:
        * global.PathRegs == null
        */
        footerGlobalPathList.process(request, content);
        assertFalse(content.has(PATH_LIST_CONTEXT_PROPERTY_NAME));

        /*
        * CASE 1:
        * global.PathRegs != null
        */
        content.set("global.pathRefs", pathRefs);
        footerGlobalPathList.process(request, content);
        assertEquals(pathList, content.get(PATH_LIST_CONTEXT_PROPERTY_NAME));

    }


}

