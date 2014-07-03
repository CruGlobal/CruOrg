package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.cru.util.PageUtils;

import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddDailyContentPagePathsContextProcessor
 *
 * Runs for the daily content page.
 * Sets both previous and next paths.
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/06/03    | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddDailyContentPagePathsContextProcessor
        extends AbstractResourceTypeCheckContextProcessor<TemplateContentModel>{


    public static final String DAILY_CONTENT_PAGE_RESOURCE_TYPE =
            "CruOrgApp/components/page/daily-content";
    public static final String PREVIOUS_PAGE_KEY = "previousPage";
    public static final String NEXT_PAGE_KEY = "nextPage";


    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(DAILY_CONTENT_PAGE_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {

        @SuppressWarnings("unchecked")
        Map<String, Object> contentPageObject = (Map<String, Object>) contentModel.get(GLOBAL_PAGE_CONTENT_KEY);
        Page currentPage = PageUtils.getContainingPage(request.getResource());

        Page previousPage = getPreviousPage(currentPage);
        if (null != previousPage) {
            contentPageObject.put(PREVIOUS_PAGE_KEY, previousPage.getPath());
        }
        Page nextPage = getNextPage(currentPage);
        if (null != nextPage) {
            contentPageObject.put(NEXT_PAGE_KEY, nextPage.getPath());
        }
    }

    /**
     * Retrieves the previous daily content page. First, it tries to get the previous sibling. If it fails, it looks
     * in its parent's previous sibling for the last page. If that fails, looks in its grandparent's sibling for its
     * last grandchild. If that fails, it returns null.
     * @param currentPage the page to get the previous page from
     * @return the previous page
     */
    private Page getPreviousPage(final Page currentPage) {
        Page parentPage = currentPage.getParent();
        int currentPageIndex = PageUtils.getPageIndex(currentPage);
        Page previousPage = PageUtils.getPage(parentPage, currentPageIndex - 1);

        if (null == previousPage) {
            //maybe the previous page is the last day of the previous month, we'll try to find out
            if (PageUtils.isDayPage(currentPage)) {
                // if it's not a day page, then it's probably a periodical page, we won't try to get the previous month
                // if it is a day page, we'll try to get its parent (a month page)
                Page monthPage = currentPage.getParent();
                if (PageUtils.isMonthPage(monthPage)) {
                //if the child page is a day page, this one will likely be a month page, but we still check to be sure
                    Page monthPageParent = monthPage.getParent();
                    int monthPageIndex = PageUtils.getPageIndex(monthPage); //the index of the month page
                    Page previousMonthPage = PageUtils.getPage(monthPageParent, monthPageIndex - 1);
                    //we try to get the previous month page. If it exists, we'll get the last day page
                    if (null != previousMonthPage) {
                        int lastDayPageIndex = PageUtils.numberOfChildren(previousMonthPage) - 1;
                        previousPage = PageUtils.getPage(previousMonthPage, lastDayPageIndex);
                    } else {
                        //ok, maybe the current page is January 1st, we'll need to see if there is a previous year
                        Page yearPage = monthPage.getParent();
                        int yearPageIndex = PageUtils.getPageIndex(yearPage);
                        if (PageUtils.isYearPage(yearPage)) { //if it's not a year page, we stop looking
                            Page previousYearPage = PageUtils.getPage(yearPage.getParent(), yearPageIndex - 1);
                            if (null != previousYearPage) { //we get the previous year page
                                int lastMonthIndex = PageUtils.numberOfChildren(previousYearPage) - 1;
                                //from that year, we look for the last month
                                Page lastMonthPage = PageUtils.getPage(previousYearPage, lastMonthIndex);
                                if (null != lastMonthPage) {
                                    //now that we have the last month, we look for its last day
                                    int lastDayPageIndex = PageUtils.numberOfChildren(lastMonthPage) - 1;
                                    previousPage = PageUtils.getPage(lastMonthPage, lastDayPageIndex);
                                }
                            }
                        }
                    }
                }
            }
        }
        return previousPage;
    }

    /**
     * Retrieves the next daily content page. First, it tries to get the next sibling. If it fails, it looks
     * in its parent's next sibling for the first page. If that fails, it looks in its grandparent's next sibling for
     * its first grandchild. If that fails, it returns null.
     * @param currentPage the page to get the next page from
     * @return the next page
     */
    private Page getNextPage(final Page currentPage) {
        Page parentPage = currentPage.getParent();
        int currentPageIndex = PageUtils.getPageIndex(currentPage);
        Page nextPage = PageUtils.getPage(parentPage, currentPageIndex + 1);

        if (null == nextPage) {
            //maybe the next page is the first day of the following month, we'll try to find out
            if (PageUtils.isDayPage(currentPage)) {
                // if it's not a day page, then it's probably a periodical page, we won't try to get the following month
                // if it is a day page, we'll try to get its parent (a month page)
                Page monthPage = currentPage.getParent();
                if (PageUtils.isMonthPage(monthPage)) {
                //if the child page is a day page, this one will likely be a month page, but we still check to be sure
                    Page monthPageParent = monthPage.getParent();
                    int monthPageIndex = PageUtils.getPageIndex(monthPage); //the index of the month page
                    Page followingMonthPage = PageUtils.getPage(monthPageParent, monthPageIndex + 1);
                    //we try to get the following month page. If it exists, we'll get the last day page
                    if (null != followingMonthPage) {
                        nextPage = PageUtils.getPage(followingMonthPage, 0);
                    } else {
                        //ok, maybe the current page is December 31st, we'll need to see if there is a following year
                        Page yearPage = monthPage.getParent();
                        int yearPageIndex = PageUtils.getPageIndex(yearPage);
                        if (PageUtils.isYearPage(yearPage)) { //if it's not a year page, we stop looking
                            Page followingYearPage = PageUtils.getPage(yearPage.getParent(), yearPageIndex + 1);
                            if (null != followingYearPage) { //we get the previous year page
                                //from that year, we look for the first month
                                Page firstMonthPage = PageUtils.getPage(followingYearPage, 0);
                                if (null != firstMonthPage) {
                                    //now that we have the first month, we look for its first day
                                    nextPage = PageUtils.getPage(firstMonthPage, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return nextPage;
    }


}
