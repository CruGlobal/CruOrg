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

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static com.xumak.base.Constants.GLOBAL_PAGE_CONTENT_KEY;
import static com.xumak.base.Constants.IMAGE_PATH;
import static com.xumak.base.Constants.TITLE;
import static com.xumak.base.templatingsupport.TemplatingSupportFilter.TEMPLATE_CONTENT_MODEL_ATTR_NAME;

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
       @Property(name = "service.description", value = "returns the page's image path."),
       @Property(name = "sling.servlet.selectors", value = "content"),
       @Property(name = "sling.servlet.extensions", value = "json"),
       @Property(name = "sling.servlet.resourceTypes",
               value = {ArticleContentServlet.ARTICLE_PAGE_RESOURCE_TYPE})
})
public class ArticleContentServlet
       extends SlingSafeMethodsServlet {

   protected final static String ARTICLE_PAGE_RESOURCE_TYPE = "CruOrgApp/components/page/article";
   protected final static String SUBTITLE = "subtitle";
   protected final static String DATE_TEXT = "dateText";
   protected final static String TWITTER_USER = "twitterUser";
   protected final static String CAPTION = "imageCaption";
   protected final static String CREDIT = "imageCredit";
   protected final static String AUTHOR = "author";

    @Override
   protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
           throws ServletException, IOException {
       response.setContentType("application/json");
       PrintWriter out = response.getWriter();
       JSONObject jsonObject = new JSONObject();
       TemplateContentModel contentModel =
               (TemplateContentModel) request.getAttribute(TEMPLATE_CONTENT_MODEL_ATTR_NAME);
       //Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
       Map<String, Object> pageObject = (Map<String, Object>) contentModel.get(GLOBAL_PAGE_CONTENT_KEY);

       try {
           jsonObject.put(IMAGE_PATH, pageObject.get(IMAGE_PATH));
           jsonObject.put(TITLE, pageObject.get(TITLE));
           jsonObject.put(SUBTITLE, pageObject.get(SUBTITLE));
           jsonObject.put(AUTHOR, pageObject.get(AUTHOR));
           jsonObject.put(DATE_TEXT, pageObject.get(DATE_TEXT));
           jsonObject.put(TWITTER_USER, pageObject.get(TWITTER_USER));
           jsonObject.put(CAPTION, pageObject.get(CAPTION));
           jsonObject.put(CREDIT, pageObject.get(CREDIT));

           out.write(jsonObject.toString());
       } catch (JSONException e) {
           throw new ServletException(e);
       }
   }
}
