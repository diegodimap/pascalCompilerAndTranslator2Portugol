package arquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ArquivoPascal {

    private static String nomeArquivo = "";

    public static void salvarArquivo(String str){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar Arquivo NitroPascal");
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return (f.getName().endsWith(".pas") || f.getName().endsWith(".inc") || f.isDirectory());
            }

            @Override
            public String getDescription() {
                return "Pascal (.pas, .inc)";
            }
        });
        chooser.setCurrentDirectory(new File("."));
        int select = chooser.showSaveDialog(null);
        if (select == JFileChooser.APPROVE_OPTION) {
            try {
                File arquivo = chooser.getSelectedFile();
                RandomAccessFile file = new RandomAccessFile(arquivo, "rw");
                file.write(str.getBytes());
                file.close();
                JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso!!!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "falha");
            }
        }
    }

    public static String lerArquivo(String path) {
        String str, texto = "";
        JFileChooser eleitor = new JFileChooser();
        eleitor.setDialogTitle("Abrir Arquivo NitroPascal");
        eleitor.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                return (f.getName().endsWith(".pas") || f.getName().endsWith(".inc") || f.isDirectory());
            }

            @Override
            public String getDescription() {
                return "Pascal (.pas,.inc)";
            }
        });
        eleitor.setCurrentDirectory(new File(path));

        int result = eleitor.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File arquivo = eleitor.getSelectedFile();
            nomeArquivo = arquivo.getName();
            try {
                BufferedReader in = new BufferedReader(new FileReader(arquivo));
                while ((str = in.readLine()) != null) {
                    texto += str + "\n"; //String.format("%-6d %s %s", linha++, str, "\n");
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return texto;
    }

    public static String getNomeArquivo() {
        return nomeArquivo;
    }

    
}
