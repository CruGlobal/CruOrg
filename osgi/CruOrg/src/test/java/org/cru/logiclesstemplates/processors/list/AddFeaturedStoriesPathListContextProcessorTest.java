package org.cru.logiclesstemplates.processors.list;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.testing.sling.MockResource;
import org.apache.sling.commons.testing.sling.MockResourceResolver;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.cru.test.MockTemplateContentModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static com.xumak.base.Constants.HTML;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATHREFS_CONTENT_KEY_NAME;
import static org.cru.logiclesstemplates.processors.list.AddFeaturedStoriesPathListContextProcessor.*;
import static org.junit.Assert.assertEquals;

/*
* DESCRIPTION
* -----------------------------------------------------------------------------
* 
* -----------------------------------------------------------------------------
*
* CHANGE HISTORY
* -----------------------------------------------------------------------------
* Version | Date        | Developer             | Changes
* 1.0     | 5/15/14     | JFlores               | Initial Creation
* -----------------------------------------------------------------------------
*
==============================================================================
*/
@RunWith(MockitoJUnitRunner.class)
public class AddFeaturedStoriesPathListContextProcessorTest {


    @InjectMocks private AddFeaturedStoriesPathListContextProcessor featuredStoriesPathList;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcess() throws Exception {
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest("/", null, HTML, null, null);
        SlingHttpServletResponse response = Mockito.mock(SlingHttpServletResponse.class);
        MockTemplateContentModel contentModel = new MockTemplateContentModel(request, response);
        MockResourceResolver resolver = new MockResourceResolver();

        //prepare data
        contentModel.set(GLOBAL_HOMEPAGE_PATH_PROPERTY_NAME, "/test");
        MockResource resource = new MockResource(resolver, "/test/jcr:content/main-slider", "cq:Page");
        ArrayList<String>  compareSliderPathArray = new ArrayList<String>();

        /*
        * CASE 0:
        * array = empty;
        */
        resolver.addResource(resource);
        request.setResourceResolver(resolver);
        featuredStoriesPathList.process(request, contentModel);
        assertEquals(compareSliderPathArray, contentModel.get(PATH_LIST_CONTEXT_PROPERTY_NAME));


        /*
        * CASE 1:
        * array.length = FEATURED_STORIES_MAX_ITEMS
        */
        int maxItems = FEATURED_STORIES_MAX_ITEMS;
        compareSliderPathArray = simulateSliders(FEATURED_STORIES_MAX_ITEMS);
        String[] testSliderPathArray = simulateSliders(maxItems).toArray(new String[maxItems]);
        resource.addProperty(PATHREFS_CONTENT_KEY_NAME, testSliderPathArray);

        featuredStoriesPathList.process(request, contentModel);
        assertEquals(compareSliderPathArray, contentModel.get(PATH_LIST_CONTEXT_PROPERTY_NAME));


        /*
        * CASE 2:
        * array.length > FEATURED_STORIES_MAX_ITEMS
        */
        maxItems = FEATURED_STORIES_MAX_ITEMS + 2;
        testSliderPathArray = simulateSliders(maxItems).toArray(new String[maxItems]);
        resource.addProperty(PATHREFS_CONTENT_KEY_NAME, testSliderPathArray);
        featuredStoriesPathList.process(request, contentModel);
        assertEquals(compareSliderPathArray, contentModel.get(PATH_LIST_CONTEXT_PROPERTY_NAME));


        /*
        * CASE 3:
        * array.length < FEATURED_STORIES_MAX_ITEMS
        */
        maxItems = FEATURED_STORIES_MAX_ITEMS - 1;
        compareSliderPathArray = simulateSliders(maxItems);
        testSliderPathArray = simulateSliders(maxItems).toArray(new String[maxItems]);
        resource.addProperty(PATHREFS_CONTENT_KEY_NAME, testSliderPathArray);
        featuredStoriesPathList.process(request, contentModel);
        assertEquals(compareSliderPathArray, contentModel.get(PATH_LIST_CONTEXT_PROPERTY_NAME));

    }



    /**
     * Fill arrayList of sliders with simulated data.
     * @param elements
     * @return arrayList with sliders.
     */
    private  ArrayList<String>  simulateSliders(final int elements)throws Exception{

        ArrayList<String>  sliders = new ArrayList<String>();
        //fill array with simulated data
        for (int item = 0; item < elements; item++){
           sliders.add("slider_" + item);
        }
        return sliders;
    }


}
