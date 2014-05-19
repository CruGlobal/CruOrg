package org.cru.logiclesstemplates.processors.dailycontent;

import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddYesterdaysPagePathContextProcessor
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
public class AddYesterdaysPagePathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {

    public static final String IS_YESTERDAY_DEFAULT_PATH = "isYesterdayDefaultPath";

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        contentObject.put(CURRENT_RESOURCE_KEY, request.getResource());

        String yesterdaysPagePath =  getDailyContentPath(YESTERDAY, contentObject);

        contentObject.put(YESTERDAY, request.getResourceResolver().map(yesterdaysPagePath));
        //IS_YESTERDAY_DEFAULT_PATH lets us know if the path obtained for 'yesterday' is the default path
        //if it is, it means yesterday we were not within the period specified in the daily content component,
        //so we set this flag to let the code on the client side we no longer need to display a 'previous' button
        contentObject.put(IS_YESTERDAY_DEFAULT_PATH, getDefaultPath(contentObject).equals(yesterdaysPagePath));
    }

}
