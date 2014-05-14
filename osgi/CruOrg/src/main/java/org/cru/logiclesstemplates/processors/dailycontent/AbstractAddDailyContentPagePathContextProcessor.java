package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
//import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.cru.util.DateUtils;
import org.cru.util.PageUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.*;
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

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(DAILY_CONTENT_RESOURCE_TYPE);
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {



        LocalDate date = new LocalDate(Calendar.getInstance());
        System.out.println("DATE" + date);
        //ResourceResolver resourceResolver = request.getResourceResolver();
        //Resource resource = request.getResource();
        //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        //Page currentPage = pageManager.getContainingPage(resource);
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        //String defaultPath = (String) contentObject.get(DEFAULT_PATH);
        //defaultPath = (null == defaultPath) ? "" : defaultPath;
        //Calendar today = Calendar.getInstance();
        Calendar startDate = (Calendar) contentObject.get(START_DATE);
        if (null != startDate) {
            startDate.set(HOUR_OF_DAY, 0);
            startDate.set(MINUTE, 0);
        }
        Calendar endDate = (Calendar) contentObject.get(END_DATE);
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
    protected String getPeriodicalPagePath(final String day, final Map<String, Object> contentObject){
        String periodicalPagePath = getDefaultPath(contentObject);
        DateTime requiredDate = getDate(day, contentObject);
        DateTime startDate = getDate(START_DATE, contentObject);
        DateTime endDate = getDate(END_DATE, contentObject);
        if (DateUtils.isDateBetween(requiredDate, startDate, endDate)){
            //TODO fix; if today and start date are in different years, it will break
            int index = Days.daysBetween(startDate, today).getDays();

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
        return "";//periodicalPagePath;
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
     *
     * @param day
     * @param contentObject
     * @return
     */
    protected String getDailyContentPath(final String day, final Map<String, Object> contentObject){
        String dailyContentPath = "";
        if (null == contentObject.get(DISPLAY_PERIODICALLY_KEY)){
            DateTime date = getDate(day, contentObject);
            dailyContentPath = getDatePagePath(date, getCurrentPage(contentObject));
        } else {
            dailyContentPath = getPeriodicalPagePath(day, contentObject);
        }
        return dailyContentPath;
    }


    /**
     *
     * @param which
     * @param contentObject
     * @return
     */
    protected DateTime getDate(final String which, final Map<String, Object> contentObject){
        DateTime date;
        if (START_DATE.equals(which)) {
            date = new DateTime(contentObject.get(START_DATE));
        } else if (END_DATE.equals(which)) {
            date = new DateTime(contentObject.get(END_DATE));
            date.withHourOfDay(23);
            date.withMinuteOfHour(59);
            date.withSecondOfMinute(59);
        }else if (YESTERDAY.equals(which)) {
            date = new DateTime();
            date = date.minusDays(1);
        } else if (TOMORROW.equals(which)) {
            date = new DateTime();
            date = date.plusDays(1);
        } else{
            date = new DateTime();
        }
        return date;
    }

    /**
     *
     * @param contentObject
     * @return
     */
    private String getDefaultPath(final Map<String, Object> contentObject) {
        String defaultPath = (String) contentObject.get(DEFAULT_PATH);
        return (null != defaultPath) ? defaultPath : "";
    }

    /**
     *
     * @param contentObject
     * @return
     */
    private Page getCurrentPage(final Map<String, Object> contentObject) {
        Page currentPage = null;
        String currentResourcePath = (String) contentObject.get(PATH);
        if(null != currentResourcePath){
            currentPage = getContainingPage(currentResourcePath);
        }
        return currentPage;
    }

    /**
     *
     * @param componentResourcePath
     * @return
     */
    private Page getContainingPage(String componentResourcePath){
        Page containingPage = null;
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            Resource componentResource = resourceResolver.getResource(componentResourcePath);
            if (null != componentResource) {
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                containingPage = pageManager.getContainingPage(componentResource);
            }
        } catch (LoginException e) {
            log.error("Error logging in. {}", e);
            e.printStackTrace();
        } finally {
            if (null != resourceResolver){
                resourceResolver.close();
            }
        }
        return containingPage;
    }

    @Override
    protected boolean mustExist() {
        return false;
    }
}
