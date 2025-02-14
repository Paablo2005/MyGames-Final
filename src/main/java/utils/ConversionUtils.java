package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilidades para la conversión de nombres de plataformas a sus identificadores correspondientes.
 * <p>
 * Esta clase contiene un mapa estático que asocia el nombre de una plataforma con su identificador,
 * el cual es utilizado por la API RAWG para filtrar o buscar juegos según la plataforma.
 * </p>
 */
public class ConversionUtils {

    /**
     * Mapa que asocia el nombre de la plataforma con su identificador en formato de cadena.
     */
    public static final Map<String, String> PLATFORM_MAP = new HashMap<>();
    
    static {
        PLATFORM_MAP.put("PC", "4");
        PLATFORM_MAP.put("PlayStation 5", "187");
        PLATFORM_MAP.put("Xbox One", "1");
        PLATFORM_MAP.put("PlayStation 4", "18");
        PLATFORM_MAP.put("Xbox Series S/X", "186");
        PLATFORM_MAP.put("Nintendo Switch", "7");
        PLATFORM_MAP.put("iOS", "3");
        PLATFORM_MAP.put("Android", "21");
        PLATFORM_MAP.put("Nintendo 3DS", "8");
        PLATFORM_MAP.put("Nintendo DS", "9");
        PLATFORM_MAP.put("Nintendo DSi", "13");
        PLATFORM_MAP.put("macOS", "5");
        PLATFORM_MAP.put("Linux", "6");
        PLATFORM_MAP.put("Xbox 360", "14");
        PLATFORM_MAP.put("Xbox", "80");
        PLATFORM_MAP.put("PlayStation 3", "16");
        PLATFORM_MAP.put("PlayStation 2", "15");
        PLATFORM_MAP.put("PlayStation", "27");
        PLATFORM_MAP.put("PS Vita", "19");
        PLATFORM_MAP.put("PSP", "17");
        PLATFORM_MAP.put("Wii U", "10");
        PLATFORM_MAP.put("Wii", "11");
        PLATFORM_MAP.put("GameCube", "105");
        PLATFORM_MAP.put("Nintendo 64", "83");
        PLATFORM_MAP.put("Game Boy Advance", "24");
        PLATFORM_MAP.put("Game Boy Color", "43");
        PLATFORM_MAP.put("Game Boy", "26");
        PLATFORM_MAP.put("SNES", "79");
        PLATFORM_MAP.put("NES", "49");
        PLATFORM_MAP.put("Classic Macintosh", "55");
        PLATFORM_MAP.put("Apple II", "41");
        PLATFORM_MAP.put("Commodore / Amiga", "166");
        PLATFORM_MAP.put("Atari 7800", "28");
        PLATFORM_MAP.put("Atari 5200", "31");
        PLATFORM_MAP.put("Atari 2600", "23");
        PLATFORM_MAP.put("Atari Flashback", "22");
        PLATFORM_MAP.put("Atari 8-bit", "25");
        PLATFORM_MAP.put("Atari ST", "34");
        PLATFORM_MAP.put("Atari Lynx", "46");
        PLATFORM_MAP.put("Atari XEGS", "50");
        PLATFORM_MAP.put("Genesis", "167");
        PLATFORM_MAP.put("SEGA Saturn", "107");
        PLATFORM_MAP.put("SEGA CD", "119");
        PLATFORM_MAP.put("SEGA 32X", "117");
        PLATFORM_MAP.put("SEGA Master System", "74");
        PLATFORM_MAP.put("Dreamcast", "106");
        PLATFORM_MAP.put("3DO", "111");
        PLATFORM_MAP.put("Jaguar", "112");
        PLATFORM_MAP.put("Game Gear", "77");
        PLATFORM_MAP.put("Neo Geo", "12");
        PLATFORM_MAP.put("Web", "171");
    }
}
