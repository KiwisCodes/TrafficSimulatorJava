import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import it.polito.appeal.traci.SumoTraciConnection;

public class SimpleSumoConnection {

    public static void main(String[] args) {
        // 1. Define paths
        String sumoBinary = "/Users/apple/sumo/bin/sumo-gui"; // or "sumo-gui" for visualization
        String configFile = "sumo-files/minimal.sumocfg";

        // 2. Create connection using documented constructor
        SumoTraciConnection conn = new SumoTraciConnection(sumoBinary, configFile);

        // Optional: show SUMO logs in the console
        conn.printSumoOutput(true);
        conn.printSumoError(true);

        // 3. Add options (TraCI options must not include "--")
        conn.addOption("start", null);          // automatically start SUMO
        conn.addOption("quit-on-end", "true");  // quit when simulation ends
        
        conn.addOption("delay", "1000");        // 1000ms = 1 second delay per step
        try {
            // 4. Start SUMO process and connect
            conn.runServer();
            System.out.println("✅ Connected to SUMO.");

            // 5. Run simulation for 50 steps
            for (int step = 0; step < 50; step++) {
                conn.do_timestep();

                @SuppressWarnings("unchecked")
                List<String> vehicleIds = (List<String>)
                    conn.do_job_get(Vehicle.getIDList());

                System.out.println("Step " + step + " Vehicles: " + vehicleIds);
            }

            // 6. Close connection
            conn.close();
            System.out.println("✅ Simulation finished and connection closed.");

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
