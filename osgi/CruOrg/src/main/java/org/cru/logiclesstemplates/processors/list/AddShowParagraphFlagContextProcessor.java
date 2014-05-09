package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Sets;

import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.AddComponentPropertiesContextProcessor;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Set;
import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;
import static com.xumak.base.Constants.HTML_EXT;
import static com.xumak.base.Constants.IS_EDIT_MODE;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddFeaturedStoriesPathListContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 6/5/14      | jurizar                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */

@Component
@Service
public class AddShowParagraphFlagContextProcessor extends AddComponentPropertiesContextProcessor {

    public static final String ARTICLE_LONG_FORM_RESOURCE_TYPE = "CruOrgApp/components/section/article-long-form";
    public static final String CURRENT_SIBLING_PROPERTY_NAME = "page.currentSibling";
    public static final String SHOW_CONTENT_PROPERTY_NAME = "content.showContent";
    public static final String TOTAL_SIBLINGS_PROPERTY_NAME = "page.totalSiblings";
    public static final String NEXT_SIBLINGS_PROPERTY_NAME = "page.nextSiblings";
    public static final String PREV_SIBLINGS_PROPERTY_NAME = "page.prevSiblings";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{ARTICLE_LONG_FORM_RESOURCE_TYPE});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        int currentSelectorInt = 1;
        int currentSiblingInt;
        int total = (Integer) contentModel.get(TOTAL_SIBLINGS_PROPERTY_NAME);

        String[] selectors = request.getRequestPathInfo().getSelectors();

        if (selectors.length > 0){
            currentSelectorInt = Integer.parseInt(selectors[0]);
            if (contentModel.has(TOTAL_SIBLINGS_PROPERTY_NAME)){
                if (currentSelectorInt > total){
                    currentSelectorInt = total;
                }
            }
        }
        String currentSibling = (String) contentModel.get(CURRENT_SIBLING_PROPERTY_NAME);
        if (currentSibling == null){
            currentSibling = "1";
            currentSiblingInt = 1;
            contentModel.set(CURRENT_SIBLING_PROPERTY_NAME , currentSibling);
        } else {
            int current = Integer.parseInt(currentSibling) + 1;
            currentSiblingInt = current;
            currentSibling = String.valueOf(current);
            contentModel.set(CURRENT_SIBLING_PROPERTY_NAME , currentSibling);
        }
        if ("true".equals(contentModel.getAsString(GLOBAL_PAGE_CONTENT_KEY + "." + IS_EDIT_MODE)) ||
                currentSelectorInt == currentSiblingInt){
            contentModel.set(SHOW_CONTENT_PROPERTY_NAME, "show");
        }

        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
        Page currentPage = pageManager.getContainingPage(request.getResource()).adaptTo(Page.class);

        if (currentSelectorInt > 1){
            String prevPath = currentPage.getPath() + "." + (currentSelectorInt - 1) + HTML_EXT;
            contentModel.set(PREV_SIBLINGS_PROPERTY_NAME, prevPath);
        }
        if (currentSelectorInt < total){
            String nextPath = currentPage.getPath() + "." + (currentSelectorInt + 1) + HTML_EXT;
            contentModel.set(NEXT_SIBLINGS_PROPERTY_NAME, nextPath);
        }



    }
    @Override
    public int priority(){
        return 1000;
    }
}
