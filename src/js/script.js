const btn = document.getElementById("mover-btn");
const box = document.getElementById("box-solution");

btn.addEventListener("click", () => {
    box.classList.toggle("active");
});