import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertaTask extends JFrame {
    private JPanel InsertaTask;
    private JTextField TaskName;
    private JTextField ShortDesc;
    private JTextField Priority;
    private JButton InsertTaskButton;
    private JCheckBox reminderCheck;
    private JPanel DateChoose;


    private JDateChooser dateChooser = new JDateChooser();

    public InsertaTask() {
        add(InsertaTask);
        setSize(400, 200);
        setLocation(200, 300);
        setTitle("Insert a Task Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Calendar
        DateChoose.add(dateChooser);

        InsertTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the user input from the text fields
                String taskName = TaskName.getText();
                String shortDescription = ShortDesc.getText();
                java.sql.Date deadline;
                try {
                    deadline = new java.sql.Date(dateChooser.getDate().getTime());
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "Please select a deadline date", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Integer priority = null;
                    if (!Priority.getText().isEmpty()) {
                        priority = Integer.parseInt(Priority.getText());
                    }

                Boolean reminderImage = reminderCheck.isSelected();
                // check if all required fields are filled
                if (taskName.isEmpty() || shortDescription.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill out all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // insert the task into the database
                DB DB = new DB();
                DB.insertTask(taskName, shortDescription, deadline, priority, reminderImage);
                JOptionPane.showMessageDialog(null, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // close the frame
                dispose();
            }
        });
    }
}