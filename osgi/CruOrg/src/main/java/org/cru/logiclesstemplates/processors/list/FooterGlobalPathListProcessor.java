package org.cru.logiclesstemplates.processors.list;


import java.util.*;


import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor;

@Component
@Service
public class FooterGlobalPathListProcessor extends AddPagePathListContextProcessor {

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet("CruOrgApp/components/section/footer");
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {
        String pathRefsProp = "global.pathRefs";
        if (contentModel.has(pathRefsProp)) {
            Object pathRefs = contentModel.get(pathRefsProp);
            Collection<String> pathList = buildPathList(pathRefs);
            contentModel.set("list.paths", pathList);
        }
    }

    private Collection<String> buildPathList(final Object pathRefs) {
        Collection<String> pathList = new ArrayList();
       if (pathRefs.getClass().isArray()) {
            String[] pathReftsArr = (String[]) pathRefs;
            pathList.addAll(Arrays.asList(pathReftsArr));
       } else {
            pathList.add((String) pathRefs);
       }
       return pathList;
    }

    @Override
    protected boolean mustExist() {
        return false;
    }


}