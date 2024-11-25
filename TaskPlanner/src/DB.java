import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;


public class DB extends JFrame{
    private JTextField startDateField;
    private JTextField endDateField;
    private Connection connection;

    // Database connection
    public DB() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SE2224_20070006009?user=root&password=41135061528");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to insert a new task into the database
    public void insertTask(String taskName, String shortDescription, java.sql.Date deadline, Integer priority, Boolean reminderImage) {
        try {
            String query = "INSERT INTO tasks (task_name, short_description, deadline, entry_date";
            String values = "VALUES (?, ?, ?, ?";
            if (priority != null) {
                query += ", priority";
                values += ", ?";
            }
            query += ", reminder_image) ";
            values += ", ?)";

            PreparedStatement statement = connection.prepareStatement(query + values);
            statement.setString(1, taskName);
            statement.setString(2, shortDescription);
            statement.setDate(3, deadline);
            // Current date
            statement.setDate(4, new java.sql.Date(System.currentTimeMillis()));

            if (priority != null) {
                statement.setInt(5, priority);
                statement.setBoolean(6, reminderImage);
            } else {
                statement.setBoolean(5, reminderImage);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     // Method to show all tasks in the database
     public void showAllTasks() {
         setTitle("All Tasks");
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         setSize(600, 200);
         setLocation(20, 300);

         String[] columnNames = {"ID", "Task Name", "Short Description", "Deadline", "Priority", "Reminder Image", "Entry Date"};
         DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
         JTable table = new JTable(tableModel);
         // Create the filter panel
         JPanel filterPanel = new JPanel(new FlowLayout());
         // Add the filter panel to the frame
         add(filterPanel, BorderLayout.NORTH);
         // Disable the table header
         table.getTableHeader().setReorderingAllowed(false);
         table.getTableHeader().setEnabled(false);
         // Create scroll pane
         JScrollPane scrollPane = new JScrollPane(table);
         add(scrollPane);

         // Add the start date label and field
         JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD): ");
         startDateField = new JTextField(10);
         filterPanel.add(startDateLabel);
         filterPanel.add(startDateField);

         // Add the end date label and field
         JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD): ");
         endDateField = new JTextField(10);
         filterPanel.add(endDateLabel);
         filterPanel.add(endDateField);

         // Select all tasks from database
         try {
             String query = "SELECT * FROM tasks";
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery();
             while (rs.next()) {
                 int taskId = rs.getInt("id");
                 String taskName = rs.getString("task_name");
                 String shortDescription = rs.getString("short_description");
                 Date deadline = rs.getDate("deadline");
                 int priority = rs.getInt("priority");
                 boolean reminderImage = rs.getBoolean("reminder_image");
                 Date entryDate = rs.getDate("entry_date");

                 Object[] rowData = {taskId, taskName, shortDescription, deadline, priority, reminderImage, entryDate};
                 tableModel.addRow(rowData);
             }
             // if no task found in the database, display an error message
             if (tableModel.getRowCount() == 0) {
                 JOptionPane.showMessageDialog(null, "No tasks found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
             } else {
                 setVisible(true);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         // Delete task button
         JButton deleteButton = new JButton("Delete a task");
         // Action listener for delete task button
         deleteButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 // Call the deleteTask() method when the button is clicked
                 deleteTask(table);
             }
         });
         // Create new panel for other buttons
         JPanel panel = new JPanel(new BorderLayout());
         panel.add(scrollPane, BorderLayout.CENTER);
         add(panel);
         // Update task button
         JButton updateButton = new JButton("Update a Task");
         // View task image button
         JButton viewTaskImageButton = new JButton("View Task Image");
         // Sort by priority button
         JButton sortByPriorityButton = new JButton("Sort By Priority");
         // Filter tasks button
         JButton filterTasksButton = new JButton("Filter Tasks");
         filterPanel.add(filterTasksButton);

         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
         // Add the all buttons to the panel
         buttonPanel.add(deleteButton);
         buttonPanel.add(updateButton);
         buttonPanel.add(viewTaskImageButton);
         buttonPanel.add(sortByPriorityButton);
         panel.add(buttonPanel, BorderLayout.SOUTH);

         // Action listener for update task button
         updateButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 // Call the updateTask() method when the button is clicked
                 updateTask(table);
             }
         });

         // Action listener for view task image button
         viewTaskImageButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 // Call the viewTaskImage() method when the button is clicked
                 viewTaskImage(table);
             }
         });

         // Action listener for sort by priority button
         sortByPriorityButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 // Sort Priority screen appears
                 sortPriority sortPriority = new sortPriority();
                 sortPriority.setVisible(true);
                 sortPriority.setResizable(false);
             }
         });

         // Action listener for filter tasks button
         filterTasksButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                // Call the filterTasks() method when the button is clicked
                filterTasks(table);
             }
         });

     }

    //Method to delete a task from the database
     public void deleteTask(JTable table){
         // Retrieve data for selected task
         int selectedRow = table.getSelectedRow();
         if (selectedRow == -1){
             JOptionPane.showMessageDialog(null, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         int taskID = (int) table.getValueAt(selectedRow, 0);

        try {
            String query = "DELETE FROM tasks WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, taskID);
            int deletedRows = statement.executeUpdate();
            if (deletedRows == 0){
                JOptionPane.showMessageDialog(null, "Task with ID " + taskID + " not found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Task with ID " + taskID + " deleted successfully", "success", JOptionPane.INFORMATION_MESSAGE);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
     }


     // Method to update a task from the database
    public void updateTask(JTable table){
        // Retrieve data for the selected task
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(null, "Please select a row to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int taskId = (int) table.getValueAt(selectedRow, 0);

        try {
            String query = "SELECT * FROM tasks WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, taskId);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "Task with ID " + taskId + " not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String taskName = rs.getString("task_name");
            String shortDescription = rs.getString("short_description");
            java.sql.Date deadline = rs.getDate("deadline");
            int priority = rs.getInt("priority");
            boolean reminderImage = rs.getBoolean("reminder_image");

            // Populate input fields with retrieved data
            JTextField TaskName = new JTextField(taskName);
            JTextField shortDesc = new JTextField(shortDescription);
            JTextField deadLine = new JTextField(deadline.toString());
            JTextField Priority = new JTextField(Integer.toString(priority));
            JCheckBox reminderCheck = new JCheckBox();
            reminderCheck.setSelected(reminderImage);

            JPanel panel = new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Task Name"));
            panel.add(TaskName);
            panel.add(new JLabel("Short Description"));
            panel.add(shortDesc);
            panel.add(new JLabel("Deadline:"));
            panel.add(deadLine);
            panel.add(new JLabel("Priority:"));
            panel.add(Priority);
            panel.add(new JLabel("Reminder Image:"));
            panel.add(reminderCheck);

            int result = JOptionPane.showConfirmDialog(null, panel, "Edit Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION){
               // Update task in the database
            query = "UPDATE tasks SET task_name = ?, short_description = ?, deadline = ?, priority = ?, reminder_image = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, TaskName.getText());
            statement.setString(2, shortDesc.getText());
            statement.setDate(3, java.sql.Date.valueOf(deadLine.getText()));
            statement.setInt(4, Integer.parseInt(Priority.getText()));
            if (Integer.parseInt(Priority.getText()) < 0){
                JOptionPane.showMessageDialog(null, "Priority must be a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            statement.setBoolean(5, reminderCheck.isSelected());
            statement.setInt(6, taskId);

            int rowsUpdated =  statement.executeUpdate();
            if (rowsUpdated == 0){
                JOptionPane.showMessageDialog(null, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Task with ID " + taskId + " updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Update table view
                table.getModel().setValueAt(TaskName.getText(), selectedRow, 1);
                table.getModel().setValueAt(shortDesc.getText(), selectedRow, 2);
                table.getModel().setValueAt(deadLine.getText(), selectedRow, 3);
                table.getModel().setValueAt(Priority.getText(), selectedRow, 4);
                table.getModel().setValueAt(reminderCheck.isSelected(), selectedRow, 5);
               }
           }
        } catch (SQLException | NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Please enter a valid integer for priority", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to view task image
    public void viewTaskImage(JTable table){
        // Retrieve data for the selected task
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(null, "Please select a row to view image", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int taskID = (int) table.getValueAt(selectedRow, 0);
        boolean hasImage = (boolean) table.getValueAt(selectedRow, 5);

        if (!hasImage){
            JOptionPane.showMessageDialog(null, "The task does not include an image", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Define the imagePath
        String imagePath = "C:\\Users\\Halim\\IdeaProjects\\TaskPlanner\\task_images\\task" + taskID + ".jpg";
        File imageFile = new File(imagePath);
        if (!imageFile.exists()){
            JOptionPane.showMessageDialog(null, "Image file not found for task " + taskID ,"Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(imageLabel);
        frame.pack();
        frame.setVisible(true);
    }

    // Method to sort tasks by priority
    public void sortByPriority(JTable table, java.sql.Date deadline){
       try {
           // Retrieve all the tasks from the database
           String query = "SELECT * FROM tasks WHERE deadline = ?";
           PreparedStatement statement = connection.prepareStatement(query);
           statement.setDate(1, deadline);
           ResultSet rs = statement.executeQuery();


           // Create an ArrayList to store the tasks
           ArrayList<Task> tasks = new ArrayList<>();

           // Add the tasks that meet the filter criteria to the ArrayList
           while (rs.next()){
               Task task = new Task(
                       rs.getInt("id"),
                       rs.getString("task_name"),
                       rs.getString("short_description"),
                       rs.getDate("deadline"),
                       rs.getInt("priority"),
                       rs.getBoolean("reminder_image"),
                       rs.getDate("entry_date")
               );
               tasks.add(task);
           }

           // Sort the tasks by priority using selection sort
           for (int i = 0 ; i < tasks.size() ; i++){
               int minIndex = i;
               for (int j = i + 1 ; j < tasks.size() ; j++){
                   if (tasks.get(j).getPriority() < tasks.get(minIndex).getPriority()){
                       minIndex = j;
                   }
               }
               if (minIndex != i){
                   Task temp = tasks.get(i);
                   tasks.set(i, tasks.get(minIndex));
                   tasks.set(minIndex, temp);
               }
           }

           // Set up the table model
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("ID");
           model.addColumn("Task Name");
           model.addColumn("Short Description");
           model.addColumn("Deadline");
           model.addColumn("Priority");
           model.addColumn("Reminder Image");
           model.addColumn("Entry Date");

           // Add the sorted tasks to the table model
           for (Task task : tasks){
               Object[] rowData = {
                 task.getId(),
                 task.getName(),
                 task.getDescription(),
                 task.getDeadline(),
                 task.getPriority(),
                 task.hasReminderImage(),
                 task.getEntryDate()
               };
               model.addRow(rowData);
           }

           // Set the table model
           table.setModel(model);

       } catch (SQLException e){
           e.printStackTrace();
       }
    }

    // Method to filter tasks
    private void filterTasks(JTable table) {
        try {
              java.sql.Date startDate = null, endDate = null;
              String query;
             if (startDateField.getText().isEmpty() || endDateField.getText().isEmpty()){
                 // if start or end date fields are empty, show all tasks
                 query = "SELECT * FROM tasks";
             } else {
                 // otherwise, filter tasks by deadline
                 startDate = Date.valueOf(startDateField.getText());
                 endDate = Date.valueOf(endDateField.getText());
                 query = "SELECT * FROM tasks WHERE deadline BETWEEN ? AND ?";
             }
            PreparedStatement statement = connection.prepareStatement(query);
            if (!query.equals("SELECT * FROM tasks")) {
                statement.setDate(1, startDate);
                statement.setDate(2, endDate);
            }
            ResultSet rs = statement.executeQuery();

            // Create an ArrayList to store the tasks
            ArrayList<Task> tasks = new ArrayList<>();

            // Add the tasks that meet the filter criteria to the ArrayList
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("task_name"),
                        rs.getString("short_description"),
                        rs.getDate("deadline"),
                        rs.getInt("priority"),
                        rs.getBoolean("reminder_image"),
                        rs.getDate("entry_date")
                );
                tasks.add(task);
            }

            // Set up the table model
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Task Name");
            model.addColumn("Short Description");
            model.addColumn("Deadline");
            model.addColumn("Priority");
            model.addColumn("Reminder Image");
            model.addColumn("Entry Date");

            // Add the tasks to the table model
            for (Task task : tasks) {
                Object[] rowData = {
                        task.getId(),
                        task.getName(),
                        task.getDescription(),
                        task.getDeadline(),
                        task.getPriority(),
                        task.hasReminderImage(),
                        task.getEntryDate(),
                };
                model.addRow(rowData);
            }

            // Set the table model
            table.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to show notification when a task deadline is approaching
    public void showNotifications(){
        try {
            // Get the current date
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            // Query the database to find all tasks that have a deadline of 1 day or less from the current date
            String query = "SELECT * FROM tasks WHERE DATEDIFF(deadline, ?) <= 1";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, currentDate);
            ResultSet rs = statement.executeQuery();

            // If any tasks are found, display a message dialog box to notify the user
            if (rs.next()){
                String message = "The following tasks have a deadline of 1 day or less: ";
                do {
                    message += "\n- " + rs.getString("task_name");
                } while (rs.next());
                JOptionPane.showMessageDialog(null, message);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
