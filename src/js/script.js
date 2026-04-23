const btn = document.getElementById("mover-btn");
const box = document.getElementById("box-solution");

btn.addEventListener("click", () => {
    box.classList.toggle("active");
});

const hamburger = document.getElementById("hamburger")
const links = document.getElementById("navbar")

hamburger.addEventListener("click", () =>{
    links.classList.toggle("show")
})