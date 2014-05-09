package org.cru.util;

import org.apache.commons.lang.StringUtils;
import com.day.jcr.vault.util.JcrConstants;

public class Constants {
    public static final String AUTHOR = "author";
    public static final String TWITTER_USER = "twitterUser";
    public static final String TEXT_PROPERTY = "./text";
    public static final String DOT_SEPARATOR = ".";
    public static final String COMMA_SEPARATOR = ",";
    public static final String SEARCH_IN_PROPERTIES = StringUtils.join(new String[]{
            JcrConstants.JCR_TITLE,
            JcrConstants.JCR_DESCRIPTION,
            TEXT_PROPERTY,
            AUTHOR,
            TWITTER_USER
    }, COMMA_SEPARATOR);
    public static final String LAST_PUBLISHED_KEY = "lastPublished";
    public static final String DATE_RANGE_KEY = "daterange";
    public static final String PROPERTY_KEY = "property";
    public static final String DATE_PROPERTY = "/date";
    public static final String QUERY_PARAMETER = "Query";
    public static final String START_PARAMETER = "start";
    public static final String PAGE_NUMBER_KEY = "pageNumber";
    public static final String URL_KEY = "URL";
    public static final String DEFAULT_CURRENT_PAGE_NAME = "currentPage";
}
