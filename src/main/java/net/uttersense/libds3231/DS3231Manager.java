package net.uttersense.libds3231;

import java.util.ArrayList;
import java.util.Iterator;

public class DS3231Manager {

    private ArrayList clientListeners;
    private static DS3231Manager instance = null; //Static ensures we only create one instance of this class
    private String ds3231_text = null;
    static String DEBUG_LOG_TAG = "LOG";


    //Load the native library:
    static {
        System.loadLibrary("ds3231-jni");
    }
    /*
     * Declare the JNI native methods:
     */
    public native String stringFromJNI();
    public native int initialise();
    public native void close();
    public native void startDataFeed(int interval);
    public native void stopDataFeed();
    public native void setTimerInt(int interval);

    //Private Constructor - can not be called externally
    private DS3231Manager()
    {

        //Set up array for client listeners:
        clientListeners = new ArrayList();


        System.out.println("KeypadManager: ...In Constructor");

    }


    public static DS3231Manager getInstance()
    {
        synchronized (DS3231Manager.class) {
            if (instance == null) {
                instance = new DS3231Manager();

            }
        }
        System.out.println("DS3231Manager: getInstance() called");
        //Start off timer task
        return instance;
    }

    public void registerListener(IDS3231Listener client)
    {
        clientListeners.add(client);
        System.out.println("Client listener (DS3231) has been registered...");
    }

    public void unregisterListener(IDS3231Listener client)
    {
        clientListeners.remove(client);
        System.out.println("Client (DS3231) has been un-registered...");

    }

    //Callback function from native library:
    public void updateStrData(String str)
    {
        ds3231_text = str;
        notifyClients(str);
    }

    private void notifyClients(String str)
    {
        for (Iterator it = clientListeners.iterator(); it.hasNext();   ) {
            ((IDS3231Listener) (it.next())).onDS3231Event(str);
            System.out.println("KeypadManager: Callback has been triggered...");
        }
    }


}

