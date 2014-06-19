package org.cru.logiclesstemplates.processors.list;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.cru.util.PublishDateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/* DESCRIPTION
* -----------------------------------------------------------------------------
* AddFeaturedStoriesPathListContextProcessor
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer              | Changes
* 1.0     | 14/05/06    | jurizar                | Initial Creation
* 1.0     | 14/05/12    | palecio                | Renamed class, minor refactoring
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@Component
@Service
public class AddRelatedStoriesPathsContextProcessor extends AbstractAddQueriedPathListContextProcessor {

    public static final String XUMAK_TAG_NAV_LIST_RESOURCE_TYPE = "CruOrgApp/components/section/related-stories";
    public static final String PATH_LIST_CONTEXT_PROPERTY_NAME = "list.paths";
    public static final String PATHREF_GLOBAL_KEY_NAME = "global.pathRef";
    public static final String MAX_GLOBAL_KEY_NAME = "global.max";
    public static final String HOME_PAGE_PATH_KEY_NAME = "global.homepagePath";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(XUMAK_TAG_NAV_LIST_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        Collection<String> pathList = new ArrayList();
        Resource resource = request.getResource();
        ResourceResolver resourceResolver = request.getResourceResolver();

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        //get actual page path
        Page currentPage = pageManager.getContainingPage(resource).adaptTo(Page.class);

        //get PathReference
        String pathRef = contentModel.getAsString(PATHREF_GLOBAL_KEY_NAME);
        pathRef = StringUtils.isNotBlank(pathRef) ? pathRef :  contentModel.getAsString(HOME_PAGE_PATH_KEY_NAME);
        //get Tags
        Tag[] tags = currentPage.getTags();
        ArrayList<String> ids = new ArrayList<String>();
        for (Tag tag: tags){
            ids.add(tag.getTagID());
        }
        RangeIterator<Resource> pages = findByTags(resource, pathRef, ids);
        if (pages != null) {
            //get max number of items
            long max = getMaxNumber(contentModel, pages.getSize(), MAX_GLOBAL_KEY_NAME);
            pathList = PublishDateUtils.getSortedPathList(pages, resource, max);
        }
        int modular = pathList.size() % 3;
        if (pathList.size() > 0){
            contentModel.set("content.showContent", "true");
        }
        if (modular == 2 || pathList.size() == 2){
            String path = (String) ((ArrayList) pathList).get(pathList.size() - 2);
            Page p = resourceResolver.getResource(path).adaptTo(Page.class);
            contentModel.set("content.page21path", p.getPath());
            contentModel.set("content.page21title", p.getTitle());
            contentModel.set("content.page21description", p.getDescription());


            String path2 = (String) ((ArrayList) pathList).get(pathList.size() - 1);
            Page p2 = resourceResolver.getResource(path2).adaptTo(Page.class);
            contentModel.set("content.page22path", p2.getPath());
            contentModel.set("content.page22title", p2.getTitle());
            contentModel.set("content.page22description", p2.getDescription());

            ((ArrayList) pathList).remove(pathList.size() - 1);
            ((ArrayList) pathList).remove(pathList.size() - 1);

        } else  if (modular == 1 || pathList.size() == 1){
            String path = (String) ((ArrayList) pathList).get(pathList.size() - 1);
            Page p = resourceResolver.getResource(path).adaptTo(Page.class);
            contentModel.set("content.page1path", p.getPath());
            contentModel.set("content.page1title", p.getTitle());
            contentModel.set("content.page1description", p.getDescription());
            ((ArrayList) pathList).remove(pathList.size() - 1);
        }
        contentModel.set(PATH_LIST_CONTEXT_PROPERTY_NAME, pathList);
    }


}
