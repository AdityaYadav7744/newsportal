document.addEventListener("DOMContentLoaded", function () {
    const headers = document.querySelectorAll(".accordion-header");
    headers.forEach(function(header) {
        header.addEventListener("click", function() {
            const accordionItem = this.closest(".accordion-item");
            accordionItem.classList.toggle("active");
        });
    });
});