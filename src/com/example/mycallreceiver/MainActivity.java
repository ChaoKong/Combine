package com.example.mycallreceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener,LocationListener {
	
	
	
	private static final String TAG = "MainActivity";
	int Start_truth = -10;
	int End_truth = -10;
    NotificationManager manager;
    Notification myNotication;
	public static int raw_counter = 0;
	

	File root = null;
	File dir = null;
	JSONObject Ground_truth = null;
	JSONObject tmp_truth = null;
	JSONObject tmp_call = null;
	JSONObject Location = null;
	JSONObject Result1 = null;
	JSONObject Result2 = null;
	
	
	public String GroundTruthFile_str = "GroundTruthFile.txt";
	int callType = 0;
	
//	GoogleApiClient mGoogleApiClient;
//	LocationRequest mLocationRequest;
//	Location mLocation;
//	Location mLastLocation;
//	int LocationFlag = 0;
//	int StartTransmit = 0;
	
	
	TextView textCallEnd_reading;
	TextView textCallStart_reading;
	TextView textLocationStart_reading;
	TextView textLocationEnd_reading;
	
	
	TextView textAudioResult_reading;
	TextView textLightResult_reading;
	TextView textAllResult_reading;
	TextView textLocationSingle_reading;
	
	File ResultFile = null;
	FileOutputStream foutResult = null;
	OutputStreamWriter outwriterResult = null;
	
	public int calculate_mode1 = 0;
	
	public int calculate_mode2 = 0;
	
	public String location1="";
	public String location2="";
	
	String result1_Str = "call start result";
	int result1 = 0;
	double result1_con = 0.0;
	String result2_Str = "call end result";
	int result2 = 0;
	double result2_con = 0.0;

	public Thread waitForSending = null;
	Context context = null;
	
	File GroundTruthFile = null;
	FileOutputStream foutGroundtruth = null;
	OutputStreamWriter outwriterGroundtruth = null;
	public String Groundtruthfile_str= "";
	
	GoogleApiClient mGoogleApiClient;
	LocationRequest mLocationRequest;
	Location mLocation;
	Location mLastLocation;
	int LocationFlag = 0;
	int StartTransmit = 0;
	String location_global ="";
	public Thread LocationThread = null;
	public int LocationThreadstart = 0;
    public long start_location_time = 0;
    public long stop_location_time = 0;
    
    
	private boolean isRecording = false;
	int sampleRate = 48000;
    private Thread recordingThread = null;
    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format
    
    
	AudioManager	am	= null;
	AudioRecord record =null;
	MediaPlayer mediaPlayer = null;
	
	SensorManager mySensorManager = null;
	Sensor LightSensor = null;
	Sensor ProxiSensor = null;
	
	
	File SoundFile = null;
	FileOutputStream foutSound = null;
	OutputStreamWriter outwriterSound = null;
	
	
	File SoundDataFile = null;
	FileOutputStream foutSoundData = null;
	OutputStreamWriter outwriterSoundData = null;
	
	String device_ID;
	
	File ProxiFile = null;
	FileOutputStream foutProxi = null;
	OutputStreamWriter outwriterProxi = null;
	
	File AllInfoFile = null;
	FileOutputStream foutAllInfo = null;
	OutputStreamWriter outwriterAllInfo = null;
	
	
	WifiManager wifiManager;
	WifiInfo wifiInfo = null; 
	
	TelephonyManager telephonyManager = null;
	
	BufferedReader bufferedReader = null;
	
	File InputRGBFile = null;
	
	
    private ArrayList<Double> lightValue = null;
    private ArrayList<Double> RValue = null;
    private ArrayList<Double> GValue = null;
    private ArrayList<Double> BValue = null;
    private ArrayList<Double> WValue = null;
    private ArrayList<Integer> TValue = null;
    private ArrayList<Integer> WifiValue = null;
    
    
    private float ReadProxi = 0;
    private int RecordFlag = 0;

    
    
    public int Audio_flag = 0;

  
    public int proxi_count = 0;
    public long proxi_time = 0;
    public long cur_proxi_time = 0;
    public int proxi_start = 0;
    public long stop_proxi_time = 0;
    public long stop_proxi_time_end = 0;
    public long stop_proxi_time_end2 = 0;
    public Thread proxiThread = null;
    public Thread proxiThread2 = null;
    public int register_light = 0;
    public int proxi_thread_start = 0;
    public int proxi_thread_start2 = 0;
    
    public int CallType = 0;
    
    public int RGBAvailabe = 0;
    
    public int isS6 = 0;
    public int Previous_volume = 10;
    
    StringBuilder finalString = null;
    
    public static JSONObject TestObject = null;
    
    public int TestType =0;
    public int Result_TestType = 0;
    public int runAudioTest=0;
    //   1 : light test;  2: Audio test; 3: All test;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    	
		
		setContentView(R.layout.activity_main);
		textCallStart_reading = (TextView) findViewById(R.id.CallStart_reading);
		textCallEnd_reading = (TextView) findViewById(R.id.CallEnd_reading);
		textLocationStart_reading = (TextView) findViewById(R.id.LocationStart_reading);
		textLocationEnd_reading = (TextView) findViewById(R.id.LocationEnd_reading);
		
		
		textAudioResult_reading = (TextView) findViewById(R.id.Audio_result);
		textLightResult_reading = (TextView) findViewById(R.id.Light_result);
		textAllResult_reading = (TextView) findViewById(R.id.All_result);
		textLocationSingle_reading = (TextView) findViewById(R.id.LocationSingle_reading);		
		
		
		context = this.context;
		
		root = android.os.Environment.getExternalStorageDirectory();
		dir = new File(root.getAbsolutePath() + "/CallDetection");
		dir.mkdirs();
		Groundtruthfile_str = dir+"/GroundTruthFile.txt";
		
    	// Create an instance of GoogleAPIClient.
    	if (mGoogleApiClient == null) {
    	    mGoogleApiClient = new GoogleApiClient.Builder(this)
    	        .addConnectionCallbacks((ConnectionCallbacks) this)
    	        .addOnConnectionFailedListener((OnConnectionFailedListener) this)
    	        .addApi(LocationServices.API)
    	        .build();
    	}
    	mGoogleApiClient.connect();

		final int[] mFiles = new int[] { R.raw.light_model6, R.raw.light_range_set6, R.raw.audio_model2, R.raw.audio_range2, R.raw.chirp14_file };
		final CharSequence[] filenames = { "model_light", "range_light", "model_audio", "range_audio", "chirp_file" };
		for (int i = 0; i < mFiles.length; i++) {
			try {
				if (dir.mkdirs() || dir.isDirectory()) {
					String str_song_name = dir + "/" + filenames[i];
					CopyRAWtoSDCard(mFiles[i], str_song_name);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Intent service_intent = getIntent();
		Log.d("service intent in on create", service_intent.toString());
	
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter("ActiveTestResult"));
		context = this.context;
		Intent service_intent = getIntent();
		Log.d("service intent in onresume", service_intent.toString());
		Bundle b = service_intent.getExtras();
		if (b!=null){
			
			Result_TestType = 0;
			String tmp_groundth =(String) b.get("ground_truth"); 
			if (tmp_groundth!=null)
			{
				Log.d("received ground truth in resume", tmp_groundth);
				try {
					Ground_truth = new JSONObject(tmp_groundth);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("get ground truth from service intent", Ground_truth.toString());
				
				mGoogleApiClient.connect();
				LocationFlag = 0;
				
				LocationThread = null;
				getLocation();
				
				final CharSequence[] items = { "Indoor", "Outdoor", "Unknown" };

				// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				// WindowManager.LayoutParams.FLAG_FULLSCREEN);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("At the END of the call");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							End_truth = -1;
							break;
						case 1:
							End_truth = 1;
							break;
						case 2:
							End_truth = 0;
							break;
						default:
							End_truth = -10;
						}
						try {
							Log.d("add end ", Ground_truth.toString());
							Ground_truth.put("End_ground_truth", End_truth);
							Log.d("add end finish", Ground_truth.toString());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						waitSending();
						Log.d(TAG, Integer.toString(End_truth));
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
				builder2.setTitle("At the START of the call ");
				builder2.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item2) {
						switch (item2) {
						case 0:
							Start_truth = -1;
							break;
						case 1:
							Start_truth = 1;
							break;
						case 2:
							Start_truth = 0;
							break;
						default:
							Start_truth = -10;
						}
						try {
							Log.d("add start", Ground_truth.toString());
							Ground_truth.put("Start_ground_truth", Start_truth);
							Log.d("add start finish", Ground_truth.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// Do something with the selection
						Log.d(TAG, Integer.toString(Start_truth));
					}
				});
				AlertDialog alert2 = builder2.create();
				alert2.show();

				try {
					location1 = Ground_truth.getString("Location1");
					textLocationStart_reading.setText("Location(call start): " + location1);	

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Result1 = Ground_truth.getJSONObject("result");
					Result2 = Ground_truth.getJSONObject("result2");
					result1_Str = Result1.getString("Result");
					result2_Str = Result2.getString("Result");
					calculate_mode1 = Result1.getInt("mode");
					calculate_mode2 = Result2.getInt("mode");
					printResults(result1_Str, 1, calculate_mode1,0);
					printResults(result2_Str, 2, calculate_mode2,0);
					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}

		}
		
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    unregisterReceiver(broadcastReceiver);
	    Log.d("status", "onDestroy");
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    Log.d("status", "onPause");
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    Log.d("status", "onstart");
		Intent service_intent = getIntent();
		Log.d("service intent in onstart", service_intent.toString());	
	}
	
	@Override
	public void onRestart() {
	    super.onRestart();
	    Log.d("status", "onRestart");
		Intent service_intent = getIntent();
		Log.d("service intent in onRestart", service_intent.toString());
	    //registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
	}
	
	@Override
	public void onStop() {
	    super.onStop();
	    Log.d("status", "onStop");
	    this.finish();
	    //registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
	}
	
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
 
            String message = b.getString("message");
            Log.d("message in broadcastReceiver", message);
			if (message!=null)
			{
				
				try {
					Ground_truth = new JSONObject(message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("get message from service intent", Ground_truth.toString());
				
				mGoogleApiClient.connect();
				LocationFlag = 0;
				
				LocationThread = null;
				getLocation();
				
				final CharSequence[] items = { "Indoor", "Outdoor", "Unknown" };



				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Active Test");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							End_truth = -1;
							break;
						case 1:
							End_truth = 1;
							break;
						case 2:
							End_truth = 0;
							break;
						default:
							End_truth = -10;
						}
						try {
							Log.d("add end ", Ground_truth.toString());
							
							Ground_truth.put("End_ground_truth", End_truth);
							Log.d("add end finish", Ground_truth.toString());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Log.d("start to send information","start to sending information");
						Log.d("add end groud truth", String.valueOf(End_truth));
						waitSending();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				
				
				try {
					JSONObject tmp_Result2 = Ground_truth.getJSONObject("result2");	
					Result_TestType = tmp_Result2.getInt("testType");
					if (Result_TestType==3)
					{
						Ground_truth.put("Start_ground_truth", End_truth);
					}
					Log.d("testType in main activity", String.valueOf(Result_TestType));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					
					if (Result_TestType==3)
					{
						Result1 = Ground_truth.getJSONObject("result");
						
						Result2 = Ground_truth.getJSONObject("result2");
						result1_Str = Result1.getString("Result");
						result2_Str = Result2.getString("Result");
						calculate_mode1 = Result1.getInt("mode");
						calculate_mode2 = Result2.getInt("mode");
						printResults(result1_Str, 1, calculate_mode1,1);
						printResults(result2_Str, 2, calculate_mode2,2);
						printResults(result2_Str, 2, calculate_mode2,3);}
					else
					{
						Result2 = Ground_truth.getJSONObject("result2");
				
						result2_Str = Result2.getString("Result");
		
						calculate_mode2 = Result2.getInt("mode");
						printResults(result2_Str, 2, calculate_mode2,Result_TestType);}
					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}

 
        };
	};
	
    
    public void printResults(String result_str,int CallType, int calculate_mode, int testType){
    	
    	String tmp_result_str = result_str;
    	if (testType==0){
			if (CallType == 1) {

				String[] splitStr_result1 = tmp_result_str.split("\\s+");
				result1 = Integer.parseInt(splitStr_result1[0]);
				result1_con = Double.parseDouble(splitStr_result1[1]);
				if (result1 == -1) {
					result1_Str = "Result: " + "indoor   " + "     confidence:  " + splitStr_result1[1];
				} else if (result1 == 1) {
					if ((result1_con < 0.9) && (calculate_mode == 1)) {
						result1_Str = "Result: " + "Unknown   ";

					} else {
						result1_Str = "Result:	" + "outdoor   " + "     confidence:  " + splitStr_result1[1];
					}
				} else {
					result1_Str = "Result: " + "Unknown   ";
				}
				textCallStart_reading.setText("Call Start:     " + result1_Str);
			}

			if (CallType == 2) {

				String[] splitStr_result2 = tmp_result_str.split("\\s+");
				result2 = Integer.parseInt(splitStr_result2[0]);
				result2_con = Double.parseDouble(splitStr_result2[1]);
				if (result2 == -1) {
					result2_Str = "Result: " + "indoor   " + "     confidence:  " + splitStr_result2[1];
				} else if (result2 == 1) {
					if ((result2_con < 0.9) && (calculate_mode == 1)) {
						result2_Str = "Result:	" + "Unknown   ";
					} else {
						result2_Str = "Result:	" + "outdoor   " + "     confidence:  " + splitStr_result2[1];
					}
				} else {
					result2_Str = "Result:	" + "Unknown   ";
				}
				textCallEnd_reading.setText("Call end  :     " + result2_Str);
			}
    	}
    	else
    	{
			if (testType == 3) {				
				int tmp_result=0;
				double tmp_confidence = 0;
				String tmp_result_Str="" ;
				if (result1==result2)
				{
					tmp_result = result1;
					tmp_confidence = 1- (1-result1_con) * (1-result2_con);
				}
				else{
					if (result1_con>result2_con)
					{
						tmp_result = result1;
						tmp_confidence = result1_con * (1-result2_con);
					}
					else
					{
						tmp_result = result2;
						tmp_confidence = result2_con * (1-result1_con);
						
					}
				}
				
				if (tmp_result == -1) {
					tmp_result_Str = "Result: " + "indoor   " + "     confidence:  " + tmp_confidence;
				} else if (tmp_result == 1) {
					 
					tmp_result_Str = "Result:	" + "outdoor   " + "     confidence:  " + tmp_confidence;
					
				} 
				textAllResult_reading.setText("All result:     " + tmp_result_Str);
				
			}
				
			
			if (testType==1)
			{
				String[] splitStr_result1 = tmp_result_str.split("\\s+");
				result1 = Integer.parseInt(splitStr_result1[0]);
				result1_con = Double.parseDouble(splitStr_result1[1]);
				if (result1 == -1) {
					result1_Str = "Result: " + "indoor   " + "     confidence:  " + splitStr_result1[1];
				} else if (result1 == 1) {
					if ((result1_con < 0.9) && (calculate_mode == 1)) {
						result1_Str = "Result: " + "Unknown   ";

					} else {
						result1_Str = "Result:	" + "outdoor   " + "     confidence:  " + splitStr_result1[1];
					}
				} else {
					result1_Str = "Result: " + "Unknown   ";
				}
				textLightResult_reading.setText("Light result:     " + result1_Str);
			}
			
			if (testType==2)
			{
				String[] splitStr_result2 = tmp_result_str.split("\\s+");
				result2 = Integer.parseInt(splitStr_result2[0]);
				result2_con = Double.parseDouble(splitStr_result2[1]);
				if (result2 == -1) {
					result2_Str = "Result: " + "indoor   " + "     confidence:  " + splitStr_result2[1];
				} else if (result2 == 1) {
					if ((result2_con < 0.9) && (calculate_mode == 1)) {
						result2_Str = "Result:	" + "Unknown   ";
					} else {
						result2_Str = "Result:	" + "outdoor   " + "     confidence:  " + splitStr_result2[1];
					}
				} else {
					result2_Str = "Result:	" + "Unknown   ";
				}
				textAudioResult_reading.setText("Audio result  :     " + result2_Str);
				
			}
    		
    	}
    }
    

	public void SendInformation() {

		Thread t = new Thread() {

			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the
									// child Thread
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {
					HttpPost post = new HttpPost("http://psychic-rush-755.appspot.com/upload");
					StringEntity se = new StringEntity(Ground_truth.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					post.setEntity(se);
					response = client.execute(post);

					/* Checking response */
					if (response != null) {
						InputStream in = response.getEntity().getContent(); // Get
																			// the
																			// data
																			// in
																			// the
																			// entity
						String s = getStringFromInputStream(in);
						
						Log.d("Jack-Response", s);
					}
					Ground_truth = null;
					Ground_truth = new JSONObject();
					raw_counter = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		t.start();	
		Log.d("send start", Ground_truth.toString());
		if (waitForSending!= null)
		{
			if (!waitForSending.isInterrupted());
			{
				waitForSending.interrupt();
				waitForSending = null;
				Log.d("stop waitForSending", "stop waitForSending in SendInformation");
				
			}
		}
		
	}
	
	
	private void CopyRAWtoSDCard(int id, String path) throws IOException {
	    InputStream in = getResources().openRawResource(id);
	    FileOutputStream out = new FileOutputStream(path);
	    byte[] buff = new byte[1024];
	    int read = 0;
	    try {
	        while ((read = in.read(buff)) > 0) {
	            out.write(buff, 0, read);
	        }
	    } finally {
	        in.close();
	        out.close();
	    }
	}
	
	public void waitSending() {

		waitForSending = new Thread() {

			public void run() {
				while(!Thread.interrupted())
			    {
					if (Ground_truth != null) {
						Log.d("ground truth in wait sending",Ground_truth.toString() );
						if ( Ground_truth.has("Location2") && Ground_truth.has("End_ground_truth")) {
							Log.d("satify requirements", "triger the SendInformation ");
							SendInformation();
							break;
						}
					} else {
						break;
					}
				
			    }
			}
		};
		waitForSending.start();
	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
	

	@Override
	public void onConnected(Bundle connectionHint) {
		// Provides a simple way of getting a device's location and is well
		// suited for
		// applications that do not require a fine-grained location and that do
		// not need location
		// updates. Gets the best and most recent location currently available,
		// which may be null
		// in rare cases when a location is not available.
		mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
	
	}
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes
		// might be returned in
		// onConnectionFailed.
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// The connection to Google Play services was lost for some reason. We
		// call connect() to
		// attempt to re-establish the connection.
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (LocationFlag == 0){
			location_global = Double.toString(location.getLatitude()) + " "+Double.toString(location.getLongitude());	
			LocationFlag = 1;	
			Log.d("locationChanged", location_global);
		}

	}
	
	public void getLocation() {


		start_location_time = System.currentTimeMillis();
		stop_location_time = start_location_time + 3000;
	    Log.d("start_location_time start", String.valueOf(start_location_time));
		Log.d("stop_location_time start", String.valueOf(stop_location_time));

		if (LocationThreadstart == 0) {
			LocationThreadstart = 1;
			Log.d("start Locationthread", "start Location thread");
			LocationThread = new Thread() {

				public void run() {
					while (!Thread.interrupted()) {

						while (true) {

							if (start_location_time > stop_location_time) {
								Log.d("start_location_time", String.valueOf(start_location_time));
								Log.d("stop_location_time", String.valueOf(stop_location_time));
								location2 = "fail";
								break;
							}

							if ((mLastLocation != null) || (location_global.length() > 2)) {
								Log.d("mLastLocation", mLastLocation.toString());
								Log.d("location_global", location_global);
								if (mLastLocation != null) {
									location2 = Double.toString(mLastLocation.getLatitude()) + " "
											+ Double.toString(mLastLocation.getLongitude());

								} else {

									location2 = location_global;
								}
								break;
							}
							start_location_time = System.currentTimeMillis();
							//Log.d("get location in thread", "get location in thread");
						}
						

						putLocation();
						Thread.currentThread().interrupt();
						return;
						
					}
				}
			};
			LocationThread.start();
		}

	}

	private void putLocation() {
		
		Log.d("put location 2", location2);

		if (Ground_truth!=null)
		{
			try {
				Ground_truth.put("Location2",location2);
				LocationThreadstart = 0;
				textLocationEnd_reading.post(new Runnable() {
				    public void run() {
				    	if (Result_TestType==0) {
				    	textLocationEnd_reading.setText("Location(call  end):  " + location2);}
				    	else
				    	{
				    		textLocationSingle_reading.setText("Location:  " + location2);
				    	}
				    } 
				});		
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
    private void init(){
    		
    	Context context = this;
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = MediaPlayer.create(this, R.raw.chirp814);
		mediaPlayer.setVolume(0.6f, 0.6f);
		Log.d("mediaPlayer create",mediaPlayer.toString());
		InputRGBFile = new File("/sys/devices/virtual/sensors/light_sensor/raw_data");
		
		
		RGBAvailabe = 0;
		if (InputRGBFile.exists())
		{
			
			RGBAvailabe = 1;
			try {
				bufferedReader = new BufferedReader(new FileReader(InputRGBFile));
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				RGBAvailabe = 0;
				Log.d("try to read RGB, unavailable", "try to read RGB, unavailable");
				e2.printStackTrace();
				
			}
						
			Log.d("RGB available", String.valueOf(RGBAvailabe));
			
		}
		
		
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        
        mySensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        
        
    	telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
    	device_ID= telephonyManager.getDeviceId();
    	
    	
		SoundFile = new File(dir, "SoundRecord.txt");
    	if (!SoundFile.exists()){
    		try {
    			SoundFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
		SoundDataFile = new File(dir, "SoundData.txt");
    	if (!SoundDataFile.exists()){
    		try {
    			SoundDataFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


		ProxiSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		if (ProxiSensor != null) {
			// textProxi_available.setText("Sensor PROXIMITY Available");
			ProxiFile = new File(dir, "ProxiRecord.txt");
			if (!ProxiFile.exists()) {
				try {
					ProxiFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			// textProxi_available.setText("Sensor PROXIMITY NOT Available");
			// textTemper_reading.setText("Temperature(C): ");
		}
		
    	
    	
    	AllInfoFile = new File(dir,"AllInfoRecord.txt");
    	if (!AllInfoFile.exists()){
    		try {
    			AllInfoFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	

    	proxi_count = 0;
    	RecordFlag = 0;
    	proxiThread = null;
    	proxiThread2 = null;
    	register_light = 0;
    	runAudioTest = 0;
    	proxi_thread_start = 0;
    	
    	TestType = 0;
    	
    	String deviceModel = Build.MODEL;
    	String S6 = "g920t";
    	if (deviceModel.toLowerCase().contains(S6.toLowerCase())){
    		isS6 =1;
    	}
    	
    	TestObject = new JSONObject();
    	Log.i(TAG, "init successfully");
    }
    
    
	boolean isAudioTest = false;

	public void AudioSwitch(View view) throws IOException {
		Button AudioTestBtn=(Button) findViewById(R.id.Audio_switch);
		

		init();
		
		TestType = 2;
		
		registerProxiSensor();

		if (isS6!=1){
			AudioTestBtn.setText("Audio test not supported");
		}
	} 
	
	
	boolean isLightTest = false;

	public void LightSwitch(View view) throws IOException {
		Button LightTestBtn=(Button) findViewById(R.id.Light_switch);		
		
		init();
		
		TestType = 1;
		
		registerProxiSensor();

        
		if (isLightTest == true) {	

			isLightTest = false;
			LightTestBtn.setText("start light test off");
				
			
		}else {	
		
			isLightTest = true;
			LightTestBtn.setText("start light test on");
		}
	
	}
	
	
	private void runAudiotest()
	{
		if (isS6==1)
		{
			startRecording();
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startEmitting();
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			stopEmitting();
			stopRecording();
			Audio_flag = 1;
		}
		else
		{
			Audio_flag=0;
		}
		
	}
	
	
	boolean isAllTest = false;

	public void AllSwitch(View view) throws IOException {
		Button AllTestBtn=(Button) findViewById(R.id.All_switch);
		
		init();
		
		TestType = 3;
		
		registerProxiSensor();
		
		if (isAllTest == true) {	

			isAllTest = false;
			AllTestBtn.setText("Start all test off");
				
			
		}else {	
		
			isAllTest = true;
			AllTestBtn.setText("Start all test on");
		}
	
	} 
    
 
    
    private void registerLightSensor() {
    	
	    lightValue = new ArrayList<Double>();
	    RValue = new ArrayList<Double>();
	    GValue = new ArrayList<Double>();
	    BValue = new ArrayList<Double>();
	    WValue = new ArrayList<Double>();
	    TValue = new ArrayList<Integer>();
	    WifiValue = new ArrayList<Integer>();	
	    long curTime = System.currentTimeMillis();
	    Log.d("create array", "create array "+String.valueOf(curTime));
    	
		if(LightSensor != null){
			
			if(AllInfoFile.exists()){
				  
				  try{
		    		
					  foutAllInfo = new FileOutputStream(AllInfoFile,true);
					  outwriterAllInfo= new OutputStreamWriter(foutAllInfo);
				
				  } catch(Exception e)
				  {

				  }
			}
			mySensorManager.registerListener(LightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}

		if (proxiThread!= null)
		{
			if (!proxiThread.isInterrupted());
			{
				proxiThread.interrupt();
				proxiThread = null;
				Log.d("stop proxi thread", "stop proxi thread in register light");
				
			}
		}
		
    }
    
    
    
    private void unregisterLightSensor()
    {
    	if(LightSensor != null){
			mySensorManager.unregisterListener(LightSensorListener,LightSensor);
			
			try {
				outwriterAllInfo.flush();
				outwriterAllInfo.close();
				foutAllInfo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	}      	
	    lightValue.clear();
	    RValue.clear();
	    GValue.clear();
	    BValue.clear();
	    WValue.clear();
	    TValue.clear();
	    WifiValue.clear();
	   	
    }
    
    
    private void unregisterProxiSensor()
    {
    	
    	if(ProxiSensor != null){
			mySensorManager.unregisterListener(ProxiSensorListener,ProxiSensor);
			try {
				//outwriterProxi.flush();
				outwriterProxi.close();
				foutProxi.close();
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} 
	
    }
    
    
    
    private void registerProxiSensor() {
    	
		if ( ProxiSensor != null){
			
			if(ProxiFile.exists()){
			  
				try{
	    		
					foutProxi = new FileOutputStream(ProxiFile,true);
					outwriterProxi = new OutputStreamWriter(foutProxi);
				} catch(Exception e)
				{
				}
			}
			mySensorManager.registerListener(ProxiSensorListener, ProxiSensor, SensorManager.SENSOR_DELAY_FASTEST);
		}		
    }
        

    private void startEmitting(){
    	Previous_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
    	Log.d("previous volume", String.valueOf(Previous_volume));
    	am.setStreamVolume(AudioManager.STREAM_MUSIC,10,0);
    	Log.d("current volume", String.valueOf(10));
    	mediaPlayer.setVolume(0.6f, 0.6f);
    	mediaPlayer.start();	
    }
    
    
    private void stopEmitting() {
    	mediaPlayer.pause();
    	mediaPlayer.release();
    	mediaPlayer = null;
    }   
    
   
    
    private void startRecording() {

		int min = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		record = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, min);
        record.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

        //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        int num = 0;
        short sData[] = new short[BufferElements2Rec];

        
        try {
        	foutSound = new FileOutputStream(SoundFile,true);
        	outwriterSound = new OutputStreamWriter(foutSound);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
        	foutSoundData = new FileOutputStream(SoundDataFile);
        	outwriterSoundData = new OutputStreamWriter(foutSoundData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            num=record.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            if (num == BufferElements2Rec){
	            long curTime = System.currentTimeMillis();
	            String curTimeStr = ""+curTime+";   ";
	            Log.i(TAG,curTimeStr);
	            try {
	            	outwriterSound.append(curTimeStr);
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	        	String strI = ""+(sData.length);
	        	Log.i(TAG, strI);
	        	
	        	for ( int i=0; i<sData.length;i++){
	        		try {
	        			String tempS = Short.toString(sData[i])+"    ";
	        			outwriterSound.append(tempS);
	        			outwriterSoundData.append(tempS);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
        		try {
        			String tempS = "\n";
        			outwriterSound.append(tempS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
        	try {
        		outwriterSound.flush();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
        	try {
        		outwriterSoundData.flush();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
            
        }
       
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != record) {
        	
        	am.setStreamVolume(AudioManager.STREAM_MUSIC,Previous_volume,0);
            isRecording = false;
            record.stop();
            record.release();
            record = null;

            recordingThread = null;
        }
    }
        
    private final SensorEventListener LightSensorListener= new SensorEventListener(){

    	@Override
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    		// TODO Auto-generated method stub
    	}
    	@Override
    	public void onSensorChanged(SensorEvent event) {
    		if(event.sensor.getType() == Sensor.TYPE_LIGHT){
    			
   			
    			double lightvalue = 0.0;
    			int Wifi_RSSI=0;
    			long timeSta = System.currentTimeMillis();
    			String curTimeStr = ""+timeSta+";   ";
    			Log.d("light sensor change:", curTimeStr);
				
			    			
    			if (lightValue.size()==5){
   				
    				
    				Log.d("light value size is 5", "light value size is 5");
    				RecordFlag =0;
    				double Light_Sum = 0;
    				double R_Sum = 0;
    				double G_Sum = 0;
    				double B_Sum = 0;
    				double W_Sum = 0;
    				double Wifi_Sum = 0;
    				for ( int i = 0; i<5; i++){
    					Light_Sum = Light_Sum + lightValue.get(i);
    					R_Sum = R_Sum + RValue.get(i);
    					G_Sum = G_Sum + GValue.get(i);
    					B_Sum = B_Sum + BValue.get(i);
    					W_Sum = W_Sum + WValue.get(i);
    					Wifi_Sum = Wifi_Sum + (double)WifiValue.get(i);	
    					
    				}
    				Light_Sum = Light_Sum/5;
    				R_Sum = R_Sum/5;
    				G_Sum = G_Sum/5;
    				B_Sum = B_Sum/5;
    				W_Sum = W_Sum/5;
    				Wifi_Sum = Wifi_Sum/5;
    					
    				String Light_RGB_Wifi = String.valueOf(Light_Sum)+" "+String.valueOf(R_Sum)+" "+String.valueOf(G_Sum)+" "+String.valueOf(B_Sum)+" "+String.valueOf(W_Sum)+" "+String.valueOf(Wifi_Sum);
    				   
    				Log.d("get Ave info","get Ave info");
             
                    sendFinalJSON(TestType,Light_RGB_Wifi,Audio_flag);	
                    unregisterLightSensor();
                    Log.d("unregister light sensor", "unregister light");
    			 					
    			}

    			
	            
	            lightvalue = (double) (event.values[0]);
	            Log.d(TAG, String.valueOf(lightvalue));				
    			SupplicantState supState; 
    			
    			wifiInfo = wifiManager.getConnectionInfo();
    			
    			supState = wifiInfo.getSupplicantState();

	            
	            Log.d(TAG, String.valueOf(wifiInfo.getRssi()));
	            Wifi_RSSI = wifiInfo.getRssi();
	            finalString = new StringBuilder();
	            if (RGBAvailabe==1) {
					try {
						bufferedReader = new BufferedReader(new FileReader(InputRGBFile));
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if (bufferedReader != null) {
						String line;
						try {
							while ((line = bufferedReader.readLine()) != null) {

								finalString.append(line);
								String[] tmp_RGB = line.split(",");
								int tmp_I = 3 - Integer.parseInt(tmp_RGB[5]);
								double tmp_time = Math.pow(4, tmp_I);
								double tmp_R = (Double.parseDouble(tmp_RGB[0])) * tmp_time;
								double tmp_G = (Double.parseDouble(tmp_RGB[1])) * tmp_time;
								double tmp_B = (Double.parseDouble(tmp_RGB[2])) * tmp_time;
								double tmp_W = (Double.parseDouble(tmp_RGB[3])) * tmp_time;

								lightValue.add(lightvalue);
								WifiValue.add(Wifi_RSSI);
								RValue.add(tmp_R);
								GValue.add(tmp_G);
								BValue.add(tmp_B);
								WValue.add(tmp_W);
								Log.d(TAG, line);
							

							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						bufferedReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            else{
					lightValue.add(lightvalue);
					WifiValue.add(Wifi_RSSI);
					RValue.add(0.0);
					GValue.add(0.0);
					BValue.add(0.0);
					WValue.add(0.0);
					
	            }
	            
	            Log.d("light value size", String.valueOf(lightValue.size()));

				if (RecordFlag==1) {
					String Light_RGB_Wifi = String.valueOf(lightvalue) +" "+finalString.toString()+" "+String.valueOf(Wifi_RSSI);
					Log.d("Light RGB wifi", Light_RGB_Wifi);
					writeJSON(outwriterAllInfo,timeSta,"rawData",Light_RGB_Wifi);
					
					Log.d("call add","calladd");
					sendJSON(timeSta,"rawData",Light_RGB_Wifi);
					finalString = null;

				}
		
    		}
    	}
    };
    
    private final SensorEventListener ProxiSensorListener= new SensorEventListener(){

    	@Override
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    		// TODO Auto-generated method stub
    
    	}

    	@Override
    	public void onSensorChanged(SensorEvent event) {
    		if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
    			
    			proxi_time = System.currentTimeMillis();
    			cur_proxi_time = proxi_time;
    			stop_proxi_time = proxi_time + 1500;
    			stop_proxi_time_end = cur_proxi_time + 1000;
    			String curTimeStr = ""+proxi_time+";   ";
    			ReadProxi = event.values[0];
    			
    			Log.d("proxi count",String.valueOf(proxi_count));
    			Log.d("proxi value", String.valueOf(ReadProxi));
    			Log.d("proxi time",String.valueOf(proxi_time));
    			Log.d("cur proxi time",String.valueOf(cur_proxi_time));
    			Log.d("stop_proxi_time_end",String.valueOf(stop_proxi_time_end));
    			Log.d("stop_proxi_time",String.valueOf(stop_proxi_time));
    			Log.d("RecordFlag",String.valueOf(RecordFlag));
    			
    			
				if (proxiThread!= null)
				{
					if (!proxiThread.isInterrupted());
					{
						proxiThread.interrupt();
						proxiThread = null;
						Log.d("stop thread", "stop proxithread registed light sensor");     						
					}
				}
    			
    			if (ReadProxi<1)
    			{
    			
    			}
    			
        		if (ReadProxi>1)
        		{	
        		
        			
        				if (RecordFlag ==1){
            				if (proxiThread!= null)
            				{
            					if (!proxiThread.isInterrupted());
            					{
            						proxiThread.interrupt();
            						proxiThread = null;
            						Log.d("stop thread", "stop proxithread registed light sensor");     						
            					}
            				}
        					
        				}
						if (RecordFlag == 0) {
			    			if (proxi_thread_start ==0)
			    			{
			    				proxi_thread_start = 1;
			    				Log.d("start proxithread","first start proxithread");
			    				proxiThread = new Thread() {

			    					public void run() {
			    						while(!Thread.interrupted())
			    					    {
				    						while (RecordFlag==0 )
				    						{
				    							if ((cur_proxi_time > stop_proxi_time_end) && (ReadProxi>1))
				    							{
					    							Log.d("cur proxi time in EndingCallFlag 1", String.valueOf(cur_proxi_time));
					    							Log.d("stop_proxi_time end in EndingCallFlag 1", String.valueOf(stop_proxi_time_end));
					    							Log.d("readProxi",String.valueOf(ReadProxi));
				    								break;
				    							}
				    							cur_proxi_time = System.currentTimeMillis();
//				    							Log.d("cur proxi time in thread", String.valueOf(cur_proxi_time));
//				    							Log.d("stop_proxi_time in thread", String.valueOf(stop_proxi_time));
				    						
				    						}

				    						RecordFlag = 1;
				    						Log.d("RecordFlag in thread",String.valueOf(RecordFlag));
				    						unregisterProxiSensor();
				    						
				    						if (TestType == 1) {
				    							if (register_light == 0) {
				    								register_light = 1;
				    								Log.d("register light in test type 1",
				    										"register light in first thread");
				    								registerLightSensor();

				    							}
				    						}
				    						
				    						if ((TestType ==2) && (isS6==1)){
				    							if (runAudioTest==0){
				    								runAudioTest=1;
				    								runAudiotest();	
				    								Log.d("run audio test in type 2",
				    										"run audio test in type 2");
				    								String tmp_Light_RGB_Wifi = "0 0 0 0 0 0";
				    								sendFinalJSON(TestType,tmp_Light_RGB_Wifi,Audio_flag);
				    							}
				    						}
				    						
				    						if (TestType ==3){
				    							
				    							if (register_light == 0) {
				    								register_light = 1;
				    								Log.d("register light in test type 3",
				    										"register light in test type 3");
				    								registerLightSensor();
				    							}
				    							if (isS6==1)
				    							{
				    								if (runAudioTest==0){
				    									runAudioTest=1;
					    								runAudiotest();
					    								Log.d("run audio test in type 3",
					    										"run audio test in type 3");
				    								}

				    							}
				    							else
				    							{
				    								Audio_flag = 0;
				    							}
				    						}
				    						
				    						
				    						Thread.currentThread().interrupt();
				    						return;

			    					    }

			    					}
			    				};
			    				proxiThread.start();
			    			}							
						}
        			}
        			
        		proxi_count = proxi_count + 1;
	            try {
	            	outwriterProxi.append(curTimeStr);
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	            try {
					outwriterProxi.append(String.valueOf(event.values[0])+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            try {
					outwriterProxi.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
    		}
    	}
     
    }; 
    

    
    
	public void writeJSON(OutputStreamWriter myWriter, long timestamp, String tag, String info) {
		JSONObject object = new JSONObject();
		try {

			object.put("timestamp", timestamp);
			object.put(tag, info);
			String content = object.toString() + "\n";
			myWriter.append(content);
			Log.d("write json", content);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendJSON(long timestamp, String tag, String info){
		JSONObject tmp_object = new JSONObject();
		Log.d("write json", "write json start");
		try {
			tmp_object.put("timestamp", timestamp);
			tmp_object.put(tag, info);
			
			TestObject.put("raw" + String.valueOf(raw_counter), tmp_object);

			Log.d("send json", tmp_object.toString());
			raw_counter = raw_counter + 1;
			Log.d("raw_counter in sendjson", String.valueOf(raw_counter));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

	public void sendFinalJSON(int Type, String info, int useAudio) {
		try {
			Log.d("start to send inent", "start to send inent");

				Log.d("start to send call start", "start to send call start");
				TestObject.put("AveValue", info);
				TestObject.put("testType",Type);
				TestObject.put("Audioflag", Audio_flag);
				Log.d("RGBAvailable", String.valueOf(RGBAvailabe));
				TestObject.put("RGBAvailable", RGBAvailabe);
        		JSONObject tmp_object = new JSONObject();
        		try {
					tmp_object.put("deviceID", device_ID);
        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
        		TestObject.put("device_info", tmp_object);
        		
        		tmp_object= null;
        		
				Intent it = new Intent();
				it.setAction("active_test");
				String tmp_intent_mes = TestObject.toString();
				it.putExtra("activeTest", tmp_intent_mes);
				it.setClass(this, MyService.class);
				this.startService(it);
				Log.d("send active test", "active test");
      			Log.d("send active test", tmp_intent_mes);
    			raw_counter = 0;
            	TestObject = null;
            	TestObject = new JSONObject();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
