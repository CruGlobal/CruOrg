package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.cru.util.PageUtils;
import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;


import java.util.Map;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddContentServletPathContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/05/02    | palecio                | Initial Creation
 * 1.0     | 14/05/13    | palecio                | Added Joda Time, refactoring
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddContentServletPathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {

    public static final String SERVLET_PATH_KEY = "contentServletPath";
    public static final String SERVLET_PATH_SUFFIX = ".contentmodel.page.json";

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        contentObject.put(CURRENT_RESOURCE_KEY, request.getResource());
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String todaysPagePath = getDailyContentPath(TODAY, contentObject);
        Page todaysPage = pageManager.getPage(todaysPagePath);
        if (todaysPage == null || !PageUtils.isArticlePage(todaysPage)){//if today's page is null or is not an article
            todaysPage = pageManager.getPage(getDefaultPath(contentObject)); //try to get the default page
        }
        if (todaysPage != null) {
            contentObject.put(SERVLET_PATH_KEY, request.getResourceResolver().map(getContentServletPath(todaysPage)));
        }

    }

    private String getContentServletPath(final Page page){
        String contentServletPath = "";
        Resource todaysPageContentResource = page.getContentResource();
        if (null != todaysPageContentResource) {
            if (PageUtils.isArticlePage(page)) {
                contentServletPath = todaysPageContentResource.getPath() + SERVLET_PATH_SUFFIX;
            }
        }
        return contentServletPath;
    }
}
