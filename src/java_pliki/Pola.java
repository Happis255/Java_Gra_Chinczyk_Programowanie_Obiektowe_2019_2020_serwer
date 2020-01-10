package java_pliki;

public class Pola implements java.io.Serializable {

    public Pola() {
    }

    private String barwa_pola;
    private int position_x;
    private int position_y;

    public int getPosition_x() {
        return position_x;
    }

    public int getPosition_y() {
        return position_y;
    }

    public void setPosition_x(int x) {
        this.position_x = x;
    }

    public void setPosition_y(int y) {
        this.position_y = y;
    }

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

