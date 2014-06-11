package org.cru.logiclesstemplates.processors;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;
import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static org.cru.util.PageUtils.ARTICLE_RESOURCE_TYPE;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddIsCaptionEmptyFlagContextProcessor
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 11/6/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddIsCaptionEmptyFlagContextProcessor
        extends AbstractResourceTypeCheckContextProcessor <TemplateContentModel>{

    public static final String IMAGE_CAPTION_KEY_NAME = "imageCaption";
    public static final String IMAGE_CREDIT_KEY_NAME = "imageCredit";
    public static final String CAPTION_KEY_NAME = "caption";
    public static final String IS_CAPTION_EMPTY_FLAG_KEY_NAME = "isCaptionEmpty";

    public static final String CAPTIONED_IMAGE_RESOURCE_TYPE = "CruOrgApp/components/section/captioned-image";
    public static final String MEDIA_EMBED_RESOURCE_TYPE = "CruOrgApp/components/section/media-embed";
    public static final String ARTICLE_LONG_FORM_RESOURCE_TYPE = "CruOrgApp/components/section/article-long-form";



    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(ARTICLE_RESOURCE_TYPE,
                               CAPTIONED_IMAGE_RESOURCE_TYPE,
                               MEDIA_EMBED_RESOURCE_TYPE,
                               ARTICLE_LONG_FORM_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        Resource resource = request.getResource();
        boolean isCaptionEmptyFlag = true;
        log.info(isCaptionEmptyFlag + "");
        if (resource.isResourceType(ARTICLE_RESOURCE_TYPE)) {
            Map<String, Object> pageObject = (Map<String, Object>) contentModel.get(GLOBAL_PAGE_CONTENT_KEY);
            String imageCaption = (String) pageObject.get(IMAGE_CAPTION_KEY_NAME);
            String imageCredit = (String) pageObject.get(IMAGE_CREDIT_KEY_NAME);
            if ((null != imageCaption) || (null != imageCredit)) {
                isCaptionEmptyFlag = false;
            }
            pageObject.put(IS_CAPTION_EMPTY_FLAG_KEY_NAME, isCaptionEmptyFlag);

        } else {
            Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
            String imageCaption = (String) contentObject.get(IMAGE_CAPTION_KEY_NAME);
            String caption = (String) contentObject.get(CAPTION_KEY_NAME);
            String imageCredit = (String) contentObject.get(IMAGE_CREDIT_KEY_NAME);
            if ((null != imageCaption) || (null != caption) || (null != imageCredit)) {
                isCaptionEmptyFlag = false;
            }
            contentObject.put(IS_CAPTION_EMPTY_FLAG_KEY_NAME, isCaptionEmptyFlag);

        }


    }

}
