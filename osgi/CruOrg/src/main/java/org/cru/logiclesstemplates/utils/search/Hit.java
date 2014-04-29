package org.cru.logiclesstemplates.utils.search;


import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.cru.logiclesstemplates.utils.Search;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * This is a result of a list of results in a query search, you can found
 * here the title of the page, a fragment of the text where the match world
 * founded.
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 29/4/14     | oklee                  | Selectors
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
public class Hit
{
    private final Search search;
    private final com.day.cq.search.result.Hit hit;

    public Hit(com.day.cq.search.result.Hit hit, Search search)
    {
        this.search = search;
        this.hit = hit;
    }

    public String getTitle()
            throws RepositoryException
    {
        String excerpt = (String)this.hit.getExcerpts().get("jcr:title");
        if (excerpt != null) {
            return excerpt;
        }
        return getPageOrAsset().getName();
    }

    public String getExcerpt()
            throws RepositoryException
    {
        return this.hit.getExcerpt();
    }

    public String getURL()
            throws RepositoryException
    {
        Node n = getPageOrAsset();
        String url = search.getRequest().getContextPath() + n.getPath();
        if (isPage(n)) {
            url = url + ".html";
        }
        return url;
    }

    public String getExtension()
            throws RepositoryException
    {
        String url = getURL();
        int idx = url.lastIndexOf('.');
        return idx >= 0 ? url.substring(idx + 1) : "";
    }

    public Map getProperties()
            throws RepositoryException
    {
        return this.hit.getProperties();
    }

    private boolean isPageOrAsset(Node n)
            throws RepositoryException
    {
        return (isPage(n)) || (n.isNodeType("dam:Asset"));
    }

    private boolean isPage(Node n)
            throws RepositoryException
    {
        return (n.isNodeType("cq:Page")) || (n.isNodeType("cq:PseudoPage"));
    }

    private Node getPageOrAsset()
            throws RepositoryException
    {
        Node n = this.hit.getNode();
        while ((!isPageOrAsset(n)) && (n.getName().length() > 0)) {
            n = n.getParent();
        }
        return n;
    }
}