package com.gihhub.miho73.physics.main;

import com.gihhub.miho73.physics.physics.Particle;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame {
    public static Main me;

    private final Dimension ChamberSize = new Dimension(500, 500);

    public JLabel particles = new JLabel();
    public JLabel locationd = new JLabel("Enter the ID");
    public JLabel velocityd = new JLabel("Enter the ID");
    private final JTextField vx = new JTextField();
    private final JTextField vy = new JTextField();
    private final JTextField x = new JTextField();
    private final JTextField y = new JTextField();

    public static Chamber chamber;
    public Main()  {
        setLayout(null);

        try {
            chamber = new Chamber();
            chamber.setLocation(0,0);
            chamber.setSize(ChamberSize);
            chamber.setBackground(Color.black);
            //chamber.StartPhysics();
            add(chamber);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        JLabel addParticle = new JLabel("Add a particle");
        addParticle.setFont(new Font("Segoe Print", Font.BOLD, 30));
        addParticle.setLocation(520,10);
        addParticle.setSize(300,30);
        add(addParticle);


        JLabel location = new JLabel("Location: ");
        location.setFont(new Font("Segoe Print", Font.BOLD, 20));
        location.setLocation(550,50);
        location.setSize(100,27);
        add(location);

        x.setFont(new Font("Segoe Print", Font.PLAIN, 20));
        x.setLocation(650,50);
        x.setSize(50,27);
        y.setFont(new Font("Segoe Print", Font.PLAIN, 20));
        y.setLocation(x.getX()+x.getWidth()+5,50);
        y.setSize(50,27);
        x.getDocument().addDocumentListener(new UpdateLocationPrev());
        y.getDocument().addDocumentListener(new UpdateLocationPrev());
        add(x); add(y);


        JLabel velocity = new JLabel("Velocity: ");
        velocity.setFont(new Font("Segoe Print", Font.BOLD, 20));
        velocity.setLocation(550,77);
        velocity.setSize(100,27);
        add(velocity);

        vx.setFont(new Font("Segoe Print", Font.PLAIN, 20));
        vx.setLocation(650,77);
        vx.setSize(50,27);
        vy.setFont(new Font("Segoe Print", Font.PLAIN, 20));
        vy.setLocation(x.getX()+x.getWidth()+5,77);
        vy.setSize(50,27);
        add(vx); add(vy);

        JButton add = new JButton("Add");
        add.setFont(new Font("Segoe Print", Font.BOLD, 20));
        add.setSize(205,27);
        add.setLocation(550, 120);
        add.addActionListener(e -> {
            Particle particle = new Particle();
            particle.VerticalV = Double.parseDouble(vy.getText());
            particle.HorizontalV = Double.parseDouble(vx.getText());
            particle.id = chamber.particles.size()+1;
            particle.setLocation(
                    Integer.parseInt(x.getText()),
                    Integer.parseInt(y.getText())
            );
            chamber.AddQueue.add(particle);
        });
        add.setBackground(Color.white);
        add(add);

        JLabel trace = new JLabel("Trace a particle");
        trace.setFont(new Font("Segoe Print", Font.BOLD, 30));
        trace.setLocation(520,160);
        trace.setSize(300,30);
        add(trace);

        JLabel id = new JLabel("Particle ID: ");
        id.setFont(new Font("Segoe Print", Font.BOLD, 20));
        id.setLocation(520,200);
        id.setSize(130,27);
        add(id);

        JTextField idEnt = new JTextField();
        idEnt.setFont(new Font("Segoe Print", Font.BOLD, 20));
        idEnt.setLocation(640,200);
        idEnt.setSize(100,27);
        add(idEnt);
        idEnt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Update();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                Update();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                Update();
            }
            private void Update() {
                String s = idEnt.getText();
                if(s.equals("")) {
                    chamber.TraceId = -1;
                    locationd.setText("Enter the ID");
                    velocityd.setText("Enter the ID");
                    return;
                }
                chamber.TraceId = Integer.parseInt(s);
            }
        });

        locationd.setFont(new Font("Segoe Print", Font.BOLD, 20));
        locationd.setLocation(540,230);
        locationd.setSize(400,27);
        add(locationd);
        velocityd.setFont(new Font("Segoe Print", Font.BOLD, 20));
        velocityd.setLocation(540,260);
        velocityd.setSize(400,27);
        add(velocityd);

        particles.setFont(new Font("Segoe Print", Font.BOLD, 20));
        particles.setSize(480,20);
        particles.setLocation(10, 505);
        add(particles);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.darkGray);

        JTextField command = new JTextField("PAUSED");
        command.setEditable(false);
        command.setLocation(10, 530);
        command.setSize(140, 25);
        command.setFont(new Font("Segoe Print", Font.BOLD, 14));
        add(command);
        command.addKeyListener(new KeyListener() {
            private boolean isPlaying = false;
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isPlaying = !isPlaying;
                    if(isPlaying) {
                        chamber.start();
                        command.setText("Simulating");
                    }
                    else {
                        chamber.pause();
                        command.setText("Paused");
                    }
                }
                else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    command.setText("Update 1 Frame");
                    chamber.UpdateNext();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    command.setText("Update Data");
                    chamber.UpdateBasic();
                }
                else if(e.getKeyCode() == KeyEvent.VK_R) {
                    try {
                        remove(chamber);
                        Chamber chamberx = new Chamber();
                        chamberx.setLocation(0,0);
                        chamberx.setSize(ChamberSize);
                        chamberx.setBackground(Color.black);
                        add(chamberx);
                        chamberx.UpdateBasic();
                        chamber = chamberx;
                        chamberx.UpdateParticlesNumber();
                        command.setText("Reloaded");
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { }
        });

        setVisible(true);
    }

    private class UpdateLocationPrev implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            Update();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            Update();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            Update();
        }
        private void Update() {
            if(x.getText().equals("") || y.getText().equals("")) {
                chamber.prevY = -1;
                chamber.prevX = -1;
                return;
            }
            chamber.prevX = Integer.parseInt(x.getText());
            chamber.prevY = Integer.parseInt(y.getText());
        }
    }

    public static void main(String[] args) {
        me = new Main();
        chamber.UpdateParticlesNumber();
    }
}
