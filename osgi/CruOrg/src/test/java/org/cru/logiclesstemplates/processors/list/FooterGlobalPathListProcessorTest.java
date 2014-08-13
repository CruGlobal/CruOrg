package org.cru.logiclesstemplates.processors.list;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;

import static com.xumak.base.Constants.HTML;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATH_LIST_CONTEXT_PROPERTY_NAME;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.spy;

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
@RunWith(MockitoJUnitRunner.class)
public class FooterGlobalPathListProcessorTest {

    @InjectMocks private FooterGlobalPathListProcessor footerGlobalPathList;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);

        //prepare data
        Collection<String> pathRefs = new ArrayList();
        pathRefs.add(testPath);
        pathRefs.add(testPath + "/article");

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
        assertEquals(pathRefs, content.get(PATH_LIST_CONTEXT_PROPERTY_NAME));

    }


}

