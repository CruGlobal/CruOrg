package org.cru.logiclesstemplates.processors.dailycontent;

import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;


import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;


/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddTodaysPagePathContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 14/05/02    | palecio                | Initial Creation
 * 1.0     | 14/05/13    | palecio                | Added Joda Time, refactoring
 *
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddTodaysPagePathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {


    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        Map<String, Object> contentObject = (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        contentObject.put(TODAY, getDailyContentPath(TODAY, contentObject));

    }

}
