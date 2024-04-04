package com.example.ise_oc_projet;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Enzo LEVILLAIN
 * This class is used to acces bluetooth socket everywhere
 */
public class Singleton_BT_interface {

    private static BluetoothSocket mBt_socket;
    private static final String TAG = "Singleton_BT_interface";

    /**
     * push new BT socket to use for controlling robot
     * @param bt_connexion Bluetooth socket to use
     */
    public static void set(BluetoothSocket bt_connexion) {
        closeConnexion();
        mBt_socket = bt_connexion;
    }

    /**
     * check if the socket is connected and not null
     * @return true if not null and connected
     */
    public static boolean isConnected(){
        return !isNull() && mBt_socket.isConnected();
    }

    /**
     * check if socket isn't null
     * @return true is not null
     */
    public static boolean isNull(){
        return mBt_socket == null;
    }

    /**
     * function to call to close the socket
     *
     */
    public static void closeConnexion(){
        try{
            if(!isNull()) {
                mBt_socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "socket cannot be closed correctly ! ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Send a given String emssage through the socket.
     * @param command command to send
     * @return value taken:
     *  StateSocket.OK command has been sent successfully
     *  StateSocket.NULL socket is null
     *  StateSocket.DISCONNECTED socket is not null but socket is closed
     *  StateSocket.SENDING_ERROR socket available but error occur on sending
     */
    public static StateSocket send(String command) {

        StateSocket toRtn;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        byte[] bytes = command.getBytes();

        Log.d(TAG, "Sending command:"+command+" to socket");


        if(mBt_socket == null){
            Log.e(TAG, "socket is null, message cannot be send");
            return StateSocket.NULL;
        }else if(!isConnected()){
            Log.e(TAG, "socket is closed, message cannot be send");
            return StateSocket.DISCONNECTED;
        }

        try { tmpOut = mBt_socket.getOutputStream(); }
        catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
            return StateSocket.SENDING_ERROR;
        }

        try {
            assert tmpOut != null;
            tmpOut.write(bytes);
            toRtn =  StateSocket.OK;
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
            toRtn =  StateSocket.SENDING_ERROR;
        }

        return toRtn;
    }


}
