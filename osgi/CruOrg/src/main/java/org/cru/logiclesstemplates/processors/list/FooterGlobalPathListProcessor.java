package org.cru.logiclesstemplates.processors.list;


import java.util.*;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.xumak.base.Constants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor;
import org.apache.sling.api.resource.ResourceResolver;

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
            contentModel.set("list.titles", buildPageTitles(request, pathList));
        }
    }

    private Collection<String> buildPathList(final Object pathRefs) {
        Collection<String> pathList = new ArrayList();
        if ((pathRefs instanceof Collection)) {
            for (String pathRef : (Collection<String>) pathRefs) {
                pathList.add(pathRef);
            }
        } else if (pathRefs.getClass().isArray()) {
            String[] pathReftsArr = (String[]) pathRefs;
            pathList.addAll(Arrays.asList(pathReftsArr));
        } else {
            pathList.add((String) pathRefs);
        }
        return pathList;
    }

    private Collection<Map<String, Object>> buildPageTitles(
        final SlingHttpServletRequest request,
        final Collection<String> pathList) {
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Collection<Map<String, Object>> pageTitles = new ArrayList();
        for (String path : pathList){
            Map<String, Object> pageDetails = new HashMap();
            Page page = pageManager.getPage(path);

            if (page != null){
                pageDetails.put("title", page.getTitle());
                pageDetails.put("path", path);
                pageTitles.add(pageDetails);
            }
        }
        return pageTitles;
    }

    @Override
    protected boolean mustExist() {
        return false;
    }

    @Override
    public int priority() {
        return Constants.HIGHER_PRIORITY;
    }
}
