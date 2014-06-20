package org.cru.logiclesstemplates.processors.list;


import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xumak.base.Constants.HTML;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATH_PROPERTY_NAME;
import static junit.framework.Assert.assertEquals;
import static org.cru.test.TestUtils.testPath;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * CountryListPathsContextProcessorTest
 *
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 6/20/14     | Juan Flores         | Initial Creation
 * ------------------------------------------------------------------ 
 */

@RunWith(MockitoJUnitRunner.class)
public class CountryListPathsContextProcessorTest {

    @InjectMocks private CountryListPathsContextProcessor countryListPaths;
    private String completeTestPath = testPath + "/Article.html";


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
        List<Map<String, Object>> listObj = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        request.setResourceResolver(resolver);
        request.setResource(resource);

        /*
        * CASE 0:
        * list.pages.element.path
        */
        map.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, completeTestPath);
        listObj.add(map);
        content.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, listObj);
        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.map(anyString())).thenReturn(map.get(PATH_DETAILS_LIST_PATH_PROPERTY_NAME).toString().
                                                    replace(testPath, ""));
        countryListPaths.process(request, content);
        completeTestPath = completeTestPath.replace(testPath, "");
        ArrayList<HashMap<String, Object>> listPages = content.getAs(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, ArrayList.class);
        assertEquals(completeTestPath, listPages.get(0).get(PATH_DETAILS_LIST_PATH_PROPERTY_NAME));

    }


}
