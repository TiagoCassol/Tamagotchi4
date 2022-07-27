package tamagotchi4;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Input implements KeyListener{
    
    private boolean [] button;
    public boolean escape, test;

    
    public Input (){
        button = new boolean[256];
            }
    
    public void updade(){
        escape = button [KeyEvent.VK_ESCAPE];
        test = button [KeyEvent.VK_SPACE];
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
     
    }

    @Override
    public void keyPressed(KeyEvent e) {
       button[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
      button[e.getKeyCode()] = false;
      if (Tamagotchi4.menu == true && Tamagotchi4.name.length() < 10){
        if (e.getKeyCode() == 65){
            Tamagotchi4.name += "a";
        }
        if (e.getKeyCode() == 66){
            Tamagotchi4.name += "b";
        }
        if (e.getKeyCode() == 67){
            Tamagotchi4.name += "c";
        }
        if (e.getKeyCode() == 68){
            Tamagotchi4.name += "d";
        }
        if (e.getKeyCode() == 69){
            Tamagotchi4.name += "e";
        }
        if (e.getKeyCode() == 70){
            Tamagotchi4.name += "f";
        }
        if (e.getKeyCode() == 71){
            Tamagotchi4.name += "g";
        }
        if (e.getKeyCode() == 72){
            Tamagotchi4.name += "h";
        }
         if (e.getKeyCode() == 73){
            Tamagotchi4.name += "i";
        }
         if (e.getKeyCode() == 74){
            Tamagotchi4.name += "j";
        }
        if (e.getKeyCode() == 75){
            Tamagotchi4.name += "k";
        }
        if (e.getKeyCode() == 76){
            Tamagotchi4.name += "l";
        }
        if (e.getKeyCode() == 77){
            Tamagotchi4.name += "m";
        }
        if (e.getKeyCode() == 78){
            Tamagotchi4.name += "n";
        }
        if (e.getKeyCode() == 79){
            Tamagotchi4.name += "o";
        }
        if (e.getKeyCode() == 80){
            Tamagotchi4.name += "p";
        }
        if (e.getKeyCode() == 81){
            Tamagotchi4.name += "q";
        }
         if (e.getKeyCode() == 82){
            Tamagotchi4.name += "r";
        }
         if (e.getKeyCode() == 83){
            Tamagotchi4.name += "s";
        }
        if (e.getKeyCode() == 84){
            Tamagotchi4.name += "t";
        }
        if (e.getKeyCode() == 85){
            Tamagotchi4.name += "u";
        }
        if (e.getKeyCode() == 86){
            Tamagotchi4.name += "v";
        }
        if (e.getKeyCode() == 87){
            Tamagotchi4.name += "w";
        }
        if (e.getKeyCode() == 88){
            Tamagotchi4.name += "x"; 
        }
        if (e.getKeyCode() == 89){
            Tamagotchi4.name += "y";
        }
        if (e.getKeyCode() == 90){
            Tamagotchi4.name += "z";
        }
        
        
    }
      if (Tamagotchi4.menu == true){
          if (e.getKeyCode() == 8){
              if (Tamagotchi4.name.length()> 0) {
                  Tamagotchi4.name = Tamagotchi4.name.substring(0, Tamagotchi4.name.length()-1);
              }
          }
          if(e.getKeyCode() ==10){
              Tamagotchi4.menu = false;
          }
      }
    }
    
  
}
