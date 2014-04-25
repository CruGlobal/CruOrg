package org.cru.logiclesstemplates.processors;


import static com.xumak.base.Constants.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.xss.XSSAPI;
import com.day.cq.wcm.foundation.Search;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.ContentModel;

@Component
@Service
public class SearchProcessor extends AbstractResourceTypeCheckContextProcessor<ContentModel> {
    private static final Logger log = LoggerFactory.getLogger(
            Thread.currentThread().getStackTrace()[0].getClassName());

    @Override
    public String requiredResourceType() {
        return "CruOrgApp/components/section/searchresults";
    }

    @Override
    public void process(final SlingHttpServletRequest request, final ContentModel contentModel) {
        log.debug("SearchProcessor Process");
        Map<String, Object> contentObject =  (Map<String, Object>)contentModel.get(RESOURCE_CONTENT_KEY);
        Map<String, Object> designObject =  (Map<String, Object>)contentModel.get(GLOBAL_PAGE_CONTENT_KEY);

        try {
            Search search = new Search(request);

            String pagesToSearch = (String) contentObject.get("pagesToSearch");
            String resultsPerPage = (String) contentObject.get("resultsPerPage");
            String resultTarget = (String) contentObject.get("resultTarget");

            //Default Labels values
            String searchButtonText = getProperty(contentObject,
                    "searchButtonText",
                    "Search");
            String statisticsText = getProperty(contentObject,
                    "statisticsText",
                    "Results {0} - {1} of {2} for {3} ({4} seconds)");
            String noResultsText = getProperty(contentObject,
                    "noResultsText",
                    "Your search - {0} - did not match any documents.");
            String spellcheckText = getProperty(contentObject,
                    "spellcheckText",
                    "Did you mean: {0}");
            String resultPagesText = getProperty(contentObject,
                    "resultPagesText",
                    "Results");
            String previousText = getProperty(contentObject,
                    "previousText",
                    "Previous");
            String nextText = getProperty(contentObject,
                    "nextText",
                    "Next");

            if (StringUtils.isNotBlank(pagesToSearch)) {
                search.setSearchIn(pagesToSearch);
                search.setHitsPerPage(Long.valueOf(resultsPerPage));
                Search.Result result = search.getResult();
                List<Search.Hit> hits = result.getHits();

                SlingBindings bindings = (SlingBindings)request.getAttribute(SlingBindings.class.getName());
                XSSAPI xssAPI = (bindings.getSling().getService(XSSAPI.class)).getRequestSpecificAPI(request);

                final String escapedQuery = xssAPI.encodeForHTML(search.getQuery());
                final String escapedQueryForAttr = xssAPI.encodeForHTMLAttr(search.getQuery());

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
                contentObject.put("hits", hits);

                //replace tokens in some labels.
                statisticsText = MessageFormat.format(statisticsText,
                        contentObject.get("startIndex"),
                        contentObject.get("numberOfHits"),
                        contentObject.get("totalMatches"),
                        "<strong>" + escapedQuery + "</strong>",
                        contentObject.get("executionTime"));
                noResultsText = MessageFormat.format(noResultsText,
                        "<strong>" + escapedQuery + "</strong>");
                spellcheckText = MessageFormat.format(spellcheckText,
                        "<a href=\"" + designObject.get("path") + ".html?q=" + result.getSpellcheck() + "\" >" +
                            result.getSpellcheck() +
                        "</a>");


                //labels
                contentObject.put("searchButtonText", searchButtonText);
                contentObject.put("statisticsText", statisticsText);
                contentObject.put("noResultsText", noResultsText);
                contentObject.put("spellcheckText", spellcheckText);
                contentObject.put("resultPagesText", resultPagesText);
                contentObject.put("previousText", previousText);
                contentObject.put("nextText", nextText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(final Map<String, Object> contentObject, final String key, final String placeholder) {
        String value = (String) contentObject.get(key);
        if (StringUtils.isBlank(value)) {
            value = placeholder;
        }

        return value;
    }
}
