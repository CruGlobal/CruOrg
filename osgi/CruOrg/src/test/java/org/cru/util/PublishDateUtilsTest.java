package org.cru.util;

import com.day.cq.tagging.impl.JcrNodeResourceIterator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.testing.jcr.MockNode;
import org.apache.sling.commons.testing.jcr.MockNodeIterator;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.Calendar;
import java.util.List;

import static com.day.cq.commons.jcr.JcrConstants.JCR_CREATED;
import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static com.xumak.base.Constants.JCR_CONTENT;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 5/12/14     | JFlores                | Initial Creation
 * ------------------------------------------------------------------
 */
public class PublishDateUtilsTest{

    private String URI = "/content/cru/article";
    private MockResourceResolver resolver = null;
    private MockResource resource = null;
    private JcrNodeResourceIterator nodeResourceIterator = null;
    @Mock private MockResourceResolver resolverToSpy;
    @Mock private ValueMap valueMap = null;

    @Before
    public void init() throws Exception{
        resolverToSpy = spy(new MockResourceResolver());
        valueMap = mock(ValueMap.class);
    }

    @Test
    public void testGetSortedPathList () throws Exception {
        List<String> pathList;
        long max = 0;

        /*
         * Case #0: No results are returned.
         * A null RangeIterator object is provided.
         * A null Resource Object is provided
         *
         */
        nodeResourceIterator = new JcrNodeResourceIterator(resolver, null);
        pathList = PublishDateUtils.getSortedPathList(nodeResourceIterator, resource, max);
        assertEquals(pathList.size(), 0);


        /*
         * Case #1: No results are returned.
         * A null RangeIterator object is provided.
         * A null Resource Object is provided.
         */
        max = 0;
        resolver = mock(MockResourceResolver.class);
        resource = spy(new MockResource(resolver, URI, ""));
        nodeResourceIterator = new JcrNodeResourceIterator(resolver, null);
        PageManager pageManager = mock(PageManager.class);

        when(resource.getResourceResolver().adaptTo(PageManager.class)).thenReturn(pageManager);
        when(pageManager.getContainingPage(resource)).thenReturn(mock(Page.class));
        when(pageManager.getContainingPage((resource)).getPath()).thenReturn("Main resource path");

        pathList = PublishDateUtils.getSortedPathList(nodeResourceIterator, resource, max);
        assertEquals(pathList.size(), 0);

        /*
         * Case #2: A list of paths is returned.
         * A RangeIterator with three elements is provided.
         *
         */
        resolverToSpy = populateResolver();
        nodeResourceIterator = mockRangeIterator(resolverToSpy);
        JcrNodeResourceIterator nodeResourceIteratorForMocking = mockRangeIterator(resolverToSpy);

        max = 10;
        Page eachPage = mock(Page.class);

        while (nodeResourceIteratorForMocking.hasNext()) {
            Resource eachResource = nodeResourceIteratorForMocking.next();
            when(pageManager.getContainingPage(eachResource)).thenReturn(eachPage);
            when(eachPage.getPath()).thenReturn("Generic path_one", "Generic path_two", "Generic path_three");
        }

        pathList = PublishDateUtils.getSortedPathList(nodeResourceIterator, resource, max);
        assertNotSame(pathList.size(), 0);
    }

    @Test
    public void testGetPublishDate () throws Exception {
        /*
         * Case #0: created date property is returned.
         * jcr:created property is provided.
         */
        resolver = mock(MockResourceResolver.class);
        resource = spy(new MockResource(resolver, URI, NT_PAGE));
        resource.addProperty(JCR_CREATED, Calendar.getInstance());
        Calendar createDate = PublishDateUtils.getPublishDate(resource);
        assertNotNull(createDate);

        /*
         * Case #1: created and published date is returned.
         * jcr:created and date property is provided.
         */
        valueMap = mock(ValueMap.class);
        resource.addProperty(JCR_CONTENT, Calendar.getInstance());
        resource.addProperty("date", Calendar.getInstance());
        Calendar publishDate = PublishDateUtils.getPublishDate(resource);
        assertNotNull(publishDate);

    }

    @Test
    public void testGetSortedList () throws Exception {
        /*
         * Case #0: no results are returned.
         * A null RangeIterator object is provided.
         *
         */
        List<Resource> resourceList = null;
        nodeResourceIterator = new JcrNodeResourceIterator(resolver, null);
        resourceList = PublishDateUtils.getSortedList(nodeResourceIterator);
        assertEquals(resourceList.size(), 0);

        /*
         * Case #1: a list of resources is returned.
         * A RangeIterator object with predefined number of elements is provided.
         *
         */
        nodeResourceIterator = mockRangeIterator(populateResolver());
        resourceList = PublishDateUtils.getSortedList(nodeResourceIterator);
        assertNotSame(resourceList.size(), 0);

    }

    /**
     * Populate a RangeIterator with predefined amount of nodes
     * @param resolver
     * @return nodeResourceIterator
     */
    private JcrNodeResourceIterator mockRangeIterator(final MockResourceResolver resolver){
        MockNode node1 = new MockNode(URI + 1);
        MockNode node2 = new MockNode(URI + 2);
        MockNode node3 = new MockNode(URI + 3);
        Node [] nodeArray = new Node[]{node1, node2, node3};
        NodeIterator nodeIterator = new MockNodeIterator(nodeArray);
        JcrNodeResourceIterator nodeResourceIterator = new JcrNodeResourceIterator(resolver, nodeIterator);

        return nodeResourceIterator;
    }

    /**
     * Add resources to a resolver
     * @return resolverToSpy with resources added to it.
     */
    private MockResourceResolver populateResolver(){

        //Create test Resources, needs to create Range Iterator.
        MockResourceResolver tempResolver = spy(new MockResourceResolver());
        MockResource resource1 = spy(new MockResource(resolver, URI + 1, ""));
        MockResource resource2 = spy(new MockResource(resolver, URI + 2, ""));
        MockResource resource3 = spy(new MockResource(resolver, URI + 3, ""));
        DateTime dateTime = new DateTime();

        //add date Property
        resource1.addProperty(JCR_CREATED, dateTime.toGregorianCalendar());
        resource2.addProperty(JCR_CREATED, dateTime.minus(3).toGregorianCalendar());
        resource3.addProperty(JCR_CREATED, dateTime.minus(1).toGregorianCalendar());

        //add to ResourceResolver
        tempResolver.addResource(resource1);
        tempResolver.addResource(resource2);
        tempResolver.addResource(resource3);

        return tempResolver;
    }
}
