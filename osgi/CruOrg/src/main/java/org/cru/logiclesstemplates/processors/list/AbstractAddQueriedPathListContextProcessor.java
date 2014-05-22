package org.cru.logiclesstemplates.processors.list;


import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.TagManager;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddQueriedPagePathListContextProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;


/*
* DESCRIPTION
* -----------------------------------------------------------------------------
*  AbstractAddQueriedPathListContextProcessor
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/22/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@Component
@Service
public abstract class AbstractAddQueriedPathListContextProcessor extends AddQueriedPagePathListContextProcessor {


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
     * @param maxKey
     * @return Long with max number of items.
     */
    protected long getMaxNumber(final TemplateContentModel contentModel, final long listSize,
                                final String maxKey)throws Exception{
        long max = 3;
        if (contentModel.has(maxKey)) {
            max = contentModel.getAs(maxKey, Long.class);
            //not exist limit
            if (max == 0) {
                max = listSize;
            }
        }
        return max;
    }


}
