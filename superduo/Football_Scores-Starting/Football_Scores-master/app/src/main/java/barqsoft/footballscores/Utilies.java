package barqsoft.footballscores;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.LayoutDirection;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import barqsoft.footballscores.sync.FootballScoresSyncAdapter;
import barqsoft.footballscores.util.ConstantUtil;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {
  /* Moved to constants 
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;*/

	/**
	 * 
	 * @param context
	 * @param league_num
	 * @return
	 */
    public static String getLeague(Context context, int league_num) {
        switch (String.valueOf(league_num)) {
           
            case ConstantUtil.SERIE_A:
                return context.getString(R.string.seriaa);
          
            case ConstantUtil.PREMIER_LEAGUE:
                return context.getString(R.string.premierleague);
           
            case ConstantUtil.CHAMPIONS_LEAGUE:
                return context.getString(R.string.champions_league);
           
            case ConstantUtil.PRIMERA_DIVISION:
                return context.getString(R.string.primeradivison);
            case ConstantUtil.BUNDESLIGA1:
            case ConstantUtil.BUNDESLIGA2:
            case ConstantUtil.Bundesliga3:
            	  return context.getString(R.string.bundesliga);
            default:
                return context.getString(R.string.not_known);
        }
    }

    /**
     * 
     * @param context
     * @param match_day
     * @param league_num
     * @return
     */
    public static String getMatchDay(Context context,int match_day, int league_num) {
        //  if (league_num == CHAMPIONS_LEAGUE) {
        if (league_num == Integer.parseInt(ConstantUtil.CHAMPIONS_LEAGUE)) {
            if (match_day <= 6) {
                return context.getString(R.string.group_stages);
            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.first_knockout_round);				//First Knockout round
            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.quarter_final);			//QuarterFinal
            } else if (match_day == 11 || match_day == 12) {
                return  context.getString(R.string.semi_final);			//SemiFinal
            } else {
            	return  context.getString(R.string.final_text);
            }
        } else {
           //return "Matchday : " + String.valueOf(match_day);
        	return String.format(context.getString(R.string.matchday_text),match_day);
        }
    }

    /**
     * 
     * @param home_goals
     * @param awaygoals
     * @return
     */
    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    /**
     * 
     * @param teamname
     * @return
     */
    public static int getTeamCrestByTeamName(String teamname) {
        if (teamname == null) {
            return R.drawable.no_icon;
        }
        switch (teamname) { 
           
            case "Arsenal FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
          
            case "Swansea City FC":
                return R.drawable.swansea_city_afc;
           
            case "Leicester City FC":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
         
            case "West Bromwich Albion FC":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;

            //New Teams added;

            case "AFC Bournemouth":
                return R.drawable.afc_bournemouth;
            case "Aston Villa FC":
                return R.drawable.aston_villa;
            case "Cardiff City FC":
                return R.drawable.cardiff_city;
            case "Chelsea FC":
                return R.drawable.chelsea;
            case "Crystal Palace FC":
                return R.drawable.crystal_palace_fc;
            case "Fulham FC":
                return R.drawable.fulham;
            case "Hull City AFC":
                return R.drawable.hull_city_afc_hd_logo;
            case "Liverpool FC":
                return R.drawable.liverpool;
            case "Manchester City FC":
                return R.drawable.manchester_city;
            case "Newcastle United FC":
                return R.drawable.newcastle_united;
            case "Norwich City FC":
                return R.drawable.norwich_city;
            case "Queens Park Rangers FC":
                return R.drawable.queens_park_rangers_hd_logo;
            case "Southampton FC":
                return R.drawable.southampton_fc;
            case "Watford FC":
                return R.drawable.watford_fc;

            case "Almeria":
                return R.drawable.almeria_hd_logo;
          
            case "Athletic Club":
                return R.drawable.athletic_bilbao_logo;
            case "Club Atlético de Madrid":
                return R.drawable.athletico_madrid_logo;
            case "FC Barcelona":
                return R.drawable.barcelona_fc_logo;
            case "CA Osasuna":
                return R.drawable.ca_osasuna_logo;
            case "RC Celta de Vigo":
                return R.drawable.celta_vigo_logo;
            case "Cordoba CF":
                return R.drawable.cordoba_cf_hd_logo;
            case "RC Deportivo La Coruna":
                return R.drawable.deportivo_la_coruna_logo;
            case "SD Eibar":
                return R.drawable.eibar_sd_hd_logo;
            case "Elche CF":
                return R.drawable.elche_cf_hd_logo;
            case "RCD Espanyol":
                return R.drawable.espanyol_logo;
            case "Getafe CF":
                return R.drawable.getafe_logo;
            case "Granada CF":
                return R.drawable.granada_cf_hd_logo;
            case "Levante UD":
                return R.drawable.levante_ud_logo;
            case "Málaga CF":
                return R.drawable.malaga_cf_logo;
            case "Moghreb Tétouan":
                return R.drawable.moghreb_tetouan_logo;
            case "Rayo Vallecano de Madrid":
                return R.drawable.rayo_vallecano_logo;
            case "Real Betis":
                return R.drawable.real_betis_logo;
            case "Real Madrid CF":
                return R.drawable.real_madrid_logo;
            case "Real Sociedad de Fútbol":
                return R.drawable.real_sociedad_logo;
            case "Sevilla FC":
                return R.drawable.sevilla_logo;
            case "Sporting Gijón":
                return R.drawable.sporting_de_gijon_logo;
            case "UD Las Palmas":
                return R.drawable.ud_las_palmas_logo;
            case "Valencia CF":
                return R.drawable.valencia_logo;
            case "Villarreal CF":
                return R.drawable.villarreal_logo;

            //Bundesliga

            case "Bayer 04 Leverkusen":
                return R.drawable.bayer_leverkusen;
            case "Borussia Dortmund":
                return R.drawable.borussia_dortmund;
            case "Borussia Mönchengladbach":
                return R.drawable.borussia_monchengladbach;
            case "Eintracht Frankfurt":
                return R.drawable.eintracht_frankfurt;
            case "FC Augsburg":
                return R.drawable.fc_augsburg;
            case "FC Bayern Munich":
                return R.drawable.fc_bayem_munich;
            case "FC Koln":
                return R.drawable.fc_koln;
            case "Schalke 04":
                return R.drawable.fc_schalke;
            case "Hamburger SV":
                return R.drawable.hamburger_sv;
            case "Hannover 96":
                return R.drawable.hannover;
            case "Hertha Berlin":
                return R.drawable.hertha_bsc;
            case "Werder Bremen":
                return R.drawable.sv_werder_bremen;
            case "1899 Hoffenheim":
                return R.drawable.tsg_hofferheim;
            case "VfB Stuttgart":
                return R.drawable.vfb_stuttgart;
            case "VfL Wolfsburg":
                return R.drawable.vfl_wolfsburg;

            //Seria A

            case "A.C. Cesena":
                return R.drawable.ac_cesena;
            case "AC Chievo Verona":
                return R.drawable.ac_chievo_verona;
            case "A.S. Livorno Calcio":
                return R.drawable.as_livorno_calcio;
            case "AC Milan":
                return R.drawable.ac_milan;
            case "ACF Fiorentina":
                return R.drawable.acf_florentina;
            case "AS Roma":
                return R.drawable.as_roma;
            case "Atalanta BC":
                return R.drawable.atalanta_bc;
            case "Bologna FC":
                return R.drawable.bologna_fc;
            case "Cagliari Calcio":
                return R.drawable.cagliari_calcio;
            case "Carpi FC":
                return R.drawable.carpi_fc;
            case "Empoli FC":
                return R.drawable.empoli_fc;
            case "Frosinone Calcio":
                return R.drawable.frosinone_calcio;
            case "Genoa CFC":
                return R.drawable.genoa_cfc;
            case "Hellas Verona FC":
                return R.drawable.hellas_verona_fc;
            case "FC Internazionale Milano":
                return R.drawable.inter_milan;
            case "Juventus Turin":
                return R.drawable.juventus_fc;
            case "S.S.D. Parma Calcio 1913":
                return R.drawable.ssd_parma_calcio;
            case "SS Lazio":
                return R.drawable.ss_lazio;
            case "SSC Napoli":
                return R.drawable.ssc_napoli;
            case "Torino FC":
                return R.drawable.torino_fc;
            case "UC Sampdoria":
                return R.drawable.uc_sampdoria;
            case "US Cittá di Palermo":
                return R.drawable.us_citta_di_palermo;
            case "US Sassuolo Calcio":
                return R.drawable.us_sassuolo_calcio;
            case "Udinese Calcio":
                return R.drawable.udinese_calcio;

            default:
                return R.drawable.no_icon;
        }
    }


    /**
     * Check network state before connecting
     * @param context
     * @return
     */
    public static boolean checkNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
       
        return ((networkInfo != null && networkInfo.isConnectedOrConnecting()));
    }

    /**
     * Get network state value intdef value
     * @param context
     * @return
     */
    @SuppressWarnings("ResourceType")
    static public
    @FootballScoresSyncAdapter.NetworkStatus
    int getNetworkState(Context context) {
        String syncStatus = context.getString(R.string.pref_status_key);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(syncStatus, FootballScoresSyncAdapter.SERVER_STATUS_UNKNOWN);

    }

    /**
     * Get fragment index based on layout direction and match date
     * @param context
     * @param date
     * @return
     */
    public static int getFragmentIndex(Context context, String date) {
     
        Calendar todayDate = Calendar.getInstance();
        todayDate.setTime(new Date());
        int position;
        Calendar matchDate = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            matchDate = Calendar.getInstance();
            matchDate.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int layoutDirection = getLayoutDirection(context);
        int diff;
        diff = todayDate.get(Calendar.DAY_OF_YEAR) - matchDate.get(Calendar.DAY_OF_YEAR);
        if (layoutDirection == LayoutDirection.LTR) {
            switch (diff) {
                case -1:
                    position = 3;
                    break;
                case -2:
                    position = 4;
                    break;
                case 0:
                    position = 2;
                    break;

                case 1:
                    position = 1;
                    break;
                case 2:
                    position = 0;
                    break;
                default:
                    position = 2;
                    break;
            }
        } else {
            switch (diff) {
                case -1:
                    position = 1;
                    break;
                case -2:
                    position = 0;
                    break;
                case 0:
                    position = 2;
                    break;

                case 1:
                    position = 3;
                    break;
                case 2:
                    position = 4;
                    break;
                default:
                    position = 2;
                    break;
            }

        }
        return position;
    }

    /**
     * Check layout direction LTR/RTL
     * @param context
     * @return
     */
    public static int getLayoutDirection(Context context) {
        int layoutDirection = LayoutDirection.LTR;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutDirection = context.getResources().getConfiguration().getLayoutDirection();
        }
        return layoutDirection;
    }

    /**
     * Get tab index for RTL
     * @param index
     * @return
     */
    public static int getPositionForRTL(int index) {
        int position = 2;
        switch (index) {
            case 0:
                position = position + 2;
                break;

            case 1:
                position = position + 1;
                break;

            case 2:
                position = 2;
                break;

            case 3:
                position = position - 1;
                break;

            case 4:
                position = position - 2;
                break;

            default:
                position = 2;
                break;
        }
        return position;
    }
    
    /**
     * Convert 24 hr time to 12 hr
     * @param time
     * @return
     */

    public static final String convertTime(String time) {
        String convertedTime = null;
        try {
            SimpleDateFormat _24HrFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HrFormat = new SimpleDateFormat("hh:mm a");
            Date _24HrTime = _24HrFormat.parse(time);
            convertedTime = _12HrFormat.format(_24HrTime);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return convertedTime;
    }
}
