import java.io.Serializable;
import java.time.LocalDate;

public class DeadlineItem implements Serializable{

  /* Instance Variables */
  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  private LocalDate deadline; 

  /* Constructors */
  public DeadlineItem(String name, String description, LocalDate deadline) {
    this.name = name;
    this.description = description;
    this.deadline = deadline;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
    
  public LocalDate getDeadline() {
    return deadline;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
  }

  @Override
  public String toString() {
    String outputString = String.format("Name: %s\nDescription: %s\nDeadline: %tF", name, description, deadline);
    return outputString;
  }

}
