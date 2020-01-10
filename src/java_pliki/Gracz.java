package java_pliki;

import java.io.Serializable;

public class Gracz implements Serializable {
    private String nazwa_gracza;
    private int numer_gracza;
    private int liczba_pionkow_w_bazie = 4;
    private int liczba_pionkow_na_finishu = 0;
    private Pionek []tablica_pionkow = new Pionek[4];

    private int[] tablica_pol_startowych = new int [4];

    public Gracz() {
    }

    public Gracz(String nazwa_gracza, int nr){
        this.nazwa_gracza = nazwa_gracza;
        this.numer_gracza = nr;

        switch (this.numer_gracza) {
            case 0:
                for (int i = 0; i < 4; i ++){
                    this.tablica_pol_startowych[i] = i;
                }
                break;
            case 1:
                for (int i = 0; i < 4; i ++){
                    this.tablica_pol_startowych[i] = i+4;
                }
                break;
            case 2:
                for (int i = 0; i < 4; i ++){
                    this.tablica_pol_startowych[i] = i+8;
                }
                break;
            case 3:
                for (int i = 0; i < 4; i ++){
                    this.tablica_pol_startowych[i] = i+12;
                }
                break;
        }
        for (int i = 0; i < 4; i++){
            tablica_pionkow[i] = new Pionek(i, nazwa_gracza, i+(4*nr));
        }
    }


    @Override
    public String toString() {
        return "java_pliki.Gracz { " +
                "kolor = " + nazwa_gracza + '\'' +
                ", numer gracza = " + numer_gracza +
                ", liczba pionkow w bazie = " + liczba_pionkow_w_bazie +
                ", liczba pionkow na finishu = " + liczba_pionkow_na_finishu + " }";
    }

    public String getNazwa_gracza() {
        return nazwa_gracza;
    }

    public void setNazwa_gracza(String nazwa_gracza) {
        this.nazwa_gracza = nazwa_gracza;
    }

    public int getNumer_gracza() {
        return numer_gracza;
    }

    public void setNumer_gracza(int numer_gracza) {
        this.numer_gracza = numer_gracza;
    }

    public int getLiczba_pionkow_w_bazie() {
        return liczba_pionkow_w_bazie;
    }

    public void setLiczba_pionkow_w_bazie(int liczba_pionkow_w_bazie) {
        this.liczba_pionkow_w_bazie = liczba_pionkow_w_bazie;
    }

    public int getLiczba_pionkow_na_finishu() {
        return liczba_pionkow_na_finishu;
    }

    public void setLiczba_pionkow_na_finishu(int liczba_pionkow_na_finishu) {
        this.liczba_pionkow_na_finishu = liczba_pionkow_na_finishu;
    }

    public Pionek[] getTablica_pionkow() {
        return tablica_pionkow;
    }

    public void setTablica_pionkow(Pionek[] tablica_pionkow) {
        this.tablica_pionkow = tablica_pionkow;
    }

    public int[] getTablica_pol_startowych() {
        return tablica_pol_startowych;
    }

    public void setTablica_pol_startowych(int[] tablica_pol_startowych) {
        this.tablica_pol_startowych = tablica_pol_startowych;
    }
}
