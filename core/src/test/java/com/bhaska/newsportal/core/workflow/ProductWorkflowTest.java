package com.bhaska.newsportal.core.workflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.jcr.Session;

import com.adobe.cq.dam.cfm.*;
import com.adobe.granite.workflow.*;
import com.adobe.granite.workflow.exec.*;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.*;

import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductWorkflowTest {

    private ProductWorkflow workflow;

    private WorkItem workItem;
    private WorkflowSession workflowSession;
    private WorkflowData workflowData;
    private MetaDataMap metaDataMap;

    private ResourceResolver resolver;
    private Resource resource;
    private Resource metadataResource;
    private ModifiableValueMap metadataMap;

    private ContentFragment contentFragment;
    private ContentElement element;

    private Replicator replicator;
    private Session session;

    @BeforeEach
    void setUp() {

        workflow = new ProductWorkflow();

        workItem = mock(WorkItem.class);
        workflowSession = mock(WorkflowSession.class);
        workflowData = mock(WorkflowData.class);
        metaDataMap = mock(MetaDataMap.class);

        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        metadataResource = mock(Resource.class);
        metadataMap = mock(ModifiableValueMap.class);

        contentFragment = mock(ContentFragment.class);
        element = mock(ContentElement.class);

        replicator = mock(Replicator.class);
        session = mock(Session.class);

        workflow.replicator = replicator;

        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn("/content/test");

        when(workflowSession.adaptTo(Session.class)).thenReturn(session);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resolver);
    }

    // ================= SUCCESS =================
    @Test
    void testExecute_Success() throws Exception {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn("value");

        when(resolver.getResource("/content/test/jcr:content/metadata"))
                .thenReturn(metadataResource);

        when(metadataResource.adaptTo(ModifiableValueMap.class))
                .thenReturn(metadataMap);

        workflow.execute(workItem, workflowSession, metaDataMap);

        verify(metadataMap).put("productStatus", "approved");
        verify(resolver).commit();
        verify(replicator).replicate(session, ReplicationActionType.ACTIVATE, "/content/test");
    }


    @Test
    void testExecute_ResolverNull() throws Exception {

        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(null);

        workflow.execute(workItem, workflowSession, metaDataMap);

        verifyNoInteractions(replicator);
    }


    @Test
    void testExecute_ResourceNull() {

        when(resolver.getResource("/content/test")).thenReturn(null);

        assertThrows(NullPointerException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }

    @Test
    void testExecute_ContentFragmentNull() {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(null);

        assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }

    // ================= EMPTY FIELDS =================
    @Test
    void testExecute_EmptyFields() {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn(""); // empty

        assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }

    // ================= NULL FIELD =================
    @Test
    void testExecute_FieldNull() {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn(null); // NULL

        assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }

    // ================= PARTIAL FIELD NULL =================
    @Test
    void testExecute_OneFieldMissing() {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement("productName")).thenReturn(element);
        when(contentFragment.getElement("productDescription")).thenReturn(element);
        when(contentFragment.getElement("productPrice")).thenReturn(element);
        when(contentFragment.getElement("productImage")).thenReturn(element);

        when(element.getContent())
                .thenReturn("name")
                .thenReturn("desc")
                .thenReturn(null)   // one field missing
                .thenReturn("img");

        assertThrows(WorkflowException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }

    // ================= METADATA RESOURCE NULL =================
    @Test
    void testExecute_MetadataResourceNull() throws Exception {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn("value");

        when(resolver.getResource("/content/test/jcr:content/metadata"))
                .thenReturn(null);

        workflow.execute(workItem, workflowSession, metaDataMap);

        verify(resolver, never()).commit();
    }

    // ================= METADATA MAP NULL =================
    @Test
    void testExecute_MetadataMapNull() throws Exception {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn("value");

        when(resolver.getResource("/content/test/jcr:content/metadata"))
                .thenReturn(metadataResource);

        when(metadataResource.adaptTo(ModifiableValueMap.class))
                .thenReturn(null);

        workflow.execute(workItem, workflowSession, metaDataMap);

        verify(resolver, never()).commit();
    }

    // ================= REPLICATION EXCEPTION =================
    @Test
    void testExecute_ReplicationException() throws Exception {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn("value");

        when(resolver.getResource("/content/test/jcr:content/metadata"))
                .thenReturn(metadataResource);

        when(metadataResource.adaptTo(ModifiableValueMap.class))
                .thenReturn(metadataMap);

        doThrow(new ReplicationException("error"))
                .when(replicator)
                .replicate(any(), any(), anyString());

        assertThrows(RuntimeException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }

    // ================= PERSISTENCE EXCEPTION =================
    @Test
    void testExecute_PersistenceException() throws Exception {

        when(resolver.getResource("/content/test")).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);

        when(contentFragment.getElement(anyString())).thenReturn(element);
        when(element.getContent()).thenReturn("value");

        when(resolver.getResource("/content/test/jcr:content/metadata"))
                .thenReturn(metadataResource);

        when(metadataResource.adaptTo(ModifiableValueMap.class))
                .thenReturn(metadataMap);

        doThrow(new PersistenceException("error"))
                .when(resolver).commit();

        assertThrows(RuntimeException.class, () ->
                workflow.execute(workItem, workflowSession, metaDataMap));
    }
}