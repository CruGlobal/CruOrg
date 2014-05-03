package org.cru.logiclesstemplates.processors;


import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.cru.logiclesstemplates.utils.Search;

import com.adobe.granite.xss.XSSAPI;
import com.day.cq.search.result.SearchResult;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.ContentModel;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * It manage all the possible content of a search with selectors for the
 * Search Results component.
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

@Component
@Service
public class SearchContextProcessor extends AbstractResourceTypeCheckContextProcessor<ContentModel> {
    private static Map<String, Object> contentObject = new HashMap<String, Object>();

    @Override
    public String requiredResourceType() {
        return "CruOrgApp/components/section/searchresults";
    }

    @Override
    public void process(final SlingHttpServletRequest request, final ContentModel contentModel) {
        contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);

        try {
            //initialize the search class
            Search search = new Search(request);

            //get some labels.
            String pagesToSearch = (String) contentObject.get("pagesToSearch");
            String resultsPerPage = (String) contentObject.get("resultsPerPage");

            String nextText = (String) contentObject.get("nextText");
            String previousText = (String) contentObject.get("previousText");
            String noResultsText = (String) contentObject.get("noResultsText");
            String statisticsText = (String) contentObject.get("statisticsText");

            //the property pageToSearch must be configured.
            if (StringUtils.isNotBlank(pagesToSearch)) {
                search.setSearchIn(pagesToSearch);
                search.setHitsPerPage(Long.valueOf(resultsPerPage));
                SearchResult result = search.getResult();
                List<Map<String, Object>> hits = search.getHits();
                List<Map<String, Object>> resultPages = search.getResultPages();

                SlingBindings bindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());
                XSSAPI xssAPI = (bindings.getSling().getService(XSSAPI.class)).getRequestSpecificAPI(request);

                //query can be a parameter so is necessary encode it for html.
                final String escapedQuery = xssAPI.encodeForHTML(search.getQuery());

                contentObject.put("escapedQuery", escapedQuery);
                contentObject.put("resultPages", resultPages);
                contentObject.put("showPagination", resultPages.size() > 1);
                contentObject.put("previousPage", search.getPreviousPage());
                contentObject.put("nextPage", search.getNextPage());
                contentObject.put("startIndex", result.getStartIndex() + 1);
                contentObject.put("numberOfHits", result.getStartIndex() + hits.size());
                contentObject.put("totalMatches", result.getTotalMatches());
                contentObject.put("executionTime", result.getExecutionTime());
                contentObject.put("hits", hits);

                //assign labels with replaced tokens.
                contentObject.put("nextText", getFormat(nextText));
                contentObject.put("previousText", getFormat(previousText));
                contentObject.put("noResultsText", getFormat(noResultsText));
                contentObject.put("statisticsText", getFormat(statisticsText));
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
    private String getFormat(final String value) {
        return MessageFormat.format(value,
                contentObject.get("escapedQuery"),
                contentObject.get("totalMatches"),
                contentObject.get("startIndex"),
                contentObject.get("numberOfHits"),
                contentObject.get("executionTime"));
    }
}
