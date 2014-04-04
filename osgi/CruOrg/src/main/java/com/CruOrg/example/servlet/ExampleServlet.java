package com.CruOrg.example.servlet;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: antonio
 * Date: 10/8/13
 * Time: 8:36 AM
 * To change this template use File | Settings | File Templates.
 */
@SlingServlet(
        label = "Example Servlet",
        paths = { "/bin/bedrock/example" },
        methods = { "GET" },
        extensions = { "html" },
        metatype = false
)
public class ExampleServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -1749870401142597298L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleServlet.class);
    @Override
    protected void doGet(
            final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException,
            IOException {
        doPost(request,response);
    }


    @Override
    protected void doPost(
            final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException,
            IOException {
        LOGGER.info("Executing test servlet");
        response.getWriter().write("this is a test servlet.");
    }

}
