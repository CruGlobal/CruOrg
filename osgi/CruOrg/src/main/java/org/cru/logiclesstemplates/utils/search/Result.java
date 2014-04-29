package org.cru.logiclesstemplates.utils.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.RowIterator;

import org.cru.logiclesstemplates.utils.Search;

import com.day.cq.search.facets.Facet;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * It manage all the results pages for the pagination, it also contains all the
 * hits founded in the query search.
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
public final class Result
{
    private final Search search;
    private final SearchResult result;
    private final List<Hit> hits;
    private String spellSuggestion;
    private List<Page> resultPages;

    public Result(SearchResult result, Search search)
    {
        this.search = search;
        this.result = result;
        this.hits = new ArrayList();
        for (com.day.cq.search.result.Hit h : result.getHits()) {
            this.hits.add(new Hit(h, search));
        }
    }

    public List<Page> getResultPages()
            throws RepositoryException
    {
        if (this.resultPages == null)
        {
            this.resultPages = new ArrayList();
            for (ResultPage rp : this.result.getResultPages()) {
                this.resultPages.add(new Page(rp, search));
            }
        }

        return this.resultPages;
    }

    public Page getPreviousPage()
            throws RepositoryException
    {
        ResultPage previous = this.result.getPreviousPage();
        if (previous != null) {
            return new Page(previous, search);
        }
        return null;
    }

    public Page getNextPage()
            throws RepositoryException
    {
        ResultPage next = this.result.getNextPage();
        if (next != null) {
            return new Page(next, search);
        }
        return null;
    }

    public String getSpellcheck()
    {
        if (this.spellSuggestion == null) {
            try
            {
                Session session = search.getRequest().getResourceResolver().adaptTo(Session.class);
                RowIterator rows = session.getWorkspace().getQueryManager().createQuery("/jcr:root[rep:spellcheck('${query}')]/(rep:spellcheck())".replaceAll("\\$\\{query\\}", Matcher.quoteReplacement(search.getSearch().getQuery())), "xpath").execute().getRows();



                String suggestion = null;
                if (rows.hasNext())
                {
                    Value v = rows.nextRow().getValue("rep:spellcheck()");
                    if (v != null) {
                        suggestion = v.getString();
                    }
                }
                if (suggestion == null) {
                    return null;
                }
                search.getSearch().setQuery(suggestion);
                if (search.getSearch().getResult().getTotalMatches() > 0L) {
                    this.spellSuggestion = suggestion;
                } else {
                    this.spellSuggestion = "";
                }
            }
            catch (RepositoryException e)
            {
                this.spellSuggestion = "";
            }
        }
        if (this.spellSuggestion.length() == 0) {
            return null;
        }
        return this.spellSuggestion;
    }

    public long getStartIndex()
    {
        return this.result.getStartIndex();
    }

    public long getTotalMatches()
    {
        return this.result.getTotalMatches();
    }

    public String getExecutionTime()
    {
        return this.result.getExecutionTime();
    }

    public long getExecutionTimeMillis()
    {
        return this.result.getExecutionTimeMillis();
    }

    public Map<String, Facet> getFacets()
            throws RepositoryException
    {
        return this.result.getFacets();
    }

    public List<Hit> getHits()
    {
        return this.hits;
    }
}