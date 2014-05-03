package org.cru.logiclesstemplates.processors.dailycontent;

import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

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

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {

        super.process(request, contentModel);
        contentObject.put(TOMORROW, getDailyContentPath(TOMORROW));

    }

}
