import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
public class Synthacks
{
    JFrame f;
    JTable j;
    
    void CreateTable(String[][] table)
    {
        f = new JFrame();
        f.setTitle("Calender");
        String[][] data = new String[table[0].length][table.length];
        for(int r = 0; r<table[0].length; r++)
        {
            for(int c = 0; c<table.length; c++)
            {
                // if(c==0)
                // {   
                    // if(table[c][r].equals("a"))
                    // {
                        // data[r][c]= "available";
                    // }
                    // else
                    // {
                        // data[r][c]= "unavailable";
                    // }   
                // }
                // else if(c==1)
                // {   
                    // data[r][c]= table[c][r].substring(6,8)+"/"+table[c][r].substring(4,6)+"/"+table[c][r].substring(0,4);
                // }
                // else if((c==2)||(c==3))
                // {   
                    // data[r][c]= table[c][r].substring(0,2)+":"+table[c][r].substring(3,5);
                // }
                // else
                    data[r][c]=table[c][r];
            }
        }
        
        String[] columnNames = { "Availibility","Date", "Start(24hr)", "End(24hr)", "Event Name"};
        j = new JTable(data, columnNames);
        j.setBounds(30, 40, 200, 300);
        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        f.setSize(500, 200);
        f.setVisible(true);
    }
    
    String createIcsEvent(String nameOfEvent, String startTime, String endTime)
    {
        String icsEvent = "\nBEGIN:VEVENT\nSUMMARY:"+nameOfEvent+"\nDTSTART;TZID=Asia/Kolkata:"+startTime+"\nDTEND;TZID=Asia/Kolkata:"+endTime+"\nEND:VEVENT\n";
        return icsEvent;
    }
    
    void createIcsFile(String[][] table)throws IOException
    {
        Path fileName = Path.of("/Users/Arushi/Downloads/icsFile.ics");
        //need to create code to put path
        String header = "BEGIN:VCALENDAR\nVERSION:2.0\nCALSCALE:GREGORIAN";
        Files.writeString(fileName, header);
        int numCol = table[0].length;
        int c = 0;
        while(c<numCol)
        {
            if(table[0][c].equalsIgnoreCase("u"))
            {
                String startTime= table[1][c]+"T"+table[2][c];
                String endTime= table[1][c]+"T"+table[3][c];
                String event = createIcsEvent(table[4][c],startTime,endTime);
                Files.writeString(fileName, event, StandardOpenOption.APPEND);
            }
            c++;
        }
        String footer = "END:VCALENDAR";
        Files.writeString(fileName, header, StandardOpenOption.APPEND);
        System.out.println("worked");
    }
    
    public void main()throws IOException{
        Scanner sc = new Scanner(System.in);
        
        //PART 1
        System.out.println("Enter the number of available HOURLY time slots in the WEEK (max: 24*7 = 168)");
        int numslots = sc.nextInt();
        if((numslots>168)||(numslots<1))
        {
            while ((numslots>168)||(numslots<1))
            {
                System.out.println("Error in input. Please enter within the range.");
                System.out.println("Enter the number of available HOURLY time slots in the WEEK (max: 24*7 = 168)");
                numslots = sc.nextInt();
            }
        }
        
        String[][] timeslots = new String[5][numslots];
        
        int i=0;
        while (i<numslots)
        {
            //availability
            timeslots[0][i] = "a";
            //date
            System.out.println("Enter the date in the format yearmonthday (EX: August 8 2022 => 20220808)");
            String date = sc.next();
            timeslots[1][i] = date;
            //start time
            System.out.println("Enter the start time in the format hourminutesecond (EX: 10:30 PM => 223000 ");
            String starttime = sc.next();
            timeslots[2][i] = starttime;
            //end time
            System.out.println("Enter the end time in the format hourminutesecond (EX: 10:30 PM => 223000 ");
            String endtime = sc.next();
            timeslots[3][i] = endtime;
            
            //confirmation
            System.out.println("DATE: "+date);
            System.out.println("START TIME: "+starttime);
            System.out.println("END TIME: "+endtime);
            System.out.println("Is it confirmed? (y or n)");
            String check1 = sc.next();
            
                if (check1.equals("y"))
                {
                    CreateTable(timeslots);
                    i++;
                }
                else if (check1.equals("n"))
                {
                    System.out.println("Please re-enter the information for this time slot");
                }
                else 
                {
                    while ((check1!="y")&&(check1!="n"))
                    {
                        System.out.println("Is it confirmed? (y or n)");
                        check1 = sc.next();
                        if (check1.equals("y"))
                        {
                            CreateTable(timeslots);
                            i++;
                        }
                        else if (check1.equals("n"))
                        {
                            System.out.println("Please re-enter the information for this time slot");
                        }
                    }
                

        
        //PART 2
        int x=1;
        while (x==1)
        {
            System.out.println("Enter the event name");
            String eventname = sc.next();
            System.out.println("Enter the number of hours required for the event");
            int numhrs = sc.nextInt();
            int numofavail = 0;
            for (int j=0; j<numslots; j++)
            {
                if (timeslots[0][j].equals("a"))
                {
                    numofavail++;
                }
            }
            if (numofavail<numhrs)
            {
                System.out.println("Not enough available hours for this event");
            }
            else
            {
                int f = 0;
                int ind = 0;
                while (f<numhrs)
                {
                    if (timeslots[0][ind].equals("u"))
                    {
                        ind++;
                    }
                    else
                    {
                        timeslots[0][ind] = "u";
                        timeslots[4][ind] = eventname;
                        f++;
                        ind++;
                    }
                }
                CreateTable(timeslots);
            }

            //check for loop continuation
            System.out.println("Are there any more events to be added? (y or n)");
            String check2 = sc.next();
            if (check2.equals("n"))
            {
                x=0;
            }
        }
        
        //PART 3
        createIcsFile(timeslots);
    }
    
}
}
}