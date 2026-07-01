(function (document, $) {
    "use strict";

    $(document).on("foundation-contentloaded", function () {

        let $country = $("[name='./country']");
        let $state = $("[name='./state']");
        let $stateWrapper = $state.closest(".coral-Form-fieldwrapper");

        // Initially hide state
        $stateWrapper.hide();

        $country.on("change", function () {

            let country = $(this).val();

            if (!country) {
                $stateWrapper.hide();
                return;
            }

            $stateWrapper.show();

            // Call servlet
            $.ajax({
                url: "/bin/countryStates",
                type: "GET",
                data: { country: country },
                success: function (response) {

                    $state.empty();

                    response.states.forEach(function (state) {
                        $state.append(
                            '<option value="' + state + '">' + state + '</option>'
                        );
                    });
                },
                error: function () {
                    console.log("API failed → fallback handled in backend");
                }
            });
        });
    });

})(document, Granite.$);