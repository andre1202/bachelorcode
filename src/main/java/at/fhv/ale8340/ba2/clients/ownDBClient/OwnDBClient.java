package at.fhv.ale8340.ba2.clients.ownDBClient;

import at.fhv.ale8340.ba2.clients.IClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class OwnDBClient implements IClient {
    private final Logger logger = (Logger) LoggerFactory.getLogger(OwnDBClient.class);


    @Override
    public boolean loadData(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Socket socket = new Socket("localhost", 9943);

            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println("SELECT * WHERE TIME " + printDate(startDate) + " < TIMESTAMP > " + printDate(endDate) + ";");
            pw.println("");
            pw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String tmp;
            int count =0;
            while((tmp = br.readLine()) != null){

//                System.out.println(tmp);
//                if(tmp.equals("=END=")){
//                    break;
//                }
                count++;
                if(tmp.equals("=END=")){
                    System.out.println("Number Of Entrys: " + count);
                    System.out.println(tmp);
                    break;
                }
            }

            br.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadSensorID(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Socket socket = new Socket("localhost", 9943);

            PrintWriter pw = new PrintWriter(socket.getOutputStream());
//            pw.println("COUNT sensorid WHERE TIME " + printDate(startDate) + " < TIMESTAMP > " + printDate(endDate) +
//                    " AND SENSORID " + printList(sensorIDS) + ";");
            pw.println("COUNT sensorid WHERE TIME " + printDate(startDate) + " < TIMESTAMP > "
                    + printDate(endDate)+ ";");
            pw.println("");
            pw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String tmp;
            int count = 0;
            while((tmp = br.readLine()) != null){

                System.out.println(tmp);
                count ++;
                if(tmp.equals("=END=")){
                    System.out.println("Number Of Entrys: " + (count-1));
                    System.out.println(tmp);
                    break;
                }

            }

            br.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadValues(LocalDateTime startDate, LocalDateTime endDate, List<String> sensorIDS, Double min_value, Double max_value) {
        try {
            Socket socket = new Socket("localhost", 9943);

            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println("SELECT * WHERE TIME " + printDate(startDate) + " < TIMESTAMP > " + printDate(endDate) +
                    " AND SENSORID " + printList(sensorIDS) + " AND VALUE " + min_value + " < VALUE > " + max_value +
                    ";");
            pw.println("");
            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String tmp;
            int count = 0;
            while((tmp = br.readLine()) != null){

//                System.out.println(tmp);
//                if(tmp.equals("=END=")){
//                    break;
//                }

                count++;
                if(tmp.equals("=END=")){
                    System.out.println("Number Of Entrys: " + count);
                    System.out.println(tmp);
                    break;
                }
            }

            br.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String displayString() {
        return "OwnDB";
    }


    public String printDate(LocalDateTime localDateTime){
        StringBuilder sb = new StringBuilder();
        sb.append(localDateTime.getDayOfMonth());
        sb.append("-");
        sb.append(localDateTime.getMonth().getValue());
        sb.append("-");
        sb.append(localDateTime.getYear());
        sb.append("T");
        sb.append(localDateTime.getHour());
        sb.append(":");
        sb.append(localDateTime.getMinute());
        sb.append(":");
        sb.append(localDateTime.getSecond());
        return sb.toString();
    }

    public String printList(List<String> list){

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < list.size(); i++){
            sb.append("\"");
            sb.append(list.get(i));
            sb.append("\"");

            if(i < (list.size()-2)){
                sb.append(", ");
            }

        }
        sb.append("]");
        return sb.toString();

    }
}
