document.addEventListener("DOMContentLoaded", function () {

    const carousel = document.querySelector(".carousel-v1");
    if (!carousel) return;

    const wrapper = carousel.querySelector(".carousel-wrapper");
    const slides = carousel.querySelectorAll(".carousel-item");

    let currentIndex = 0;
    const totalSlides = slides.length;

    /* Create Navigation Buttons */
    const prevBtn = document.createElement("button");
    prevBtn.className = "carousel-prev";
    prevBtn.innerHTML = "&#10094;";

    const nextBtn = document.createElement("button");
    nextBtn.className = "carousel-next";
    nextBtn.innerHTML = "&#10095;";

    carousel.appendChild(prevBtn);
    carousel.appendChild(nextBtn);

    /* Create Dots */
    const dotsContainer = document.createElement("div");
    dotsContainer.className = "carousel-dots";

    for (let i = 0; i < totalSlides; i++) {
        const dot = document.createElement("span");
        dot.className = "carousel-dot";
        if (i === 0) dot.classList.add("active");

        dot.addEventListener("click", function () {
            currentIndex = i;
            updateCarousel();
        });

        dotsContainer.appendChild(dot);
    }

    carousel.appendChild(dotsContainer);

    const dots = carousel.querySelectorAll(".carousel-dot");

    function updateCarousel() {
        wrapper.style.transform = `translateX(-${currentIndex * 100}%)`;

        dots.forEach((dot, index) => {
            dot.classList.toggle("active", index === currentIndex);
        });
    }

    nextBtn.addEventListener("click", function () {
        currentIndex = (currentIndex + 1) % totalSlides;
        updateCarousel();
    });

    prevBtn.addEventListener("click", function () {
        currentIndex = (currentIndex - 1 + totalSlides) % totalSlides;
        updateCarousel();
    });

    /* Auto Slide */
    setInterval(function () {
        currentIndex = (currentIndex + 1) % totalSlides;
        updateCarousel();
    }, 5000);

    updateCarousel();

});