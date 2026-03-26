package com.bhaska.newsportal.core.schedulers;

import java.util.List;

public class WeeklySiteReportJobConsumer {

    public static class Page {
        boolean hasTitle;
        boolean hasDescription;

        public Page(boolean hasTitle, boolean hasDescription) {
            this.hasTitle = hasTitle;
            this.hasDescription = hasDescription;
        }
    }

    public static class Asset {
        boolean isAsset;
        List<Asset> children;

        public Asset(boolean isAsset, List<Asset> children) {
            this.isAsset = isAsset;
            this.children = children;
        }
    }

    public static class Report {
        int totalPages;
        int missingTitle;
        int missingDescription;
        int totalAssets;
    }

    // =========================
    // MAIN METHOD
    // =========================
    public Report generateReport(List<Page> pages, Asset asset) {

        Report report = new Report();

        // Branch 1
        if (pages == null) {
            report.totalAssets = countAssets(asset);
            return report;
        }

        for (Page page : pages) {

            report.totalPages++;

            // Branch 2
            if (!page.hasTitle) {
                report.missingTitle++;
            }

            // Branch 3
            if (!page.hasDescription) {
                report.missingDescription++;
            }
        }

        report.totalAssets = countAssets(asset);

        return report;
    }

    // =========================
    // RECURSIVE METHOD
    // =========================
    public int countAssets(Asset asset) {

        // Branch 4
        if (asset == null) {
            return 0;
        }

        int count = 0;

        // Branch 5
        if (asset.isAsset) {
            count++;
        }

        // Branch 6
        if (asset.children != null) {
            for (Asset child : asset.children) {
                count += countAssets(child);
            }
        }

        return count;
    }
}