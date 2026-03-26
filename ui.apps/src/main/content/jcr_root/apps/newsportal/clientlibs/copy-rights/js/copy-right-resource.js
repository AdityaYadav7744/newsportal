document.addEventListener("DOMContentLoaded", function () {

    const container = document.getElementById("copyright-container");

    if (!container) {
        console.error("Container not found");
        return;
    }

    const resourcePath = container.dataset.path;

    if (!resourcePath) {
        console.error("Resource path not found");
        return;
    }

    const url = `${resourcePath}.copyright.json`;

    fetch(url)
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
            console.error("Error fetching data:", error);
        });

});