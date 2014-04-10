package com.CruOrg.example.logiclesstemplates.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor;

@Component
@Service
public class FooterGlobalPathListProcessor extends AddPagePathListContextProcessor {
    public FooterGlobalPathListProcessor() {}

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{"CruOrgApp/components/section/footer"});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        String pathRefsProp = "global.pathRefs";
        if (contentModel.has(pathRefsProp)) {
            Object pathRefs = contentModel.get(pathRefsProp);

            Collection<String> pathList = new ArrayList();
            if ((pathRefs instanceof Collection)) {
                for (String pathRef : (Collection<String>)pathRefs) {
                    pathList.add(pathRef);
                }
            } else if (pathRefs.getClass().isArray()) {
                pathList = Arrays.asList((String[]) pathRefs);
            } else {
                pathList.add((String)pathRefs);
            }
            contentModel.set("list.paths", pathList);
        }
    }
}
