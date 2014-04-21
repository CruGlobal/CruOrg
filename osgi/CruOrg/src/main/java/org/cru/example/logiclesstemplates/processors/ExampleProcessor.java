package org.cru.example.logiclesstemplates.processors;

import com.xumak.base.templatingsupport.AbstractResourceTypeCheckContextProcessor;
import com.xumak.base.templatingsupport.ContentModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import java.util.Map;

import static com.xumak.base.Constants.RESOURCE_CONTENT_KEY;


@Component
@Service
public class ExampleProcessor
        extends AbstractResourceTypeCheckContextProcessor<ContentModel> {

    @Override
    public String requiredResourceType() {
        return "CruOrgApp/components/section/titleexample";

    }


    @Override
    public void process(final SlingHttpServletRequest request, final ContentModel contentModel)
            throws Exception {
        Map<String, Object> contentObject =  (Map<String, Object>) contentModel.get(RESOURCE_CONTENT_KEY);
        contentObject.put("exampleText", "This is a text from a processor :)");
    }

}
