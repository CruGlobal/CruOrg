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

import javax.jcr.Node;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;
import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static java.util.Calendar.DATE;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddDailyContentPagesContextProcessor
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
public class AbstractAddDailyContentPagePathContextProcessor extends AbstractResourceTypeCheckContextProcessor<TemplateContentModel>{

    public static final String DAILY_CONTENT_RESOURCE_TYPE = "CruOrgApp/components/section/daily-content";
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
    protected Calendar today = Calendar.getInstance();
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
        startDate = (Calendar) contentObject.get(START_DATE);
        endDate = (Calendar) contentObject.get(END_DATE);
        endDate.add(DATE, 1);
    }

    protected String getPeriodicalPagePath(String day){
        String periodicalPagePath = defaultPath;
        if(DateUtils.isDateBetween(today, startDate, endDate)){
            int index = today.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);

            if(YESTERDAY.equals(day)){
                index--;
            }else if(TOMORROW.equals(day)){
                index++;
            }

            Page periodicalPage = PageUtils.getPage(currentPage, index);
            if(null != periodicalPage){
                periodicalPagePath = periodicalPage.getPath();
            }
        }
        return periodicalPagePath;
    }

    protected String getDatePagePath(Calendar date){
        Page page = PageUtils.getPageFromDate(currentPage, date);
        return (null != page) ? page.getPath() : "";
    }

    protected String getDailyContentPath(String dateString){
        String dailyContentPath = "";
        if(null == contentObject.get(DISPLAY_PERIODICALLY_KEY)){
            Calendar date = getDate(dateString);
            dailyContentPath = getDatePagePath(date);
        }else{
            dailyContentPath = getPeriodicalPagePath(dateString);
        }
        return dailyContentPath;
    }

    protected Calendar getDate(String dateString){
        Calendar date = Calendar.getInstance();
        if(YESTERDAY.equals(dateString)) {
            date.add(DATE, -1);
        }else if(TOMORROW.equals(dateString)) {
            date.add(DATE, 1);
        }
        return date;
    }
    @Override
    protected boolean mustExist() {
        return false;
    }
}
