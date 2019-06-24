package com.example.helloworld;

import android.os.AsyncTask;

public class AktualizacjaDanych {




    public void aktualizuj() {

        new Polaczenie(1).execute("http://students.mimuw.edu.pl/~lk406698/FDNT/materialy_prasowe/");
        new Polaczenie(2).execute("http://students.mimuw.edu.pl/~lk406698/FDNT/formacja/");
        new Polaczenie(3).execute("http://students.mimuw.edu.pl/~lk406698/FDNT/ogl_ogolne/");
        new Polaczenie(4).execute("http://students.mimuw.edu.pl/~lk406698/FDNT/ogl_wspolnotowe/");
        new Polaczenie(5).execute("http://students.mimuw.edu.pl/~lk406698/FDNT/materialy/");

    }

    private class Polaczenie extends AsyncTask<String, Void, String> {


        private int któraZakładka;

        private Polaczenie(int i) {
            któraZakładka = i;
        }

        @Override
        protected String doInBackground(String... urls) {

            PobieranieTresci p = new PobieranieTresci();
            return p.pobierz(urls[0]);
        }

        // onPostExecute displays the results of the doInBackgroud and also we
        // can hide progress dialog.
        @Override
        protected void onPostExecute(String result) {

            switch (któraZakładka) {

                case 1:
                    Dane.materiałyPrasowe = result;
                    break;
                case 2:
                    Dane.formacja = result;
                    break;
                case 3:
                    Dane.oglOgólne = result;
                    break;
                case 4:
                    Dane.oglWspólnotowe = result;
                    break;
                case 5:
                    Dane.materiały = result;
                    break;
            }

        }
    }
}
