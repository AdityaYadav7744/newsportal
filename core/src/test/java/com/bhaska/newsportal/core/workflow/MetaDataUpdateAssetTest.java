package com.bhaska.newsportal.core.workflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

class MetaDataUpdateAssetTest {

    private MetaDataUpdateAsset workflowProcess;

    private WorkItem workItem;
    private Workflow workflowInstance;
    private WorkflowSession workflowSession;
    private WorkflowData workflowData;
    private MetaDataMap metaDataMap;

    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver resourceResolver;
    private Resource resource;
    private ModifiableValueMap modifiableValueMap;

    private static final String PAYLOAD_PATH = "/content/dam/newsportal/sample-asset";

    @BeforeEach
    void setUp() throws LoginException {
        workflowProcess = new MetaDataUpdateAsset();

        // Initialize Granite Workflow structure mocks
        workItem = mock(WorkItem.class);
        workflowInstance = mock(Workflow.class);
        workflowSession = mock(WorkflowSession.class);
        workflowData = mock(WorkflowData.class);
        metaDataMap = mock(MetaDataMap.class);

        // Initialize Sling Resource framework mocks
        resourceResolverFactory = mock(ResourceResolverFactory.class);
        resourceResolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        modifiableValueMap = mock(ModifiableValueMap.class);

        // Inject the mocked field via reflection (simulating OSGi service injection)
        // Note: Using a direct field assignment since the field is package-private/private
        java.lang.reflect.Field field;
        try {
            field = MetaDataUpdateAsset.class.getDeclaredField("resourceResolverFactory");
            field.setAccessible(true);
            field.set(workflowProcess, resourceResolverFactory);
        } catch (Exception e) {
            fail("Failed to inject field via reflection for testing: " + e.getMessage());
        }

        // Stub out the deep nested payload tracking path: workItem.getWorkflow().getWorkflowData().getPayload()
        when(workItem.getWorkflow()).thenReturn(workflowInstance);
        when(workflowInstance.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
    }

    // ================= SUCCESS SCENARIO =================
    @Test
    void testExecute_Success() throws Exception {
        // Arrange
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
        when(resourceResolver.getResource(PAYLOAD_PATH)).thenReturn(resource);
        when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(modifiableValueMap);

        // Act
        workflowProcess.execute(workItem, workflowSession, metaDataMap);

        // Assert
        verify(modifiableValueMap).put("author", "admin");
        verify(resourceResolver).commit();
        verify(resourceResolver).close(); // Ensures try-with-resources handles cleanup safely
    }

    // ================= EXCEPTION PATHS (FORCING RUNTIME_EXCEPTION CATCH) =================
    @Test
    void testExecute_LoginExceptionThrowsRuntimeException() throws Exception {
        // Arrange
        LoginException mockException = new LoginException("Service login disabled");
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenThrow(mockException);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                workflowProcess.execute(workItem, workflowSession, metaDataMap));

        // Verifies the catch block correctly wraps the root cause
        assertEquals(mockException, thrown.getCause());
    }

    @Test
    void testExecute_CommitExceptionThrowsRuntimeException() throws Exception {
        // Arrange
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
        when(resourceResolver.getResource(PAYLOAD_PATH)).thenReturn(resource);
        when(resource.adaptTo(ModifiableValueMap.class)).thenReturn(modifiableValueMap);

        PersistenceException mockException = new PersistenceException("Read-only repository access state");
        doThrow(mockException).when(resourceResolver).commit();

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                workflowProcess.execute(workItem, workflowSession, metaDataMap));

        assertEquals(mockException, thrown.getCause());
        verify(resourceResolver).close(); // Confirms resource closes even if transactional commit errors out
    }

    @Test
    void testExecute_NullPointerExceptionThrowsRuntimeException() throws Exception {
        // Arrange
        when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenReturn(resourceResolver);
        // Returns null to force a NullPointerException when calling .adaptTo() down the line
        when(resourceResolver.getResource(PAYLOAD_PATH)).thenReturn(null);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                workflowProcess.execute(workItem, workflowSession, metaDataMap));

        assertInstanceOf(NullPointerException.class, thrown.getCause());
        verify(resourceResolver).close();
    }
}