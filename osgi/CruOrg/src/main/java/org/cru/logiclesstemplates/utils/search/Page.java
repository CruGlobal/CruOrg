package org.cru.logiclesstemplates.utils.search;

import org.cru.logiclesstemplates.utils.Search;

import com.day.cq.commons.PathInfo;
import com.day.cq.search.result.ResultPage;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * It's a specific page for the search results and the pagination, it is
 * necessary for get all the URL with selectors and the correct page numbers
 * if the pagination.
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
public final class Page
{
    private final ResultPage rp;
    private final Search search;

    public Page(ResultPage rp, Search search)
    {
        this.rp = rp;
        this.search = search;
    }

    public boolean isCurrentPage()
    {
        return this.rp.isCurrentPage();
    }

    public long getIndex()
    {
        return this.rp.getIndex();
    }

    public long getPageNumber() {
        return getIndex()+1;
    }

    /**
     *
     * @return the correct url with selectors.
     */
    public String getURL()
    {

        PathInfo pathInfo = new PathInfo(search.getRequest().getRequestURI());
        StringBuilder url = new StringBuilder();
        url.append(pathInfo.getResourcePath());
        url.append('.').append(this.rp.getStart());
        url.append('.').append(Search.encodeURL(search.getSearch().getQuery()));
        url.append('.').append(pathInfo.getExtension());

        return url.toString();
    }
}