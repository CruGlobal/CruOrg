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
 * 1.0     | 14/05/02    | palecio                | Initial Creation
 * 1.0     | 14/05/13    | palecio                | Added Joda Time, refactoring
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
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        String tomorrowsPagePath = getDailyContentPath(TOMORROW, contentObject);
        contentObject.put(TOMORROW, tomorrowsPagePath);
        //IS_TOMORROW_DEFAULT_PATH lets us know if the path obtained for 'tomorrow' is the default path
        //if it is, it means we're no longer within the period specified in the daily content component,
        //so we set this flag to let the code on the client side we no longer need to display a 'next' button
        contentObject.put(IS_TOMORROW_DEFAULT_PATH, getDefaultPath(contentObject).equals(tomorrowsPagePath));

    }

}
