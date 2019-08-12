/*To jest skrypt który sprawdza na której podstronie jesteś i na tej podstawie ustawia
    styl css elementu belki nawigacyjnej
 */

let activeURL = document.URL;
let activeURLOnNav;
switch(activeURL) {
    case 'file:///android_asset/kontakt.html':
        activeURLOnNav= document.querySelector(".foundation");
        activeURLOnNav.className = "currentCard";
        break;
    case 'file:///android_asset/kontaktBiuro.html':
            activeURLOnNav= document.querySelector(".office");
            activeURLOnNav.className = "currentCard";
        break;
    case 'file:///android_asset/kontaktZarzad.html':
            activeURLOnNav= document.querySelector(".leadership");
            activeURLOnNav.className = "currentCard";
        break;
}