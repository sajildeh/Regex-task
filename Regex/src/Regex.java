import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	public static void main(String[] args) { 
		String fileContent="";
		String interfaceName="";
		String ipAddress="";
		String description="";
		String ifSpeed="";
		String duplexMode="";
		String adminStatus="";
		String operationStatus="";
		String macAddress="";
		String mtu="";
		ArrayList<String> inputFile=new ArrayList<>();  //for interface data
		int outputCount=0;  //interfaces number
		int flag=0; //to skip first line

		try {
			File file = new File("Show Interface output.txt");   //open file and read from it
			BufferedReader ReadFileBuffer = new BufferedReader(new FileReader(file));
			String inputLine;
			while ((inputLine=ReadFileBuffer.readLine())!=null) {
				while (inputLine.length()==0) //for empty lines
					inputLine=ReadFileBuffer.readLine();

				if(inputLine.toCharArray()[0]!= ' ' && flag!=0) {  //if there is no space in the beginning of the string, this means new interface
					outputCount++;
					inputFile.add(fileContent);  //add interface data 
					fileContent="";   //clear the string
					fileContent=fileContent+"\n";
					fileContent=fileContent+inputLine;
				}
				else {
					flag=1;
					fileContent=fileContent+"\n";
					fileContent=fileContent+inputLine;		    		
				}

			}
			outputCount++;
			inputFile.add(fileContent);
			fileContent="";
			fileContent=fileContent+"\n";
			fileContent=fileContent+inputLine;
			ReadFileBuffer.close();      //close file

			FileWriter fileWrite = new FileWriter("OutputFile.txt");   //open file and write on it
			BufferedWriter WriteFileBuffer = new BufferedWriter(fileWrite);
			WriteFileBuffer.write("Matched interfaces are " + outputCount + "\n\n");  //write data on file

			for(int j=0;j<inputFile.size();j++){  //loop on interfaces
				interfaceName=FindInterfaceName(inputFile,j);
				ipAddress=FindIPAddress(inputFile,j);
				description=FindInterfaceDescription(inputFile,j);
				ifSpeed=FindIfSpeed(inputFile,j);
				duplexMode=FindDuplexMode(inputFile,j);
				adminStatus=FindAdminStatus(inputFile,j);
				operationStatus=FindOperationStatus(inputFile,j);
				macAddress=FindMACAddress(inputFile,j);
				mtu=FinMTU(inputFile,j);

				WriteFileBuffer.write(outputString(j+1,interfaceName,ipAddress,description,
						ifSpeed,duplexMode,adminStatus,operationStatus,macAddress,mtu));     //write data on file
			}

			WriteFileBuffer.close();  //close file
			System.out.println("Check file for output!\n");

		} catch (FileNotFoundException e) {
			System.out.println("File not found\n");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error\n");
			//e.printStackTrace();
		}
	}
	//*******************************************************************************************************************************
	private static String FindIPAddress(ArrayList<String> inputFile, int j) {
		//find the ip address
		Pattern pattern1 = Pattern.compile("(([0-9]{1,3}[\\.]){3}[0-9]{1,3})",Pattern.MULTILINE);  //define the regex
		Matcher matcher1 = pattern1.matcher(inputFile.get(j));   //apply the regex on string
		if(matcher1.find()) {   //if match found do:
			return matcher1.group();
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindInterfaceName(ArrayList<String> inputFile, int j) {
		//find the interface name
		Pattern pattern = Pattern.compile("(^\\w+....(?=\\s))",Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(inputFile.get(j));
		if(matcher.find()) {
			return matcher.group();
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindInterfaceDescription(ArrayList<String> inputFile, int j) {
		//find interface description
		Pattern pattern2 = Pattern.compile("(?<=Description:).*",Pattern.MULTILINE);
		Matcher matcher2 = pattern2.matcher(inputFile.get(j));
		if(matcher2.find()) {
			return matcher2.group();
		}
		else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindIfSpeed(ArrayList<String> inputFile, int j) {
		//find ifSpeed
		Pattern pattern3 = Pattern.compile("(\\b,.*(?=, 100))",Pattern.MULTILINE);
		Matcher matcher3 = pattern3.matcher(inputFile.get(j));
		if(matcher3.find()) {
			return matcher3.group();
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindDuplexMode(ArrayList<String> inputFile, int j) {
		//find duplex mode
		Pattern pattern4 = Pattern.compile("(\\b.*(?=, 100))",Pattern.MULTILINE);
		Matcher matcher4 = pattern4.matcher(inputFile.get(j));
		if(matcher4.find()) {
			String duplexMode=matcher4.group();
			String [] arrOfStr = duplexMode.split(",");
			return arrOfStr[0];
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindAdminStatus(ArrayList<String> inputFile, int j) {
		//find admin status
		Pattern pattern5 = Pattern.compile("(?<=is )[\\w]*",Pattern.MULTILINE);
		Matcher matcher5 = pattern5.matcher(inputFile.get(j));
		if(matcher5.find()) {
			return matcher5.group();
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindOperationStatus(ArrayList<String> inputFile, int j) {
		//find operation status
		Pattern pattern6 = Pattern.compile("(?<=is )[\\w]* ",Pattern.MULTILINE);
		Matcher matcher6 = pattern6.matcher(inputFile.get(j));
		if(matcher6.find()) {
			return matcher6.group();	
		}else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FindMACAddress(ArrayList<String> inputFile, int j) {
		//find mac address
		Pattern pattern7 = Pattern.compile("[0-9a-f]{4}.[0-9a-f]{4}.[0-9a-f]{4}",Pattern.MULTILINE);
		Matcher matcher7 = pattern7.matcher(inputFile.get(j));
		if(matcher7.find()) {
			return matcher7.group();	
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	private static String FinMTU(ArrayList<String> inputFile, int j) {
		//find mtu
		Pattern pattern8 = Pattern.compile("(?<=MTU )\\w*",Pattern.MULTILINE);
		Matcher matcher8 = pattern8.matcher(inputFile.get(j));
		if(matcher8.find()) {
			return matcher8.group();	
		} else {
			return " ";
		}
	}
	//*******************************************************************************************************************************
	public static String outputString(int interfaceNumber, String interfaceName, String ipAddress, String description, String ifSpeed,
			String duplexMode, String adminStatus, String operationStatus, String macAddress, String mtu) {
		return  "Interface #" + interfaceNumber +":" +
				"\n\tInterface name: " + interfaceName +
				"\n\tIp address: " + ipAddress + 
				"\n\tInterface description: " + description +
				"\n\tIfSpeed: " + ifSpeed +
				"\n\tDuplex mode: " + duplexMode +
				"\n\tAdmin status: " + adminStatus +
				"\n\tOperation status: " + operationStatus +
				"\n\tMac address: " + macAddress +
				"\n\tMTU: " + mtu +
				"\n\n";
	}
	//*******************************************************************************************************************************
}
