package testpracticsample.eim.systems.cs.pub.ro.testpracticsample.Network;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import testpracticsample.eim.systems.cs.pub.ro.testpracticsample.Utilities.Constants;
import testpracticsample.eim.systems.cs.pub.ro.testpracticsample.Utilities.Utilities;
//import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.Utilities.Utilities;

public class ClientThread extends Thread {
    private String address;
    private int port;
    private String city;
    private TextView weatherForecastTextView;

    private Socket socket;

    public ClientThread(int port, String city, TextView weatherForecastTextView) {
        this.port = port;
        this.city = city;
        this.weatherForecastTextView = weatherForecastTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(city);
            printWriter.flush();
            String weatherInformation;
            final StringBuilder stringBuilder = new StringBuilder();
            while ((weatherInformation = bufferedReader.readLine()) != null) {
                stringBuilder.append(weatherInformation);

                final String finalizedWeateherInformation = weatherInformation;
                weatherForecastTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        weatherForecastTextView.setText(finalizedWeateherInformation);
                    }
                });
            }
            weatherForecastTextView.post(new Runnable() {
                @Override
                public void run() {
                    weatherForecastTextView.setText(stringBuilder.toString());
                }
            });

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
