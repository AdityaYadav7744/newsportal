package com.bhaska.newsportal.core.workflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Iterator;
import javax.jcr.RangeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageUnPublishWorkflowTest {

    private PageUnPublishWorkflow workflow;

    private WorkItem workItem;
    private Workflow workflowInstance;
    private WorkflowSession workflowSession;
    private WorkflowData workflowData;
    private MetaDataMap workflowMetadataMap;
    private MetaDataMap argumentMetadataMap;

    private ResourceResolver resolver;
    private Resource resource;

    private LiveRelationshipManager liveRelationshipManager;
    private RangeIterator rangeIterator;
    private LiveRelationship liveRelationship1;

    private static final String PAYLOAD_PATH = "/content/newsportal/us/en/article";

    @BeforeEach
    void setUp() throws Exception {
        workflow = new PageUnPublishWorkflow();

        workItem = mock(WorkItem.class);
        workflowInstance = mock(Workflow.class);
        workflowSession = mock(WorkflowSession.class);
        workflowData = mock(WorkflowData.class);
        workflowMetadataMap = mock(MetaDataMap.class);
        argumentMetadataMap = mock(MetaDataMap.class);

        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);

        liveRelationshipManager = mock(LiveRelationshipManager.class);
        rangeIterator = mock(RangeIterator.class);
        liveRelationship1 = mock(LiveRelationship.class);

        // Inject reference mock via reflection
        java.lang.reflect.Field field = PageUnPublishWorkflow.class.getDeclaredField("liveRelationshipManager");
        field.setAccessible(true);
        field.set(workflow, liveRelationshipManager);

        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);

        when(workItem.getWorkflow()).thenReturn(workflowInstance);
        when(workflowInstance.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getMetaDataMap()).thenReturn(workflowMetadataMap);

        Session mockSession = mock(Session.class);
        Workspace mockWorkspace = mock(Workspace.class);
        QueryManager mockQueryManager = mock(QueryManager.class);
        Query mockQuery = mock(Query.class);
        QueryResult mockQueryResult = mock(QueryResult.class);

        when(resolver.adaptTo(Session.class)).thenReturn(mockSession);
        when(mockSession.getWorkspace()).thenReturn(mockWorkspace);
        when(mockWorkspace.getQueryManager()).thenReturn(mockQueryManager);
        when(mockQueryManager.createQuery(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.execute()).thenReturn(mockQueryResult);
        when(mockQueryResult.getNodes()).thenReturn(mock(javax.jcr.NodeIterator.class));
    }



    @Test
    void testExecute_ResourceResolverNull() {
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(null);

        assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, argumentMetadataMap));
    }

    @Test
    void testExecute_ResourceNull() {
        when(resolver.getResource(PAYLOAD_PATH)).thenReturn(null);

        assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, argumentMetadataMap));
    }


}