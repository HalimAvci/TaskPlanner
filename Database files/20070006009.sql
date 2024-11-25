-- Show existing tasks in the database
SELECT id as ID, task_name as 'Task Name', short_description as 'Short Description',
deadline as Deadline, priority as Priority, reminder_image as 'Reminder Image',
entry_date as 'Entry Date'
FROM tasks;

-- Delete a task from database with specific ID
DELETE FROM tasks WHERE id = 4;

-- Show a task from database with specific ID
SELECT * FROM tasks WHERE id = 6;




