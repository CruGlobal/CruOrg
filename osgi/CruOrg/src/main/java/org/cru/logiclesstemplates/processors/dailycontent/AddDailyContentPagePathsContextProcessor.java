package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.cru.util.PageUtils;

import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddDailyContentPagePathsContextProcessor
 *
 * Runs for the daily content page.
 * Sets both previous and next paths.
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/06/03    | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddDailyContentPagePathsContextProcessor
        extends AbstractResourceTypeCheckContextProcessor<TemplateContentModel>{


    public static final String DAILY_CONTENT_PAGE_RESOURCE_TYPE =
            "CruOrgApp/components/page/daily-content";
    public static final String PREVIOUS_PAGE_KEY = "previousPage";
    public static final String NEXT_PAGE_KEY = "nextPage";


    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(DAILY_CONTENT_PAGE_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        Map<String, Object> contentPageObject = (Map<String, Object>) contentModel.get(GLOBAL_PAGE_CONTENT_KEY);
        Page currentPage = PageUtils.getContainingPage(request.getResource());
        Page parentPage = currentPage.getParent();
        int currentPageIndex = PageUtils.getPageIndex(currentPage);
        Page previousPage = PageUtils.getPage(parentPage, currentPageIndex - 1);
        Page nextPage = PageUtils.getPage(parentPage, currentPageIndex + 1);

        if (null != previousPage) {
            contentPageObject.put(PREVIOUS_PAGE_KEY, previousPage.getPath());
        }
        if (null != nextPage) {
            contentPageObject.put(NEXT_PAGE_KEY, nextPage.getPath());
        }
    }


}
