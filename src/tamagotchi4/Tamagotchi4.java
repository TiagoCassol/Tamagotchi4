package tamagotchi4;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Tamagotchi4 implements Runnable {

    private boolean run = false;
    public Thread thread;
    public int running = 0, resetting = 0;
    // ___INICIAR FRAMES E CANVAS___________________________________________________________________
    public JFrame frame = new JFrame();
    public Canvas canvas = new Canvas();
    // CONFIGURAR FRAMES E CANVAS________________________________________________________________
    public final int Width = 750, Height = 750;  // dimensão da janela
    Dimension canvasSize = new Dimension(Width, Height);
    public Input input = new Input();
    public BufferStrategy bs;
    public Graphics g;

    // fIGURAS DO PERSONAGEM______________________________________________________________________
    public static BufferedImage character, characterB, characterS, characterM, characterH;
    public BufferedImage[] player = new BufferedImage[4];
    public boolean blink = false, play = false, wash = false, eat = false, sleep = false, health = false, death = false;
    private int x, y;
    private final int scale = 1;
    public static String name = "";

    public static int clean = 950, hunger = 950, sleepy = 950, happiness = 950, healthy = 950;
    public static final int maxClean = 950, maxHunger = 950, maxSleepy = 950, maxHappiness = 950, maxHealthy = 950;
    private Rectangle cham = new Rectangle();

    // OUTRAS IMAGENS_____________________________________________________________________________
    public static BufferedImage food, bed, shower, smiley, ball, fly, medicine, medicineButton;

    // INTERAÇÕES COM O TAMAGOTCHI_______________________________________________________________
    private final Rectangle boxOne = new Rectangle(20, Height / 4 * 3 + 30, 148, 148);
    private final Rectangle boxTwo = new Rectangle(40 + 148, Height / 4 * 3 + 20, 148, 148);
    private final Rectangle boxThree = new Rectangle(Width - 148 * 2 - 40, Height / 4 * 3 + 20, 148, 148);
    private final Rectangle boxFour = new Rectangle(Width - 148 - 20, Height / 4 * 3 + 20, 148, 148);
    private final Rectangle boxFive = new Rectangle(Width - 315 - 148, Height / 4 * 3 + 20, 148, 148);
    private boolean one = false, two = false, three = false, four = false, five = false;

    //TELA DE LOADING______________________________________________________________________________
    private boolean loading = true;
    private int loadingCount = 0;
    private int flashing = 0;
    private boolean loadBar = false;

    // MENU PRINCIPAL_______________________________________________________________________________
    public static boolean menu = true;

    //PAUSA_________________________________________________________________________________________
    private boolean pause = false;
    public int paused = 0;
    public boolean confirm;
    // ÁREA DO COMANDO
    private final Rectangle resume = new Rectangle(Width / 2 - 100, Height / 12 + 25, 200, 50);
    private final Rectangle pauseHelp = new Rectangle(Width / 2 - 100, Height / 12 * 3 + 25, 200, 50);
    private final Rectangle reset = new Rectangle(Width / 2 - 100, Height / 12 * 5 + 25, 200, 50);
    private final Rectangle exit = new Rectangle(Width / 2 - 100, Height / 8 * 7 + 25, 200, 50);

    private final Rectangle yes = new Rectangle(Width / 2 - 200 + 20 + 190, Height / 2 - 65, 180, 180);
    private final Rectangle no = new Rectangle(Width / 2 - 200 + 10, Height / 2 - 65, 180, 180);

    //PÁGINA DE AJUDA________________________________________________________________________________
    private boolean help = false;
    private final Rectangle menuHelp = new Rectangle(Width / 2 - 100, Height / 12 * 11 + 20, 200, 50);
    private final Rectangle back = new Rectangle(50, Height / 8 * 7 + 25, 200, 50);

    // COR DE FUNDO_________________________________________________________________________________
    public int tmp = 170;
    public boolean blue = false;

    private boolean left, right, draw;

    //SALVAR E CARREGAR____________________________________________________________________________
    static List<String> inputs = new ArrayList<>();
    public static String loc;

    public static void main(String[] args) throws IOException {
        Tamagotchi4 game = new Tamagotchi4();

        character = ImageIO.read(Tamagotchi4.class.getResource("/images/normal.png"));
        characterB = ImageIO.read(Tamagotchi4.class.getResource("/images/piscando.png"));
        characterS = ImageIO.read(Tamagotchi4.class.getResource("/images/banho.png"));
        characterM = ImageIO.read(Tamagotchi4.class.getResource("/images/comendo.png"));
        food = ImageIO.read(Tamagotchi4.class.getResource("/images/comida.png"));
        bed = ImageIO.read(Tamagotchi4.class.getResource("/images/zzz.png"));
        shower = ImageIO.read(Tamagotchi4.class.getResource("/images/chuveiro.png"));
        smiley = ImageIO.read(Tamagotchi4.class.getResource("/images/iconeFeliz.png"));
        ball = ImageIO.read(Tamagotchi4.class.getResource("/images/bola.png"));
        fly = ImageIO.read(Tamagotchi4.class.getResource("/images/comida.png"));
        //medicine = ImageIO.read(Tamagotchi4.class.getResource("/images/Remedio.png"));
        //medicineButton = ImageIO.read(Tamagotchi4.class.getResource("/images/Remedio.png"));

        String path = System.getProperty("user.home") + File.separator + "Documents" 
                + File.separator + "Tamagotchi Save File";
        File customDir = new File(path);
        if (customDir.exists()) {
            System.out.println("save file exists");
            menu = false;
            load(path);
        } else if (customDir.mkdirs()) {
            System.out.println("save file exists");
            newSave(path);
        } else {
            System.out.println("error finding or creating save file");
        }
        loc = path;
        game.createDisplay();
        game.start();
    }

    //LOAD / CARREGAR O JOGO SALVO
    public static void load(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path + "/Save.zbdS"));

        while (scanner.hasNext()) {
            if (scanner.hasNextLine()) {
                inputs.add(scanner.nextLine());
            } else {
                scanner.nextLine();
            }
        }

        fill();
    }

    public static void newSave(String path) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("here");
        PrintWriter writer = new PrintWriter(path + "/Save.zbdS", "UTF-8");
        writer.close();
    }

    public void save(String name, int hunger, int sleepy, int happy, int clean, int health) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(loc + "/Save.zbdS", "UTF-8");
        writer.println(name);
        writer.println(hunger);
        writer.println(happy);
        writer.println(clean);
        writer.println(sleepy);
        writer.println(health);
        writer.close();
    }

    private void createDisplay() {
        frame = new JFrame("");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(dim.width / 2 - Width / 2, dim.height / 2 - Height / 2);
        frame.setSize(Width, Height);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        
        

        canvas = new Canvas();
        canvas.setPreferredSize(canvasSize);
        canvas.setMaximumSize(canvasSize);
        canvas.setMinimumSize(canvasSize);
        canvas.setFocusable(false);

        frame.add(canvas);
        canvas.addMouseListener(new CustomListener());
        frame.addKeyListener(input);
        frame.pack();
        run = true;

        player[0] = character;
        player[1] = characterB;
        player[2] = characterS;
        player[3] = characterM;

        run();
    }

    private void update() {
        if (input.escape && pause == false && loading == false && menu == false && running > 25) {
            pause = true;
            running = 0;
        }
        if (input.escape && pause == true && paused > 25) {
            pause = false;
            paused = 0;
        }
        if (hunger < 0) {
            hunger = 0;
        }
        if (sleepy < 0) {
            sleepy = 0;
        }
        if (clean < 0) {
            clean = 0;
        }
        if (happiness < 0) {
            happiness = 0;
        }
        if (healthy < 0) {
            healthy = 0;
        }
    }

    private void render() throws FileNotFoundException, UnsupportedEncodingException {
        save(name, hunger, sleepy, happiness, clean, healthy);

        if (tmp == 225) {
            blue = false;
        }
        if (tmp == 170) {
            blue = true;
        }
        if (blue == true) {
            tmp += 1;
        }
        if (blue == false) {
            tmp -= 1;
        }
        Color backgroud = new Color(2, 190, tmp);

        x = Width / 2 - (character.getWidth() * scale) / 2;
        y = Height / 2 - (character.getHeight() * scale) / 2;
        cham = new Rectangle(x, y, character.getWidth() * scale, character.getHeight() * scale);
        bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        
        try{
        g = bs.getDrawGraphics();
        }catch(Exception e){
           System.out.println(e.getMessage());
            
       }
        g.clearRect(0, 0, Width, Height);

        //fundo
        g.setColor(backgroud);
        g.fillRect(0, 0, Width, Height);
        g.setFont(new Font("Arial", Font.BOLD, 25));

        //PAGINA DE LOADING___________________________________________________________________________
        if (loading == true) {
            loadingCount++;
            flashing++;
            g.setColor(Color.yellow);
            if (loadBar == false) {
                g.fillRect(12, 402, loadingCount * 3, 96);
            }
            g.setColor(Color.black);
            g.drawRect(10, 400, Width - 20, 100);
            g.setColor(Color.yellow);
            if (flashing < 50) {
                g.drawString("LOADING", Width / 2 - 50, 350);
            }
            if (flashing > 100) {
                flashing = 0;
            }
            if (loadingCount > 242) {
                loadBar = true;
                g.setColor(Color.green);
                g.fillRect(12, 402, 242 * 3, 96);
            }
            if (loadingCount > 300) {
                loading = false;
            }
        } else if (loading == false) {

            // MENU INICIAL______________________________________________________________________________
            if (menu == true) {
                g.setColor(Color.yellow);
                g.fillRect(150, 150, 400, 400);
                g.setColor(Color.black);
                g.drawString("Enter a name:  " + name, 200, 200);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("(10 characters max)", 275, 230);
                g.drawString("Press ENTER to continue.", 255, 260);
                g.drawRect(375, 180, 160, 25);
                // BOTÃO DE AJUDA__________________________________________________________________________
                g.setColor(Color.red);
                g.fillRect(Width / 2 - 100, Height / 12 * 11, 200, 50);
                g.setColor(new Color(255, 100, 0));
                g.fillRect(Width / 2 - 95, Height / 12 * 11 + 5, 190, 40);
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString("HELP!", Width / 2 - 30, Height / 12 * 11 + 33);
                if (onScreen() == true) {
                    if (menuHelp.contains(frame.getMousePosition()) && left == true) {
                        help = true;
                    }
                }
            }
            // JOGO PRINCIPAL___________________________________________________________________________
            if (menu == false && pause == false) {
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.setColor(Color.black);
                if (blink == true || sleep == true) {
                    g.drawString(name, cham.x + cham.width / 2, cham.y - 10);
                    g.drawImage(player[1], 0, 0, character.getWidth() * scale, character.getHeight() * scale, null);
                } else if (wash == true) {
                    g.drawString(name, cham.x + cham.width / 2, cham.y - 10);
                    g.drawImage(player[2], 0, 0, character.getWidth() * scale, character.getHeight() * scale, null);
                } else if (eat == true) {
                    g.drawString(name, cham.x + cham.width / 2, cham.y - 10);
                    g.drawImage(player[3], 0, 0, character.getWidth() * scale, character.getHeight() * scale, null);
                } else if (play == true) {
                    g.drawString(name, cham.x + cham.width / 2, cham.y - 30);
                    g.drawImage(player[0], 0, 0 - 20, character.getWidth() * scale, character.getHeight() * scale, null);
                } else {
                    g.drawString(name, cham.x + cham.width / 2, cham.y - 10);
                    g.drawImage(player[0], 0, 0, character.getWidth() * scale, character.getHeight() * scale, null);
                }

                //BARRAS DO STATUS____________________________________________________________________
                g.setColor(Color.black);
                g.drawImage(food, 15, 15, 40, 40, null);
                g.drawRect(20, 70, 28, 480);
                g.setColor(Color.red);
                g.fillRect(22, 72, 25, hunger / 2);

                g.setColor(Color.black);
                g.drawImage(smiley, 65, 15, 40, 40, null);
                g.drawRect(70, 70, 28, 480);
                g.setColor(new Color(89, 208, 255));
                g.fillRect(72, 72, 25, happiness / 2);

                g.setColor(Color.black);
                g.drawImage(shower, Width - 55, 15, 40, 40, null);
                g.drawRect(Width - 48, 70, 28, 480);
                g.setColor(new Color(153, 255, 102));
                g.fillRect(Width - 46, 72, 25, clean / 2);

                g.setColor(Color.black);
                g.drawImage(bed, Width - 105, 15, 40, 40, null);
                g.drawRect(Width - 98, 70, 28, 480);
                g.setColor(new Color(153, 51, 225));
                g.fillRect(Width - 96, 72, 25, sleepy / 2);

                /*
                g.setColor(Color.black);
                g.drawImage(medicine, Width - 400, 15, 40, 40, null);
                g.drawRect(Width - 3 - 600, 47, 455, 30);
                g.setColor(new Color(0, 225, 0));
                g.fillRect(Width / 3 - 100, Height / 30 + 25, 450, 25);
                 */
                //OBJETOS E OPÇÕES DE INTERAÇÕES_________________________________________________________
                g.setColor(Color.yellow);
                g.fillRoundRect(0, Height / 4 * 3, Width, Height / 4 * 3, 50, 50);

                g.setColor(Color.red);
                g.drawImage(fly, boxOne.x, boxOne.y, boxOne.width, boxOne.height, null);
                g.drawImage(ball, boxTwo.x, boxTwo.y, boxTwo.width, boxTwo.height, null);
                g.drawImage(shower, boxThree.x, boxThree.y, boxThree.width, boxThree.height, null);
                g.drawImage(bed, boxFour.x, boxFour.y, boxFour.width, boxFour.height, null);
                // g.drawImage(medicine, boxFive.x, boxFive.y, boxFive.width, boxFive.height, null);
                try{
                if (onScreen() == true) {
                    if (boxOne.contains(frame.getMousePosition()) && two == false && three == false && four == false && five == false && left == true) {
                        one = true;
                        draw = true;
                    }
                    if (boxTwo.contains(frame.getMousePosition()) && one == false && three == false && four == false && five == false && left == true) {
                        two = true;
                        draw = true;
                    }
                    if (boxThree.contains(frame.getMousePosition()) && two == false && one == false && four == false && five == false && left == true) {
                        three = true;
                        draw = true;
                    }
                    if (boxFour.contains(frame.getMousePosition()) && two == false && three == false && one == false && five == false && left == true) {
                        four = true;
                        draw = true;
                    }
                    // if (boxFive.contains(frame.getMousePosition()) && two == false && three == false && one == false && four == false && left == true) {
                    //   five = true;
                    //  draw = true;
                    //}
                    // pós açoes
                    if (cham.contains(frame.getMousePosition()) && draw == true && one == true) {
                        eat();
                        draw = false;
                    }
                    if (cham.contains(frame.getMousePosition()) && draw == true && two == true) {
                        play();
                        draw = false;
                    }
                    if (cham.contains(frame.getMousePosition()) && draw == true && three == true) {
                        wash();
                        draw = false;
                    }
                    if (cham.contains(frame.getMousePosition()) && draw == true && four == true) {
                        sleep();
                        draw = false;
                    }
                    if (cham.contains(frame.getMousePosition()) && draw == true && five == true) {
                        sleep();
                        draw = false;
                    }

                    if (draw && one) {
                        g.drawImage(fly, frame.getMousePosition().x - 25, frame.getMousePosition().y - 25, 60, 50, null);
                    }
                    if (draw && two) {
                        g.drawImage(ball, frame.getMousePosition().x - 25, frame.getMousePosition().y - 25, 50, 50, null);
                    }
                    if (draw && three) {
                        g.drawImage(shower, frame.getMousePosition().x - 25, frame.getMousePosition().y - 25, 50, 50, null);
                    }
                    if (draw && four) {
                        g.drawImage(bed, frame.getMousePosition().x - 25, frame.getMousePosition().y - 25, 50, 50, null);
                    }
                    // if (draw && five) {
                    //     g.drawImage(medicine, frame.getMousePosition().x - 25, frame.getMousePosition().y - 25, 50, 50, null);
                    // }
                }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                   // frame.dispose();
                }
            }
            try{
            if (pause == true && help == false) {
                //System.out.println("paused");

                g.setColor(new Color(0, 135, 255));
                g.fillRoundRect(20, 20, Width - 40, Height - 40, 50, 50);

                // BOTÃO DE RETORNAR AO JOGO "RESUME"__________________________________________________
                g.setColor(Color.red);
                g.fillRect(Width / 2 - 100, Height / 12, 200, 50); // localização e dimensão do retangulo vermelho maior do botão
                g.setColor(new Color(255, 100, 0));
                g.fillRect(Width / 2 - 95, Height / 12 + 5, 190, 40);  // localização e dimensão do retangulo laranja menor do botão
                g.setFont(new Font("Arial", Font.BOLD, 25)); // dimensão do texto
                g.setColor(Color.black);
                g.drawString("Resume", Width / 2 - 50, Height / 12 + 35);// localização do texto
                if (onScreen()) {
                    if (resume.contains(frame.getMousePosition()) && left == true) {
                        pause = false;
                        paused = 0;
                    }
                }
                
                // BOTÃO DE AJUDA "HELP"____________________________________________________________________
                g.setColor(Color.red);
                g.fillRect(Width / 2 - 100, Height / 12 * 3, 200, 50);// localização e dimensão do retangulo vermelho maior do botão
                g.setColor(new Color(255, 100, 0));
                g.fillRect(Width / 2 - 95, Height / 12 * 3 + 5, 190, 40);// localização e dimensão do retangulo laranja menor do botão
                g.setFont(new Font("Arial", Font.BOLD, 25));// dimensão do texto
                g.setColor(Color.black);
                g.drawString("Help", Width / 2 - 30, Height / 12 * 3 + 35);// localização do texto
                if (onScreen()) {
                    if (pauseHelp.contains(frame.getMousePosition()) && left == true) {
                        help = true;
                    }
                }
                // BOTÃO DE REINICIAR "RESET"_____________________________________________________________
                g.setColor(Color.red);
                g.fillRect(Width / 2 - 100, Height / 12 * 5, 200, 50);// localização e dimensão do retangulo vermelho maior do botão
                g.setColor(new Color(255, 100, 0));
                g.fillRect(Width / 2 - 95, Height / 12 * 5 + 5, 190, 40);// localização e dimensão do retangulo laranja menor do botão
                g.setFont(new Font("Arial", Font.BOLD, 25));// dimensão do texto
                g.setColor(Color.black);
                g.drawString("Reset", Width / 2 - 30, Height / 12 * 5 + 35);// localização do texto
                if (onScreen()) {
                    if (reset.contains(frame.getMousePosition()) && left == true) {
                        confirm = true;
                    }
                }
                if (confirm == true) {
                    g.setColor(Color.green);
                    g.fillRect(Width / 2 - 200, Height / 2 - 100, 400, 200);
                    g.setColor(Color.red);
                    g.fillRect(Width / 2 - 200 + 10, Height / 2 - 100 + 10, 180, 180);
                    g.setColor(new Color(0, 200, 0));
                    g.fillRect(Width / 2 - 200 + 20 + 190, Height / 2 - 100 + 10, 180, 180);
                    g.setColor(Color.green);
                    g.drawString("Are you sure?", Width / 2 - 80, Height / 12 * 5 - 45);

                    if (onScreen() == true) {
                        if (yes.contains(frame.getMousePosition()) && resetting > 25 && left) {
                            hunger = maxHunger;
                            happiness = maxHappiness;
                            clean = maxClean;
                            sleepy = maxSleepy;
                            healthy = maxHealthy;
                            name = "";
                            menu = true;
                            pause = false;
                            confirm = false;
                            resetting = 0;
                        }
                        if (no.contains(frame.getMousePosition()) && resetting > 25 && left) {
                            confirm = false;
                            resetting = 0;
                        }
                    }
                }

                // // BOTÃO DE SAIR "EXIT"___________________________________________________________________
                g.setColor(Color.red);
                g.fillRect(Width / 2 - 100, Height / 8 * 7, 200, 50);// localização e dimensão do retangulo vermelho maior do botão
                g.setColor(new Color(255, 100, 0));
                g.fillRect(Width / 2 - 95, Height / 8 * 7 + 5, 190, 40);// localização e dimensão do retangulo laranja menor do botão
                g.setFont(new Font("Arial", Font.BOLD, 25));// dimensão do texto
                g.setColor(Color.black);
                g.drawString("Exit", Width / 2 - 25, Height / 8 * 7 + 35);// localização do texto
                if (onScreen()) {
                    if (exit.contains(frame.getMousePosition()) && left == true) {
                        System.exit(0);
                    }
                }

            }
            }catch(Exception e){
                    System.out.println(e.getMessage());
                   // frame.dispose();
                }
            if (help == true) {
                g.setColor(new Color(0, 100, 255));
                g.fillRect(15, 15, Width - 30, Height - 30);
                g.setColor(Color.red);
                g.fillRect(50, Height / 8 * 7, 190, 50);
                g.setColor(new Color(255, 100, 0));
                g.fillRect(55, Height / 8 * 7 + 5, 180, 40);
                g.setColor(Color.black);
                g.drawString("BACK", 110, Height / 8 * 7 + 35);

                g.drawString("The game is simple: keep your little friend alive!", 40, 50);
                g.drawString("Click the icons on the bottom of the screen", 40, 80);
                g.drawString("to keep it fed, happy, clean and well-rested.", 40, 110);
                g.drawString("This game will save your progress automatically.", 40, 140);
                g.drawString("To leave the game press the EXIT button when paused.", 40, 170);
                g.drawString("ENJOY!", 40, 200);
                try{
                if (onScreen() == true) {
                    if (back.contains(frame.getMousePosition()) && left == true) {
                        System.out.println("leave");
                        help = false;
                    }
                }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                   // frame.dispose();
                }

            }
        }
       // try{
            
        bs.show();
       // }catch(Exception e){
      //      System.out.println(e.getMessage());
       // }
        g.dispose();
    }

    public void start() {
        if (run == false) {
            return;
        } else {
            run = true;
            thread = new Thread(this);
            thread.start();
        }

    }

    @Override
    public void run() {
        long time;
        long lastTime = System.currentTimeMillis();
        long eventTime = System.currentTimeMillis();
        int fps = 60;
        int b = 0, p = 0, s = 0, w = 0, e = 0, h = 0, d = 0, counter = 0;
        while (run) {
            time = System.currentTimeMillis();
            if ((double) (time - lastTime) / 1000 > (double) 1 / fps) {
                update();
                input.updade();
                try {
                    render();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (UnsupportedEncodingException ex) {
                    System.out.println(ex.getMessage());
                }
                if (blink == true) {
                    b++;
                }
                if (pause == false && menu == false && loading == false && running < 100) {
                    running++;
                }
                if (confirm == true) {
                    resetting++;
                }
                if (b == 10) {
                    blink = false;
                    b = 0;
                }
                if (play == true) {
                    p++;
                }
                if (p == 8) {
                    play = false;
                    p = 0;
                }
                if (eat == true) {
                    e++;
                }
                if (e == 15) {
                    eat = false;
                    e = 0;
                }
                if (sleep == true) {
                    s++;
                }
                if (s == 75) {
                    sleep = false;
                    s = 0;
                }
                if (wash == true) {
                    w++;
                }
                if (w == 50) {
                    wash = false;
                    w = 0;
                }
                if (health == true) {
                    h++;
                }
                if (h == 50) {
                    health = false;
                    h = 0;
                }

                lastTime = time;
            }

            if ((double) (time - eventTime) / 1000 > 1 && menu == false && loading == false && help == false) {
                counter++;
                eventTime = time;
                hunger -= 1;
                happiness -= 3;
                healthy -= 1;
                if (counter == 1) {
                    sleepy -= 1;
                }
                if (counter == 3) {
                    clean -= 4;
                    if (eat == false && sleep == false) {
                        blink();
                    }
                    if (hunger <= 50 && happiness <= 50 && sleepy <= 50 && clean <=50) {
                        death();
                    }
                    counter = 0;
                }
            }
        }

    }

    public void blink() {
        blink = true;
        System.out.println("blink");

    }

    public void death() {
        death = true;
        System.out.println("DEATH");
        JOptionPane.showMessageDialog(null, "Your friend is dead.", "GAME OVER", JOptionPane.ERROR_MESSAGE);
        hunger = maxHunger;
        happiness = maxHappiness;
        clean = maxClean;
        sleepy = maxSleepy;
        healthy = maxHealthy;
        name = "";
        menu = true;
        pause = false;
        confirm = false;
        resetting = 0;

    }

    // MODIFICAÇÃO DOS STATUS DO JOGO CONFORME AÇÃO DO USUÁRIO________________________________
    public void eat() {
        System.out.println("eat");
        eat = true;
        hunger += 50;
        sleepy -= 5;
        if (hunger > maxHunger) {
            hunger = maxHunger;
        }
    }

    public void play() {
        System.out.println("play");
        play = true;
        happiness += 90;
        clean -= 40;
        hunger -= 10;
        sleepy -= 25;
        healthy -= 50;
        if (happiness > maxHappiness) {
            happiness = maxHappiness;
        }
    }

    public void wash() {
        System.out.println("clean");
        wash = true;
        clean += 75;
        happiness -= 50;
        if (clean > maxClean) {
            clean = maxClean;
        }
    }

    public void sleep() {
        System.out.println("sleep");
        sleep = true;
        sleepy += 350;
        hunger -= 150;
        happiness -= 100;
        if (sleepy > maxSleepy) {
            sleepy = maxSleepy;
        }
    }

    public void heal() {
        System.out.println("heal");
        health = true;
        healthy += 350;
        happiness -= 150;
        if (healthy > maxHealthy) {
            healthy = maxHealthy;
        }
    }

    public boolean onScreen() {
        boolean ret = false;
        if (MouseInfo.getPointerInfo().getLocation().x > frame.getX() && MouseInfo.getPointerInfo().getLocation().x < frame.getX() + Width
                && MouseInfo.getPointerInfo().getLocation().y > frame.getY() && MouseInfo.getPointerInfo().getLocation().y < frame.getY() + Height && frame.isFocused() == true) {
            ret = true;

        }
        return ret;
    }

    private static void fill() {
        if (inputs.size() == 1) {
            if (check(0) == true) {
                name = "unknown";
            } else {
                name = inputs.get(0);
            }
        }
        if (inputs.size() == 2) {
            if (check(0) == true) {
                name = "unknown";
            } else {
                name = inputs.get(0);
            }
            if (check(1) == true) {
                hunger = maxHunger;
            } else {
                hunger = Integer.parseInt(inputs.get(1));
            }
        }
        if (inputs.size() == 3) {
            if (check(0) == true) {
                name = "unknown";
            } else {
                name = inputs.get(0);
            }
            if (check(1) == true) {
                hunger = maxHunger;
            } else {
                hunger = Integer.parseInt(inputs.get(1));
            }
            if (check(2) == true) {
                happiness = maxHappiness;
            } else {
                happiness = Integer.parseInt(inputs.get(2));
            }
        }
        if (inputs.size() == 4) {
            if (check(0) == true) {
                name = "unknown";
            } else {
                name = inputs.get(0);
            }
            if (check(1) == true) {
                hunger = maxHunger;
            } else {
                hunger = Integer.parseInt(inputs.get(1));
            }
            if (check(2) == true) {
                happiness = maxHappiness;
            } else {
                happiness = Integer.parseInt(inputs.get(2));
            }
            if (check(3) == true) {
                clean = maxClean;
            } else {
                clean = Integer.parseInt(inputs.get(3));
            }
        }
        if (inputs.size() == 5) {
            if (check(0) == true) {
                name = "unknown";
            } else {
                name = inputs.get(0);
            }
            if (check(1) == true) {
                hunger = maxHunger;
            } else {
                hunger = Integer.parseInt(inputs.get(1));
            }
            if (check(2) == true) {
                happiness = maxHappiness;
            } else {
                happiness = Integer.parseInt(inputs.get(2));
            }
            if (check(3) == true) {
                clean = maxClean;
            } else {
                clean = Integer.parseInt(inputs.get(3));
            }
            if (check(4) == true) {
                sleepy = maxSleepy;
            } else {
                sleepy = Integer.parseInt(inputs.get(4));
            }

            if (inputs.size() == 6) {
                if (check(0) == true) {
                    name = "unknown";
                } else {
                    name = inputs.get(0);
                }
                if (check(1) == true) {
                    hunger = maxHunger;
                } else {
                    hunger = Integer.parseInt(inputs.get(1));
                }
                if (check(2) == true) {
                    happiness = maxHappiness;
                } else {
                    happiness = Integer.parseInt(inputs.get(2));
                }
                if (check(3) == true) {
                    clean = maxClean;
                } else {
                    clean = Integer.parseInt(inputs.get(3));
                }
                if (check(4) == true) {
                    sleepy = maxSleepy;
                } else {
                    sleepy = Integer.parseInt(inputs.get(4));
                }
                if (check(5) == true) {
                    healthy = maxHealthy;
                } else {
                    healthy = Integer.parseInt(inputs.get(5));
                }
            }

        }
    }

    public static boolean check(int i) {
        boolean placeHolder = false;
        if (inputs.get(i).equals("")) {
            placeHolder = true;
        }
        return placeHolder;
    }

    private class CustomListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                left = true;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                right = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            left = false;
            right = false;
            draw = false;

            one = false;
            two = false;
            three = false;
            four = false;
            five = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
