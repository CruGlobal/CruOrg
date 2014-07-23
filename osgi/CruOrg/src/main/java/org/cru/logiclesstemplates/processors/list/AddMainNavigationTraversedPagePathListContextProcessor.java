package org.cru.logiclesstemplates.processors.list;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddTraversedPagePathListContextProcessor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.*;

import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATHS_PROPERTY_NAME;
import static com.xumak.extended.contextprocessors.lists.ListConstants.PATH_DETAILS_LIST_PATH_PROPERTY_NAME;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddMainNavigationTraversedPagePathListContextProcessor
 * -----------------------------------------------------------------------------
 * Recursively finds all the children of all the pages starting from the base page up until a defined
 * depth and stores all the paths in the content model under list.paths
 * The main difference from the default TraversedPagePathListContextProcessor is the 'breakNav' property,
 * If this property is true in a page, another level of paths will be created under said page.
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/05/16    | jflores                | Initial Creation
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

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        Resource resource = request.getResource();

        if (resource != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            String pathRefListContentKeyName = getPathRefListKeyName(resource);
            if (contentModel.has(pathRefListContentKeyName)) {
                String pathRef = contentModel.getAsString(pathRefListContentKeyName);

                Collection<Map<String, Object>> pathList = new ArrayList<>();
                if (pathRef != null) {
                    Page page = pageManager.getContainingPage(pathRef);
                    int depth = LIST_DEFAULT_DEPTH;
                    String depthListContentKeyName = getDepthKeyName(resource);
                    if (contentModel.has(depthListContentKeyName)) {
                        depth =  Integer.parseInt(contentModel.getAsString(depthListContentKeyName));
                    }
                    if (null != page) {
                        pathList = extractNavPathList(page, new PageFilter(request), depth, depth);
                    }
                }
                contentModel.set(PATH_LIST_CONTEXT_PROPERTY_NAME, pathList);
            }
        }
    }

    /**
     *
     * @param page the page used as base
     * @param filter used to filter out pages, can be null
     * @param depth the number of levels left to check (will decrease each time the method is called)
     * @param absoluteDepth the absolute number of levels to check
     * @return the list of paths under the base page
     */
    public Collection<Map<String, Object>>
            extractNavPathList(final Page page, final Filter<Page> filter, final int depth, final int absoluteDepth) {
        List<Map<String, Object>> pathList = new ArrayList<>();
        List<Map<String, Object>> tmpPathList = new ArrayList<>(); //help for breakNav
        Iterator<Page> children = page.listChildren(filter);
        if (depth > 0) {
            while (children.hasNext()) {
                Page child = children.next();
                Map<String, Object> currentPath = new HashMap<>();
                String path = child.getPath();
                Collection<Map<String, Object>> childPaths =
                        extractNavPathList(child, filter, depth - 1, absoluteDepth);
                currentPath.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, path);
                currentPath.put(PATH_DETAILS_LIST_PATHS_PROPERTY_NAME, childPaths);
                //Add one level to breakNav
                if (((absoluteDepth == 3 && depth == 2) || (absoluteDepth == 2 && depth == 1))) {
                    Boolean isBreakNav = child.getProperties().containsKey(BREAK_NAV_KEY);
                    if (isBreakNav && !tmpPathList.isEmpty()) {
                        pathList.add(getSecondMap(page.getPath(), tmpPathList));
                        tmpPathList = new ArrayList<>();
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

    private Map<String, Object> getSecondMap(final String path, final Collection<Map<String, Object>> paths){
        Map<String, Object> newPath = new HashMap<>();
        newPath.put(PATH_DETAILS_LIST_PATH_PROPERTY_NAME, path);
        newPath.put(PATH_DETAILS_LIST_PATHS_PROPERTY_NAME, paths);
        return  newPath;
    }


    @Override
    protected boolean mustExist() {
        return false;
    }
}
