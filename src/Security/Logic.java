package Security;

import java.util.Date;

import Main.Main;
import database.types.Door;

public class Logic {

	//reference obj to main to get the current doorID, can be changed.
	static Date d = new Date();
	static int door;
	
	
	/*
	 * can enter doors relating to their section
	 * can be given permission to use extra doors
	 * Log all instances of a door being used
	 */
	public static void seniorStaffPermission() 
	{
		door = Door.getCurrentDoor();
		System.out.println("Using doorID : "+ door+" as : Senior Staff");
		
	}
	
	
	/*
	 * can enter doors relating to their section
	 * Log all instances of a door being used
	 */
	public static void juniorStaffPermission() 
	{
		door = Door.getCurrentDoor();
		System.out.println("Using doorID : "+ door+" as : Junior Staff");
		
	}
	
	/*
	 * can enter doors relating to their section
	 * can be given permission to use extra doors
	 * Log all instances of a door being used
	 */
	public static void studentPermission() 
	{
		System.out.println("Using doorID "+ door +" as : Student");
		
	}
	
	/*
	 * can enter all doors 9AM-5PM
	* Log all instances of a door being used
	 */
	public static void dayStaffPermission() 
	{
		System.out.println("Using doorID : "+ door+" as : Day Staff");
		
		int now = d.getHours();
		
		if(now >= 7 || now <= 17)
		{
			System.out.println("Time constraint is valid, logging door attempt.");
		}
		else
		{
			System.out.println("Time constraint is invalid, checking door section...");
			
		}
		
	}
	/*
	 * can enter all doors 5PM-9AM
	 * Log all instances of a door being used
	 */
	public static void nightStaffPermission() 
	{
		System.out.println("Using doorID : "+ door+" as : Night Staff");
		
		int now = d.getHours();
		
		if(now <= 17 || now > 9)
		{
			System.out.println("Time constraint is valid, logging door attempt.");
		}
		else
		{
			System.out.println("Time constraint is invalid, checking door section...");
			
		}
		
	}

	

}
