package splash;

import gui.NitroPascal;

public class Splash {
    public static void main(String[] args) {
        final SplashNitroPascal sp = new SplashNitroPascal();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                sp.setVisible(true);
            }
        });
        //Espera um tempo para que a imagem de splash seja exibida
    	Runnable runner = new Runnable() {
    		public void run() {
            try {
            	Thread.sleep(4000);
            } catch (InterruptedException e) {
            }

         }
    	};
    	runner.run();

        sp.dispose();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NitroPascal().setVisible(true);
            }
        });

    }
}
