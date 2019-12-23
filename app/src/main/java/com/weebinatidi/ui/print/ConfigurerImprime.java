package com.weebinatidi.ui.print;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.ui.InvoiceDetailsActivity;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.util.Set;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

import static com.weebinatidi.ui.InvoiceDetailsActivity.EXTRA_INVOICE_ID;
import static com.weebinatidi.ui.InvoiceDetailsActivity.EXTRA_ISARCHIVE;
import static com.weebinatidi.ui.InvoiceDetailsActivity.EXTRA_IS_BILL_OR_DEPOSIT;

/**
 * Created by mbp on 28/07/2017.
 */


public class ConfigurerImprime extends Activity {
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //��ȡ�豸��Ϣ
    public static BluetoothService mService = null;
    public static String textToBePrinted;
    private int conn_flag = 0;
    Button btnSearch;
    Button btnSendDraw;
    Button btnSend;
    Button btnClose;
    String whatotdo;
    LinearLayout PrintLin;
    TextView PrintMsg;
    /**
     * ����һ��Handlerʵ�����ڽ���BluetoothService�෵�ػ�������Ϣ
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            Toast.makeText(getApplicationContext(), R.string.blueconnectionsuccess,
                                    Toast.LENGTH_SHORT).show();
                            /**Reptx Addons**/
                            PrintMsg.setVisibility(View.VISIBLE);
                            PrintLin.setVisibility(View.GONE);

                            /**Reptx Addons**/
//							btnClose.setEnabled(true);
//							btnSend.setEnabled(true);
//							btnSendDraw.setEnabled(true);

