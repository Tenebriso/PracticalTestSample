package testpracticsample.eim.systems.cs.pub.ro.testpracticsample;
import testpracticsample.eim.systems.cs.pub.ro.testpracticsample.Network.*;
import testpracticsample.eim.systems.cs.pub.ro.testpracticsample.Utilities.Constants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // server
    private EditText serverPortEdited;
    private Button connectButton = null;

    // client
    private Button goButton;
    private EditText clientPortEdited;
    private TextView information;
    private EditText query;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEdited.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }


    private GetInfoButtonClickListener getInfoButtonClickListener = new GetInfoButtonClickListener();
    private class GetInfoButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientPort = clientPortEdited.getText().toString();
            /*
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            */
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String city = query.getText().toString();
            if (city == null || city.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            information.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(Integer.parseInt(clientPort), city, information);
            clientThread.start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPortEdited = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);


        information = (TextView) findViewById(R.id.weather_forecast_text_view);
        query = (EditText) findViewById(R.id.city_edit_text);
        clientPortEdited = (EditText)findViewById(R.id.client_port_edit_text);
        goButton = (Button) findViewById(R.id.get_weather_forecast_button);
        goButton.setOnClickListener(getInfoButtonClickListener);
    }


    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
