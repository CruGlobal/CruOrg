package org.cru.logiclesstemplates.processors;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.ContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.*;

import static com.xumak.extended.contextprocessors.images.AbstractImageContextProcessor.IMAGE_PATH_CONTEXT_PROPERTY_NAME;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME;

/*
 * DESCRIPTION
 * ------------------------------------------------------------------
 * MapImagePathsContextProcessor
 *
 * Modifies the contentModel list map, removing a
 * specified part of imagePath, configured on
 * Sling Resource Resolver Mappings.
 * ------------------------------------------------------------------
 * CHANGE HISTORY
 * ------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 6/6/14      | Isaías Sosa            | Initial Creation
 * ------------------------------------------------------------------ 
 */

@Component
@Service
public class MapImagePathsContextProcessor extends AbstractResourceTypeCheckContextProcessor {

    public static final String MAIN_SLIDER = "CruOrgApp/components/section/main-slider";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(MAIN_SLIDER);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final ContentModel contentModel) throws Exception {

        if (contentModel.has(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME)) {
            Collection<Map<String, Object>> modifiedListObj = new ArrayList<Map<String, Object>>();
            Collection<Map<String, Object>> pageList =
                    (List<Map<String, Object>>) contentModel.get(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME);
            //Remove "/content/cru/us/en" from every imagePath.
            for (Map<String, Object> pageInfo : pageList) {
                Map<String, Object> modifiedPageInfo = new HashMap<String, Object>(pageInfo);
                String newImagePath = request.getResourceResolver()
                        .map((String) modifiedPageInfo.get(IMAGE_PATH_CONTEXT_PROPERTY_NAME));
                modifiedPageInfo.put(IMAGE_PATH_CONTEXT_PROPERTY_NAME, newImagePath);
                modifiedListObj.add(modifiedPageInfo);
            }
            contentModel.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, modifiedListObj);
        }
    }
}
