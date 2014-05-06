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


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddTodaysPagePathContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2/5/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddTodaysPagePathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {

    public static final String SERVLET_PATH_KEY = "contentServletPath";
    public static final String SERVLET_PATH_SUFFIX = ".content.json";

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        super.process(request, contentModel);
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String todaysPagePath = getDailyContentPath(TODAY);
        Page todaysPage = pageManager.getPage(todaysPagePath);
        if (todaysPage == null || !PageUtils.isArticlePage(todaysPage)){
            todaysPage = pageManager.getPage(defaultPath);
        }

        contentObject.put(SERVLET_PATH_KEY, getContentServletPath(todaysPage));
        contentObject.put(TODAY, defaultPath);

    }

    private String getContentServletPath(final Page page){
        String contentServletPath = "";
        if (null != page) {
            Resource todaysPageContentResource = page.getContentResource();
            if (null != todaysPageContentResource) {
                if (PageUtils.isArticlePage(page)) {
                    contentServletPath = todaysPageContentResource.getPath() + SERVLET_PATH_SUFFIX;
                }
            }
        }
        return contentServletPath;
    }
}
