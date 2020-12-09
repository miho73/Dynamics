package com.github.miho73.physics.main;

import com.github.miho73.physics.physics.Particle;
import com.github.miho73.physics.physics.Physics;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Random;
import java.util.Vector;

public class Chamber extends JPanel {
    private Dimension screenSize = new Dimension(500, 500);
    public Vector<Particle> particles = new Vector<>();
    public Vector<Particle> AddQueue = new Vector<>();
    public int prevX = -1, prevY = -1;
    public String SETTING_FILE_PATH;
    private JSONArray manualParticle = null;
    public int TraceId = -1;

    public boolean Gravity;
    public int RefreshTime;
    public boolean AutoParticleGen;
    public int NumberOfParticles;

    private void LoadProperties() throws ParseException, IOException, NullPointerException {
        JSONParser parser = new JSONParser();
        JSONObject settingsRoot = (JSONObject) parser.parse(new FileReader(SETTING_FILE_PATH));
        Gravity = (boolean)settingsRoot.get("Gravity");
        RefreshTime = Long.valueOf((long) settingsRoot.get("refresh")).intValue();
        AutoParticleGen = (boolean)settingsRoot.get("ParticleAutoGeneration");
        if(!AutoParticleGen) {
            if(!settingsRoot.containsKey("Particles")) throw new InvalidPropertiesFormatException("JSON specified to use manual particle but does not contain particle data.");
            manualParticle = (JSONArray) settingsRoot.get("Particles");
        }
        else {
            NumberOfParticles = Long.valueOf((long) settingsRoot.get("NumberOfParticles")).intValue();
        }
    }

    public Chamber(String initFile) throws IOException, ParseException {
        SETTING_FILE_PATH = initFile;
        LoadProperties();
        if(AutoParticleGen) {
            for (int i=0; i<NumberOfParticles; i++) {
                Particle particle = new Particle();
                particle.setLocation(
                        new Random().nextInt(screenSize.width),
                        new Random().nextInt(screenSize.height)
                );
                particle.color = Color.white;
                particle.id = i;
                particle.HorizontalV = new Random().nextInt(20)-10;
                particle.VerticalV = new Random().nextInt(20)-10;
                particles.add(particle);
            }
        }
        else {
            int cnt = 0;
            for (Object o : manualParticle) {
                JSONObject part = (JSONObject)o;
                Particle particle = new Particle();
                particle.setLocation(
                        Long.valueOf((long) part.get("LocationX")).intValue(),
                        Long.valueOf((long) part.get("LocationY")).intValue()
                );
                particle.VerticalV = Double.parseDouble(part.get("VerticalV").toString());
                particle.HorizontalV = Double.parseDouble(part.get("HorizontalV").toString());
                String cCode = (String)part.get("Color");
                particle.color = new Color(
                    Integer.parseInt(cCode.substring(0, 2), 16),
                    Integer.parseInt(cCode.substring(2, 4), 16),
                    Integer.parseInt(cCode.substring(4, 6), 16)
                );
                particle.id = cnt;
                cnt++;
                particles.add(particle);
            }
        }
    }

    public void UpdateParticlesNumber() {
        Main.me.particles.setText("The number of particles: "+particles.size());
    }

    UpdateFrame updateFrame;
    public void StartPhysics() {
        updateFrame = new UpdateFrame();
        updateFrame.start();
    }

    public void pause() {
        updateFrame.interrupt();
    }
    public void start() {
        updateFrame = new UpdateFrame();
        updateFrame.start();
    }

    //TODO: ERROR, When initial particle is zero, particles never appear
    private class UpdateFrame extends Thread {
        private Physics physics = new Physics(getWidth(), getHeight(), particles, Gravity);
        @Override
        public void run() {
            try {
                while(true) {
                    physics.updateParticle();
                    physics.updatePosition();
                    repaint();

                    if(!AddQueue.isEmpty()) {
                        particles.addAll(AddQueue);
                        AddQueue.clear();
                        UpdateParticlesNumber();
                    }

                    if(TraceId != -1) {
                        if(particles.size()<=TraceId) {
                            Main.me.locationd.setText("NO DATA");
                            Main.me.velocityd.setText("NO DATA");
                        }
                        else {
                            Particle pa = particles.elementAt(TraceId);
                            if (pa==null) {
                                Main.me.locationd.setText("NO DATA");
                                Main.me.velocityd.setText("NO DATA");
                            }
                            else {
                                Main.me.locationd.setText("Location: ("+pa.getX()+", "+pa.getY()+")");
                                Main.me.velocityd.setText("Velocity: ("+Math.round(pa.HorizontalV*100)/100+", "+Math.round(pa.VerticalV*100)/100+")");
                            }
                        }
                    }

                    Thread.sleep(RefreshTime);
                }
            }
            catch (InterruptedException e) {}
        }
    }

    public void UpdateNext() {
        Physics physics = new Physics(getWidth(), getHeight(), particles, Gravity);
        physics.updateParticle();
        physics.updatePosition();

        UpdateBasic();
    }

    public void UpdateBasic() {
        repaint();
        if(!AddQueue.isEmpty()) {
            particles.addAll(AddQueue);
            AddQueue.clear();
            UpdateParticlesNumber();
        }

        if(TraceId != -1) {
            if(particles.size()<=TraceId) {
                Main.me.locationd.setText("NO DATA");
                Main.me.velocityd.setText("NO DATA");
            }
            else {
                Particle pa = particles.elementAt(TraceId);
                if (pa==null) {
                    Main.me.locationd.setText("NO DATA");
                    Main.me.velocityd.setText("NO DATA");
                }
                else {
                    Main.me.locationd.setText("Location: ("+pa.getX()+", "+pa.getY()+")");
                    Main.me.velocityd.setText("Velocity: ("+Math.round(pa.HorizontalV*100)/100+", "+Math.round(pa.VerticalV*100)/100+")");
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        //g.setColor(Color.white);
        particles.forEach((particle -> {
            if(particle.id == TraceId) {
                g.setColor(Color.pink);
                g.fillOval(particle.getX(), particle.getY(), 8, 8);
            }
            else {
                g.setColor(particle.color);
                g.fillOval(particle.getX(), particle.getY(), 5, 5);
            }
        }));
        if(prevY == -1 || prevX == -1) return;
        g.setColor(Color.red);
        g.drawOval(prevX, prevY, 5, 5);
    }
}
