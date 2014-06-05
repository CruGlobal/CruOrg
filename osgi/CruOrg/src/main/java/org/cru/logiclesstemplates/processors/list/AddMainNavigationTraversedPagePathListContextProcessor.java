package org.cru.logiclesstemplates.processors.list;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.google.common.collect.Sets;
import com.xumak.extended.contextprocessors.lists.AddTraversedPagePathListContextProcessor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.*;

import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATHS_PROPERTY_NAME;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATH_PROPERTY_NAME;

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
    public static final String BREAK_NAV_KEY = "breakNav";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(MAIN_NAVIGATION_RESOURCE_TYPE);
    }



    private Collection<Map<String, Object>>
            extractNavPathList(final Page page, final Filter<Page> filter, final int depth) {
        List<Map<String, Object>> pathList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> tmpPathList = new ArrayList<Map<String, Object>>(); //help for breakNav
        Iterator<Page> children = page.listChildren(filter);
        if (depth > 0) {
            while (children.hasNext()) {
                Page child = children.next();
                Map<String, Object> currentPath = new HashMap<String, Object>();
                String path = child.getPath();
                Collection<Map<String, Object>> childPaths = extractNavPathList(child, filter, depth - 1);
                currentPath.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, path);
                currentPath.put(PATH_DETAILS_LIST_PATHS_PROPERTY_NAME, childPaths);
                //Add one level to breakNav
                if (depth == 2) {
                    Boolean isBreakNav = child.getProperties().containsKey(BREAK_NAV_KEY);
                    if (isBreakNav && !tmpPathList.isEmpty()) {
                        pathList.add(getSecondMap(page.getPath(), tmpPathList));
                        tmpPathList = new ArrayList<Map<String, Object>>();
                    }
                    tmpPathList.add(currentPath);
                } else {
                    pathList.add(currentPath);
                }

            }
            if (!tmpPathList.isEmpty()){
                pathList.add(getSecondMap(page.getPath(), tmpPathList));
            }

        }
        return pathList;
    }

    @Override
    protected Collection<Map<String, Object>>
    extractPathList(final Page page, final SlingHttpServletRequest request, final int depth){
        return extractNavPathList(page, new PageFilter(request), depth);
    }

    private Map<String, Object> getSecondMap(final String path, final Collection<Map<String, Object>> paths){
        Map<String, Object> newPath = new HashMap<String, Object>();
        newPath.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, path);
        newPath.put(PATH_DETAILS_LIST_PATHS_PROPERTY_NAME, paths);
        return  newPath;
    }


    @Override
    protected boolean mustExist() {
        return false;
    }
}
