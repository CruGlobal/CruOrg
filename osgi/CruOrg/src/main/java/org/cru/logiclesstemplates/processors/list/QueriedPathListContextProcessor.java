package org.cru.logiclesstemplates.processors.list;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.TagManager;
import com.xumak.extended.contextprocessors.lists.AddQueriedPagePathListContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import com.google.common.collect.Sets;
import org.cru.util.PublishDateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Component
@Service
public class QueriedPathListContextProcessor extends AddQueriedPagePathListContextProcessor {

    public static final String XUMAK_TAG_NAV_LIST_RESOURCE_TYPE = "CruOrgApp/components/section/lists/queried";
    public static final String QUERY_CONTENT_KEY_NAME = "content.query";
    public static final String MAX_CONTENT_KEY_NAME = "content.max";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{XUMAK_TAG_NAV_LIST_RESOURCE_TYPE});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        Collection<String> pathList = new ArrayList();
        Resource resource = request.getResource();

        if (contentModel.has(PATHREFS_LIST_CONTENT_KEY) && contentModel.has(QUERY_CONTENT_KEY_NAME)) {
            //get PathReference
            String pathRef = contentModel.getAsString(PATHREFS_LIST_CONTENT_KEY);
            //get Tags
            List<String> tagsList = contentModel.getAs(QUERY_CONTENT_KEY_NAME , List.class);
            RangeIterator<Resource> pages = findByTags(resource, pathRef, tagsList);
            if (pages != null) {
                //get max number of items
                long max = getMaxNumber(contentModel, pages.getSize());
                pathList = PublishDateUtils.getSortedPathList(pages, resource, max);
            }
        }
        contentModel.set(PATHS_LIST_CONTEXT_KEY, pathList);
    }


    /**
     * get Iterator of resources based in list of tags.
     * @param  resource
     * @param pathRef path of reference.
     * @param tagsList list of tags.
     * @return ArrayList
     */
    protected RangeIterator<Resource> findByTags(final Resource resource, final String pathRef,
                                               final List<String> tagsList) {
        RangeIterator<Resource> pages = null;
        if (resource != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

            if (tagManager != null && StringUtils.isNotBlank(pathRef) && tagsList != null) {

                String[] tagsArray = tagsList.toArray(new String[tagsList.size()]);
                if (tagsArray.length > 0) {
                    //find by tag
                    pages = tagManager.find(pathRef, tagsArray, true);
                }
            }
        }
        return pages;
    }


    /**
     * get Max number of items
     * @param contentModel
     * @param listSize
     * @return Long with max number of items.
     */
    protected long getMaxNumber(final TemplateContentModel contentModel, final long listSize)throws Exception{
        long max = 0;
        if (contentModel.has(MAX_CONTENT_KEY_NAME)) {
            max = contentModel.getAs(MAX_CONTENT_KEY_NAME, Long.class);
            //not exist limit
            if (max == 0) {
                max = listSize;
            }
        }
        return max;
    }

}
