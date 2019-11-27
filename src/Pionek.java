public class Pionek implements java.io.Serializable  {
    @Override

    public String toString() {
        return "Pionek{ " +
                "numer pionka = " + numer_pionka +
                ", kolor pionka = '" + kolor_pionka + '\'' +
                ", numer pola = " + numer_pola +
                " }";
    }

    private int numer_pionka;
    private String kolor_pionka;
    private int numer_pola;

    public Pionek(int a, String b, int c){
        numer_pionka = a;
        kolor_pionka = b;
        numer_pola = c;
    }

    public int getNumer_pionka() {
        return numer_pionka;
    }

    public String getKolor_pionka() {
        return kolor_pionka;
    }

    public void setNumer_pola(int numer_pola) {
        this.numer_pola = numer_pola;
    }

    public int getNumer_pola() {
        return numer_pola;
    }
}
