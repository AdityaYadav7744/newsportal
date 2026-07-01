
(function ($, window, document) {
    "use strict";

    var registry = $(window).adaptTo("foundation-registry");

    // Register a custom validator for unique selections
    registry.register("foundation.validation.validator", {
        selector: "[data-foundation-validation='unique-deal-validator']",
        validate: function (el) {
            var $currentSelect = $(el);
            // Find the parent multifield container to isolate scoping per tab
            var $multifield = $currentSelect.closest("coral-multifield");
            if (!$multifield.length) return;

            var currentValue = $currentSelect.val();
            if (!currentValue) return;

            var duplicates = 0;

            // FIX: Target coral-select directly inside this specific multifield container
            $multifield.find("coral-select").each(function () {
                if ($(this).val() === currentValue) {
                    duplicates++;
                }
            });

            // If the value appears more than once, block submission
            if (duplicates > 1) {
                return "This deal has already been selected. Please choose a unique deal.";
            }
        }
    });

    // Dynamically clear or trigger error visual cues as the author interacts with the choices
    $(document).on("change", "[data-foundation-validation='unique-deal-validator']", function () {
        var $multifield = $(this).closest("coral-multifield");
        if ($multifield.length) {
            // FIX: Update validity state for all coral-select elements inside the multifield
            $multifield.find("coral-select").each(function () {
                var api = $(this).adaptTo("foundation-validation");
                if (api) {
                    api.checkValidity();
                    api.updateValidity();
                }
            });
        }
    });

})(Granite.$, jQuery(window), document);











