package org.cru.logiclesstemplates.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.PathInfo;
import com.day.cq.search.Predicate;
import com.day.cq.search.SimpleSearch;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * This is the main class for search results with the selectors
 * behavior.
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 29/4/14     | oklee                  | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
public final class Search {
    private static final Logger log = LoggerFactory.getLogger(
            Thread.currentThread().getStackTrace()[0].getClassName());
    private final SlingHttpServletRequest request;
    private final SimpleSearch search;
    private String query;
    private String start;

    public Search(final SlingHttpServletRequest request) {
        this.request = request;
        this.search = (request.getResource().adaptTo(SimpleSearch.class));

        //compatibility with selectors.
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        String[] selectors = pathInfo.getSelectors();
        this.query = request.getParameter("q");
        this.start = request.getParameter("start");
        if (selectors.length > 0) {
            this.query = selectors[selectors.length - 1];
            if (selectors.length > 1) {
                this.start = selectors[selectors.length - 2];
            }
        }

        if (this.query != null) {
            try {
                setQuery(new String(this.query.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                log.error("Search error setting query", e);
            }
        }
        if (this.start != null) {
            try {
                this.search.setStart(Long.parseLong(this.start));
            } catch (NumberFormatException e) {
                log.error("Search error setting start", e);
            }
        }

        //for get the last published pages first.
        Predicate lastModPredicate = new Predicate("lastPublished", "daterange");
        lastModPredicate.set("property", "jcr:content/cq:lastPublished");
        this.search.addPredicate(lastModPredicate);
    }

    public SearchResult getResult() throws RepositoryException {
        if (StringUtils.isBlank(query)) {
            log.warn("Search.getResult() warning: query string is empty.");
            return null;
        }

        return this.search.getResult();
    }

    public String getQuery() {
        return this.query != null ? this.query : "";
    }

    public void setQuery(final String query) {
        this.search.setQuery(query);
    }

    public void setHitsPerPage(final long num) {
        this.search.setHitsPerPage(num);
    }

    public String getSearchIn() {
        return this.search.getSearchIn();
    }

    public void setSearchIn(final String searchIn) {
        this.search.setSearchIn(searchIn);
    }

    public static String encodeURL(final String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @return the correct url with selectors.
     */
    public String getURL(final ResultPage page) {

        PathInfo pathInfo = new PathInfo(request.getRequestURI());
        StringBuilder url = new StringBuilder();
        url.append(pathInfo.getResourcePath());
        url.append('.').append(page.getStart());
        url.append('.').append(Search.encodeURL(search.getQuery()));
        url.append('.').append(pathInfo.getExtension());

        return url.toString();
    }

    public long getPageNumber(final ResultPage page) {
        return page.getIndex() + 1;
    }

    public Map<String, Object> getResultPageProperties(final ResultPage resultPage) {
        if (resultPage == null) {
            return null;
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("URL", getURL(resultPage));
        resultMap.put("pageNumber", getPageNumber(resultPage));
        resultMap.put("currentPage", resultPage.isCurrentPage());

        return resultMap;
    }

    /**
     * @return the previous page properties for the pagination.
     * @throws RepositoryException
     */
    public Map<String, Object> getPreviousPage() throws RepositoryException {
        SearchResult searchResult = getResult();
        if (searchResult == null) {
            return new HashMap<String, Object>();
        }

        ResultPage resultPage = searchResult.getPreviousPage();
        return getResultPageProperties(resultPage);
    }

    /**
     * @return the next page properties for the pagination.
     * @throws RepositoryException
     */
    public Map<String, Object> getNextPage() throws RepositoryException {
        SearchResult searchResult = getResult();
        if (searchResult == null) {
            return new HashMap<String, Object>();
        }

        ResultPage resultPage = searchResult.getNextPage();
        return getResultPageProperties(resultPage);
    }

    /**
     * @return all the page results.
     * @throws RepositoryException
     */
    public List<Map<String, Object>> getResultPages() throws RepositoryException {
        SearchResult searchResult = getResult();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (searchResult == null) {
            return result;
        }

        List<ResultPage> resultPages = searchResult.getResultPages();
        for (ResultPage page : resultPages) {
            result.add(getResultPageProperties(page));
        }
        return result;
    }

    /**
     * @param n is a node
     * @return determine if the node n is a page or asset.
     * @throws RepositoryException
     */
    private boolean isPageOrAsset(final Node n)
            throws RepositoryException {
        return (isPage(n)) || (n.isNodeType("dam:Asset"));
    }

    /**
     * @param n is the node
     * @return true if the node n is a page.
     * @throws RepositoryException
     */
    private boolean isPage(final Node n)
            throws RepositoryException {
        return (n.isNodeType("cq:Page")) || (n.isNodeType("cq:PseudoPage"));
    }

    private Node getPageOrAsset(final Hit hit)
            throws RepositoryException {
        Node n = hit.getNode();
        while ((!isPageOrAsset(n)) && (n.getName().length() > 0)) {
            n = n.getParent();
        }
        return n;
    }

    /**
     * @param hit of the search
     * @return the url of the hit.
     * @throws RepositoryException
     */
    public String getURL(final Hit hit)
            throws RepositoryException {
        Node n = getPageOrAsset(hit);
        String url = request.getContextPath() + n.getPath();
        if (isPage(n)) {
            url = url + ".html";
        }
        return url;
    }

    public Map<String, Object> getHitProperties(final Hit hit) throws RepositoryException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("URL", getURL(hit));
        resultMap.put("title", hit.getTitle());
        resultMap.put("excerpt", hit.getExcerpt());

        return resultMap;
    }

    public List<Map<String, Object>> getHits() throws RepositoryException {
        SearchResult searchResult = getResult();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (searchResult == null) {
            return result;
        }
        List<Hit> resultHits = searchResult.getHits();
        for (Hit hit : resultHits) {
            result.add(getHitProperties(hit));
        }
        return result;
    }
}
