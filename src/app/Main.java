package app;


import math.Vector;

public class Main {

    public static void main(String[] args) {
        //Window w = new Window();

        Vector v = new Vector(1, 0, 0);
        Vector u = new Vector(0, 1, 0);

        System.out.println(v.dot(u));
    }


}
