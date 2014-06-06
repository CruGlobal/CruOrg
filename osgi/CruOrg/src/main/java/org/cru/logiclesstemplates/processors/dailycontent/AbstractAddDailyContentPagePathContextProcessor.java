package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.cru.util.DateUtils;
import org.cru.util.PageUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import java.util.Map;
import java.util.Set;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddDailyContentPagesContextProcessor
 *
 * Runs for the daily content component.
 * Its process method sets the fields needed.
 * Has methods to choose what path to return based on the isPeriodical, startDate
 * and endDate properties.
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/04/30    | palecio                | Initial Creation
 * 1.0     | 14/05/12    | palecio                | Added Joda Time

 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public abstract class AbstractAddDailyContentPagePathContextProcessor
        extends AbstractResourceTypeCheckContextProcessor<TemplateContentModel>{


    public static final String DAILY_CONTENT_RESOURCE_TYPE =
            "CruOrgApp/components/section/daily-content";
    public static final String DISPLAY_PERIODICALLY_KEY = "displayPeriodically";
    public static final String CURRENT_RESOURCE_KEY = "currentResource";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String DEFAULT_PATH = "defaultPath";
    public static final String TODAY = "today";
    public static final String TOMORROW = "tomorrow";
    public static final String YESTERDAY = "yesterday";
    public static final String EXCLUDE_YEAR = "excludeYear";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(DAILY_CONTENT_RESOURCE_TYPE);
    }

    /**
     * Gets the page path under currentPage that corresponds to a given day
     * @param day today, tomorrow or yesterday
     * @return the corresponding page path
     */
    protected String getPeriodicalPagePath(final String day, final Map<String, Object> contentObject){
        String periodicalPagePath = getDefaultPath(contentObject);
        DateTime requiredDate = getDate(day, contentObject);
        DateTime startDate = getDate(START_DATE, contentObject);
        DateTime endDate = getDate(END_DATE, contentObject);

        if (contentObject.containsKey(EXCLUDE_YEAR)) {
            int year = new DateTime().getYear();
            startDate = startDate.withYear(year);
            //resolved hypothetical case: select December (startDate) and January(endDate).
            if (startDate.getMonthOfYear() > endDate.getMonthOfYear()) {
               year++;
            }
            endDate = endDate.withYear(year);
        }

        if (DateUtils.isDateBetween(requiredDate, startDate, endDate)){
            int index = Days.daysBetween(startDate, requiredDate).getDays();
            if (index >= 0) {
                Page periodicalPage = PageUtils.getPage(getCurrentPage(contentObject), index);
                if (null != periodicalPage){
                    periodicalPagePath = periodicalPage.getPath();
                }
            }
        }
        return periodicalPagePath;
    }

    /**
     * gets page path under the specified date.
     * e.g. for june 24 2014, this method will try to get {@code currentPage}/2014/06/24
     * @param date the date to look for
     * @return the path under {@code currentPage} that corresponds to {@code date}
     */
    protected String getDatePagePath(final DateTime date, final Page currentPage){
        Page page = PageUtils.getPageFromDate(currentPage, date);
        return (null != page) ? page.getPath() : "";
    }

    /**
     * Looks for the display periodically property on the content model, if found, this method
     * will try to get the page within that period, else it will try to find the page under a certain date
     * @param day {@code TODAY}, {@code TOMORROW} or {@code YESTERDAY}
     * @param contentObject the content object
     * @return the daily content path for the day specified
     */
    protected String getDailyContentPath(final String day, final Map<String, Object> contentObject){
        String dailyContentPath;
        if (null == contentObject.get(DISPLAY_PERIODICALLY_KEY)){
            DateTime date = getDate(day, contentObject);
            dailyContentPath = getDatePagePath(date, getCurrentPage(contentObject));
        } else {
            dailyContentPath = getPeriodicalPagePath(day, contentObject);
        }
        return "".equals(dailyContentPath) ? getDefaultPath(contentObject) : dailyContentPath;

    }


    /**
     * Gets the date of the day specified
     * @param which could be {@code TODAY}, {@code TOMORROW}, {@code YESTERDAY},
     * {@code START_DATE} or {@code END_DATE}
     * @param contentObject the content object
     * @return a Date Time object with the date needed
     */
    protected DateTime getDate(final String which, final Map<String, Object> contentObject){
        DateTime date;
        switch (which) {
            case START_DATE:
                date = new DateTime(contentObject.get(START_DATE))
                        .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
                break;
            case END_DATE:
                date = new DateTime(contentObject.get(END_DATE))
                        .withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
                break;
            case YESTERDAY:
                date = new DateTime().minusDays(1);
                break;
            case TOMORROW:
                date = new DateTime().plusDays(1);
                break;
            default:
                date = new DateTime();
                break;
        }
        return date;
    }

    /**
     * Gets the default path property from the content model
     * @param contentObject the content object
     * @return The default path or an empty string
     */
    protected String getDefaultPath(final Map<String, Object> contentObject) {
        String defaultPath = (String) contentObject.get(DEFAULT_PATH);
        if (null != defaultPath){
            Resource resource = (Resource) contentObject.get(CURRENT_RESOURCE_KEY);
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            Page page = pageManager.getPage(defaultPath);
            if (null == page){
                defaultPath = "";
            }
        }
        return (null != defaultPath) ? defaultPath : "";

    }

    /**
     * Gets the current page
     * @param contentObject the content object
     * @return the current page or null
     */
    private Page getCurrentPage(final Map<String, Object> contentObject) {
        Resource currentResource = (Resource) contentObject.get("currentResource");
        return PageUtils.getContainingPage(currentResource);
    }


    @Override
    protected boolean mustExist() {
        return false;
    }
}
