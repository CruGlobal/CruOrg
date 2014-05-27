package org.cru.logiclesstemplates.processors.list;

import com.day.cq.tagging.TagManager;
import com.day.cq.tagging.impl.JcrNodeResourceIterator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.cru.test.TestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CREATED;
import static com.xumak.base.Constants.HTML;
import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATHS_LIST_CONTEXT_KEY;
import static org.cru.logiclesstemplates.processors.list.AddQueriedPathListContextProcessor.PATHREFS_CONTENT_KEY_NAME;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddQueriedPathListContextProcessorTest {

    @InjectMocks private AddQueriedPathListContextProcessor queriedPathList;
    @Mock private TagManager tagManager;
    @Mock private PageManager pageManager;
    @Mock private Page page;
    @Mock private Page page1;
    @Mock private Page page2;
    @Mock private Page page3;
    public final String QUERY_KEY_NAME = "query";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcess()throws Exception {
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/", null, HTML, null, null);
        SlingHttpServletResponse response = mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);
        MockResourceResolver resolver = spy(new MockResourceResolver());

        //tags
        final ArrayList<String> query = new ArrayList<String>();
        query.add("testing:tag1");
        query.add("testing:tag2");
        String[] tagsArray = query.toArray(new String[query.size()]);

        //prepare content Model
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put(PATHREFS_CONTENT_KEY_NAME, testPath);
        content.set(RESOURCE_CONTENT_KEY, contentMap);

        /*
        * CASE 0:
        * CONTENT.QUERY not exist
        * CONTENT.PATHREFS != null
        *
        */
        queriedPathList.process(request, content);
        assertNotNull(content.get(PATHS_LIST_CONTEXT_KEY));


        /*
        * CASE 1:
        * CONTENT.QUERY != null
        * CONTENT.PATHREFS != null
        * Range Iterator with three elements.
        */

        contentMap.put(QUERY_KEY_NAME, query);

        //Create test Resources, needs to create Range Iterator.
        MockResource resource = spy(new MockResource(resolver, testPath, ""));
        MockResource resource1 = spy(new MockResource(resolver, testPath + 1, ""));
        MockResource resource2 = spy(new MockResource(resolver, testPath + 2, ""));
        MockResource resource3 = spy(new MockResource(resolver, testPath + 3, ""));
        DateTime dateTime = new DateTime();

        //add date Property
        resource1.addProperty(JCR_CREATED, dateTime.toGregorianCalendar());
        resource2.addProperty(JCR_CREATED, dateTime.minus(3).toGregorianCalendar());
        resource3.addProperty(JCR_CREATED, dateTime.minus(1).toGregorianCalendar());

        //add to ResourceResolver
        resolver.addResource(resource1);
        resolver.addResource(resource2);
        resolver.addResource(resource3);

        JcrNodeResourceIterator nodeResourceIterator = TestUtils.mockRangeIterator(resolver, 3);
        request.setResourceResolver(resolver);
        request.setResource(resource);

        when(resource.getResourceResolver()).thenReturn(resolver);
        doReturn(tagManager).when(resolver).adaptTo(TagManager.class);
        doReturn(pageManager).when(resolver).adaptTo(PageManager.class);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(pageManager.getContainingPage(resource1)).thenReturn(page1);
        when(pageManager.getContainingPage(resource2)).thenReturn(page2);
        when(pageManager.getContainingPage(resource3)).thenReturn(page3);
        when(page.getPath()).thenReturn(testPath);
        when(page1.getPath()).thenReturn(testPath + 1);
        when(page2.getPath()).thenReturn(testPath + 2);
        when(page3.getPath()).thenReturn(testPath + 3);
        when(tagManager.find(testPath, tagsArray, true)).thenReturn(nodeResourceIterator);
        queriedPathList.process(request, content);

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(testPath + 1);
        expectedList.add(testPath + 3);
        expectedList.add(testPath + 2);
        assertEquals(expectedList, content.get(PATHS_LIST_CONTEXT_KEY));

    }


}
