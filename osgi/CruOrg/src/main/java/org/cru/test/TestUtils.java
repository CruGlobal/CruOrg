package org.cru.test;

import com.day.cq.tagging.impl.JcrNodeResourceIterator;
import org.apache.sling.commons.testing.jcr.MockNode;
import org.apache.sling.commons.testing.jcr.MockNodeIterator;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.joda.time.DateTime;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* 
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/26/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
public class TestUtils {

    public static String testPath = "/content/cru/test";
    public static String QUERY_KEY_NAME = "query";


    /**
     * Constructor: prevent Instantiation
     */
    private TestUtils(){}


    /**
     * getDateTime based in String date.
     * @param dateStr
     * @return DateTime object.
     */
    public static DateTime getDateTime(final String dateStr){
        DateTime calendarDate = new DateTime(dateStr);
        return calendarDate;
    }


    /**
     * getCalendarDate based in String date.
     * @param dateStr
     * @return Calendar object.
     */
    public static Calendar getCalendarDate(final String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendarDate = Calendar.getInstance();
        Date date = null;

        try {
            date = sdf.parse(dateStr);
            calendarDate.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendarDate;
    }



    /**
     * get Mock Node with date property added.
     * @param path
     * @param date Calendar
     * @return MockNode with specific date
     */
    public static MockNode getNodeWithDateProperty(final String path, final Calendar date){
        MockNode jcrNode = new MockNode(path + "/jcr:content", "cq:PageContent");
        try {

            jcrNode.setProperty("date", date);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return jcrNode;

    }


    /**
     * Created Partial Mock RangeIterator based in number of elements
     * @param resolver ResourceResolver
     * @param elements number of elements
     * @return Mock Range Iterator with specified number of elements.
     */
    public static JcrNodeResourceIterator mockRangeIterator(final MockResourceResolver resolver, final int elements){

        ArrayList<Node> nodes = new ArrayList<Node>();
        MockNode node;
        for (int item = 1; item <= elements; item++){
            node = new MockNode(testPath + item);
            nodes.add(node);
        }
        Node[] nodeArray = nodes.toArray(new Node[elements]);
        NodeIterator nodeIterator = new MockNodeIterator(nodeArray);
        JcrNodeResourceIterator nodeResourceIterator = new JcrNodeResourceIterator(resolver, nodeIterator);

        return nodeResourceIterator;
    }


    /**
     * get Resource Mock object, based in resource.
     * @param resource
     * @return MockNode
     */
    public static MockNode mockGetNode(final MockResource resource){
        MockNode node = new MockNode(resource.getPath(), resource.getResourceType());
        return node;
    }


}
