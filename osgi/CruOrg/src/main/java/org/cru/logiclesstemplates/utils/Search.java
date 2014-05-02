package org.cru.logiclesstemplates.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.cru.logiclesstemplates.utils.search.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.Predicate;
import com.day.cq.search.SimpleSearch;
import com.day.cq.search.Trends;

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
    private Result result;
    private String query;
    private boolean tagPredicateSet;
    private String start;

    public Search(final SlingHttpServletRequest request) {
        String language = "language";
        String property = "property";
        this.request = request;
        this.search = (request.getResource().adaptTo(SimpleSearch.class));

        String charset = "ISO-8859-1";
        if (request.getParameter("_charset_") != null) {
            charset = request.getParameter("_charset_");
        }
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
                setQuery(new String(this.query.getBytes(charset), "UTF-8"));
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
        Predicate languagePredicate = new Predicate("languages", language);
        languagePredicate.set(language, request.getParameter(language));
        this.search.addPredicate(languagePredicate);

        Predicate tagPredicate = new Predicate("tags", "tagid");
        tagPredicate.set(property, "jcr:content/cq:tags");
        tagPredicate.set("tagid", request.getParameter("tag"));
        this.search.addPredicate(tagPredicate);
        this.tagPredicateSet = (tagPredicate.get("tagid") != null);

        Predicate mimeTypePredicate = new Predicate("mimeTypes", property);
        mimeTypePredicate.set(property, "jcr:content/jcr:mimeType");
        mimeTypePredicate.set("value", request.getParameter("mimeType"));
        this.search.addPredicate(mimeTypePredicate);

        Predicate lastModPredicate = new Predicate("lastModified", "daterange");
        lastModPredicate.set(property, "jcr:content/cq:lastModified");

        lastModPredicate.set("lowerBound", request.getParameter("from"));
        lastModPredicate.set("upperBound", request.getParameter("to"));
        this.search.addPredicate(lastModPredicate);

        Predicate orderByScore = new Predicate("orderByScore", "orderby");
        orderByScore.set("orderby", "@jcr:score");
        orderByScore.set("sort", "desc");
        this.search.addPredicate(orderByScore);
    }

    public Trends getTrends() {
        return this.search.getTrends();
    }

    public Result getResult()
            throws RepositoryException {
        if ((this.result == null) &&
                ((this.search.getQuery().length() > 0) || (this.tagPredicateSet)) &&
                (this.search.getResult() != null)) {
            this.result = new Result(this.search.getResult(), this);
        }
        return this.result;
    }

    public List<String> getRelatedQueries()
            throws RepositoryException {
        return this.search.getRelatedQueries();
    }

    public String getQuery() {
        return this.query != null ? this.query : "";
    }

    public void setQuery(final String query) {
        this.search.setQuery(query);
    }

    public long getHitsPerPage() {
        return this.search.getHitsPerPage();
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

    public String getSearchProperties() {
        return this.search.getSearchProperties();
    }

    public void setSearchProperties(final String properties) {
        this.search.setSearchProperties(properties);
    }

    public void setStart(final String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public static String encodeURL(final String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public SlingHttpServletRequest getRequest() {
        return this.request;
    }

    public SimpleSearch getSearch() {
        return this.search;
    }
}
