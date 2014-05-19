package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

import com.google.common.collect.Sets;
import com.xumak.extended.contextprocessors.lists.AddTraversedPagePathListContextProcessor;
import com.xumak.extended.util.TraversedListUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import java.util.*;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddTodaysPagePathContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/05/16    | palecio                | Initial Creation
 *
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddMainNavigationTraversedPagePathListContextProcessor
        extends AddTraversedPagePathListContextProcessor {

    public static final String MAIN_NAVIGATION_RESOURCE_TYPE = "CruOrgApp/components/section/main-navigation";


    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(MAIN_NAVIGATION_RESOURCE_TYPE);
    }

    @Override
    protected Collection<Map<String, Object>>
            extractPathList(final Page page, final SlingHttpServletRequest request, final int depth){
        return TraversedListUtils.extractPathList(page, new PageFilter(request), depth);
    }

    @Override
    protected boolean mustExist() {
        return false;
    }
}
