package org.cru.logiclesstemplates.processors.list;

import com.xumak.base.templatingsupport.TemplateContentModel;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import static com.xumak.base.templatingsupport.TemplateContentModel.*;
import mockit.Expectations;
import org.apache.commons.collections.map.HashedMap;
import mockit.NonStrictExpectations;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATHREFS_LIST_GLOBAL_CONTENT_KEY;
*/

import java.lang.reflect.Field;
import java.util.*;
import static com.xumak.extended.contextprocessors.lists.AddPagePathListContextProcessor.PATH_LIST_CONTEXT_PROPERTY_NAME;


/**
 * Created with IntelliJ IDEA.
 * User: jlfp
 * Date: 5/7/14
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(JMockit.class)
public class FooterGlobalPathListProcessorTest {
    private static final Logger log = LoggerFactory.getLogger(FooterGlobalPathListProcessorTest.class);


    @Reference
    FooterGlobalPathListProcessor footerGlobalPathListProcessor;


    @Test
    public void testProcess(@Mocked final TemplateContentModel contentModel) throws Exception {

        MockSlingHttpServletRequest mockSlingHttpServletRequest = new MockSlingHttpServletRequest("", "", "", "", "");


        Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
        TemplateContentModel contentM1 = (TemplateContentModel) objenesis.newInstance(TemplateContentModel.class);


        Field aField = contentM1.getClass().getDeclaredField("currentContext");


        log.error("Template Content Model distinct to null ->" + (contentM1 != null));

        log.error("Template Content Model set ->" + (contentM1.set("hola", "hola") != null));
        log.error("Template Content Model get ->" + contentM1.toJSONString());

        log.error("Template Content Model distinct to null ->" + (contentM1 != null));


        //prepare pathrefs
        final ArrayList<String> pathrefs = new ArrayList<String>();
        pathrefs.add("/content/geometrixx/en");
        pathrefs.add("/content/geometrixx/fr");
      /*
        new NonStrictExpectations() {{

            contentModel.has(PATHREFS_LIST_GLOBAL_CONTENT_KEY);
            result = true;

            contentModel.get(PATHREFS_LIST_GLOBAL_CONTENT_KEY);
            result = "/content/geometrixx/en,/content/geometrixx/fr";


            contentModel.get(PATH_LIST_CONTEXT_PROPERTY_NAME);
            result = "asdkjaslkdj";




        }};   */

       // log.error("content Model is Null " + (contentModel != null));

        log.error("content " + (contentModel.get(PATH_LIST_CONTEXT_PROPERTY_NAME) != null));

      /*  List<String> conten = contentModel.getAs(PATH_LIST_CONTEXT_PROPERTY_NAME, List.class);
        for (String contents: conten) {
            log.error("+++++ contents" + contents);
        }  */


    }




}
