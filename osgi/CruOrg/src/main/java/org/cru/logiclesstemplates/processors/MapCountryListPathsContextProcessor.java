package org.cru.logiclesstemplates.processors;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddTraversedPagePathListContextProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.MEDIUM_PRIORITY;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATH_PROPERTY_NAME;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * CountryListPathsContextProcessor
 *
 * Modifies the contentModel list map, removing a specified part of
 * countryList Paths (configured on Sling Resource Resolver Mappings).
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 6/6/14      | Juan Flores            | Initial Creation
 * ------------------------------------------------------------------ 

 */

@Component
@Service
public class MapCountryListPathsContextProcessor extends AddTraversedPagePathListContextProcessor {

    public static final String TRAVERSED_COUNTRY_LIST = "CruOrgApp/components/section/lists/traversed-country-list";
    public static final int PRIORITY = MEDIUM_PRIORITY;


    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{TRAVERSED_COUNTRY_LIST});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        List<Map<String, Object>> pageList = new ArrayList<Map<String, Object>>();
        if (contentModel.has(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME)) {
            pageList = (List<Map<String, Object>>) contentModel.get(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME);
            for (Map<String, Object> pageDetails : pageList) {
                if (pageDetails.containsKey(PATH_DETAILS_LIST_PATH_PROPERTY_NAME)) {
                    String pagePath = pageDetails.get(PATH_DETAILS_LIST_PATH_PROPERTY_NAME).toString();
                    if (StringUtils.isNotBlank(pagePath)) {
                        String newPagePath = request.getResourceResolver().map(pagePath);
                        pageDetails.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, newPagePath);
                    }
                }
            }
            contentModel.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, pageList);
        }
    }

    @Override
    public int priority() {
        return PRIORITY;
    }

}
