import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Login  extends JFrame {


    private JPanel Login;
    private JTextField UserName;
    private JButton loginButton;
    private JPasswordField PassWord;

    public Login(){
       add(Login);
       setSize(400,200);
       setLocation(600,300);
       setTitle("Login Screen");
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  String username = UserName.getText();
                  String password = new String(PassWord.getPassword());
                  boolean isValid = validateCredentials(username,password);
                  // Check if the username and password are valid
                  if (isValid){
                      JOptionPane.showMessageDialog(null, "Logging in", "Login Successfull",JOptionPane.INFORMATION_MESSAGE);
                      // Display a notification message if there are any tasks that have deadline 1 day or less
                      DB DB = new DB();
                      DB.showNotifications();
                      // Login frame disappears and instead main frame appears
                      MainAppWindow appWindow = new MainAppWindow();
                      appWindow.setVisible(true);
                      appWindow.setResizable(false);
                      dispose();
                  // Check if the username and password fields are empty
                  } else if (username.isEmpty() || password.isEmpty()){
                      JOptionPane.showMessageDialog(null, "Please fill out all fields", "Login error", JOptionPane.ERROR_MESSAGE);
                  } else {
                      JOptionPane.showMessageDialog(null, "Invalid username or password", "Login error", JOptionPane.ERROR_MESSAGE);
                  }
            }
            private boolean validateCredentials(String username, String password){
                // Array to store the valid username and passwords
                String[] validUsernames = {"User1", "User2","User3"};
                String[] validPasswords = {"Password1","Password2","Password3"};

                // Check if the username and password match any of the valid credentials
                for (int i = 0 ; i < validUsernames.length ; i++){
                    if (username.equals(validUsernames[i]) && password.equals(validPasswords[i])){
                        return true;
                    }
                }
                return false;
            }
        });


    }


}
