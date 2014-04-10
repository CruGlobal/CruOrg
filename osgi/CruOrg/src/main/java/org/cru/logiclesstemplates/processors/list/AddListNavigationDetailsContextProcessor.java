package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddListReferencedPagesDetailsContextProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class AddListNavigationDetailsContextProcessor extends AddListReferencedPagesDetailsContextProcessor {

    public static final String LIST_NAVIGATION_KEY_NAME = "list.navigation";
    public static final String LIST_PATH_KEY_NAME = "list.paths";
    public static final String TITLE_KEY_NAME = "title";
    public static final String PATH_KEY_NAME = "path";

    @Override
    public void process(final SlingHttpServletRequest request,final TemplateContentModel contentModel)throws Exception{
        Resource resource = request.getResource();
        if (resource != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = (PageManager)resourceResolver.adaptTo(PageManager.class);
            if (contentModel.has(LIST_PATH_KEY_NAME)) {
                ArrayList<String> pathList = contentModel.getAs(LIST_PATH_KEY_NAME, ArrayList.class);
                List allPageDetailList = new ArrayList();
                for (String path : pathList) {
                    Page page = pageManager.getContainingPage(path);
                    allPageDetailList.add(extractNavigationDetails(page));
                }
                contentModel.set(LIST_NAVIGATION_KEY_NAME, allPageDetailList);
            }
        }
    }


    private Map<String, String> extractNavigationDetails(final Page page) throws Exception {
        Map pageDetails = new HashMap();

        if(page != null) {
            //assign navigation title based on priority
            String title = "";
            if(StringUtils.isNotBlank(page.getNavigationTitle())) {
                title = page.getNavigationTitle();
            } else if (StringUtils.isNotBlank(page.getPageTitle())) {
                title = page.getPageTitle();
            } else {
                title = page.getTitle();
            }
            pageDetails.put(TITLE_KEY_NAME, title);
            pageDetails.put(PATH_KEY_NAME, page.getPath());
        }

        return pageDetails;
    }

}

