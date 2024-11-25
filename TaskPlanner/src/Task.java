import java.sql.Date;

public class Task {
    private int id;
    private String name;
    private String description;
    private java.sql.Date deadline;
    private int priority;
    private boolean hasReminderImage;
    private java.sql.Date entryDate;

    public Task(int id, String name, String description, Date deadline, int priority, boolean hasReminderImage, Date entryDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.hasReminderImage = hasReminderImage;
        this.entryDate = entryDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public java.sql.Date getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public boolean hasReminderImage() {
        return hasReminderImage;
    }

    public java.sql.Date getEntryDate(){
        return entryDate;
    }

    // Override toString() method to return a string representation of the task
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                ", hasReminderImage=" + hasReminderImage +
                ", entryDate=" + entryDate +
                '}';
    }
}
