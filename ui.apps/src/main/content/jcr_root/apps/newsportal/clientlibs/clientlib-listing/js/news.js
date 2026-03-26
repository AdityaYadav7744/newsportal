document.addEventListener("DOMContentLoaded", function () {

    console.log("News JS Loaded");

    const container = document.getElementById("news-container");
    const spinner = document.getElementById("loading-spinner");
    const searchInput = document.getElementById("search-input");

    if (!container) {
        console.error("News container not found");
        return;
    }

    const apiUrl = container.dataset.api && container.dataset.api.trim()
        ? container.dataset.api.trim()
        : "https://gorest.co.in/public/v2/posts";

    const limit = container.dataset.limit && !isNaN(container.dataset.limit)
        ? parseInt(container.dataset.limit)
        : 5;

    const enableSearch = container.dataset.search === "true";

    spinner.style.display = "block";

    fetch(`${apiUrl}?per_page=${limit}`)
        .then(response => response.json())
        .then(data => {

            if (!Array.isArray(data)) {
                throw new Error("API did not return an array");
            }

            const posts = data.slice(0, limit);

            spinner.style.display = "none";

            // Render function
            function renderNews(filteredPosts) {
                let html = "";

                filteredPosts.forEach(post => {
                    html += `
                        <div class="news-card">
                            <h3>${post.title}</h3>
                            <p>${post.body}</p>
                        </div>
                    `;
                });

                container.innerHTML = html;
            }

            // Initial render
            renderNews(posts);

            // 🔥 SEARCH LOGIC
            if (enableSearch && searchInput) {
                searchInput.addEventListener("input", function () {

                    const searchValue = this.value.toLowerCase();

                    const filtered = posts.filter(post =>
                        post.title.toLowerCase().includes(searchValue)
                    );

                    renderNews(filtered);
                });
            }

        })
        .catch(error => {
            console.error("Fetch Error:", error);
            spinner.style.display = "none";
            container.innerHTML = "<p>Failed to load news.</p>";
        });

});
