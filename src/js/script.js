const btn = document.getElementById("mover-btn");
const box = document.getElementById("box-solution");

btn.addEventListener("click", () => {
    box.style.transform = "translateX(-800px)";
});