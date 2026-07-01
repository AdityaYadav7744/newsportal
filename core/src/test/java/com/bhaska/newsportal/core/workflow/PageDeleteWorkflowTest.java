package com.bhaska.newsportal.core.workflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageDeleteWorkflowTest {

    private PageDeleteWorkflow workflow;

    private WorkItem workItem;
    private WorkflowSession workflowSession;
    private WorkflowData workflowData;
    private MetaDataMap metaDataMap;

    private ResourceResolver resolver;
    private PageManager pageManager;
    private Page page;

    private static final String PAGE_PATH = "/content/newsportal/us/en/article";
    private static final String PAYLOAD_WITH_JCR = "/content/newsportal/us/en/article/jcr:content";

    @BeforeEach
    void setUp() {
        workflow = new PageDeleteWorkflow();

        // Initialize Granite Workflow structure mocks
        workItem = mock(WorkItem.class);
        workflowSession = mock(WorkflowSession.class);
        workflowData = mock(WorkflowData.class);
        metaDataMap = mock(MetaDataMap.class);

        // Initialize Sling & AEM WCM API mocks
        resolver = mock(ResourceResolver.class);
        pageManager = mock(PageManager.class);
        page = mock(Page.class);

        // Link workflowSession -> resourceResolver -> pageManager
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    }

    // ================= SUCCESS SCENARIOS =================
    @Test
    void testExecute_Success_StandardPath() throws Exception {
        // Arrange payload path without jcr:content appended
        when(workflowData.getPayload()).thenReturn(PAGE_PATH);
        when(pageManager.getPage(PAGE_PATH)).thenReturn(page);

        // Act
        workflow.execute(workItem, workflowSession, metaDataMap);

        // Assert deletion execution
        verify(pageManager).delete(page, false);
    }

    @Test
    void testExecute_Success_WithJcrContentInPayload() throws Exception {
        // Arrange payload targeting deep /jcr:content structure
        when(workflowData.getPayload()).thenReturn(PAYLOAD_WITH_JCR);
        // Ensure string manipulation cleans payload down to base path
        when(pageManager.getPage(PAGE_PATH)).thenReturn(page);

        // Act
        workflow.execute(workItem, workflowSession, metaDataMap);

        // Assert string replace functioned accurately and hit delete
        verify(pageManager).delete(page, false);
    }

    // ================= BRANCH COVERAGE: RESOLVER NULL =================
    @Test
    void testExecute_ResolverNull_ThrowsWorkflowException() {
        // Arrange
        when(workflowData.getPayload()).thenReturn(PAGE_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(null);

        // Act & Assert
        WorkflowException thrown = assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));

        assertEquals("Resolver is null", thrown.getMessage());
        verifyNoInteractions(pageManager);
    }

    // ================= BRANCH COVERAGE: PAGE NULL =================
    @Test
    void testExecute_PageNull_ThrowsWorkflowException() {
        // Arrange
        when(workflowData.getPayload()).thenReturn(PAGE_PATH);
        when(pageManager.getPage(PAGE_PATH)).thenReturn(null); // Page not found in repository

        // Act & Assert
        WorkflowException thrown = assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));

        assertEquals("Page is null", thrown.getMessage());
        try {
            verify(pageManager, never()).delete((Page) any(), anyBoolean());
        } catch (WCMException e) {
            // Needed to handle the checked signature variant inside a native catch structure
        }
    }

    // ================= EXCEPTION HANDLING COVERAGE =================
    @Test
    void testExecute_WCMException_ThrowsRuntimeException() throws Exception {
        // Arrange
        when(workflowData.getPayload()).thenReturn(PAGE_PATH);
        when(pageManager.getPage(PAGE_PATH)).thenReturn(page);

        WCMException mockWcmException = new WCMException("Internal JCR Nodes Locked or Read-Only");
        doThrow(mockWcmException).when(pageManager).delete(page, false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));

        assertInstanceOf(RuntimeException.class, thrown);
        assertEquals(mockWcmException, thrown.getCause());
    }
}