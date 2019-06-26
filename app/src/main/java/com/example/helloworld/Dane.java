package com.example.helloworld;

//chcę tu trzymać jakieś globalne dane, głównie związane z aktualnie zalogowaną osobą


public class Dane {

    private final static String wczytywanie = "Brak Danych";

    protected static Stypendysta zalogowany = new Stypendysta();

    protected static Boolean czy_zalogowany = false;

    //pobrane teksty
    protected static Boolean zakładka_ze_zmiennym_tekstem = false;
    protected static String tekstDoWyświetlenia = wczytywanie;
    protected static String materiałyPrasowe = wczytywanie;
    protected static String formacja = wczytywanie;
    protected static String oglOgólne = wczytywanie;
    protected static String oglWspólnotowe = wczytywanie;
    protected static String materiały = wczytywanie;



    protected static MainActivity ta_aktywnosc;

    protected static int doWyświetlenia = R.layout.activity_main;


    public static String imie() {return zalogowany.imie();}
    public static String nazwisko() {return zalogowany.nazwisko();}
    public static String email() {return  zalogowany.email();}
    public static String etykieta() {return zalogowany.etykieta();}

    //Rzeczy z ustawieniami
    protected static Boolean ciemny_motyw = false;

    //Stały tekst do zkłądek
    protected static final String kontakt ="Telefon: +48 22 530 48 28\n\nEmail: dzielo@episkopat.pl";
    protected static final String o_fundacji = "Kim jesteśmy?\n \n \n" +
            "  Fundacja „Dzieło Nowego Tysiąclecia” została powołana przez Konferencję Episkopatu Polski w 2000 roku jako wyraz wdzięczności dla Ojca Świętego Jana Pawła II za Jego niestrudzoną posługę duchową na rzecz Kościoła i Ojczyzny. Idea utworzenia Fundacji jako organizacji, której działalność ma upamiętniać Pontyfikat Jana Pawła II przez promowanie nauczania Papieża i wspieranie określonych przedsięwzięć społecznych, głównie w dziedzinie edukacji i kultury, narodziła się po pielgrzymce Ojca Świętego do Polski w 1999 r. \n  " +
            "Organizacją kierują: Rada składająca się z dziesięciu osób raz Zarząd liczący czterech członków. Na czele Rady stoi ks. kard. Kazimierz Nycz, a przewodniczącym Zarządu jest ks. Dariusz Kowalczyk. Siedziba Fundacji znajduje się w Warszawie w Sekretariacie Konferencji Episkopatu Polski. Nadzór państwowy nad działalnością Fundacji sprawuje minister właściwy do spraw oświaty oraz szkolnictwa wyższego oraz – niezależnie od nadzoru państwowego – Konferencja Episkopatu Polski, jako Fundator.  \n  " +
            "Fundacja działa na podstawie Ustawy z dnia 6 kwietnia 1984 r. o fundacjach, Ustawy z dnia 17 maja 1989 r. o stosunku Państwa do Kościoła Katolickiego w Polskiej Rzeczpospolitej Ludowej oraz postanowień statutu, a także jako osoba prawna posiadająca status organizacji pożytku publicznego , na mocy Ustawy z dnia 24 kwietnia 2003 r. o działalności pożytku publicznego i o wolontariacie.\n " +
            " W ciągu dziewiętnastu lat swojej działalności Fundacja objęła opieką kilka tysięcy młodych ludzi z całej Polski zgodnie z wezwaniem Ojca Świętego do „nowej wyobraźni miłosierdzia”. Około 2000 stypendystów niemalże z każdego zakątka Polski tworzy niezwykły pomnik, budowany przez Polaków wdzięcznych św. Janowi Pawłowi II.\n \n \n" +
            "Dzień Papieski \n \n \n" +
            "  Upamiętnienie pontyfikatu Papieża Polaka, do którego została powołana Fundacja, dokonuje się poprzez organizowanie corocznego Dnia Papieskiego. Stanowi on okazję do ponownego odczytania papieskiego nauczania. Zadanie to jest niezwykle ważne szczególnie po kanonizacji Ojca Świętego. Obchody Dnia Papieskiego ukazują osobę i dzieło św. Jana Pawła II młodemu pokoleniu, które nie miało już możliwości osobistego spotkania Świętego Papieża Polaka. Dzień Papieski obchodzony jest każdego roku, w niedzielę poprzedzającą wybór Karola Wojtyły na Stolicę Piotrową.\n \n \n" +
            "Nagrody TOTUS TUUS \n \n \n " +
            "  Osoby i instytucje, które w sposób szczególny przyczyniają się do promocji nauczania św. Jana Pawła II lub poprzez swoje działania realizują wezwanie Papieża Polaka do obrony godności człowieka, mają szansę zdobyć najważniejszą nagrodę Kościoła katolickiego w Polsce – Nagrodę TOTUS TUUS. Fundacja „Dzieło Nowego Tysiąclecia” przyznaje nagrody od 2000 roku. Laureatami „Katolickich Nobli” są ludzie, którzy oddali się służbie prawdzie i dobru, wyciągający pomocną dłoń zmarginalizowanym czy też dzielący się ze swoimi czytelnikami, słuchaczami lub widzami słowami św. Jana Pawła II. Inni pielęgnują i promują wartości chrześcijańskie – rdzeń naszej kultury i tożsamości. Nagrody TOTUS TUUS wręczane są co roku w wigilię Dnia Papieskiego podczas uroczystej Gali na Zamku Królewskim w Warszawie.\n\n\n" +
            "Program stypendialny\n\n\n" +
            "  Jednym z najważniejszych działań Fundacji jest prowadzony od samego początku jej istnienia program stypendialny. Stypendia przyznawane są zdolnej młodzieży pochodzącej z niezamożnych rodzin z małych miast i terenów wiejskich w oparciu o zatwierdzony przez Zarząd Fundacji Regulamin przyznawania stypendiów edukacyjnych Fundacji „Dzieło Nowego Tysiąclecia”. Fundacja rozpoczynała swoją działalność, przyznając stypendia 500 osobom. Dziś, dzięki hojności Polaków, program obejmuje prawie 2000 młodych ludzi z całej Polski, co jest możliwe tylko dzięki ofiarności i dobrej woli społeczeństwa.\n\n\n" +
            "Obozy formacyjno-integracyjne\n\n\n" +
            "  Pomysł wakacyjnego spotkania wszystkich stypendystów zrodził się w 2001 roku. Wówczas stypendium otrzymywało 500 stypendystów z 5 diecezji. Aktualnie, ze względu na dużą liczbę stypendystów, organizowane są trzy letnie obozy dla każdej z grup wiekowych: dla uczniów szkół podstawowych, gimnazjów i szkół średnich, dla studentów oraz dla maturzystów – przyszłych studentów, dla których obóz jest okazją do poznania ludzi ze swoich wspólnot akademickich i zdobycia informacji o mieście, w którym będą studiować i mieszkać. Obozy formacyjno-integracyjne stanowią integralną część programu stypendialnego.\n\n\n" +
            "";

    protected static final String nasz_patron = "Coś i Janie Pawle II";

    protected static final String dla_daroczyncy = "Coś dla Daroczyńcy";
}




