import javax.swing.*;
import java.awt.*;


public class LoadingScreen extends JFrame {

    public LoadingScreen() {
        setTitle("Loading...");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String imagePath = "img/uad.png";
        ImageIcon imageIcon = createImageIcon(imagePath);
        ImageIcon icon = new ImageIcon("img/uad.ico");
        setIconImage(icon.getImage());

        // Set window decorations
        JFrame.setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Adjust the path as needed
        setIconImage(icon.getImage());
        if (imageIcon != null) {
            setContentPane(new JLabel(imageIcon));
        } else {
            System.err.println("Error loading image: " + imagePath);
        }

        setLayout(new BorderLayout());

        JLabel loadingLabel = new JLabel("Loading, please wait...");
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(loadingLabel, BorderLayout.SOUTH);


        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(2000);
                return null;
            }

            @Override
            protected void done() {
                dispose();
                showMainApplication();
            }
        };

        worker.execute();
    }

    private void showMainApplication() {

        Kasir mainApp = new Kasir();
        mainApp.setVisible(true);
    }

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Could not find image: " + path);
            return null;
        }
    }
}
