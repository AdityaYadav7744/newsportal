package com.bhaska.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/createnode",
                "sling.servlet.methods=GET"
        }

)
public class CreateNodeUnderContent extends SlingAllMethodsServlet {

    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
      ResourceResolver resourceResolver= request.getResourceResolver();
      try{
           Resource metadataResource = resourceResolver.getResource("/content/metadata");
           if(metadataResource==null){
               Map<String , Object> map=new HashMap<>();
               map.put("jcr:primaryType","nt:unstructured");
               metadataResource= resourceResolver.create(resourceResolver.getResource("/content"),"metadata",map);
           }

           for(int i =1 ;i<=100 ; i++){
               String nodeName="employee"+i;
               if(metadataResource.getChild(nodeName)==null){
                   Map<String , Object> map=new HashMap<>();
                   map.put("jcr:primaryType", "nt:unstructured");
                   map.put("id","EMP"+i);
                   map.put("name","Employee"+i);

                   resourceResolver.create(metadataResource,nodeName,map);

               }
           }
          resourceResolver.commit();
          response.getWriter().write("100  nodes are created under the content/metadata");
      }
      catch (Exception e){
        response.getWriter().write(e.getMessage());
      }
    }
}
