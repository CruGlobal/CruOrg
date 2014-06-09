package org.cru.util;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.collections.IteratorUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.List;

import static com.day.cq.wcm.api.NameConstants.PN_TEMPLATE;
import static com.xumak.base.Constants.APPS_ROOT;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * DateUtils
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/04/30    | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
public class PageUtils {

    public static final String ARTICLE_RESOURCE_TYPE =
            "CruOrgApp/components/page/article";

    private PageUtils(){} //prevent instantiation

    /**
     * Gets the page under the specified base page that complies with the year/month/day format
     * e.g for april 26 2014, it would look for a page under {base-path}/2014/04/26
     * @param basePageParameter the place to start looking for a page
     * @param date the date to use
     * @return the page under the specified date
     */
    public static Page getPageFromDate(final Page basePageParameter, final DateTime date){
        Page basePage = basePageParameter;
        Page dayPage = null;
        int day = date.getDayOfMonth();
        int month = date.getMonthOfYear();
        int year = date.getYear();
        if (null != basePage) {
            Page yearPage = getYearPage(basePage, year);
            basePage = (null != yearPage) ? yearPage : basePage; //if there is a year page, use it as base instead
            Page monthPage = getMonthPage(basePage, month);
            if (null != monthPage) {
                dayPage = getDayPage(monthPage, day);
            }
        }
        return dayPage;
    }

    /**
     * looks for a page directly below {@code basePage} whose name is equal to {@code day}
     * @param basePage the base page
     * @param day the name of the page (represents the day of the month)
     * @return page directly below {@code basePage} whose name is equal to {@code day} or null if none is found
     */
    public static Page getDayPage(final Page basePage, final int day){
        PageManager pageManager = basePage.getPageManager();
        String dayPagePath = basePage.getPath() + "/" + day;
        Page monthPage = pageManager.getPage(dayPagePath);
        if (null == monthPage){
            //see if adding a '0' before day matches the name of any page
            dayPagePath = basePage.getPath() + "/" + String.format("%02d", day);
            monthPage = pageManager.getPage(dayPagePath);
        }
        return monthPage;
    }

    /**
     * looks for a page directly below {@code basePage} whose name is equal to {@code monthNumber}
     * @param basePage the base page
     * @param month the name of the page (represents the month of the year),
     *                    which is going to be converted to a String.
     * @return page directly below {@code basePage} whose name is equal to {@code monthNumber} or null if none is found
     */
    public static Page getMonthPage(final Page basePage, final int month){
        PageManager pageManager = basePage.getPageManager();
        String monthPagePath = basePage.getPath() + "/" + month;
        Page monthPage = pageManager.getPage(monthPagePath);
        if (null == monthPage){
            monthPagePath = basePage.getPath() + "/" + String.format("%02d", month);
            monthPage = pageManager.getPage(monthPagePath);
        }
        return monthPage;
    }

    /**
     * looks for a page directly below {@code basePage} whose name is equal to {@code year}
     * @param basePage the base page
     * @param year the name of the page (represents the year), which is going to be converted to a String.
     * @return page directly below {@code basePage} whose name is equal to {@code year} or null if none is found
     */
    public static Page getYearPage(final Page basePage, final int year){
        PageManager pageManager = basePage.getPageManager();
        String yearPagePath = basePage.getPath() + "/" + year;
        return pageManager.getPage(yearPagePath);
    }

    /**
     * Gets basePage's child at index
     * @param basePage the base page
     * @param index the index of the page to be retrieved
     * @return the page at index of null
     */
    public static Page getPage(final Page basePage, final int index){
        Iterator<Page> pageIterator = basePage.listChildren();
        List pageList = IteratorUtils.toList(pageIterator);
        Page page = null;
        if (0 <= index && index < pageList.size()) {//make sure the index is within range
            page = (Page) pageList.get(index);
        }
        return page;
    }

    /**
     * Returns the index of the page
     * @param page the page
     * @return the index
     */
    public static int getPageIndex(final Page page){
        Page parentPage = page.getParent();
        Iterator<Page> pageIterator = parentPage.listChildren();
        int index = 0;
        while (pageIterator.hasNext()) {
            if (pageIterator.next().equals(page)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Finds the 'cq:template' property under the page properties and returns its value. Strips '/apps'.
     * @param page the page we want to know the template of
     * @return the cq:template page property
     */
    public static String getTemplate(final Page page){
        String template = "";
        if (null != page) {
            ValueMap properties = page.getProperties();
            if (null != properties){
                template = properties.get(PN_TEMPLATE, ""); //get "cq:template" property
                template = template.replace(APPS_ROOT + "/", ""); //strip "/apps"
            }
        }
        return template;
    }

    /**
     * Sees if the page's template path is equal to the article template path
     * @param page the page to evaluate
     * @return true if  {@code page} is an Article Page
     */
    public static boolean isArticlePage(final Page page){
        boolean isArticlePage = false;
        if (null != page) {
            isArticlePage = page.getContentResource().isResourceType(ARTICLE_RESOURCE_TYPE);
        }
        return isArticlePage;
    }

    /**
     * Gets the page that contains certain resource
     * @param componentResource the path to the current resource
     * @return the page that contains said resource
     */
    public static Page getContainingPage(final Resource componentResource){
        PageManager pageManager = componentResource.getResourceResolver().adaptTo(PageManager.class);
        return pageManager.getContainingPage(componentResource);
    }
}
