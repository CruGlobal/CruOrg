package org.cru.logiclesstemplates.processors;


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
import static junit.framework.Assert.assertEquals;
import static org.cru.test.TestUtils.testPath;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 6/10/14      | Isaías Sosa           | Initial Creation
 * ------------------------------------------------------------------ 
 */

@RunWith(MockitoJUnitRunner.class)
public class MapImagePathsContextProcessorTest {

    @InjectMocks private MapImagePathsContextProcessor mapImagePathPrc;
    private static final String IMAGE_MODIFY_PATH = "imagePath";
    private String completeImagePath = testPath + "/image.transform/Cru704x396/image.png";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequiredResourceTypes() throws Exception {

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
        * content.list.element.imagePath
        */
        map.put(IMAGE_MODIFY_PATH, completeImagePath);
        listObj.add(map);
        content.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, listObj);
        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.map(anyString())).thenReturn(map.get(IMAGE_MODIFY_PATH).toString().
                                                    replace(testPath, ""));
        mapImagePathPrc.process(request, content);
        completeImagePath = completeImagePath.replace(testPath, "");
        ArrayList<HashMap<String, Object>> listPages = content.getAs("list.pages", ArrayList.class);
        assertEquals(completeImagePath, listPages.get(0).get(IMAGE_MODIFY_PATH));

    }


}
