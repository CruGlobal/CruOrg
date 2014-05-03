package org.cru.util;

import com.day.cq.commons.RangeIterator;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.*;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CREATED;


public class PublishDateUtils {

    public static final String KEY_PUBLISH_DATE = "date";

    /*
    * Constructor
    * */
    public PublishDateUtils(){}


    /**
     * sort by date (publish date or created date)
     * @param unsortedIterator
     * @return ArrayList
     */
    public static ArrayList<Resource> getSortedList(final RangeIterator<Resource> unsortedIterator){
        ArrayList<Resource> list = new ArrayList<Resource>();
        while (unsortedIterator.hasNext()) {
            list.add(unsortedIterator.next());
        }
        Collections.sort(list, new CreatedDateComparator());
        return list;
    }


    /**
     * get sorted path list delimited by number of elements
     * @param unsortedIterator
     * @param  resource
     * @param  max number of elements.
     * @return ArrayList
     */
    public static ArrayList<String> getSortedPathList(final RangeIterator<Resource> unsortedIterator,
                                                       final Resource resource, final Long max) {

        ArrayList<String> pathList = new ArrayList();
        ArrayList<Resource> resourceList = PublishDateUtils.getSortedList(unsortedIterator);
        Long items = 0L;

        if (resource != null && resourceList != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

            //get actual page path
            String actualPagePath = pageManager.getContainingPage(resource).getPath();

            //add Paths based in max number of elements
            Iterator<Resource> resourceIter = resourceList.iterator();
            while ((resourceIter.hasNext()) && (items < max)) {
                Resource pages = resourceIter.next();
                String path = pageManager.getContainingPage(pages).getPath();

                //add element different to actual page
                if ((actualPagePath != null) && (!path.equals(actualPagePath))){
                    pathList.add(path);
                    items++;
                }
            }
        }
        return pathList;
    }


    /**
     * get Publish Date or created Date
     * @param resource the Resource containing the date property
     * @return Calendar
     */
    public static Calendar getPublishDate(final Resource resource){
        Calendar publishDate = resource.adaptTo(ValueMap.class).get(JCR_CREATED, Calendar.class);
        ValueMap map = resource.adaptTo(ValueMap.class);
        if (map.containsKey(KEY_PUBLISH_DATE)) {
            if (map.get(KEY_PUBLISH_DATE, Calendar.class) != null){
                publishDate = map.get(KEY_PUBLISH_DATE, Calendar.class);
            }
        }
        return publishDate;
    }


    static class CreatedDateComparator implements Comparator<Resource> {
        @Override
        public int compare(final Resource a, final Resource b) {
            Calendar date1 = getPublishDate(a);
            Calendar date2 = getPublishDate(b);
            return date1.before(date2) ? 1 : date1.after(date2) ? -1 : 0;
        }
    }

}
