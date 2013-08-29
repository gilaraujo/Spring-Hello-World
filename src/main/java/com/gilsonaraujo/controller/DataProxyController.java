package com.gilsonaraujo.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 
 * This controller only returns an example string. It is meant to be as light-weight as possible.
 * 
 * @author gilson.araujo
 */

@Controller
public class DataProxyController {
    private static final Logger LOG = LoggerFactory.getLogger(DataProxyController.class);

    private static final String TEST_PAGE = "testPage";
    private static final String TEST_STRING = "testString";

    @RequestMapping(value = TEST_STRING, method = RequestMethod.GET)
    @ResponseBody
    public String requestTestString() {
        String html = "Test OK.";
        return html;
    }
    @RequestMapping(value = TEST_PAGE, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView requestTestPage() {
        ModelAndView modelAndView = new ModelAndView();
        //TODO: improve this example
        modelAndView.setViewName("pages/hello.jsp");
        modelAndView.addObject("test01", "test01 OK");
        return modelAndView;
    }
}