                            /**Reptx Addons **/
                            if (!TextUtils.isEmpty(whatotdo)) {
                                //PrintMsg.setText(getString(R.string.printingprocessing));
                                GoPrintit();
                                //finish();//ajout pisco //cette ligne permet de
                                // retourner à la page ClientPageActivity après impression
//										  PrintMsg.setText("Printing Processing");
                            }
                            /** Reptx Addons  **/
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            Log.d("��������", "��������.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                        case BluetoothService.STATE_NONE:
                            Log.d("��������", "�ȴ�����.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                    /*Toast.makeText(getApplicationContext(), R.string.bluetoothconnectionwaslost,
							Toast.LENGTH_SHORT).show();*/
                    /** Reptx Addons **/
                    PrintMsg.setVisibility(View.VISIBLE);
                    //PrintMsg.setText(getString(R.string.bluetoothconnectionwaslost));
                    PrintLin.setVisibility(View.GONE);
                    /** Reptx Addons **/
                    btnClose.setEnabled(false);
                    btnSend.setEnabled(false);
                    btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    Toast.makeText(getApplicationContext(), "connection perdue\n Veuillez configurer l'imprimante depuis le menu paramètres",
                            Toast.LENGTH_SHORT).show();
                    //PrintMsg.setVisibility(View.VISIBLE);
                    //PrintMsg.setText(getString(R.string.bluetoothconnectionwaslost));
                    //PrintLin.setVisibility(View.GONE);
                    finish();
                    break;
            }
        }

    };
    BluetoothDevice con_dev = null;
    int invoiceId = -1;
    String clientphone;
    String clientarchived;
    boolean isArchive;
    boolean isbill;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        /** Reptx Addons**/

        isbill = getIntent().getExtras().getBoolean(EXTRA_IS_BILL_OR_DEPOSIT);
        if (isbill == true) {
            invoiceId = getIntent().getExtras().getInt(EXTRA_INVOICE_ID);
            isArchive = getIntent().getExtras().getBoolean(EXTRA_ISARCHIVE);

        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PrintLin = (LinearLayout) this.findViewById(R.id.printdemoall);
        PrintMsg = (TextView) this.findViewById(R.id.printdemomsgtv);
        PrintLin.setVisibility(View.GONE);
        PrintMsg.setVisibility(View.GONE);

//		PrintMsg.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(isbill == true)
//				{
//					Intent bill =new Intent(PrintDemo.this,InvoiceDetailsActivity.class);
//					bill.putExtra(EXTRA_INVOICE_ID,invoiceId);
//					bill.putExtra(EXTRA_ISARCHIVE,isArchive);
//					startActivity(bill);
//				}
//				else
//				{
//					Intent intent = new Intent(PrintDemo.this, ClientPageActivity.class);
//					intent.putExtra(ClientPageActivity.EXTRA_CLIENT_PHONE, clientphone);
//					intent.putExtra(ClientPageActivity.EXTRA_CLIENT_ARCHIVE, "nonarchived");
//					startActivity(intent);
//				}
//			}
//		});
        /** Reptx Addons**/

        mService = new BluetoothService(this, mHandler);
        //�����������˳�����
        if (mService.isAvailable() == false) {
            /** Reptx Addons**/
            PrintLin.setVisibility(View.GONE);
            PrintMsg.setVisibility(View.VISIBLE);
            PrintMsg.setText(R.string.nobluetoothpaired);
            /** Reptx Addons**/

            Toast.makeText(this, R.string.nobluetoothpaired, Toast.LENGTH_LONG).show();
            finish();
        }


        /** Reptx Addons**/
        /**
         *
         *
         PrintLin=(LinearLayout)this.findViewById(R.id.printdemoall);
         PrintLin.setVisibility(View.GONE);

         PrintMsg =(TextView) this.findViewById(R.id.printdemomsgtv);
         PrintMsg.setVisibility(View.GONE);
         */


    }

    public  void GoPrintit() {
        if(isbill==true){
            //Toast.makeText(getApplicationContext(),"bon "+isbill,Toast.LENGTH_SHORT).show();
            mService.sendMessage(textToBePrinted + "\n", "GBK");
            Intent bill =new Intent(ConfigurerImprime.this,InvoiceDetailsActivity.class);

            bill.putExtra(EXTRA_INVOICE_ID, invoiceId);
            bill.putExtra(EXTRA_ISARCHIVE, isArchive);
            startActivity(bill);
            finish();
        }
        else{
            //Toast.makeText(getApplicationContext(),""+isbill,Toast.LENGTH_SHORT).show();
            mService.sendMessage(textToBePrinted + "\n", "GBK");
            finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //����δ�򿪣�������
        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //in case we pass what is on top side it means bluetooth is enable
        // and the process to connect should be on...
        try {


            btnSendDraw = (Button) this.findViewById(R.id.btn_test);
            btnSendDraw.setOnClickListener(new ClickEvent());
            btnSearch = (Button) this.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(new ClickEvent());
            btnSend = (Button) this.findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new ClickEvent());
            btnClose = (Button) this.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new ClickEvent());
            btnClose.setEnabled(false);
            btnSend.setEnabled(false);
            btnSendDraw.setEnabled(false);
        } catch (Exception ex) {
            Log.e("������Ϣ", ex.getMessage());
        }

        /** Reptx Addons**/
        //we can start the connection or otherwise the search right from here maybe
        GoConnectthePrint();

        Intent getthem = getIntent();
        whatotdo = getthem.getStringExtra(InvoiceDetailsActivity.EXTRA_PRINTINVOICE);

        /**    Addons Reptx**/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    /**
     * Reptx Addons
     **/
    //lets connect the default device we got and lets move on
    public void GoConnectthePrint() {
        /** Read in our database to see if e got a mac adress already **/

        String address = Config.ReadFromBase(ConfigurerImprime.this, Config.printer, 0);
        //if we got something we go...
        if (!TextUtils.isEmpty(address)) {
            // a normal mac address should have a length of 17 characters...
            if (address.length() == 17) {
                con_dev = mService.getDevByMac(address);
                mService.connect(con_dev);
            }
        }
        //as there no device saved  please lets start the search first and get one before moving on ....
        else {
            Intent serverIntent = new Intent(ConfigurerImprime.this, DeviceListActivity.class);      //��������һ����Ļ
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }

        /**  --------- **/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:      //���������
                if (resultCode == Activity.RESULT_OK) {   //�����Ѿ���
                    Toast.makeText(this, R.string.blueconnectionsuccess, Toast.LENGTH_LONG).show();
                } else {                 //�û������������
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:     //��������ĳһ�����豸
                if (resultCode == Activity.RESULT_OK) {   //�ѵ�������б��е�ĳ���豸��
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //��ȡ�б������豸��mac��ַ
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);

                    //everything
                }
                break;
        }
    }

    //��ӡͼ��
    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        sendData = pg.printDraw();
        mService.write(sendData);   //��ӡbyte�����
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isbill == true) {
            Intent bill =new Intent(ConfigurerImprime.this,InvoiceDetailsActivity.class);
            //ajout pisco
            //Intent bill =new Intent(PrintDemo.this,ClientPageActivity.class);
            // Intent bill = new Intent(PrintDemo.this, ClientPageActivity.class);
            bill.putExtra(EXTRA_INVOICE_ID, invoiceId);
            bill.putExtra(EXTRA_ISARCHIVE, isArchive);
            startActivity(bill);
        }

    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {

            //so if there is no device paired here we go first ...
            if (v == btnSearch) {
                Intent serverIntent = new Intent(ConfigurerImprime.this, DeviceListActivity.class);      //��������һ����Ļ
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            } else if (v == btnSend) {

                if (textToBePrinted.length() > 0) {
                    //first print...
                    mService.sendMessage(textToBePrinted + "\n", "GBK");
                    //send again the message to print it again... TEST
//					mService.sendMessage(textToBePrinted+"\n", "GBK");

                }
            } else if (v == btnClose) {
                mService.stop();
            } else if (v == btnSendDraw) {
                String msg = "";
                String lang = getString(R.string.strLang);
                //printImage();

                byte[] cmd = new byte[3];
                cmd[0] = 0x1b;
                cmd[1] = 0x21;
                if ((lang.compareTo("en")) == 0) {
                    cmd[2] |= 0x10;
                    mService.write(cmd);           //���?����ģʽ
                    mService.sendMessage("Congratulations!\n", "GBK");
                    cmd[2] &= 0xEF;
                    mService.write(cmd);           //ȡ��ߡ�����ģʽ
                    msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
                            + "  the company is a high-tech enterprise which specializes" +
                            " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";


                    mService.sendMessage(msg, "GBK");
                } else if ((lang.compareTo("ch")) == 0) {
                    cmd[2] |= 0x10;
                    mService.write(cmd);           //���?����ģʽ
                    mService.sendMessage("��ϲ��\n", "GBK");
                    cmd[2] &= 0xEF;
                    mService.write(cmd);           //ȡ��ߡ�����ģʽ
                    msg = "  ���Ѿ��ɹ��������������ǵ�������ӡ��\n\n"
                            + "  ����˾��һ��רҵ�����з��������������Ʊ�ݴ�ӡ�������ɨ���豸��һ��ĸ߿Ƽ���ҵ.\n\n";

                    mService.sendMessage(msg, "GBK");
                }
            }
        }
    }

    public class ConnectPaireDev extends Thread {
        public void run() {
            while (true)
                if (mService.isBTopen() == true)
                    break;

            Set<BluetoothDevice> pairedDevices = mService.getPairedDev();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (conn_flag == 1) {
                        conn_flag = 0;
                        break;
                    }
                    while (true)
                        if (conn_flag == -1 || conn_flag == 0)
                            break;
                    mService.connect(device);
                    conn_flag = 2;
                }
            }
        }
    }
}

