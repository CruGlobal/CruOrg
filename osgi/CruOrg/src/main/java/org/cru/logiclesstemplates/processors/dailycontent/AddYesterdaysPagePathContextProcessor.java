package org.cru.logiclesstemplates.processors.dailycontent;

import com.day.cq.wcm.api.Page;
import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.cru.util.PageUtils;

import java.util.Calendar;

import static java.util.Calendar.DATE;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddYesterdaysPagePathContextProcessor
 * -----------------------------------------------------------------------------
 *
 * CHANGE HISTORY
 * -----------------------------------------------------------------------------
 * Version | Date        | Developer              | Changes
 * 1.0     | 2/5/14     | palecio                | Initial Creation
 * -----------------------------------------------------------------------------
 *
  ==============================================================================
 */
@Component
@Service
public class AddYesterdaysPagePathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {

        super.process(request, contentModel);
        contentObject.put(YESTERDAY, getDailyContentPath(YESTERDAY));
    }

}
