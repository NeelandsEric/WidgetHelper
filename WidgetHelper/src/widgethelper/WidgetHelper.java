/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widgethelper;

/**
 * Main
 *
 * @author EricGummerson
 */
public class WidgetHelper implements Runnable {

    MainFrame mf;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               
        // TODO code application logic here
        WidgetHelper wh = new WidgetHelper();
        Thread t = new Thread(wh);
        t.start();
    }

    @Override
    public void run() {
        mf = new MainFrame();
        mf.setVisible(true);
    }

}
