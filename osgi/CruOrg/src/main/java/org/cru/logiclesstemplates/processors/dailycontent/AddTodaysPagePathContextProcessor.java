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

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;
import static java.util.Calendar.DATE;

/* DESCRIPTION
 * -----------------------------------------------------------------------------
 * AddTodaysPagePathContextProcessor
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
public class AddTodaysPagePathContextProcessor extends AbstractAddDailyContentPagePathContextProcessor {

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {

        super.process(request, contentModel);
        contentObject.put(TODAY, getDailyContentPath(TODAY));

    }

}
