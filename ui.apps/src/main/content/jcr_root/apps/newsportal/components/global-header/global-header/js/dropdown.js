document.addEventListener("DOMContentLoaded", function () {

    const toggle = document.querySelector(".menu-toggle");
    const nav = document.querySelector(".header-nav");
    const loginBtn = document.querySelector(".login-btn");
    const modal = document.querySelector(".login-modal");
    const closeBtn = document.querySelector(".close-btn");

    // Hamburger toggle
    toggle.addEventListener("click", function () {
        nav.classList.toggle("active");
    });

    // Mobile dropdown toggle
    document.querySelectorAll(".cmp-navigation__item--level-0 > a")
        .forEach(function (item) {
            item.addEventListener("click", function (e) {
                if (window.innerWidth <= 992) {
                    e.preventDefault();
                    this.parentElement.classList.toggle("active");
                }
            });
        });

    // Login modal
    loginBtn.addEventListener("click", function () {
        modal.style.display = "flex";
    });

    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });

    window.addEventListener("click", function (e) {
        if (e.target === modal) {
            modal.style.display = "none";
        }
    });

});