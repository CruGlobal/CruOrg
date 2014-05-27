package org.cru.logiclesstemplates.processors.list;

import com.day.cq.tagging.Tag;
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
import static com.xumak.base.Constants.*;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATHS_LIST_CONTEXT_KEY;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* 
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/22/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AddRelatedStoriesPathsContextProcessorTest {

    @InjectMocks
    private AddRelatedStoriesPathsContextProcessor relatedStoriesPaths;
    @Mock private TagManager tagManager;
    @Mock private PageManager pageManager;
    @Mock private Page page;
    @Mock private Page page1;
    @Mock private Page page2;
    @Mock private Page page3;
    @Mock private Tag tag1;
    @Mock private Tag tag2;
    public static final String PATHREF_KEY_NAME = "pathRef";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcess()throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));
        SlingHttpServletResponse response = mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);
        MockResourceResolver resolver = spy(new MockResourceResolver());

        //tags
        final ArrayList<String> query = new ArrayList<String>();
        query.add("testing:tag1");
        query.add("testing:tag1");
        String[] tagsArray = query.toArray(new String[query.size()]);

        Tag[] tags = new Tag[]{tag1, tag2};

        //prepare content Model
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put(PATHREF_KEY_NAME, testPath);
        content.set(GLOBAL_PROPERTIES_KEY, contentMap);

        MockResource resource = spy(new MockResource(resolver, testPath, ""));
        DateTime dateTime = new DateTime();
        request.setResourceResolver(resolver);
        request.setResource(resource);

        /*
        * CASE 0: Expected especial keys to page1 (title, path, description)
        * Range Iterator with one element.
        */

        //Create test Resource 1
        MockResource resource1 = spy(new MockResource(resolver, testPath + 1, ""));
        resource1.addProperty(JCR_CREATED, dateTime.toGregorianCalendar());  //add date Property
        resolver.addResource(resource1);   //add to ResourceResolver
        JcrNodeResourceIterator nodeResourceIterator = TestUtils.mockRangeIterator(resolver, 1);

        when(request.getResourceResolver()).thenReturn(resolver);
        doReturn(pageManager).when(resolver).adaptTo(PageManager.class);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.adaptTo(Page.class)).thenReturn(page);
        when(page.getTags()).thenReturn(tags);
        when(tag1.getTagID()).thenReturn("testing:tag1");
        when(tag2.getTagID()).thenReturn("testing:tag1");
        when(resource.getResourceResolver()).thenReturn(resolver);
        doReturn(tagManager).when(resolver).adaptTo(TagManager.class);
        when(tagManager.find(testPath, tagsArray, true)).thenReturn(nodeResourceIterator);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(pageManager.getContainingPage(resource1)).thenReturn(page1);
        when(page.getPath()).thenReturn(testPath);
        when(page1.getPath()).thenReturn(testPath + 1);
        when(page1.getTitle()).thenReturn(TITLE + 1);
        when(page1.getDescription()).thenReturn(DESCRIPTION + 1);
        when(resolver.getResource(testPath + 1)).thenReturn(resource1);
        doReturn(page1).when(resource1).adaptTo(Page.class);
        relatedStoriesPaths.process(request, content);

        assertEquals(TRUE, content.getAsString("content.showContent"));
        assertNotNull(content.get(PATHS_LIST_CONTEXT_KEY));
        assertEquals(testPath + 1, content.getAsString("content.page1path"));
        assertEquals(TITLE + 1, content.getAsString("content.page1title"));
        assertEquals(DESCRIPTION + 1, content.getAsString("content.page1description"));
        assertEquals(new ArrayList<>(), content.get(PATHS_LIST_CONTEXT_KEY));

        /*
        * CASE 1: Expected especial keys to page1 and page2 (title, path, description)
        * Range Iterator with two elements.
        */

        //Create test Resource 2.
        MockResource resource2 = spy(new MockResource(resolver, testPath + 2, ""));
        resource2.addProperty(JCR_CREATED, dateTime.minus(3).toGregorianCalendar());   //add date Property
        resolver.addResource(resource2); //add to ResourceResolver
        nodeResourceIterator = TestUtils.mockRangeIterator(resolver, 2);

        when(tagManager.find(testPath, tagsArray, true)).thenReturn(nodeResourceIterator);
        when(pageManager.getContainingPage(resource2)).thenReturn(page2);
        when(page2.getPath()).thenReturn(testPath + 2);
        when(page2.getTitle()).thenReturn(TITLE + 2);
        when(page2.getDescription()).thenReturn(DESCRIPTION + 2);
        when(resolver.getResource(testPath + 2)).thenReturn(resource2);
        doReturn(page2).when(resource2).adaptTo(Page.class);
        relatedStoriesPaths.process(request, content);

        assertEquals(TRUE, content.getAsString("content.showContent"));
        assertNotNull(content.get(PATHS_LIST_CONTEXT_KEY));
        assertEquals(testPath + 1, content.getAsString("content.page21path"));
        assertEquals(TITLE + 1, content.getAsString("content.page21title"));
        assertEquals(DESCRIPTION + 1, content.getAsString("content.page21description"));
        assertEquals(testPath + 2, content.getAsString("content.page22path"));
        assertEquals(TITLE + 2, content.getAsString("content.page22title"));
        assertEquals(DESCRIPTION + 2, content.getAsString("content.page22description"));
        assertEquals(new ArrayList<>(), content.get(PATHS_LIST_CONTEXT_KEY));

        /*
        * CASE 2: Expected PATHS_LIST_CONTEXT_KEY value with three elements
        * Range Iterator with three elements
        */

        //Create test Resource 3.
        MockResource resource3 = spy(new MockResource(resolver, testPath + 3, ""));
        resource3.addProperty(JCR_CREATED, dateTime.minus(1).toGregorianCalendar());  //add date Property
        resolver.addResource(resource3); //add to ResourceResolver
        nodeResourceIterator = TestUtils.mockRangeIterator(resolver, 3);

        when(tagManager.find(testPath, tagsArray, true)).thenReturn(nodeResourceIterator);
        when(pageManager.getContainingPage(resource2)).thenReturn(page2);
        when(pageManager.getContainingPage(resource3)).thenReturn(page3);
        when(page2.getPath()).thenReturn(testPath + 2);
        when(page3.getPath()).thenReturn(testPath + 3);
        relatedStoriesPaths.process(request, content);

        assertEquals(TRUE, content.getAsString("content.showContent"));
        assertNotNull(content.get(PATHS_LIST_CONTEXT_KEY));

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(testPath + 1);
        expectedList.add(testPath + 3);
        expectedList.add(testPath + 2);
        assertEquals(expectedList, content.get(PATHS_LIST_CONTEXT_KEY));

    }


}
