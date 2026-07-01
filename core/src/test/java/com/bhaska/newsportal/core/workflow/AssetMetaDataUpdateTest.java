package com.bhaska.newsportal.core.workflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

class AssetMetaDataUpdateTest {

    private AssetMetaDataUpdate workflow;

    private WorkItem workItem;
    private WorkflowSession workflowSession;
    private WorkflowData workflowData;
    private MetaDataMap metaDataMap;

    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver resourceResolver;
    private Resource resource;
    private ModifiableValueMap modifiableValueMap;

    private static final String PAYLOAD_PATH = "/content/dam/newsportal/asset.jpg";

    @BeforeEach
    void setUp() throws LoginException {
        workflow = new AssetMetaDataUpdate();

        // Initialize Granite Workflow mocks
        workItem = mock(WorkItem.class);
        workflowSession = mock(WorkflowSession.class);
        workflowData = mock(WorkflowData.class);
        metaDataMap = mock(MetaDataMap.class);

        // Initialize Sling Resource mocks
        resourceResolverFactory = mock(ResourceResolverFactory.class);
        resourceResolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        modifiableValueMap = mock(ModifiableValueMap.class);

        // Inject reference mock into the OSGi service component
        workflow.resourceResolverFactory = resourceResolverFactory;

        // Wire generic workflow behaviors
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
    }

    // ================= SUCCESS SCENARIO =================
    @Test
    void testExecute_Success() throws Exception {
        // Mock subservice resource resolver retrieval
        when(resourceResolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resourceResolver);


        when(resourceResolver.getResource(PAYLOAD_PATH)).thenReturn(resource);
        when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(modifiableValueMap);


        workflow.execute(workItem, workflowSession, metaDataMap);

        verify(modifiableValueMap).put("jcr:title", "This title added through code");
        verify(resourceResolver).commit();
        verify(resourceResolver).close();
    }
    @Test
    void testExecute_LoginExceptionHandled() throws Exception {
        // Force the resource resolver factory to drop into a LoginException
        when(resourceResolverFactory.getServiceResourceResolver(anyMap()))
                .thenThrow(new LoginException("Mock login failure"));

        assertDoesNotThrow(() -> workflow.execute(workItem, workflowSession, metaDataMap));

        verifyNoInteractions(resourceResolver);
    }

    @Test
    void testExecute_CommitExceptionHandled() throws Exception {
        when(resourceResolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resourceResolver);
        when(resourceResolver.getResource(PAYLOAD_PATH)).thenReturn(resource);
        when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(modifiableValueMap);

        doThrow(new PersistenceException("Mock commit failure"))
                .when(resourceResolver).commit();

        assertDoesNotThrow(() -> workflow.execute(workItem, workflowSession, metaDataMap));


        verify(resourceResolver).close();
    }

    @Test
    void testExecute_NullPointerExceptionHandled() throws Exception {
        when(resourceResolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resourceResolver);

        when(resourceResolver.getResource(PAYLOAD_PATH)).thenReturn(null);

        assertDoesNotThrow(() -> workflow.execute(workItem, workflowSession, metaDataMap));
        verify(resourceResolver).close();
    }
}