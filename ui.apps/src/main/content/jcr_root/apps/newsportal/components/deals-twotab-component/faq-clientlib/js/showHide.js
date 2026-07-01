document.addEventListener("DOMContentLoaded", function () {
	console.log("WRAPPER COUNT:", document.querySelectorAll(".deals-wrapper").length);
    const tabs = document.querySelectorAll(".tab-btn");
    const sections = document.querySelectorAll(".deals-section");

    console.log("Tabs found:", tabs.length);

    tabs.forEach(tab => {
        tab.addEventListener("click", function () {

            console.log("Clicked:", this.getAttribute("data-tab"));

            // remove active from all tabs
            tabs.forEach(t => t.classList.remove("active"));

            // hide all sections
            sections.forEach(s => s.classList.remove("active-tab"));

            // activate clicked tab
            this.classList.add("active");

            // show target section
            const target = document.getElementById(this.getAttribute("data-tab"));

            if (target) {
                target.classList.add("active-tab");
            } else {
                console.error("No section found for:", this.getAttribute("data-tab"));
            }
        });
    });

});