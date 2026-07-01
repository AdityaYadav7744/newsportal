document.addEventListener("DOMContentLoaded", function () {

    const popup = document.getElementById("faq-popup");
    const popupTitle = document.getElementById("faq-popup-title");
    const popupDescription = document.getElementById("faq-popup-description");
    const closeBtn = document.getElementById("faq-close-btn");

    document.querySelectorAll(".faq-card").forEach(function(card){

        card.addEventListener("click", function(){

            const title = card.getAttribute("data-title");
            const description = card.getAttribute("data-description");

            popupTitle.innerText = title;
            popupDescription.innerText = description;

            popup.style.display = "flex";

        });

    });

    closeBtn.addEventListener("click", function(){

        popup.style.display = "none";

    });

});



document.addEventListener("DOMContentLoaded", function () {

    const track = document.querySelector(".faq-carousel-track");
    const nextBtn = document.querySelector(".next-btn");
    const prevBtn = document.querySelector(".prev-btn");

    if (!track) return;

    const cards = track.querySelectorAll(".carousel-card");

    let currentIndex = 0;
    const visibleCards = 3;

    function slide() {
        const cardWidth = cards[0].offsetWidth + 20;
        track.style.transform =
            `translateX(-${currentIndex * cardWidth}px)`;
    }

    nextBtn.addEventListener("click", function () {

        if (currentIndex < cards.length - visibleCards) {
            currentIndex++;
            slide();
        }

    });

    prevBtn.addEventListener("click", function () {

        if (currentIndex > 0) {
            currentIndex--;
            slide();
        }

    });

});