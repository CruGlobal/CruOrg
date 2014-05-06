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
import java.io.PrintWriter;
import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static com.xumak.base.templatingsupport.TemplatingSupportFilter.TEMPLATE_CONTENT_MODEL_ATTR_NAME;
import static org.cru.logiclesstemplates.processors.dailycontent.AbstractAddDailyContentPagePathContextProcessor.*;
import static org.cru.logiclesstemplates.processors.dailycontent.AddTodaysPagePathContextProcessor.SERVLET_PATH_KEY;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * DailyContentServlet
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
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        TemplateContentModel contentModel =
                (TemplateContentModel) request.getAttribute(TEMPLATE_CONTENT_MODEL_ATTR_NAME);
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);

        try {
            jsonObject.put(TODAY, contentObject.get(TODAY));
            jsonObject.put(SERVLET_PATH_KEY, contentObject.get(SERVLET_PATH_KEY));
            jsonObject.put(YESTERDAY, contentObject.get(YESTERDAY));
            jsonObject.put(TOMORROW, contentObject.get(TOMORROW));
            out.print(jsonObject.toString());
        } catch (JSONException e) {
            throw new ServletException(e);
        }
    }
}
