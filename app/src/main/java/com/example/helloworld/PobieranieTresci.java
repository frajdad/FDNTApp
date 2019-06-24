package com.example.helloworld;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PobieranieTresci {

	public String pobierz(String adres) {
		
		Connection connect = Jsoup.connect(adres);

		try {
            Document document = connect.get();
            Elements t = document.select("body");
            for(Element elem: t) {
                return elem.text();
            }
            return "Pusta strona";
		}
		catch (IOException e) {
			return "Błąd połączenia";
		}

	}
}
