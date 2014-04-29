package org.cru.logiclesstemplates.processors;


import static com.xumak.base.Constants.*;

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
import org.cru.logiclesstemplates.utils.search.Hit;
import org.cru.logiclesstemplates.utils.search.Result;

import com.adobe.granite.xss.XSSAPI;
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
        Map<String, Object> designObject =  (Map<String, Object>)contentModel.get(GLOBAL_PAGE_CONTENT_KEY);

        try {
            Search search = new Search(request);

            //get some labels.
            String pagesToSearch = (String) contentObject.get("pagesToSearch");
            String resultsPerPage = (String) contentObject.get("resultsPerPage");
            String resultTarget = (String) contentObject.get("resultTarget");

            String nextText = (String) contentObject.get("nextText");
            String previousText = (String) contentObject.get("previousText");
            String noResultsText = (String) contentObject.get("noResultsText");
            String spellcheckText = (String) contentObject.get("spellcheckText");
            String statisticsText = (String) contentObject.get("statisticsText");

            if (StringUtils.isNotBlank(pagesToSearch)) {
                search.setSearchIn(pagesToSearch);
                search.setHitsPerPage(Long.valueOf(resultsPerPage));
                Result result = search.getResult();
                List<Hit> hits = result.getHits();

                SlingBindings bindings = (SlingBindings)request.getAttribute(SlingBindings.class.getName());
                XSSAPI xssAPI = (bindings.getSling().getService(XSSAPI.class)).getRequestSpecificAPI(request);

                final String escapedQuery = xssAPI.encodeForHTML(search.getQuery());
                final String escapedQueryForAttr = xssAPI.encodeForHTMLAttr(search.getQuery());
                final String spellcheck = "<a href=\"" + designObject.get("path") +
                        "." + result.getSpellcheck() + ".html\" >" +
                        result.getSpellcheck() +
                        "</a>";

                contentObject.put("escapedQuery", escapedQuery);
                contentObject.put("escapedQueryForAttr", escapedQueryForAttr);
                contentObject.put("search", search);
                contentObject.put("result", result);

                contentObject.put("resultTarget", resultTarget);
                contentObject.put("showPagination", result.getResultPages().size() > 1);
                contentObject.put("startIndex", result.getStartIndex()+1);
                contentObject.put("numberOfHits", result.getStartIndex() + hits.size());
                contentObject.put("totalMatches", result.getTotalMatches());
                contentObject.put("executionTime", result.getExecutionTime());
                contentObject.put("spellcheck", spellcheck);
                contentObject.put("hits", hits);

                //assign labels with replaced tokens.
                contentObject.put("nextText", getFormat(nextText));
                contentObject.put("previousText", getFormat(previousText));
                contentObject.put("noResultsText", getFormat(noResultsText));
                contentObject.put("spellcheckText", getFormat(spellcheckText));
                contentObject.put("statisticsText", getFormat(statisticsText));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFormat(String value) {
        return MessageFormat.format(value,
                contentObject.get("escapedQuery"),
                contentObject.get("totalMatches"),
                contentObject.get("startIndex"),
                contentObject.get("numberOfHits"),
                contentObject.get("executionTime"),
                contentObject.get("spellcheck"));
    }
}
