package org.cru.logiclesstemplates.processors;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;
import static com.xumak.base.Constants.HTML_EXT;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddRequestURLContextProcessor
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 28/4/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddRequestURLContextProcessor extends AbstractResourceTypeCheckContextProcessor <TemplateContentModel>{

    public static final String CONTENT_MODEL_SERVLET_SUFFIX = "/_jcr_content.contentmodel.page.json";
    public static final String FACEBOOK_COMMENTS_FEED_RESOURCE_TYPE =
            "CruOrgApp/components/page/article";
    public static final String REQUEST_URL_KEY = "requestURL";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(FACEBOOK_COMMENTS_FEED_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        if (contentModel.has(GLOBAL_PAGE_CONTENT_KEY)) {
            Map<String, Object> pageObject = (Map<String, Object>) contentModel.get(GLOBAL_PAGE_CONTENT_KEY);
            String requestURL = request.getRequestURL().toString();
            if (requestURL.endsWith(CONTENT_MODEL_SERVLET_SUFFIX)) {
                /*if the request is for the content model servlet (comes from the daily content component)
                 replace the content model servlet suffix for .html
                 */
                requestURL = requestURL.replace(CONTENT_MODEL_SERVLET_SUFFIX, HTML_EXT);
            }
            pageObject.put(REQUEST_URL_KEY, requestURL);
        }


    }


    @Override
    protected boolean mustExist() {
        return false;
    }
}
