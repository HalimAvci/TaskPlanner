import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class sortPriority extends JFrame{

    private JPanel sortPanel;
    private JButton sortByPriorityButton;
    private JTextField deadLineField;


    public sortPriority(){
        add(sortPanel);
        setSize(400,200);
        setLocation(600,300);
        setTitle("Sort Priority");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        sortByPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] columnNames = {"ID", "Task Name", "Short Description", "Deadline", "Priority", "Reminder Image", "Entry Date"};
                DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                JTable table = new JTable(tableModel);
                table.getTableHeader().setReorderingAllowed(false);
                table.getTableHeader().setEnabled(false);
                JScrollPane scrollPane = new JScrollPane(table);
                add(scrollPane);

                Date deadline = null;
                try {
                    deadline = Date.valueOf(deadLineField.getText());
                } catch (IllegalArgumentException ex){
                    JOptionPane.showMessageDialog(null, "Please enter a deadline", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DB DB = new DB();
                DB.sortByPriority(table, deadline);

                JFrame frame = new JFrame();
                frame.getContentPane().add(scrollPane);
                frame.setSize(600, 200);
                frame.setLocation(800, 300);
                frame.setTitle("Sort Priority Screen");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                dispose();
            }
        });
    }
}
