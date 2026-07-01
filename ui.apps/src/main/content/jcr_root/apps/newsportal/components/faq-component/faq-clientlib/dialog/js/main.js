(function (document, $) {
    "use strict";

    var TAB_TITLES = {
        manual: "Manual",
        dynamic: "Dynamic"
    };

    function getSelectedValue(element) {
        if (element.tagName === "CORAL-SELECT") {
            return element.value || (element.selectedItem ? element.selectedItem.value : "manual");
        }
        return element.value || "manual";
    }

    function toggleFields(element) {
        var targetSelector = element.dataset.cqDialogDropdownShowhideTarget;
        if (!targetSelector) {
            return;
        }

        var value = getSelectedValue(element);
        $(targetSelector).each(function () {
            var targetValue = this.dataset.showhidetargetvalue;
            if (targetValue === value) {
                $(this).removeClass("hide");
            } else {
                $(this).addClass("hide");
            }
        });
    }

    function updateSecondTabTitle(dialog, value) {
        var title = TAB_TITLES[value] || TAB_TITLES.manual;
        var $secondTabPanel = dialog.find(".cq-dialog-second-tab").closest("coral-panel");

        if ($secondTabPanel.length) {
            var panelId = $secondTabPanel.attr("id");
            if (panelId) {
                var $tab = dialog.find('coral-tab[aria-controls="' + panelId + '"]');
                if ($tab.length) {
                    $tab.find("coral-tab-label").text(title);
                }
            }
        }
    }

    function initCardTypeDialog(context) {
        var $dialog = $(context).closest(".cq-dialog");
        if (!$dialog.length) {
            $dialog = $(".cq-dialog");
        }

        $dialog.find(".cq-dialog-cardtype-select").each(function () {
            var value = getSelectedValue(this);
            toggleFields(this);
            updateSecondTabTitle($dialog, value);
        });
    }

    function onCardTypeChange(event) {
        var element = event.target;
        var $dialog = $(element).closest(".cq-dialog");
        var value = getSelectedValue(element);

        toggleFields(element);
        updateSecondTabTitle($dialog, value);
    }

    $(document).on("foundation-contentloaded", function (event) {
        initCardTypeDialog(event.target);
    });

    $(document).on("change", ".cq-dialog-cardtype-select", onCardTypeChange);
    $(document).on("coral-select:change", ".cq-dialog-cardtype-select", onCardTypeChange);

})(document, Granite.$);




