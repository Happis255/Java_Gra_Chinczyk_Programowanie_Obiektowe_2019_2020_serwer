public class Gracz implements java.io.Serializable {

    private String kolor;

    private int numer_gracza;
    private int liczba_pionkow_w_bazie = 4;
    private int liczba_pionkow_na_finishu = 0;

    private Pionek tablica_pionkow[] = new Pionek[4];

    public Gracz(String a, int b){

       this.kolor = a;
       this.numer_gracza = b;

       /* Ustawiamy pionki na polach startowych */
       for (int i = 0; i < 4; i++){
           tablica_pionkow[i] = new Pionek(i, a, i + (numer_gracza*4));
       }

       System.out.println("Dodano do gry gracza: " + (this.numer_gracza + 1) + ". Posiada pionki: ");
       for (int i = 0; i < 4; i++){
           System.out.println("- " + tablica_pionkow[i].getKolor_pionka() + " - " + tablica_pionkow[i].getNumer_pionka() + " na polu: " + tablica_pionkow[i].getNumer_pola());
       }
    }

    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
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

    @Override
    public String toString() {
        return "Gracz { " +
                "kolor = " + kolor + '\'' +
             ", numer gracza = " + numer_gracza +
             ", liczba pionkow w bazie = " + liczba_pionkow_w_bazie +
             ", liczba pionkow na finishu = " + liczba_pionkow_na_finishu + " }";
             }
}
