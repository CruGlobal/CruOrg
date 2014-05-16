package org.cru.servlets;

import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.cru.logiclesstemplates.processors.dailycontent.AbstractAddDailyContentPagePathContextProcessor;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static com.xumak.base.templatingsupport.TemplatingSupportFilter.TEMPLATE_CONTENT_MODEL_ATTR_NAME;
import static org.cru.logiclesstemplates.processors.dailycontent.AbstractAddDailyContentPagePathContextProcessor.*;
import static org.cru.logiclesstemplates.processors.dailycontent.AddContentServletPathContextProcessor.SERVLET_PATH_KEY;
import static org.cru.logiclesstemplates.processors.dailycontent.AddTomorrowsPagePathContextProcessor.IS_TOMORROW_DEFAULT_PATH;
import static org.cru.logiclesstemplates.processors.dailycontent.AddYesterdaysPagePathContextProcessor.IS_YESTERDAY_DEFAULT_PATH;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * DailyContentServlet
 *
 * Returns a json with the following properties:
 * today -> the path to today's page
 * yesterday -> the path to yesterday's page
 * tomorrow -> the path to tomorrow's page
 * servletPath -> the path to today's content servlet
 * isYesterdayDefaultPath -> true if yesterday's path contains the default path
 * isTomorrowDefaultPath -> true if tomorrow's path contains the default path
 *
 * -----------------------------------------------------------------------------
 * 
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 29/4/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
@Properties({
        @Property(name = "service.description", value = "Daily Content component Servlet"),
        @Property(name = "sling.servlet.selectors", value = "pages"),
        @Property(name = "sling.servlet.extensions", value = "json"),
        @Property(name = "sling.servlet.resourceTypes",
                value = {AbstractAddDailyContentPagePathContextProcessor.DAILY_CONTENT_RESOURCE_TYPE})
})
public class DailyContentServlet
        extends SlingSafeMethodsServlet {

     @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JSONObject jsonObject = new JSONObject();
        TemplateContentModel contentModel =
                (TemplateContentModel) request.getAttribute(TEMPLATE_CONTENT_MODEL_ATTR_NAME);
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        try {//fill the json object
            jsonObject.put(TODAY, contentObject.get(TODAY));
            jsonObject.put(SERVLET_PATH_KEY, contentObject.get(SERVLET_PATH_KEY));
            jsonObject.put(YESTERDAY, contentObject.get(YESTERDAY));
            jsonObject.put(TOMORROW, contentObject.get(TOMORROW));
            jsonObject.put(IS_YESTERDAY_DEFAULT_PATH, contentObject.get(IS_YESTERDAY_DEFAULT_PATH));
            jsonObject.put(IS_TOMORROW_DEFAULT_PATH, contentObject.get(IS_TOMORROW_DEFAULT_PATH));
            response.getWriter().print(jsonObject.toString()); //print the json object
        } catch (JSONException e) {
            throw new ServletException(e);
        }
    }
}
