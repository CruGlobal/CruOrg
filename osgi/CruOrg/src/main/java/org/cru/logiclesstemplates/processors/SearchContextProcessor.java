package org.cru.logiclesstemplates.processors;


import static com.xumak.base.Constants.GLOBAL_PROPERTIES_KEY;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.cru.util.Search;

import com.day.cq.search.result.SearchResult;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.ContentModel;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * Manages all the possible content of a search with selectors for the
 * Search Results component.
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/4/29     | oklee                  | Initial Creation
 * 1.0     | 14/5/12     | palecio                | Converted to immutable
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */

@Component
@Service
public class SearchContextProcessor extends AbstractResourceTypeCheckContextProcessor<ContentModel> {

    @Override
    public String requiredResourceType() {
        return "CruOrgApp/components/section/searchresults";
    }

    @Override
    public void process(final SlingHttpServletRequest request, final ContentModel contentModel) {
        Map<String, Object> designObject = (Map<String, Object>) contentModel.get(GLOBAL_PROPERTIES_KEY);

        try {
            //initialize the search class
            Search search = new Search(request);

            //get some labels.
            String pagesToSearch = (String) designObject.get("pagesToSearch");
            String resultsPerPage = (String) designObject.get("resultsPerPage");

            String nextText = (String) designObject.get("nextText");
            String previousText = (String) designObject.get("previousText");
            String noResultsText = (String) designObject.get("noResultsText");
            String statisticsText = (String) designObject.get("statisticsText");

            //the property pageToSearch must be configured.
            if (StringUtils.isNotBlank(pagesToSearch)) {
                search.setSearchIn(pagesToSearch);
                search.setHitsPerPage(Long.valueOf(resultsPerPage));
                SearchResult result = search.getResult();
                List<Map<String, Object>> hits = search.getHits();
                List<Map<String, Object>> resultPages = search.getResultPages();

                //query can be a parameter so is necessary encode it for html.
                final String escapedQuery = StringEscapeUtils.escapeHtml(search.getQuery());

                designObject.put("escapedQuery", escapedQuery);
                designObject.put("resultPages", resultPages);
                designObject.put("showPagination", resultPages.size() > 1);
                designObject.put("previousPage", search.getPreviousPage());
                designObject.put("nextPage", search.getNextPage());
                designObject.put("hits", hits);

                //validates we have results.
                if (result != null) {
                    designObject.put("startIndex", result.getStartIndex() + 1);
                    designObject.put("numberOfHits", result.getStartIndex() + hits.size());
                    designObject.put("totalMatches", result.getTotalMatches());
                    designObject.put("executionTime", result.getExecutionTime());
                }

                //assign labels with replaced tokens.
                designObject.put("nextText", getFormat(nextText, designObject));
                designObject.put("previousText", getFormat(previousText, designObject));
                designObject.put("noResultsText", getFormat(noResultsText, designObject));
                designObject.put("statisticsText", getFormat(statisticsText, designObject));

                designObject.put("searchResourcePath", request.getResource().getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * replace the keys {#} with the corresponding values.
     * @param value is the message with the keys to replace.
     * @return the value message with the keys replaced.
     */
    private String getFormat(final String value, final Map<String, Object> designObject) {
        return MessageFormat.format(value,
                designObject.get("escapedQuery"),
                designObject.get("totalMatches"),
                designObject.get("startIndex"),
                designObject.get("numberOfHits"),
                designObject.get("executionTime"));
    }
}
