package org.cru.logiclesstemplates.processors.list;


import static com.xumak.extended.contextprocessors.lists.ListConstants.PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddQueriedPagePathListContextProcessor;

@Component
@Service
public class QueriedFilterProcessor extends AddQueriedPagePathListContextProcessor {

    public static final String XUMAK_TAG_NAV_LIST_RESOURCE_TYPE = "CruOrgApp/components/section/lists/queried";
    public static final String XUMAK_CURATED_LIST_RESOURCE_TYPE = "CruOrgApp/components/section/lists/curated";
    public static final String TITLE_CONTENT_KEY_NAME = "content.titleFilter";
    public static final String TITLE_REMOVE_KEY_NAME = "navigationTitle";
    public static final String DESCRIPTION_CONTENT_KEY_NAME = "content.descriptionFilter";
    public static final String DESCRIPTION_REMOVE_KEY_NAME = "description";
    public static final String IMAGE_CONTENT_KEY_NAME = "content.imageFilter";
    public static final String IMAGE_REMOVE_KEY_NAME = "imagePath";
    public static final String IMAGE_VALUE_NONE = "NONE";
    public static final String IMAGE_VALUE_FIRST = "FIRST";

    public static final int PRIORITY = AddQueriedPagePathListContextProcessor.PRIORITY - 20;

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{XUMAK_TAG_NAV_LIST_RESOURCE_TYPE, XUMAK_CURATED_LIST_RESOURCE_TYPE});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{

        String imageStr = "";
        boolean titleFilter, descriptionFilter;
        int imageFilter = 0;
        if (contentModel.has(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME)) {

            if (contentModel.has(IMAGE_CONTENT_KEY_NAME)) {
                imageStr = contentModel.getAsString(IMAGE_CONTENT_KEY_NAME);
                imageFilter = getImageFilter(imageStr);
            }
            titleFilter = contentModel.has(TITLE_CONTENT_KEY_NAME);
            descriptionFilter = contentModel.has(DESCRIPTION_CONTENT_KEY_NAME);

            ArrayList<Map<String, String>> pathList =
                    contentModel.getAs(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, ArrayList.class);
            getListByFilter(pathList, titleFilter, descriptionFilter, imageFilter);
            contentModel.set(PAGE_DETAILS_LIST_CONTEXT_PROPERTY_NAME, pathList);
        }
    }


    /**
     * get List by filters
     * @param  pathList
     * @param titleFilter
     * @param descriptionFilter
     * @param imageFilter
     * @return ArrayList
     */
    private void getListByFilter(final ArrayList<Map<String, String>> pathList, final boolean titleFilter,
                                               final boolean descriptionFilter, final int imageFilter) {

        Iterator<Map<String, String>> pageDetailsIter = pathList.iterator();
        Long items = 0L;
        while (pageDetailsIter.hasNext()){
            Map<String, String> pageDetails = pageDetailsIter.next();

            if (!titleFilter) {
                pageDetails.remove(TITLE_REMOVE_KEY_NAME);
            }

            if (!descriptionFilter) {
                pageDetails.remove(DESCRIPTION_REMOVE_KEY_NAME);
            }

            if ((imageFilter == 2) || ((imageFilter == 1) && (items > 0))) {
                pageDetails.remove(IMAGE_REMOVE_KEY_NAME);
            }

            items++;
        }

    }


    /**
     * get Image Filter
     * @param imageStr
     * @return integer.
     */
    private int getImageFilter(final String imageStr)throws Exception{
        int imageFilter = 0;
        if (imageStr.equals(IMAGE_VALUE_FIRST)) {
            imageFilter = 1;
        } else if (imageStr.equals(IMAGE_VALUE_NONE)) {
            imageFilter = 2;
        }
        return imageFilter;
    }


    @Override
    public int priority() {
        return PRIORITY;
    }

}
