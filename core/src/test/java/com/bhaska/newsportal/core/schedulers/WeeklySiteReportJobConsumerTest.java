package com.bhaska.newsportal.core.schedulers;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WeeklySiteReportJobConsumerTest {

    WeeklySiteReportJobConsumer job = new WeeklySiteReportJobConsumer();

    // =========================
    // ✅ NULL PAGES
    // =========================
    @Test
    void testGenerateReport_NullPages() {

        WeeklySiteReportJobConsumer.Report report =
                job.generateReport(null, null);

        assertEquals(0, report.totalPages);
        assertEquals(0, report.missingTitle);
        assertEquals(0, report.missingDescription);
        assertEquals(0, report.totalAssets);
    }

    // =========================
    // ✅ EMPTY LIST
    // =========================
    @Test
    void testGenerateReport_EmptyPages() {

        WeeklySiteReportJobConsumer.Report report =
                job.generateReport(new ArrayList<>(), null);

        assertEquals(0, report.totalPages);
        assertEquals(0, report.totalAssets);
    }

    // =========================
    // ✅ ALL PAGE CASES
    // =========================
    @Test
    void testGenerateReport_AllBranches() {

        List<WeeklySiteReportJobConsumer.Page> pages = Arrays.asList(
                new WeeklySiteReportJobConsumer.Page(true, true),
                new WeeklySiteReportJobConsumer.Page(false, true),
                new WeeklySiteReportJobConsumer.Page(true, false),
                new WeeklySiteReportJobConsumer.Page(false, false)
        );

        WeeklySiteReportJobConsumer.Asset asset =
                new WeeklySiteReportJobConsumer.Asset(true, null);

        WeeklySiteReportJobConsumer.Report report =
                job.generateReport(pages, asset);

        assertEquals(4, report.totalPages);
        assertEquals(2, report.missingTitle);
        assertEquals(2, report.missingDescription);
        assertEquals(1, report.totalAssets);
    }

    // =========================
    // ✅ countAssets NULL
    // =========================
    @Test
    void testCountAssets_Null() {
        assertEquals(0, job.countAssets(null));
    }

    // =========================
    // ✅ SINGLE ASSET
    // =========================
    @Test
    void testCountAssets_Single() {

        WeeklySiteReportJobConsumer.Asset asset =
                new WeeklySiteReportJobConsumer.Asset(true, null);

        assertEquals(1, job.countAssets(asset));
    }

    // =========================
    // ✅ NO CHILDREN (false asset)
    // =========================
    @Test
    void testCountAssets_NoChildren() {

        WeeklySiteReportJobConsumer.Asset asset =
                new WeeklySiteReportJobConsumer.Asset(false, null);

        assertEquals(0, job.countAssets(asset));
    }

    // =========================
    // ✅ RECURSIVE CASE
    // =========================
    @Test
    void testCountAssets_Recursive() {

        WeeklySiteReportJobConsumer.Asset root =
                new WeeklySiteReportJobConsumer.Asset(false, Arrays.asList(
                        new WeeklySiteReportJobConsumer.Asset(true, null),
                        new WeeklySiteReportJobConsumer.Asset(false, Arrays.asList(
                                new WeeklySiteReportJobConsumer.Asset(true, null)
                        ))
                ));

        assertEquals(2, job.countAssets(root));
    }

    // =========================
    // ✅ EMPTY CHILDREN LIST
    // =========================
    @Test
    void testCountAssets_EmptyChildren() {

        WeeklySiteReportJobConsumer.Asset asset =
                new WeeklySiteReportJobConsumer.Asset(true, new ArrayList<>());

        assertEquals(1, job.countAssets(asset));
    }
}