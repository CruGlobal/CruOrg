package org.cru.logiclesstemplates.processors.list;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AbstractListContextProcessor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATHREFS_CONTENT_KEY_NAME;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddFeaturedStoriesPathListContextProcessor
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 24/4/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddFeaturedStoriesPathListContextProcessor extends AbstractListContextProcessor {

    public static final String FEATURED_STORIES_RESOURCE_TYPE = "CruOrgApp/components/section/featured-stories";
    public static final String GLOBAL_HOMEPAGE_PATH_PROPERTY_NAME = "global.homepagePath";
    public static final String MAIN_SLIDER_RESOURCE_NAME = "main-slider";
    public static final int FEATURED_STORIES_MAX_ITEMS = 2;

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(FEATURED_STORIES_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        ResourceResolver resourceResolver = request.getResourceResolver();
        String homePagePath = contentModel.getAsString(GLOBAL_HOMEPAGE_PATH_PROPERTY_NAME);
        contentModel.set(PATH_LIST_CONTEXT_PROPERTY_NAME, getMainSliderPaths(homePagePath, resourceResolver));
    }

    private ArrayList<String> getMainSliderPaths(final String homePagePath, final ResourceResolver resourceResolver) {
        ArrayList<String> mainSliderPathList = new ArrayList<String>();
        String mainSliderResourcePath = homePagePath + "/" + JCR_CONTENT + "/" + MAIN_SLIDER_RESOURCE_NAME;
        Resource mainSliderResource = resourceResolver.getResource(mainSliderResourcePath);
        if (null != mainSliderResource) {
            ValueMap resourceProperties = ResourceUtil.getValueMap(mainSliderResource);
            String[] mainSliderPathArray = resourceProperties.get(PATHREFS_CONTENT_KEY_NAME, String[].class);
            int mainSliderPathListSize = mainSliderPathArray.length >= FEATURED_STORIES_MAX_ITEMS ?
                    FEATURED_STORIES_MAX_ITEMS : mainSliderPathArray.length;
            mainSliderPathList.addAll(Arrays.asList(mainSliderPathArray).subList(0, mainSliderPathListSize));
        }
        return mainSliderPathList;
    }

    @Override
    protected boolean mustExist() {
        return false;
    }

}

