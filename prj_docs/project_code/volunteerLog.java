import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *  volunteerLog.Java is a file intended to log a volunteer's 
 * name, start time, end time, tasks completed, and contact information
 * @author Joseline Ly and Garrison Gibson
 */
public class volunteerLog
{
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		System.out.println("Hello and welcome to the volunteer log!");
		System.out.println("Please enter your first and last name:");
		System.out.println("Please enter hours worked: ");
		System.out.println("Please enter the tasks you have completed:");
		System.out.println("Please enter your email:");
		System.out.println("Please enter your phone number:");
		System.out.println("Thank you for volunteering!");
		scnr.close();
	}

}
