const hamburger = document.getElementById("hamburger")
const links = document.getElementById("navbar")

hamburger.addEventListener("click", () =>{
    links.classList.toggle("show")
})

const btn = document.getElementById("mover-btn");
const box = document.getElementById("box-solution");

// El botón de flechas solo existe en el inicio, no en la página de proyectos
if (btn && box) {
    btn.addEventListener("click", () => {
        box.classList.toggle("active");
    });
}