import com.tailf.jnc.*;

public class NETCONFConfigurator {

    private static final String HOST = "10.10.10.1";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    public static void configureVLAN() throws Exception {
        Device device = new Device("Router", HOST, USERNAME, PASSWORD);
        device.connect();

        try {
            // Create VLAN configuration
            String vlanConfig = "<config>" +
                                "<native xmlns=\"urn:ios\">" +
                                "<vlan><vlan-id>100</vlan-id><name>Test VLAN</name></vlan>" +
                                "<interface><Vlan><id>100</id>" +
                                "<ip><address><primary><address>10.10.10.1</address><mask>255.255.255.0</mask></primary></address></ip>" +
                                "</Vlan></interface>" +
                                "</native>" +
                                "</config>";
            device.getSession().editConfig(vlanConfig);

            // Retrieve and verify configuration
            String retrieveConfig = device.getSession().getConfig();
            System.out.println("VLAN Configuration: " + retrieveConfig);

            // Wait 5 minutes and then delete VLAN
            Thread.sleep(300000);
            String deleteVLAN = "<config>" +
                                "<native xmlns=\"urn:ios\">" +
                                "<vlan operation=\"delete\"><vlan-id>100</vlan-id></vlan>" +
                                "</native>" +
                                "</config>";
            device.getSession().editConfig(deleteVLAN);
            System.out.println("VLAN 100 deleted.");

        } finally {
            device.close();
        }
    }

    public static void main(String[] args) throws Exception {
        configureVLAN();
    }
}
