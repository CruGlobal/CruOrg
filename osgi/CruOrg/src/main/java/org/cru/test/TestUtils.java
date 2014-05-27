package org.cru.test;

import com.day.cq.tagging.impl.JcrNodeResourceIterator;
import org.apache.sling.commons.testing.jcr.MockNode;
import org.apache.sling.commons.testing.jcr.MockNodeIterator;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.ArrayList;

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


    /**
     * Constructor: prevent Instantiation
     */
    private TestUtils(){}


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
