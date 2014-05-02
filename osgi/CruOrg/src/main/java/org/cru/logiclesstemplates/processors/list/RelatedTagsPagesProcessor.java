package org.cru.logiclesstemplates.processors.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.cru.util.PublishDateUtils;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Sets;
import com.xumak.base.templatingsupport.TemplateContentModel;
import com.xumak.extended.contextprocessors.lists.AddQueriedPagePathListContextProcessor;


@Component
@Service
public class RelatedTagsPagesProcessor extends AddQueriedPagePathListContextProcessor {

    public static final String XUMAK_TAG_NAV_LIST_RESOURCE_TYPE = "CruOrgApp/components/section/related-stories";
    public static final String PATH_LIST_CONTEXT_PROPERTY_NAME = "list.paths";
    public static final String PATHREF_CONTENT_KEY_NAME = "content.pathRef";
    public static final String MAX_CONTENT_KEY_NAME = "content.max";

    @Override
    public Set<String> requiredResourceTypes() {
        return Sets.newHashSet(new String[]{XUMAK_TAG_NAV_LIST_RESOURCE_TYPE});
    }

    @Override
    public void process(final SlingHttpServletRequest request, final TemplateContentModel contentModel)throws Exception{
        log.info( "proccessing component...");
        Collection<String> pathList = new ArrayList();
        Resource resource = request.getResource();
        ResourceResolver resourceResolver = request.getResourceResolver();

        PageManager pageManager = (PageManager) resourceResolver.adaptTo(PageManager.class);

        //get actual page path
        Page currentPage = pageManager.getContainingPage(resource).adaptTo( Page.class );

        if ( true ) {
            //get PathReference
            String pathRef = contentModel.getAsString(PATHREF_CONTENT_KEY_NAME);
            //get Tags
            Tag[] tags = currentPage.getTags();
            ArrayList<String> ids = new ArrayList<String>();
            log.info( "looking for tags...");
            for ( Tag tag: tags){
                log.info( "found: " + tag.getName());
                ids.add( tag.getTagID() );
            }
            List<String> tagsList = ids; //contentModel.getAs(QUERY_CONTENT_KEY_NAME , List.class);
            RangeIterator<Resource> pages = findByTags(resource, pathRef, tagsList);
            if (pages != null) {
                //get max number of items
                long max = getMaxNumber(contentModel, pages.getSize());
                log.info( "max size allowed:" + max );
                log.info( "pages size: " + pages.getSize() );
                pathList = PublishDateUtils.getSortedPathList(pages, resource, max);
                log.info( "pages after sort : " + pathList.size() );
            }
        }
        log.info( "storing " + pathList.size() + " paths");
        int modular = pathList.size() % 3;

        if ( modular == 2 ){

             String path = (String) ((ArrayList) pathList).get( pathList.size() - 2 );
             log.info( "pat: " + path );
             Page p = resourceResolver.getResource( path ).adaptTo( Page.class );
             contentModel.set( "content.page21path", p.getPath());
             contentModel.set( "content.page21title", p.getTitle());
             contentModel.set( "content.page21description", p.getDescription());

            String path2 = (String) ((ArrayList) pathList).get( pathList.size() - 1 );
            log.info( "pat2: " + path2 );
            Page p2 = resourceResolver.getResource( path2 ).adaptTo( Page.class );
            contentModel.set( "content.page22path", p2.getPath());
            contentModel.set( "content.page22title", p2.getTitle());
            contentModel.set( "content.page22description", p2.getDescription());

            ((ArrayList) pathList).remove( pathList.size() - 1 );
            ((ArrayList) pathList).remove( pathList.size() - 1 );

        } else  if ( modular == 1 ){
            String path = (String) ((ArrayList) pathList).get( pathList.size() - 1 );
            log.info( "pat2: " + path );
            Page p = resourceResolver.getResource( path ).adaptTo( Page.class );
            contentModel.set( "content.page1path", p.getPath());
            contentModel.set( "content.page1title", p.getTitle());
            contentModel.set( "content.page1description", p.getDescription());
            ((ArrayList) pathList).remove( pathList.size() - 1 );
        }

        contentModel.set(PATH_LIST_CONTEXT_PROPERTY_NAME, pathList);
    }


    /**
     * get Iterator of resources based in list of tags.
     * @param  resource
     * @param pathRef path of reference.
     * @param tagsList list of tags.
     * @return ArrayList
     */
    private RangeIterator<Resource> findByTags(final Resource resource, final String pathRef,
                                               final List<String> tagsList) {
        log.info( "1" );
        RangeIterator<Resource> pages = null;
        log.info( "2" );
        if (resource != null) {
            log.info( "3" );
            ResourceResolver resourceResolver = resource.getResourceResolver();
            log.info( "4" );
            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
            log.info( "5" );
            if (tagManager != null && StringUtils.isNotBlank(pathRef) && tagsList != null) {
                log.info( "6" );
                String[] tagsArray = tagsList.toArray(new String[tagsList.size()]);
                log.info( "7" );
                if (tagsArray.length > 0) {
                    //find by tag
                    log.info( "length:   " + tagsArray.length );
                    pages = tagManager.find(pathRef, tagsArray, true);
                }
            }
        }
        log.info( "8 " + pages.getSize() );
        return pages;
    }


    /**
     * get Max number of items
     * @param contentModel
     * @param listSize
     * @return Long with max number of items.
     */
    private long getMaxNumber(final TemplateContentModel contentModel, final long listSize)throws Exception{
        long max = 0;
        if (contentModel.has(MAX_CONTENT_KEY_NAME)) {
            max = contentModel.getAs(MAX_CONTENT_KEY_NAME, Long.class);
            //not exist limit
            if (max == 0) {
                max = listSize;
            }
        }
        return max;
    }

}
