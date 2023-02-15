package btb.logic;

import btb.constants.Constant;
import btb.exceptions.DirectoryIsAFileException;
import btb.exceptions.FileIsADirectoryException;
import btb.tasks.Deadline;
import btb.tasks.Event;
import btb.tasks.Task;
import btb.tasks.TaskManager;
import btb.tasks.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {
    private static final String FILE_DIRECTORY = "./data";
    private static final String FILE_NAME = "tasks.txt";
    private static final String FILE_PATH = "./data/tasks.txt";

    public static String getFilePath() {
        return FILE_PATH;
    }

    public static void createFile(TaskManager tasks) throws
            FileNotFoundException, FileIsADirectoryException, DirectoryIsAFileException {

        try {
            File directory = new File(FILE_DIRECTORY);
            boolean directedCreated = directory.mkdir();
            if (directedCreated) {
                System.out.println("\t " + FILE_DIRECTORY + " does not exist.");
                System.out.println("\t The directory " + FILE_DIRECTORY + " has been created.");
                System.out.println(Constant.DOTTED_LINE);
            }

            File f = new File(FILE_PATH);
            boolean fileCreated = f.createNewFile();
            if (fileCreated) {
                System.out.println("\t " + FILE_NAME + " does not exists.");
                System.out.println("\t The file " + FILE_NAME + " has been created.");
                System.out.println(Constant.DOTTED_LINE);
            }

            if (f.isDirectory()) {
                throw new FileIsADirectoryException();
            }
            if (directory.isFile()) {
                throw new DirectoryIsAFileException();
            }

        } catch(IOException e) {
            System.out.println("\t An error occurred.");
            System.exit(-1);
        }

        printAndLoadContent(tasks);
    }

    private static void printAndLoadContent(TaskManager tasks) throws
            FileNotFoundException {
        File f = new File(FILE_PATH);
        Scanner sc = new Scanner(f);

        boolean firstLoop = true;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            System.out.println("\t " + line);

            if (firstLoop) {
                firstLoop = false;
                continue;
            }
            loadContent(line, tasks);
        }
        if (!firstLoop) {
            System.out.println(Constant.DOTTED_LINE);
        }
    }

    private static void loadContent(String description, TaskManager tasks) {
        // splits into [command, task description, status of task]
        String[] taskDescription = Parser.handleTextInputs(description);

        switch (taskDescription[0]) {
        case "todo":
            Task task = new Todo(taskDescription[1]);
            if (taskDescription[2].equals("done")) {
                task.setDone(true);
            }
            tasks.add(task);
            break;
        case "deadline":
            String[] splitDescription = Parser.handleTextDeadlineInputs(taskDescription[1]);
            task = new Deadline(splitDescription[0], splitDescription[1]);
            if (taskDescription[2].equals("done")) {
                task.setDone(true);
            }
            tasks.add(task);
            break;
        case "event":
            splitDescription = Parser.handleTextEventInputs(taskDescription[1]);
            task = new Event(splitDescription[0], splitDescription[1], splitDescription[2]);
            if (taskDescription[2].equals("done")) {
                task.setDone(true);
            }
            tasks.add(task);
            break;
        default:
            System.out.println("\t Oops, text file is in the wrong format...");
            System.out.println("\t Please delete file and try again");
            System.exit(-1);
        }
    }
}