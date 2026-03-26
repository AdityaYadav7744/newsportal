package com.bhaska.newsportal.core.workflow;


import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Session;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label = NP Product Workflow"
        }
)
public class ProductWorkflow implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ProductWorkflow.class);

    @Reference
    Replicator replicator;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession,
                        MetaDataMap metaDataMap) throws WorkflowException {
        try {
            ResourceResolver resourceResolver = null;
            Session session = workflowSession.adaptTo(Session.class);
            String payload = workItem.getWorkflowData().getPayload().toString();
            if(payload != null) {
                LOG.info("Payload path : {}", payload);
                resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
                if(resourceResolver != null) {
                    Resource resource = resourceResolver.getResource(payload);
                    ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
                    ProductDetails productDetails = null;
                    if(contentFragment != null) {
                        String productName = contentFragment.getElement("productName").getContent();
                        String productDescription = contentFragment.getElement("productDescription").getContent();
                        String productPrice = contentFragment.getElement("productPrice").getContent();
                        String productImage = contentFragment.getElement("productImage").getContent();
                        productDetails = new ProductDetails(productName, productDescription, productPrice, productImage);
                        LOG.info("Product Details : {}", productDetails);
                    }

                    if(productDetails == null) {

                        LOG.error("Product details not found.");
                        throw new WorkflowException("Product details not found.");

                    }

                    else if(productDetails.getProductName() == null || productDetails.getProductName().isEmpty()
                            || productDetails.getProductDescription() == null || productDetails.getProductDescription().isEmpty()
                            || productDetails.getProductPrice() == null || productDetails.getProductPrice().isEmpty()
                            || productDetails.getProductImage() == null || productDetails.getProductImage().isEmpty()) {

                        LOG.error("Required product fields are missing");
                        throw new WorkflowException("Required product fields are missing.");
                    }

                    else {
                        Resource metadataResource = resourceResolver.getResource(payload + "/jcr:content/metadata");

                        if (metadataResource != null) {
                            ModifiableValueMap metadataMap = metadataResource.adaptTo(ModifiableValueMap.class);
                            if (metadataMap != null) {
                                LOG.info("Adding property in CF metadata");
                                metadataMap.put("productStatus", "approved");
                                try {
                                    resourceResolver.commit();
                                    replicator.replicate(session, ReplicationActionType.ACTIVATE, payload);
                                } catch (PersistenceException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                }
            }

        } catch (ReplicationException e) {
            LOG.error("Exception happened");
            throw new RuntimeException(e);
        }
    }
}