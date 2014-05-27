package org.cru.logiclesstemplates.processors.list;

import com.day.cq.tagging.TagManager;
import com.day.cq.tagging.impl.JcrNodeResourceIterator;
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

import static com.day.cq.commons.jcr.JcrConstants.JCR_CREATED;
import static com.xumak.base.Constants.HTML;
import static org.cru.test.TestUtils.testPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
* 1.0     | 5/23/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AbstractAddQueriedPathListContextProcessorTest {

    @InjectMocks private AddQueriedPathListContextProcessor queriedPathList;
    @Mock private TagManager tagManager;
    String MaxKey = "Max";


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindByTags() throws Exception {
        MockResourceResolver resolver = spy(new MockResourceResolver());

        //tags
        final ArrayList<String> query = new ArrayList<String>();
        query.add("testing:tag1");
        query.add("testing:tag2");
        String[] tagsArray = query.toArray(new String[query.size()]);

        /*
        * CASE 0: expected range Iterator null.
        * resource == null
        * */
        assertNull(queriedPathList.findByTags(null, testPath, query));

        /*
        * CASE 1: expected range Iterator null.
        * resource != null
        * PathRef is empty
        */
        MockResource resource = spy(new MockResource(resolver, testPath, ""));

        when(resource.getResourceResolver()).thenReturn(resolver);
        doReturn(tagManager).when(resolver).adaptTo(TagManager.class);
        assertNull(queriedPathList.findByTags(resource, "", query));

        /*
        * CASE 2: expected range Iterator with three elements.
        * resource is different to null
        * PathRef is different to empty
        */

        //Create test Resources, needs to create Range Iterator.
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

        when(tagManager.find(testPath, tagsArray, true)).thenReturn(nodeResourceIterator);
        assertEquals(nodeResourceIterator, queriedPathList.findByTags(resource, testPath, query));

    }


    @Test
    public void testGetMaxNumber() throws Exception {
        MockSlingHttpServletRequest request = spy(new MockSlingHttpServletRequest("/", null, HTML, null, null));
        SlingHttpServletResponse response = mock(SlingHttpServletResponse.class);
        MockTemplateContentModel content = new MockTemplateContentModel(request, response);

        /*
        * CASE 0: result expected, default value 3
        * Max Key not exist
        * list size = 4
        */
        assertEquals(3L, queriedPathList.getMaxNumber(content, 4L, MaxKey));

        /*
        * CASE 1: result expected 2.
        * Max Key = 2
        * list size = 4
        */
        content.set(MaxKey, 2L);
        assertEquals(2L, queriedPathList.getMaxNumber(content, 4L, MaxKey));

        /*
        * CASE 2: result expected 4.
        * Max Key = 0
        * list size = 4
        */
        content.set(MaxKey, 0L);
        assertEquals(4L, queriedPathList.getMaxNumber(content, 4L, MaxKey));

    }

}
