import java.util.*;

public class FaultManager {

    static class Alarm {
        String interfaceId;
        String reason;
        Date timestamp;

        Alarm(String interfaceId, String reason) {
            this.interfaceId = interfaceId;
            this.reason = reason;
            this.timestamp = new Date();
        }

        @Override
        public String toString() {
            return "Alarm{interface=" + interfaceId + ", reason='" + reason + "', timestamp=" + timestamp + "}";
        }
    }

    private List<Alarm> alarmList = new ArrayList<>();

    public void generateAlarm(String interfaceId, String reason) {
        Alarm alarm = new Alarm(interfaceId, reason);
        alarmList.add(alarm);
        System.out.println("Generated Alarm: " + alarm);
    }

    public void correlateAlarms() {
        boolean packetLossDetected = false;
        boolean highUtilizationDetected = false;

        for (Alarm alarm : alarmList) {
            if (alarm.interfaceId.equals("ge-0/0/0") && alarm.reason.equals("Packet Loss Detected")) {
                packetLossDetected = true;
            }
            if (alarm.interfaceId.equals("ge-0/0/1") && alarm.reason.equals("High Utilization")) {
                highUtilizationDetected = true;
            }
        }

        if (packetLossDetected && highUtilizationDetected) {
            System.out.println("Root Cause Analysis: High utilization on ge-0/0/1 may be causing packet loss on ge-0/0/0.");
        } else {
            System.out.println("No correlation found between alarms.");
        }
    }

    public static void main(String[] args) {
        FaultManager manager = new FaultManager();
        manager.generateAlarm("ge-0/0/0", "Packet Loss Detected");
        manager.generateAlarm("ge-0/0/1", "High Utilization");
        manager.correlateAlarms();
    }
}
