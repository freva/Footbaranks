package com.limon.clubelo.clubelobrowser.data;

import com.limon.clubelo.clubelobrowser.R;

import java.util.HashMap;

public enum Country {
    ALL(R.drawable.flag_eu,             "ALL",    "All"),
    ALBANIA(R.drawable.flag_alb,        "ALB", "Albania"),
    ANDORRA(R.drawable.flag_and,        "AND", "Andorra"),
    ARMENIA(R.drawable.flag_arm,        "ARM", "Armenia"),
    AUSTRIA(R.drawable.flag_aut,        "AUT", "Austria"),
    AZERBAIJAN(R.drawable.flag_aze,     "AZE", "Azerbaijan"),
    BELGIUM(R.drawable.flag_bel,        "BEL", "Belgium"),
    BOSNIA(R.drawable.flag_bhz,         "BHZ", "Bosnia-Herzegovina"),
    BELARUS(R.drawable.flag_blr,        "BLR", "Belarus"),
    BULGARIA(R.drawable.flag_bul,       "BUL", "Bulgaria"),
    CROATIA(R.drawable.flag_cro,        "CRO", "Croatia"),
    CZECHOSLOVAKIA(R.drawable.flag_csr, "CSR", "Czechoslovakia"),
    CYPRUS(R.drawable.flag_cyp,         "CYP", "Cyprus"),
    CZECH_REP(R.drawable.flag_cze,      "CZE", "Czech Republic"),
    DENMARK(R.drawable.flag_den,        "DEN", "Denmark"),
    ENGLAND(R.drawable.flag_eng,        "ENG", "England"),
    SPAIN(R.drawable.flag_esp,          "ESP", "Spain"),
    ESTONIA(R.drawable.flag_est,        "EST", "Estonia"),
    FAROES(R.drawable.flag_far,         "FAR", "Faroe Islands"),
    FINLAND(R.drawable.flag_fin,        "FIN", "Finland"),
    FRANCE(R.drawable.flag_fra,         "FRA", "France"),
    WEST_GERMANY(R.drawable.flag_frg,   "FRG", "England"),
    EAST_GERMANY(R.drawable.flag_gdr,   "GDR", "East Germany"),
    GEORGIA(R.drawable.flag_geo,        "GEO", "Georgia"),
    GERMANY(R.drawable.flag_ger,        "GER", "Germany"),
    GIBRALTAR(R.drawable.flag_gib,      "GIB", "Gibraltar"),
    GREECE(R.drawable.flag_gre,         "GRE", "Greece"),
    HUNGARY(R.drawable.flag_hun,        "HUN", "Hungary"),
    IRELAND(R.drawable.flag_irl,        "IRL", "Republic of Ireland"),
    ICELAND(R.drawable.flag_isl,        "ISL", "Iceland"),
    ISRAEL(R.drawable.flag_isr,         "ISR", "Israel"),
    ITALY(R.drawable.flag_ita,          "ITA", "Italy"),
    KAZAKHSTAN(R.drawable.flag_kaz,     "KAZ", "Kazakhstan"),
    LATVIA(R.drawable.flag_lat,         "LAT", "Latvia"),
    LIECHTENSTEIN(R.drawable.flag_lie,  "LIE", "Liechtenstein"),
    LITHUANIA(R.drawable.flag_lit,      "LIT", "Lithuania"),
    LUXEMBOURG(R.drawable.flag_lux,     "LUX", "Luxembourg"),
    MACEDONIA(R.drawable.flag_mac,      "MAC", "FYR Macedonia"),
    MALTA(R.drawable.flag_mlt,          "MLT", "Malta"),
    MONTENEGRO(R.drawable.flag_mnt,     "MNT", "Montenegro"),
    MOLDOVA(R.drawable.flag_mol,        "MOL", "Moldova"),
    NETHERLANDS(R.drawable.flag_ned,    "NED", "Netherlands"),
    NORTHERN_IRELAND(R.drawable.flag_nir,"NIR", "Northern Ireland"),
    NORWAY(R.drawable.flag_nor,         "NOR", "Norway"),
    POLAND(R.drawable.flag_pol,         "POL", "Poland"),
    PORTUGAL(R.drawable.flag_por,       "POR", "Portugal"),
    ROMANIA(R.drawable.flag_rom,        "ROM", "Romania"),
    RUSSIA(R.drawable.flag_rus,         "RUS", "Russia"),
    SERBIA_MONTENEGRO(R.drawable.flag_scg,"SCG", "Serbia and Montenegro"),
    SCOTLAND(R.drawable.flag_sco,       "SCO", "Scotland"),
    SLOVAKIA(R.drawable.flag_slk,       "SLK", "Slovakia"),
    SAN_MARINO(R.drawable.flag_smr,     "SMR", "San Marino"),
    SERBIA(R.drawable.flag_srb,         "SRB", "Serbia"),
    SWITZERLAND(R.drawable.flag_sui,    "SUI", "Switzerland"),
    SLOVENIA(R.drawable.flag_svn,       "SVN", "Poland"),
    SWEDEN(R.drawable.flag_swe,         "SWE", "Norway"),
    TURKEY(R.drawable.flag_tur,         "TUR", "Turkey"),
    UKRAINE(R.drawable.flag_ukr,        "UKR", "Ukraine"),
    SOVIET_UNION(R.drawable.flag_urs,   "URS", "Soviet Union"),
    YUGOSLAVIA(R.drawable.flag_yug,     "YUG", "Yugoslavia"),
    WALES(R.drawable.flag_wal,          "WAL", "Wales"),
    CHAMPIONS_LEAGUE(R.drawable.flag_ucl,"UCL", "Champions League"),
    EUROPA_LEAGUE(R.drawable.flag_uel,  "UEL", "Europa League");

    private static HashMap<String, Country> countryList = new HashMap<>();

    static {
        for(Country county: Country.values()) {
            countryList.put(county.getCountryCode(), county);
        }
    }


    private int flagID;
    private String countryCode, countryName;
    Country(int flagID, String countryCode, String countryName) {
        this.flagID = flagID;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public int getFlagID() {
        return flagID;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public static Country getCountry(String countryCode) {
        return countryList.get(countryCode);
    }
}
