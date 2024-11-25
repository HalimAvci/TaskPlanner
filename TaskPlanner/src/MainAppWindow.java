import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainAppWindow extends JFrame {
    private JButton insertATaskButton;
    private JPanel MainAppWindow;
    private JButton showAllTasksButton;



    public MainAppWindow(){
        add(MainAppWindow);
        setSize(400,200);
        setLocation(600,300);
        setTitle("Main Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Insert a task
        insertATaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  InsertaTask insertTask = new InsertaTask();
                  insertTask.setVisible(true);
            }
        });

        // Show all tasks
        showAllTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  DB DB = new DB();
                  DB.showAllTasks();
            }
        });


    }
}
