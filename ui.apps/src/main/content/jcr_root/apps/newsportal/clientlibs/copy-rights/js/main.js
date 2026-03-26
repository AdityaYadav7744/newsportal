document.addEventListener("DOMContentLoaded", function () {

    const container = document.getElementById("copyright-container");

    // Safety check
    if (!container) {
        console.error("Container not found");
        return;
    }

    const resourcePath = container.dataset.path;   // better than getAttribute

    if (!resourcePath) {
        console.error("Resource path not found");
        return;
    }

    fetch(`/bin/copyright?path=${encodeURIComponent(resourcePath)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch data");
            }
            return response.json();
        })
        .then(data => {

            document.getElementById("componentText").innerText =
                data.componentText || "";

            document.getElementById("copyrightText").innerText =
                data.copyrightText || "";

        })
        .catch(error => {
            console.error("Error fetching copyright data:", error);
        });

});