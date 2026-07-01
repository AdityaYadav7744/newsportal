package com.bhaska.newsportal.core.servlets;

import com.bhaska.newsportal.core.service.BlogMigrationService;
import org.apache.poi.ss.usermodel.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/migration/upload",
                "sling.servlet.methods=POST"
        }
)
public class MigrationServlet extends SlingAllMethodsServlet {

    @Reference
    private BlogMigrationService blogMigrationService;

    @Override
    protected void doPost(SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
         RequestParameter file = request.getRequestParameter("file");

            if(file==null){
                response.getWriter().write("No file is Uploaded");
                return;
            }
            String fileName = file.getFileName();
            response.getWriter().write("File get successfully");

       try(InputStream inputStream = file.getInputStream()){
            Set<String> urls = getUrls(inputStream);
            blogMigrationService.blogMigrationSerive(urls);
            response.getWriter().write(urls.toString());
       }
       catch (Exception e){
        e.printStackTrace();
       }
    }

    private Set<String> getUrls(InputStream inputstreem){
        Set<String> list=new HashSet<>();
        try(Workbook workbook = WorkbookFactory.create(inputstreem)){
             Sheet sheet = workbook.getSheetAt(0);
             for(int i=0 ; i<=sheet.getLastRowNum();i++){
                Row row=sheet.getRow(i);
                if (row==null){
                    continue;
                }

                  Cell cell = row.getCell(2);
                    if (cell==null){
                        continue;
                    }
                 cell.setCellType(CellType.STRING);
                  String url = cell.getStringCellValue();
                 if (url != null && !url.trim().isEmpty()) {
                     list.add(url.trim());
                 }
             }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    return list;
    }
}
