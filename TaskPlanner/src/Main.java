import javax.swing.*;

// Main function for run the TaskPlanner application
public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login login = new Login();
                login.setVisible(true);
                login.setResizable(false);
            }
        });
    }
}
