package com.limon.clubelo.clubelobrowser.data;

import com.limon.clubelo.clubelobrowser.R;

import java.util.ArrayList;
import java.util.HashMap;

public enum League {
    ALB_O(R.drawable.logo_league_alb_0, Country.ALBANIA,    0, "Kategoria Superiore"),
    AUT_0(R.drawable.logo_league_aut_0, Country.AUSTRIA,    0, "Bundesliga"),
    BEL_0(R.drawable.logo_league_bel_0, Country.BELGIUM,    0, "Pro League"),
    BUL_0(R.drawable.logo_league_bul_0, Country.BULGARIA,   0, "A Group"),
    CRO_0(R.drawable.logo_league_cro_0, Country.CROATIA,    0, "Prva HNL"),
    CZE_0(R.drawable.logo_league_cze_0, Country.CZECH_REP,  0, "1. Liga"),
    DEN_0(R.drawable.logo_league_den_0, Country.DENMARK,    0, "Superligaen"),
    ENG_0(R.drawable.logo_league_eng_0, Country.ENGLAND,    0, "Premier League"),
    ENG_1(R.drawable.logo_league_eng_1, Country.ENGLAND,    1, "The Championship"),
    FRA_0(R.drawable.logo_league_fra_0, Country.FRANCE,     0, "Ligue 1"),
    FRA_1(R.drawable.logo_league_fra_1, Country.FRANCE,     1, "Ligue 2"),
    GER_0(R.drawable.logo_league_ger_0, Country.GERMANY,    0, "Bundesliga"),
    GER_1(R.drawable.logo_league_ger_0, Country.GERMANY,    1, "2. Bundesliga"),
    GRE_0(R.drawable.logo_league_gre_0, Country.GREECE,     0, "Superleague"),
    ISR_0(R.drawable.logo_league_isr_0, Country.ISRAEL,     0, "Premier League"),
    ITA_0(R.drawable.logo_league_ita_0, Country.ITALY,      0, "Serie A"),
    ITA_1(R.drawable.logo_league_ita_1, Country.ITALY,      1, "Serie B"),
    NED_0(R.drawable.logo_league_ned_0, Country.NETHERLANDS,0, "Eredivisie"),
    NOR_0(R.drawable.logo_league_nor_0, Country.NORWAY,     0, "Tippeligaen"),
    POL_0(R.drawable.logo_league_pol_0, Country.POLAND,     0, "Ekstraklasa"),
    POR_0(R.drawable.logo_league_por_0, Country.PORTUGAL,   0, "Primeira Liga"),
    ROM_0(R.drawable.logo_league_rom_0, Country.ROMANIA,    0, "Liga 1"),
    RUS_0(R.drawable.logo_league_rus_0, Country.RUSSIA,     0, "Premier League"),
    SCO_0(R.drawable.logo_league_sco_0, Country.SCOTLAND,   0, "Premier League"),
    SRB_0(R.drawable.logo_league_srb_0, Country.SERBIA,     0, "SuperLiga"),
    ESP_0(R.drawable.logo_league_esp_0, Country.SPAIN,      0, "Primera División"),
    ESP_1(R.drawable.logo_league_esp_1, Country.SPAIN,      1, "Segunda División"),
    SWE_0(R.drawable.logo_league_swe_0, Country.SWEDEN,     0, "Allsvenskan"),
    SUI_0(R.drawable.logo_league_sui_0, Country.SWITZERLAND,0, "Super League"),
    TUR_0(R.drawable.logo_league_tur_0, Country.TURKEY,     0, "Süper Lig"),
    UKR_0(R.drawable.logo_league_ukr_0, Country.UKRAINE,    0, "Premier League"),
    EUR_0(R.drawable.flag_ucl,          Country.ALL,        0, "Champions League"),
    EUR_1(R.drawable.flag_uel,          Country.ALL,        1, "Europa League");

    private static HashMap<String, ArrayList<League>> leagueMap = new HashMap<>();

    static {
        for(League league: League.values()) {
            if(! leagueMap.containsKey(league.getCountry().getCountryCode())) {
                leagueMap.put(league.getCountry().getCountryCode(), new ArrayList<League>());
            }

            leagueMap.get(league.getCountry().getCountryCode()).add(league);
        }
    }


    private int logoID, level;
    private Country country;
    private String leagueName;
    League(int logoID, Country country, int level, String leagueName) {
        this.logoID = logoID;
        this.country = country;
        this.level = level;
        this.leagueName = leagueName;
    }

    public int getLogoID() {
        return logoID;
    }

    public int getLevel() {
        return level;
    }

    public Country getCountry() {
        return country;
    }

    public String getLeagueName() {
        return leagueName;
    }


    public static League getLeague(String countryCode, int level) {
        if(! leagueMap.containsKey(countryCode)) return null;
        if(level < 0 || level >= leagueMap.get(countryCode).size()) return null;
        return leagueMap.get(countryCode).get(level);
    }
}
