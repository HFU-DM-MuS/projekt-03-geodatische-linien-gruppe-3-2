package utils;

public class Settings {
    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 800;

    public static int FPS = 60;
    public static int TPF = 1000 / FPS;

    public static double TIMESCALE = 1.0;

    // projection settings
    public static double PROJECTION_ALPHA = 135.0;
    public static double PROJECTION_S1 = 1 / Math.sqrt(2);

    // globe settings
    public static double GLOBE_SCALE = 200.0;
    public static double GLOBE_ROTATION = 0.0;
    public static boolean GLOBE_SHOW_WIREFRAME = true;

    // start and end coordinates
    public static double COORD_START_LAT = 0.0;
    public static double COORD_START_LONG = 0.0;
    public static double COORD_END_LAT = 0.0;
    public static double COORD_END_LONG = 0.0;
    public static boolean COORD_SHOW_ON_GLOBE = false;

    // debug
    public static boolean DRAW_DEBUG_INFO = false;
    public static double FLIGHT_PROGRESS = 0.0;
    public static double FLIGHT_SPEED = 0.1;
    public static double FLIGHT_DURATION = 3.0;
    public static double FLIGHT_START_TIME = 0.0;


}
