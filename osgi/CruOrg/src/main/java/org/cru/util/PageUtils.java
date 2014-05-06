package org.cru.util;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.collections.IteratorUtils;
import org.apache.sling.api.resource.ValueMap;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static java.util.Calendar.*;
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
 * 1.0     | 30/4/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
public class PageUtils {

    public static final String ARTICLE_TEMPLATE_PATH = "CruOrgApp/templates/article";

    private PageUtils(){} //prevent instantiation

    /**
     * Gets the page under the specified base page that complies with the year/month/day format
     * e.g for april 26 2014, it would look for a page under {base-path}/2014/04/26
     * @param basePageParameter the place to start looking for a page
     * @param calendar the date to use
     * @return the page under the specified date
     */
    public static Page getPageFromDate(final Page basePageParameter, final Calendar calendar){
        Page basePage = basePageParameter;
        Page dayPage = null;
        int day = calendar.get(DAY_OF_MONTH);
        int month = calendar.get(MONTH); // add 1 because the months from Calendar start at 0
        int year = calendar.get(YEAR);
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

    public static Page getDayPage(final Page basePage, final int day){
        PageManager pageManager = basePage.getPageManager();
        String dayPagePath = basePage.getPath() + "/" + day;
        Page monthPage = pageManager.getPage(dayPagePath);
        if (null == monthPage){
            dayPagePath = basePage.getPath() + "/" + String.format("%02d", day);
            monthPage = pageManager.getPage(dayPagePath);
        }
        return monthPage;
    }

    public static Page getMonthPage(final Page basePage, final int monthNumber){
        PageManager pageManager = basePage.getPageManager();
        int month = monthNumber + 1;
        String monthPagePath = basePage.getPath() + "/" + month;
        Page monthPage = pageManager.getPage(monthPagePath);
        if (null == monthPage){
            monthPagePath = basePage.getPath() + "/" + String.format("%02d", month);
            monthPage = pageManager.getPage(monthPagePath);
        }
        return monthPage;
    }

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
        if (0 <= index && index < pageList.size()) {
            page = (Page) pageList.get(index);
        }
        return page;
    }

    public static String getTemplate(final Page page){
        String template = "";
        if (null != page) {
            ValueMap properties = page.getProperties();
            if (null != properties){
                template = properties.get(PN_TEMPLATE, "");
                template = template.replace(APPS_ROOT + "/", "");
            }
        }
        return template;
    }

    public static boolean isArticlePage(final Page page){
        return ARTICLE_TEMPLATE_PATH.equals(getTemplate(page));
    }
}
