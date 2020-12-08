package com.gihhub.miho73.physics.physics;

import java.awt.*;

public class MathLib {
    public static double pow(double base, int power) {
        if(power == 0) return 1;
        else if (power == 1) return base;
        else if (power % 2 == 0) {
            double n = pow(base, power/2);
            return n*n;
        }
        else {
            double n = pow(base, (power-1)/2);
            return n*n*base;
        }
    }
    public static double DistencePoint(Point p1, Point p2) {
        return Math.sqrt(pow(p1.x-p2.x, 2)+pow(p1.y-p2.y, 2));
    }
}
