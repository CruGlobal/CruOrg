package org.cru.logiclesstemplates.processors.dailycontent;

import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddTomorrowsPagePathContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2/5/14      | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddTomorrowsPagePathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {

    public static final String IS_TOMORROW_DEFAULT_PATH = "isTomorrowDefaultPath";

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
       /* Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);

        super.process(request, contentModel);
        String tomorrowsPagePath = getDailyContentPath(TOMORROW);
        contentObject.put(TOMORROW, tomorrowsPagePath);
        contentObject.put(IS_TOMORROW_DEFAULT_PATH, defaultPath.equals(tomorrowsPagePath));   */

    }

}
