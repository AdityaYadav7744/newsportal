package com.bhaska.newsportal.core.listeners;

import com.bhaska.newsportal.core.service.EmailService;
import com.bhaska.newsportal.core.service.impl.NPUtilService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=user/onboarding/job"
        }
)
public class UserOnboardingJobConsumer implements JobConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(UserOnboardingJobConsumer.class);
    @Reference
    private EmailService emailService;
    @Reference
    private NPUtilService npUtilService;
    @Override
    public JobResult process(Job job) {
        String userPath = job.getProperty("userPath",String.class);
        LOG.info("Started onboarding job for user: {}", userPath);
        if (userPath == null || userPath.contains("/profile")) {
            LOG.warn("Skipping invalid user path: {}", userPath);
            return JobResult.CANCEL;
        }

        try (ResourceResolver resolver = npUtilService.getResourceResolver()) {
            Resource userResource = resolver.getResource(userPath);
            if (userResource == null) {
                LOG.error("User resource not found at {}", userPath);
                return JobResult.CANCEL;
            }
            Resource profile = userResource.getChild("profile");

            if (profile == null) {
                LOG.warn("Profile node not found for {}", userPath);
                return JobResult.CANCEL;
            }

           String email = profile.getValueMap().get("email", String.class);
            String name = profile.getValueMap().get("givenName", String.class);
            LOG.info("User details fetched → Email: {}, Name: {}", email, name);
            emailService.sendWelcomeEmail(email, name);
            callCRM(email, name);

            LOG.info("User onboarding completed successfully for {}", email);

            return JobResult.OK;

        } catch (Exception e) {
            LOG.error("Error occurred during onboarding job", e);
            return JobResult.FAILED;
        }
    }

    private void callCRM(String email, String name) {

        try {
            LOG.info("Calling CRM API for {}", email);

            URL url = new URL("https://jsonplaceholder.typicode.com/posts");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonPayload = "{"
                    + "\"email\":\"" + email + "\","
                    + "\"name\":\"" + name + "\""
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonPayload.getBytes());
            }

            int responseCode = conn.getResponseCode();

            if (responseCode != 200 && responseCode != 201) {
                LOG.error("CRM API failed with response code {}", responseCode);
                throw new RuntimeException("CRM API failed");
            }
            LOG.info("CRM sync successful for {}", email);

        } catch (Exception e) {
            LOG.error("CRM API error", e);
            throw new RuntimeException("CRM failed", e);
        }
    }
}