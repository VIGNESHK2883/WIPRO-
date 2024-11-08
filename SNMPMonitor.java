import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPMonitor {

    private static final String COMMUNITY = "public";
    private static final String SNMP_ADDRESS = "udp:10.10.10.1/161";
    private static final OID HOSTNAME_OID = new OID("1.3.6.1.2.1.1.5.0");
    private static final OID UPTIME_OID = new OID("1.3.6.1.2.1.1.3.0");
    private static final OID INTERFACE_STATUS_OID = new OID("1.3.6.1.2.1.2.2.1.8"); // Check-up/down status

    private Snmp snmp;

    public SNMPMonitor() throws Exception {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    private void getDeviceInfo() throws Exception {
        CommunityTarget target = createTarget();
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(HOSTNAME_OID));
        pdu.add(new VariableBinding(UPTIME_OID));
        pdu.setType(PDU.GET);

        ResponseEvent response = snmp.send(pdu, target);
        if (response.getResponse() != null) {
            for (VariableBinding vb : response.getResponse().getVariableBindings()) {
                System.out.println(vb.getOid() + " : " + vb.getVariable());
            }
        } else {
            System.out.println("Error: No response received.");
        }
    }

    private void monitorInterfaces() throws Exception {
        CommunityTarget target = createTarget();
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(INTERFACE_STATUS_OID));
        pdu.setType(PDU.GET);

        ResponseEvent response = snmp.send(pdu, target);
        if (response.getResponse() != null) {
            for (VariableBinding vb : response.getResponse().getVariableBindings()) {
                int status = vb.getVariable().toInt();
                if (status == 2) { // 2 means down
                    System.out.println("Alert: Interface " + vb.getOid() + " is down.");
                }
            }
        } else {
            System.out.println("Error: No response received.");
        }
    }

    private CommunityTarget createTarget() {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(COMMUNITY));
        target.setAddress(GenericAddress.parse(SNMP_ADDRESS));
        target.setRetries(2);
        target.setTimeout(1000);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    public static void main(String[] args) throws Exception {
        SNMPMonitor monitor = new SNMPMonitor();
        monitor.getDeviceInfo();
        monitor.monitorInterfaces();
    }
}
