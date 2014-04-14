package org.cru.logiclesstemplates.processors;

/**
 * Created with IntelliJ IDEA.
 * User: jurizar
 * Date: 4/11/14
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.*;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor;
import org.apache.sling.api.resource.Resource;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

@Component
@Service
public class MainSliderProcessor extends AddPagePathListContextProcessor {
    public MainSliderProcessor() {
    }

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{"/apps/CruOrgApp/components/section/main-slider"});
    }

    @Override
    public boolean accepts( final SlingHttpServletRequest request ){
        return request.getResource().getResourceType().endsWith( "CruOrgApp/components/section/main-slider") ;

    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)
            throws Exception {

        Collection<String> pathList = new ArrayList();
        Resource resource = request.getResource();

        if ( resource != null && resource.adaptTo( Node.class ) != null &&
                resource.adaptTo( Node.class).hasProperty("pathRefs") ) {

            Property p = request.getResource().adaptTo( Node.class ).getProperty("pathRefs");
            if( p.isMultiple() ){
                Value[] pathListValues = p.getValues();

                for( Value val : pathListValues ){
                    pathList.add( val.getString() );
                }
            }

            Collection<Map<String, String>> pages = new ArrayList();


            for( String path : pathList ){
                Resource r = request.getResourceResolver().getResource( path );
                if( r != null ){
                    Page page = r.adaptTo( Page.class  );
                    pages.add( extractNavigationDetails( page ));
                }
            }

            contentModel.set("list.pages",  pages );
        }
    }

    private Map<String, String> extractNavigationDetails(final Page page) throws Exception {
        Map pageDetails = new HashMap();

        if(page != null) {
            String title = "";
            if(StringUtils.isNotBlank(page.getNavigationTitle())) {
                title = page.getNavigationTitle();
            } else if (StringUtils.isNotBlank(page.getPageTitle())) {
                title = page.getPageTitle();
            } else {
                title = page.getTitle();
            }
            pageDetails.put("title", title);
            pageDetails.put("path", page.getPath());
            pageDetails.put("description", page.getProperties().get( "jcr:description", "" ));
            pageDetails.put("image", page.getProperties().get( "image/fileReference", "" ));
        }

        return pageDetails;
    }
}
