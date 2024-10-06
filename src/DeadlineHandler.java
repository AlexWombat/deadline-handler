import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;
import org.apache.commons.io.FilenameUtils;

public class DeadlineHandler {
  public static void main(String[] args) {

    DeadlineHandler deadlineHandler = new DeadlineHandler();
    Scanner input = new Scanner(System.in);

    System.out.println("\nWelcome to the deadline handler!");
    while (true) {
      System.out.println("\nEnter 1 to input a new deadline item, 2 to list all existing deadline items, 3 to edit an existing deadline item, or 4 to EXIT:");
      if (input.hasNextInt()) {
        int intInput = input.nextInt();
        input.nextLine();
        if (intInput == 1) {
          String name = deadlineHandler.promptAndGetInput(input ,"\nEnter the name of the deadline item:");
          String description = deadlineHandler.promptAndGetInput(input ,"Enter a description:");
          String deadlineDate = deadlineHandler.promptAndGetInput(input ,"Enter the deadline date (yyyy-mm-dd):");
          LocalDate deadline;
          while (true) {
            try {
              deadline = LocalDate.parse(deadlineDate);
              break;
            } catch (DateTimeParseException e) {
              deadlineDate = deadlineHandler.promptAndGetInput(input ,"Enter the date according to yyyy-mm-dd:");
            }
          }
          DeadlineItem deadlineItem = new DeadlineItem(name, description, deadline);
          deadlineHandler.save(deadlineItem);
        } else if (intInput == 2) {
          deadlineHandler.listAllDeadlines(deadlineHandler);
        } else if (intInput == 3) {
          String name = deadlineHandler.promptAndGetInput(input ,"\nEnter the name of the existing deadline item to edit:");
          Optional<DeadlineItem> optionalDeadlineItem = deadlineHandler.load(name);
          if (optionalDeadlineItem.isPresent()) {
            DeadlineItem loadedItem = optionalDeadlineItem.get();
            System.out.println("\nYou are about to edit the following deadline item:\n"+loadedItem);
            String newName = deadlineHandler.promptAndGetInput(input ,"\nEnter a new name (leave blank to keep the previous name):");
            String newDesc = deadlineHandler.promptAndGetInput(input ,"Enter a new description (leave blank to keep the previous description):");
            String newDate = deadlineHandler.promptAndGetInput(input ,"Enter a new deadline date (leave blank to keep the previous date):");
            if (!newName.trim().isEmpty()) {
              loadedItem.setName(newName); 
            }
            if (!newDesc.trim().isEmpty()) {
              loadedItem.setDescription(newDesc); 
            }
            if (!newDate.trim().isEmpty()) {
              loadedItem.setDeadline(LocalDate.parse(newDate)); 
            }
            deadlineHandler.save(loadedItem);
          } else {
            continue;
          }
          
        } else if (intInput == 4) {
          break;
        } else {
          continue;
        }
      } else {
        continue;
      }
    }
  }

  private String promptAndGetInput(Scanner input, String prompt) {
    System.out.println(prompt);
    String userInput = input.nextLine();
    return userInput;
  }

  private void listAllDeadlines(DeadlineHandler deadlineHandler) {
    File dir = new File("bin/");
    File[] directoryListing = dir.listFiles();
    System.out.println("\nExisting deadline items:");
    for (File child : directoryListing) {
      if (FilenameUtils.getExtension(child.getName()).trim().equals("ser")) {
        System.out.println("\n"+deadlineHandler.load(child.getName().split("\\.")[0]).get());
      }
    }
  }

  private void save(DeadlineItem item) {
    String fileName = "bin/"+item.getName()+".ser";
    try {
      FileOutputStream file = new FileOutputStream(fileName);
      ObjectOutputStream out = new ObjectOutputStream(file);
      out.writeObject(item);
      out.close();
      System.out.println("Item saved.");
    } catch (IOException e) {
      System.out.println("Could not save the deadline item.");
    }
  }

  private Optional<DeadlineItem> load(String name) {
    try {
      FileInputStream file = new FileInputStream("bin/"+name+".ser");
      ObjectInputStream in = new ObjectInputStream(file);
      Optional<DeadlineItem> optional = Optional.of((DeadlineItem) in.readObject());
      in.close();
      return optional;
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Could not load the deadline item.");
      return Optional.empty();
    }
  }
}
