package org.cru.test;

import com.xumak.base.templatingsupport.TemplateContentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* MockTemplateContentModel
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/09/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
public class MockTemplateContentModel extends TemplateContentModel {

   /**
    * Constructor.
    * @param request
    * @param response
    */
    public MockTemplateContentModel(final SlingHttpServletRequest request, final SlingHttpServletResponse response){
        super(request, response);
    }


}
