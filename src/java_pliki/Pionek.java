package java_pliki;

public class Pionek implements java.io.Serializable  {

    public Pionek() {
    }

    @Override

    public String toString() {
        return "java_pliki.Pionek{ " +
                "numer pionka = " + numer_pionka +
                ", kolor pionka = '" + nazwa_wlasciciela + '\'' +
                ", numer pola = " + numer_pola +
                " }";
    }

    private int numer_pionka;
    private String nazwa_wlasciciela;
    private int numer_pola;

    public Pionek(int a, String b, int c){
        numer_pionka = a;
        nazwa_wlasciciela = b;
        numer_pola = c;
    }

    public int getNumer_pionka() {
        return numer_pionka;
    }

    public String getNazwa_wlasciciela() {
        return nazwa_wlasciciela;
    }

    public void setNumer_pola(int numer_pola) {
        this.numer_pola = numer_pola;
    }

    public int getNumer_pola() {
        return numer_pola;
    }
}
