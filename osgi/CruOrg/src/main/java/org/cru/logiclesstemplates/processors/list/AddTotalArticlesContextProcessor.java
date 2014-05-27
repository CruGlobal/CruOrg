package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.foundation.Paragraph;
import com.day.cq.wcm.foundation.ParagraphSystem;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AbstractListContextProcessor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import static com.xumak.base.Constants.HIGHER_PRIORITY;

import java.util.*;

/* DESCRIPTION
* -----------------------------------------------------------------------------
* AddTotalArticlesContextProcessor
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
public class AddTotalArticlesContextProcessor extends AbstractListContextProcessor {

    public static final String ARTICLE_LONG_FORM_RESOURCE_TYPE = "CruOrgApp/components/section/article-long-form";
    public static final String TOTAL_SIBLINGS_PROPERTY_NAME = "page.totalSiblings";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{ARTICLE_LONG_FORM_RESOURCE_TYPE});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        Resource  mainResource = request.getResource();
        Resource resource = null;

        if (mainResource != null && mainResource.getParent() != null){
            resource = mainResource.getParent();
            ParagraphSystem paragraphSystem = ParagraphSystem.create(resource, request);
            List<Paragraph> paragraphs = paragraphSystem.paragraphs();

            int count = 0;
            for (Paragraph p : paragraphs){
                if (ARTICLE_LONG_FORM_RESOURCE_TYPE.equals(p.getResource().getResourceType())){
                    count++;
                }
            }
            contentModel.set(TOTAL_SIBLINGS_PROPERTY_NAME, new Integer(count));
        }
    }

    @Override
    public int priority(){
        return HIGHER_PRIORITY + 1;
    }
}
