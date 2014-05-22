package org.cru.logiclesstemplates.processors.list;

import com.day.cq.commons.RangeIterator;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.cru.util.PublishDateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Component
@Service
public class AddQueriedPathListContextProcessor extends AbstractAddQueriedPathListContextProcessor {

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
                long max = getMaxNumber(contentModel, pages.getSize(), MAX_CONTENT_KEY_NAME);
                pathList = PublishDateUtils.getSortedPathList(pages, resource, max);
            }
        }
        contentModel.set(PATHS_LIST_CONTEXT_KEY, pathList);
    }


}
