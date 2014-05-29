package org.cru.logiclesstemplates.processors.list;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.foundation.Paragraph;
import com.day.cq.wcm.foundation.ParagraphSystem;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import javax.jcr.Node;
import java.util.*;

import static com.xumak.base.Constants.TITLE;
import static com.xumak.base.Constants.PATH;
import static com.xumak.base.Constants.HTML_EXT;



/* DESCRIPTION
* -----------------------------------------------------------------------------
* AddFeaturedStoriesPathListContextProcessor
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer              | Changes
* 1.0     | 14/05/06    | jurizar                | Initial Creation
* 1.0     | 14/05/12    | palecio                | Changed superclass, added validations for resource
* -----------------------------------------------------------------------------
*
==============================================================================
*/


@Component
@Service
public class AddArticleSiblingsContextProcessor
        extends AbstractResourceTypeCheckContextProcessor<TemplateContentModel> {

    public static final String INSET_SIDEBAR_RESOURCE_TYPE = "CruOrgApp/components/section/inset-sidebar";
    public static final String ARTICLE_LONG_FORM_RESOURCE_TYPE = "CruOrgApp/components/section/article-long-form";
    public static final String ALL_SIBLINGS_PROPERTY_NAME = "content.allSiblings";
    public static final String TOTAL_SIBLINGS_PROPERTY_NAME = "page.totalSiblings";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(INSET_SIDEBAR_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        Resource  resource = request.getResource();
        resource = (null != resource) ? resource.getParent() : resource;
        resource = (null != resource) ? resource.getParent() : resource;

        if (null != resource){

            ParagraphSystem paragraphSystem = ParagraphSystem.create(resource, request);
            List<Paragraph> paragraphs = paragraphSystem.paragraphs();
            PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
            Page currentPage = pageManager.getContainingPage(resource).adaptTo(Page.class);

            int count = 1;
            List<Map<String, String>> allParagraphDetailList = new ArrayList<Map<String, String>>();
            for (Paragraph p : paragraphs){
                if (ARTICLE_LONG_FORM_RESOURCE_TYPE.equals(p.getResource().getResourceType())){
                    String path = currentPage.getPath() + "." + count + HTML_EXT;
                    allParagraphDetailList.add(extractParagraphDetails(p, path));
                    count++;
                }
            }
            contentModel.set(TOTAL_SIBLINGS_PROPERTY_NAME, new Integer(count));
            contentModel.set(ALL_SIBLINGS_PROPERTY_NAME, allParagraphDetailList);
        }
    }

    private Map<String, String> extractParagraphDetails(final Paragraph paragraph, final String path)
            throws Exception {
        Map<String, String> paragraphDetails = new HashMap<String, String>();
        if (null != paragraph){
            Node node = paragraph.adaptTo(Node.class);
            if (node.hasProperty(TITLE)){
                paragraphDetails.put(TITLE, node.getProperty(TITLE).getString());
            }
            paragraphDetails.put(PATH,  path);
        }
        return paragraphDetails;
    }

    @Override
    protected boolean mustExist() {
        return false;
    }
}
