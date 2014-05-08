package org.cru.logiclesstemplates.processors.list;


import com.google.common.collect.Sets;

import com.xumak.extended.contextprocessors.lists.AddListReferencedPagesDetailsContextProcessor;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import static org.cru.logiclesstemplates.processors.list.FooterGlobalPathListProcessor.FOOTER_RESOURCE_TYPE;

/**
 * User: joshuaoransky
 * Date: 3/7
 * Time: 0:28
 * Purpose:
 * Location:
 */
@Component
@Service
public class AddFooterListReferencedPagesDetailsContextProcessor
        extends AddListReferencedPagesDetailsContextProcessor {


    @Override
    public java.util.Set<String> requiredResourceTypes() {
        return Sets.newHashSet(FOOTER_RESOURCE_TYPE);
    }

    @Override
    protected boolean mustExist() {
        return false;
    }

}
