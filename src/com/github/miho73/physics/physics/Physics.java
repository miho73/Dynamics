package com.github.miho73.physics.physics;

import java.util.Vector;

public class Physics {
    public final int FPS = 100;
    public final double COLLISION_D = 5.0;
    private Vector<Particle> particles;
    private boolean Gravity;

    public Physics(int rWall,  int dWall, Vector<Particle> particles, boolean gravity) {
        rightWall = rWall;
        downWall = dWall;
        this.particles = particles;
        Gravity = gravity;
    }

    private int rightWall, downWall;

    public void updateParticle() {
        for (Particle particle : particles) {
            //GRAVITY
            if(Gravity) particle.VerticalV += Constants.GRAVITY_ACCELERATION/FPS;
            //Collision
            for (Particle collisionPeer : particles) {
                if(particle.id == collisionPeer.id) continue;
                double distance = MathLib.DistencePoint(particle.getLocation(), collisionPeer.getLocation());
                if(distance <= COLLISION_D) {

                    double HorizontalVMean = (particle.HorizontalV+collisionPeer.HorizontalV)/2;
                    double VerticalVMean = (particle.VerticalV+collisionPeer.VerticalV)/2;
                    particle.VerticalV = VerticalVMean;
                    collisionPeer.VerticalV = VerticalVMean;
                    particle.HorizontalV = HorizontalVMean;
                    collisionPeer.HorizontalV = HorizontalVMean;
                }
            }
        }
    }

    public void updatePosition() {
        for (Particle particle : particles) {
            int x = new Long(Math.round(particle.getX()+particle.HorizontalV)).intValue();
            int y = new Long(Math.round(particle.getY()+particle.VerticalV)).intValue();
            //Boundary
            if(x<0) {
                particle.HorizontalV = -particle.HorizontalV;
                x = new Long(Math.round(particle.getX()+particle.HorizontalV)).intValue();
            }
            else if(y<0) {
                particle.VerticalV = -particle.VerticalV;
                y = new Long(Math.round(particle.getY()+particle.VerticalV)).intValue();
            }
            else if(downWall-7 <= particle.getY() && particle.getY() <= downWall+7) {
                particle.VerticalV = -particle.VerticalV;
                y = new Long(Math.round(particle.getY()+particle.VerticalV)).intValue();
            }
            else if(particle.getX()>=rightWall) {
                particle.HorizontalV = -particle.HorizontalV;
                x = new Long(Math.round(particle.getX()+particle.HorizontalV)).intValue();
            }
            particle.setLocation(x, y);
        }
    }
}
