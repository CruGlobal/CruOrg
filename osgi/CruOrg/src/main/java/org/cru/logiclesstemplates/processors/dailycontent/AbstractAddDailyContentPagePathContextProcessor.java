package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.cru.util.DateUtils;
import org.cru.util.PageUtils;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

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
 * 1.0     | 30/4/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AbstractAddDailyContentPagePathContextProcessor
        extends AbstractResourceTypeCheckContextProcessor<TemplateContentModel>{

    public static final String DAILY_CONTENT_RESOURCE_TYPE =
            "CruOrgApp/components/section/daily-content";
    public static final String DISPLAY_PERIODICALLY_KEY = "displayPeriodically";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String DEFAULT_PATH = "defaultPath";
    public static final String TODAY = "today";
    public static final String TOMORROW = "tomorrow";
    public static final String YESTERDAY = "yesterday";

    protected Page currentPage;
    protected Map<String, Object> contentObject;
    protected String defaultPath;
    protected Calendar startDate;
    protected Calendar endDate;
    protected Calendar today;
    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(DAILY_CONTENT_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = request.getResource();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        currentPage = pageManager.getContainingPage(resource);
        contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        defaultPath = (String) contentObject.get(DEFAULT_PATH);
        defaultPath = (null == defaultPath) ? "" : defaultPath;
        today = Calendar.getInstance();
        startDate = (Calendar) contentObject.get(START_DATE);
        if (null != startDate) {
            startDate.set(HOUR_OF_DAY, 0);
            startDate.set(MINUTE, 0);
        }
        endDate = (Calendar) contentObject.get(END_DATE);
        if (null != endDate) {
            endDate.set(HOUR_OF_DAY, 23);
            endDate.set(MINUTE, 59);
        }
    }

    /**
     * Gets the page path under currentPage that corresponds to a given day
     * @param day today, tomorrow or yesterday
     * @return the corresponding page path
     */
    protected String getPeriodicalPagePath(final String day){
        String periodicalPagePath = defaultPath;
        if (DateUtils.isDateBetween(today, startDate, endDate)){
            //TODO fix; if today and start date are in different years, it will break
            int index = today.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);

            if (YESTERDAY.equals(day)){
                index--; //if we want yesterday's page, we substract 1 from today's index
            } else if (TOMORROW.equals(day)){
                index++; //if we want tomorrow's page, we add 1 to today's index
            }

            Page periodicalPage = PageUtils.getPage(currentPage, index);
            if (null != periodicalPage){
                periodicalPagePath = periodicalPage.getPath();
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
    protected String getDatePagePath(final Calendar date){
        Page page = PageUtils.getPageFromDate(currentPage, date);
        return (null != page) ? page.getPath() : "";
    }

    /**
     * Gets the correct content path taking into account {@code isPeriodical} property, {@code startDate},
     * {@code endDate} and today's date
     * @param dateString "yesterday", "today" or "tomorrow"
     * @return the content path for the corresponding day
     */
    protected String getDailyContentPath(final String dateString){
        String dailyContentPath = "";
        if (null == contentObject.get(DISPLAY_PERIODICALLY_KEY)){
            Calendar date = getDate(dateString);
            dailyContentPath = getDatePagePath(date);
        } else {
            dailyContentPath = getPeriodicalPagePath(dateString);
        }
        return dailyContentPath;
    }

    /**
     * gets the date corresponding to yesterday, today or tomorrow
     * @param dateString "yesterday", "today" or "tomorrow"
     * @return the date corresponding to yesterday, today or tomorrow
     */
    protected Calendar getDate(final String dateString){
        Calendar date = Calendar.getInstance();
        if (YESTERDAY.equals(dateString)) {
            date.add(DATE, -1);
        } else if (TOMORROW.equals(dateString)) {
            date.add(DATE, 1);
        }
        return date;
    }
    @Override
    protected boolean mustExist() {
        return false;
    }
}
