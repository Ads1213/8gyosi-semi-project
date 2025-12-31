const loginlink = document.querySelector("#login-link");
const loginwindow = document.querySelector("#login-window")

loginlink.addEventListener("click", (e) => {
   loginwindow.classList.add("show");
   loginwindow.classList.remove("hidden");
});

const loginbtn = document.querySelector("login-btn")
loginbtn.addEventListener("click", (e) => {
    location.href = ``;

});