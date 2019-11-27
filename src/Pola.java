public class Pola implements java.io.Serializable {

    private String barwa_pola;

    public String getBarwa_pola() {
        return barwa_pola;
    }

    public void setBarwa_pola(String barwa_pola) {
        this.barwa_pola = barwa_pola;
    }

    public int getNumer_pola() {
        return numer_pola;
    }

    public void setNumer_pola(int numer_pola) {
        this.numer_pola = numer_pola;
    }

    public Pionek getStojacy_na_nim_pionek() {
        return stojacy_na_nim_pionek;
    }

    public void setStojacy_na_nim_pionek(Pionek stojacy_na_nim_pionek) {
        this.stojacy_na_nim_pionek = stojacy_na_nim_pionek;
    }

    private int numer_pola;
    private Pionek stojacy_na_nim_pionek;

    public Pola(String barwa_pola, int numer_pola, Pionek stojacy_na_nim_pionek) {
        this.barwa_pola = barwa_pola;
        this.numer_pola = numer_pola;
        this.stojacy_na_nim_pionek = stojacy_na_nim_pionek;
    }

    @Override
    public String toString() {
        return "Pole{ " +
                "barwa pola = '" + barwa_pola + '\'' +
                ", numer pola = " + numer_pola +
                ", stojacy na nim pionek = " + stojacy_na_nim_pionek +
                " } ";
    }
}
