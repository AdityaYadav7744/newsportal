document.addEventListener("DOMContentLoaded", function () {

    const carousel = document.querySelector(".carousel-v2");
    if (!carousel) return;

    const wrapper = carousel.querySelector(".carousel-wrapper");
    const slides = carousel.querySelectorAll(".carousel-item");
    const prevBtn = carousel.querySelector(".carousel-prev");
    const nextBtn = carousel.querySelector(".carousel-next");
    const dotsContainer = carousel.querySelector(".carousel-dots");

    let currentIndex = 0;
    const totalSlides = slides.length;

    /* Create dots */
    for (let i = 0; i < totalSlides; i++) {
        const dot = document.createElement("span");
        dot.classList.add("carousel-dot");

        if (i === 0) {
            dot.classList.add("active");
        }

        dot.addEventListener("click", function () {
            currentIndex = i;
            updateCarousel();
        });

        dotsContainer.appendChild(dot);
    }

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