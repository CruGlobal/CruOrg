package org.cru.util;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static com.day.cq.wcm.api.NameConstants.NT_PSEUDO_PAGE;
import static com.day.cq.dam.api.DamConstants.NT_DAM_ASSET;
import static com.day.cq.wcm.api.commands.WCMCommand.PAGE_TITLE_PARAM;
import static com.day.cq.search.Predicate.PARAM_EXCERPT;
import static com.xumak.base.Constants.*;
import static org.apache.commons.lang.CharEncoding.UTF_8;

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

    public Search(final SlingHttpServletRequest request) {
        this.request = request;
        this.search = (request.getResource().adaptTo(SimpleSearch.class));

        //compatibility with selectors.
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        String[] selectors = pathInfo.getSelectors();
        this.query = request.getParameter(Constants.QUERY_PARAMETER);
        String start = request.getParameter(Constants.START_PARAMETER);
        if (selectors.length > 0) {
            this.query = selectors[selectors.length - 1];
            if (selectors.length > 1) {
                start = selectors[selectors.length - 2];
            }
        }

        if (this.query != null) {
            try {
                setQuery(new String(this.query.getBytes(UTF_8)));
            } catch (UnsupportedEncodingException e) {
                log.error("Search error setting query", e);
            }
        }
        if (start != null) {
            try {
                this.search.setStart(Long.parseLong(start));
            } catch (NumberFormatException e) {
                log.error("Search error setting start", e);
            }
        }

        //for search in specific properties.
        this.search.setSearchProperties(Constants.SEARCH_IN_PROPERTIES);

        //for get the last published pages first.
        Predicate lastModPredicate = new Predicate(Constants.LAST_PUBLISHED_KEY, Constants.DATE_RANGE_KEY);
        lastModPredicate.set(Constants.PROPERTY_KEY, JCR_CONTENT + Constants.DATE_PROPERTY);
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

    private void setQuery(final String query) {
        this.search.setQuery(query);
    }

    public void setHitsPerPage(final long num) {
        this.search.setHitsPerPage(num);
    }

    public void setSearchIn(final String searchIn) {
        this.search.setSearchIn(searchIn);
    }

    private static String encodeURL(final String url) {
        try {
            return URLEncoder.encode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @return the correct url with selectors.
     */
    private String getURL(final ResultPage page) {

        PathInfo pathInfo = new PathInfo(request.getRequestURI());
        StringBuilder url = new StringBuilder();
        url.append(pathInfo.getResourcePath());
        url.append(Constants.DOT_SEPARATOR).append(page.getStart());
        url.append(Constants.DOT_SEPARATOR).append(Search.encodeURL(search.getQuery()));
        url.append(Constants.DOT_SEPARATOR).append(pathInfo.getExtension());

        return url.toString();
    }

    /**
     * Get the number of the page for manage the pagination.
     * @param page to get the number.
     * @return the page index + 1
     */
    private long getPageNumber(final ResultPage page) {
        return page.getIndex() + 1;
    }

    /**
     * Retrieve if is the current page, the number of the page for the navigation and the
     * URL using selectors.
     * @param resultPage to get the properties.
     * @return a map with the properties of the result page.
     */
    private Map<String, Object> getResultPageProperties(final ResultPage resultPage) {
        if (resultPage == null) {
            return null;
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(Constants.URL_KEY, getURL(resultPage));
        resultMap.put(Constants.PAGE_NUMBER_KEY, getPageNumber(resultPage));
        resultMap.put(Constants.DEFAULT_CURRENT_PAGE_NAME, resultPage.isCurrentPage());

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
        return (isPage(n)) || (n.isNodeType(NT_DAM_ASSET));
    }

    /**
     * @param n is the node
     * @return true if the node n is a page.
     * @throws RepositoryException
     */
    private boolean isPage(final Node n)
            throws RepositoryException {
        return (n.isNodeType(NT_PAGE)) || (n.isNodeType(NT_PSEUDO_PAGE));
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
    private String getURL(final Hit hit)
            throws RepositoryException {
        Node n = getPageOrAsset(hit);
        String url = request.getContextPath() + n.getPath();
        if (isPage(n)) {
            url = url + HTML_EXT;
        }
        return url;
    }

    /**
     * set the URL the title and the excerpt into a map structure.
     * @param hit
     * @return the URL, title and excerpt of the hit
     * @throws RepositoryException
     */
    private Map<String, Object> getHitProperties(final Hit hit) throws RepositoryException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(Constants.URL_KEY, getURL(hit));
        resultMap.put(PAGE_TITLE_PARAM, hit.getTitle());
        resultMap.put(PARAM_EXCERPT, hit.getExcerpt());

        return resultMap;
    }

    /**
     * retrieve a list of property hits in form of map with the correct URL
     * the title of the hit and the excerpt.
     * @return a list of hit properties.
     * @throws RepositoryException
     */
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
